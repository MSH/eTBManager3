package org.msh.etbm.commons.entities.query;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.WorkspaceData;
import org.msh.etbm.services.usersession.UserSession;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the query builder interface
 * Created by rmemoria on 28/10/15.
 */
public class QueryBuilderImpl<E> implements QueryBuilder<E> {

    /**
     * Default number of entities per page
     */
    public static final int DEFAULT_RECORDS_PER_PAGE = 50;

    /**
     * The entity class being queried
     */
    private Class entityClass;

    /**
     * Parameters to be passed to the query
     */
    private Map<String, Object> parameters;

    /**
     * List of restrictions to be applied in the query
     */
    private List<String> restrictions;

    /**
     * List of key words assigned to field in order by.
     * If reverse ordering is different from declaration, declare key like 'key desc'
     */
    private Map<String, String> orderByMap = new HashMap<>();

    /**
     * It is not the field itself, but the key in the orderByMap property
     */
    private String orderByKey;

    /**
     * The default ordering, if no one is defined
     */
    private String defaultOrderByKey;

    /**
     * If true, the order by will be descending
     */
    private boolean orderByDescending;

    /**
     * Initial page of the search result. Null value indicates there is no pagging
     */
    private Integer page;

    /**
     * Number of records per page. Default is DEFAULT_RECORDS_PER_PAGE value
     */
    private Integer recordsPerPage;

    private EntityManager entityManager;

    private UserSession userSession;

    private DozerBeanMapper mapper;

    public QueryBuilderImpl(Class entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Create HQL instruction to return number of entities from query
     * @return
     */
    public String createHQLCount() {
        StringBuilder s = new StringBuilder("select count(*) ");
        s.append("from " + entityClass.getSimpleName());
        s.append('\n');

        addHQLRestrictions(s);

        return s.toString();
    }

    /**
     * Create HQL instruction to query entities
     * @return
     */
    public String createHQL() {
        StringBuilder s = new StringBuilder("from " + entityClass.getSimpleName());
        s.append('\n');

        addHQLRestrictions(s);

        s.append('\n');

        addHQLOrderBy(s);

        return s.toString();
    }

    /**
     * Add order by HQL instruction, if necessary, to the query
     * @param hql
     */
    protected void addHQLOrderBy(StringBuilder hql) {
        if (orderByKey == null) {
            orderByKey = defaultOrderByKey;
            if (orderByKey == null) {
                return;
            }
        }

        String field;
        if (orderByDescending) {
            field = orderByMap.get(orderByKey + " desc");
            if (field == null) {
                field = orderByMap.get(orderByKey);
                if (field != null) {
                    field += " desc";
                }
            }
        }
        else {
            field = orderByMap.get(orderByKey);
        }

        if (field == null) {
            throw new IllegalArgumentException("Invalid order by clause " + orderByKey);
        }

        hql.append("order by " + field);
    }

    protected void addHQLRestrictions(StringBuilder hql) {
        boolean bWhere = false;
        if (WorkspaceData.class.isAssignableFrom(entityClass) && userSession.getUserWorkspace() != null) {
            hql.append("where workspace.id = :wsid\n");
            setParameter("wsid", userSession.getUserWorkspace().getWorkspace().getId());
            bWhere = true;
        }

        if (restrictions != null) {
            for (String restr: restrictions) {
                if (bWhere) {
                    hql.append(" and ");
                }
                else {
                    hql.append(" where ");
                    bWhere = true;
                }
                hql.append(restr);
            }
        }
    }

    /**
     * Create query from hql instruction (count or result set)
     * @return
     */
    protected Query createQuery(String hql) {
        Query qry = entityManager.createQuery(hql);

        // has parameters ?
        if (parameters != null) {
            for (String p: parameters.keySet()) {
                Object val = parameters.get(p);
                qry.setParameter(p, val);
            }
        }

        return qry;
    }

    /**
     * Set a parameter to be used in the query
     * @param paramName parameter name
     * @param value parameter value
     */
    @Override
    public void setParameter(String paramName, Object value) {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        parameters.put(paramName, value);
    }

    @Override
    public void addRestriction(String restriction) {
        if (restrictions == null) {
            restrictions = new ArrayList<>();
        }

        restrictions.add(restriction);
    }

    @Override
    public void addLikeRestriction(String field, String value) {
        if (value != null && !value.isEmpty()) {
            addRestriction(field + " like :" + field);
            setParameter(field, '%' + value + '%');
        }
    }

    @Override
    public void addOrderByMap(String key, String field, boolean defaultOrder) {
        orderByMap.put(key, field);
        if (defaultOrder) {
            defaultOrderByKey = key;
        }
    }

    @Override
    public long getCount() {
        Query qry = createQuery(createHQLCount());
        Number res = (Number)qry.getSingleResult();

        return res.longValue();
    }

    @Override
    public List<E> getResultList() {
        Query qry = createQuery(createHQL());

        // setup paging
        if (page != null) {
            int maxResult = recordsPerPage != null? recordsPerPage: DEFAULT_RECORDS_PER_PAGE;
            int firstResult = page * maxResult;

            qry.setMaxResults(maxResult).setFirstResult(firstResult);
        }

        return qry.getResultList();
    }

    @Override
    public QueryResult createQueryResult(Class destClass) {
        QueryResult res = new QueryResult();

        res.setCount(getCount());
        List<E> lst = getResultList();
        //List lst2 = new ArrayList<>();

        List lst2 = Lists.transform(lst, item -> mapper.map(item, destClass));

        res.setList(lst2);
        return res;
    }

    @Override
    public void initialize(EntityQuery qry) {
        this.page = qry.getPage();
        this.recordsPerPage = qry.getRecordsPerPage();
        this.orderByKey = qry.getOrderBy();
        this.orderByDescending = qry.isOrderByDescending();

        if (qry.getId() != null) {
            addRestriction("id = :id");
            setParameter("id", qry.getId());
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public DozerBeanMapper getMapper() {
        return mapper;
    }

    public void setMapper(DozerBeanMapper mapper) {
        this.mapper = mapper;
    }

    public Map<String, String> getOrderByMap() {
        return orderByMap;
    }

    public void setOrderByMap(Map<String, String> orderByMap) {
        this.orderByMap = orderByMap;
    }

    public boolean isOrderByDescending() {
        return orderByDescending;
    }

    public void setOrderByDescending(boolean orderByDescending) {
        this.orderByDescending = orderByDescending;
    }

    @Override
    public String getOrderByKey() {
        return orderByKey;
    }

    @Override
    public void setOrderByKey(String orderByKey) {
        this.orderByKey = orderByKey;
    }
}
