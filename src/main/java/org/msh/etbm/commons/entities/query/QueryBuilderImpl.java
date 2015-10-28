package org.msh.etbm.commons.entities.query;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.services.usersession.UserSession;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 28/10/15.
 */
public class QueryBuilderImpl<E> implements QueryBuilder<E> {

    /**
     * The qntity class being queried
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

        return s.toString();
    }

    /**
     * Create HQL instruction to query entities
     * @return
     */
    public String createHQL() {
        StringBuilder s = new StringBuilder("from " + entityClass.getSimpleName());
        s.append('\n');

        return s.toString();
    }

    /**
     * Create query from hql instruction (count or result set)
     * @return
     */
    protected Query createQuery(String hql) {
        Query qry = entityManager.createQuery(hql);

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
    public long getCount() {
        Query qry = createQuery(createHQLCount());
        Number res = (Number)qry.getSingleResult();

        return res.longValue();
    }

    @Override
    public List<E> getResultList() {
        Query qry = createQuery(createHQL());
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
}
