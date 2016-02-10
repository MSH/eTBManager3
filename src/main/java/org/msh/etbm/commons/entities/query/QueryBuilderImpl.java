package org.msh.etbm.commons.entities.query;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.usersession.UserRequestService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the query builder interface
 * Created by rmemoria on 28/10/15.
 */
public class QueryBuilderImpl<E> implements QueryBuilder<E> {

    private static final Pattern PARAM_PATTERN =Pattern.compile("\\:([_a-zA-Z]+)");

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
     * The HQL join to be added after the FROM clause when selecting entities
     */
    private String hqlJoin;

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
     * List of available profiles
     */
    private Map<String, Class> profiles = new HashMap<>();

    /**
     * The default profile
     */
    private String defaultProfile;

    /**
     * The selected profile
     */
    private String profile;

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

    /**
     * The entity alias used in the from clause
     */
    private String entityAlias;

    /**
     * If true, just count operation will be performed
     */
    private boolean countOnly;

    private EntityManager entityManager;

    private PlatformTransactionManager transactionManager;

    private UserRequestService userRequestService;

    private DozerBeanMapper mapper;

    /**
     * Default constructor
     * @param entityClass the entity class to be handled
     * @param entityAlias the path used in the FROM clause (optional, but required in join operation)
     */
    public QueryBuilderImpl(Class entityClass, String entityAlias) {
        this.entityClass = entityClass;
        this.entityAlias = entityAlias;
    }

    /**
     * Create HQL instruction to count number of entities from query
     * @return HQL instruction to count number of entities
     */
    public String createHQLCount() {
        StringBuilder s = new StringBuilder("select count(*) ");
        s.append("from ");
        s.append(entityClass.getSimpleName());

        if (entityAlias != null) {
            s.append(" " + entityAlias);
        }
        s.append('\n');

        addHQLRestrictions(s);

        return s.toString();
    }

    /**
     * Create HQL instruction to query entities
     * @return HQL instruction
     */
    public String createHQL() {
        StringBuilder s = new StringBuilder("from " + entityClass.getSimpleName());
        if (entityAlias != null) {
            s.append(' ');
            s.append(entityAlias);
        }
        s.append('\n');

        if (hqlJoin != null) {
            s.append(hqlJoin);
            s.append('\n');
        }

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
            throw new InvalidArgumentException("orderBy", "Invalid order by clause " + orderByKey, null);
        }

        hql.append("order by " + field);
    }

    protected void addHQLRestrictions(StringBuilder hql) {
        boolean bWhere = false;
        if (WorkspaceEntity.class.isAssignableFrom(entityClass) && userRequestService.isAuthenticated()) {
            hql.append("where ");
            if (entityAlias != null) {
                hql.append(entityAlias);
                hql.append('.');
            }
            hql.append("workspace.id = :wsid\n");
            setParameter("wsid", userRequestService.getUserSession().getWorkspaceId());
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

    @Override
    public void setHqlJoin(String hql) {
        this.hqlJoin = hql;
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
    public void addRestriction(String restriction, Object... args) {
        Matcher matcher = PARAM_PATTERN.matcher(restriction);

        // parse the restriction
        List<String> params = new ArrayList<>();
        while (matcher.find()) {
            String p = matcher.group();
            params.add(p.substring(1));
        }

        // check if number of arguments is less than number of parameters
        if (args.length < params.size()) {
            throw new RuntimeException("Number of arguments is less than number of defined parameters");
        }

        // check if any param value is null
        for (int i = 0; i < params.size(); i++) {
            Object val = args[i];
            // null values or empty strings are discharged
            if (val == null || (val instanceof String && ((String) val).isEmpty())) {
                return;
            }
        }

        // add the restriction
        addRestriction(restriction);

        // set the parameter values
        for (int i = 0; i < params.size(); i++) {
            String param = params.get(i);
            Object value = args[i];
            setParameter(param, value);
        }
    }

    @Override
    public void addLikeRestriction(String field, String value) {
        if (value != null && !value.isEmpty()) {
            String p = field.replace(".", "");
            addRestriction(field + " like :" + p);
            setParameter(p, '%' + value + '%');
        }
    }

    @Override
    public void addOrderByMap(String key, String field) {
        orderByMap.put(key, field);
    }

    @Override
    public void addDefaultOrderByMap(String key, String field) {
        addOrderByMap(key, field);
        defaultOrderByKey = key;
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
        TransactionTemplate tmpl = new TransactionTemplate(transactionManager);

        // check if destination class is set
        if (destClass == null) {
            destClass = profiles.get(defaultProfile);
            if (destClass == null) {
                throw new RuntimeException("No default profile set for class " + entityClass.getSimpleName());
            }
        }

        final Class finalDestClass = destClass;

        return tmpl.execute(transactionStatus -> {
            QueryResult res = new QueryResult();

            // check if just counting is to be performed, or list too
            if (!this.countOnly) {
                // execute the query
                List<E> lst = getResultList();

                List lst2 = new ArrayList();
                for (E ent: lst) {
                    lst2.add(mapper.map(ent, finalDestClass));
                }

                res.setList(lst2);
            }

            // is paging enabled or count only ?
            if (page != null || countOnly) {
                res.setCount(getCount());
            }
            else {
                // if there is no paging, avoid unnecessary database query
                res.setCount(res.getList().size());
            }

            return res;
        });
    }

    @Override
    public QueryResult createQueryResult() {
        Class dataClass = profile != null? profiles.get(profile): null;

        if (profile != null && dataClass == null) {
            throw new InvalidArgumentException("profile", null, "NotValid");
        }

        return createQueryResult(dataClass);
    }

    @Override
    public void addProfile(String profname, Class dataClass) {
        profiles.put(profname, dataClass);
    }

    @Override
    public void addDefaultProfile(String profname, Class dataClass) {
        profiles.put(profname, dataClass);
        defaultProfile = profname;
    }

    @Override
    public void initialize(EntityQueryParams qry) {
        this.page = qry.getPage();
        this.recordsPerPage = qry.getRecordsPerPage();
        this.orderByKey = qry.getOrderBy();
        this.orderByDescending = qry.isOrderByDescending();
        this.countOnly = qry.isCountOnly();

        // an ID was set in the search query ?
        if (qry.getId() != null) {
            addRestriction("id = :id");
            setParameter("id", qry.getId());
        }

        // IDs were set ?
        if (qry.getIds() != null) {
            String s = "";
            int count = 1;
            for (UUID id: qry.getIds()) {
                if (!s.isEmpty()) {
                    s += ",";
                }
                String key = "id" + Integer.toString(count);
                s += ":" + key;
                setParameter(key, id);
                count++;
            }
            addRestriction("id in (" + s + ")");
        }

        setProfile(qry.getProfile());
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public UserRequestService getUserRequestService() {
        return userRequestService;
    }

    public void setUserRequestService(UserRequestService userRequestService) {
        this.userRequestService = userRequestService;
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

    @Override
    public String getProfile() {
        return profile;
    }

    @Override
    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getentityAlias() {
        return entityAlias;
    }

    public void setentityAlias(String entityAlias) {
        this.entityAlias = entityAlias;
    }

    @Override
    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void setEntityAlias(String alias) {
        this.entityAlias = alias;
    }
}
