package org.msh.etbm.commons.dbcache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.CachedData;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.services.session.usersession.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * A service to update the cached data (in the cacheddata table) that has expired and
 * is automatically update by using <code>updateAt</code> or <code>updateIn</code> in
 * the annotation properties
 *
 * Created by rmemoria on 16/1/17.
 */
@Service
public class DbCacheUpdate {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DbCacheUtils utils;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ApplicationContext applicationContext;


    /**
     * Update the cached data by the method signature
     * @param method
     */
    @Transactional
    public void updateByMethod(Method method) {
        DbCache dbcacheAnnot = method.getAnnotation(DbCache.class);
        if (dbcacheAnnot == null || !isAutoRefreshable(dbcacheAnnot)) {
            return;
        }

        String entry = utils.getEntryId(method);

        List<CachedData> lst = entityManager.createQuery("from CachedData " +
                "where expiryDate <= :dt " +
                "and entryId = :entry")
                .setParameter("dt", new Date())
                .setParameter("entry", entry)
                .getResultList();

        for (CachedData cd: lst) {
            prepareRequestContext(cd);
            updateCache(cd);
        }
    }

    /**
     * Prepare the mock request context to support the use of components that depend on
     * the request scope
     * @param cd the instance of {@link CachedData} involved in the operation
     */
    private void prepareRequestContext(CachedData cd) {
        MockHttpServletRequest req = new MockHttpServletRequest();
        ServletRequestAttributes a = new ServletRequestAttributes(req);
        RequestContextHolder.setRequestAttributes(a);

        UserRequestService userRequestService = applicationContext.getBean(UserRequestService.class);
        if (userRequestService.isAuthenticated()) {
            return;
        }

        UserSession us = new UserSession();
        us.setWorkspaceId(cd.getWorkspace().getId());
        us.setWorkspaceName(cd.getWorkspace().getName());
        us.setAdministrator(true);

        userRequestService.setUserSession(us);
    }

    private void updateCache(CachedData cd) {
        Object[] args = JsonArgumentsHandler.parseJson(objectMapper, cd.getArgs());
        String[] lst = cd.getMethod().split("#");
        Class clazz = ObjectUtils.forClass(lst[0]);

        Object bean = applicationContext.getBean(clazz);
        Method[] mets = bean.getClass().getDeclaredMethods();
        Method met = null;
        for (Method aux: mets) {
            if (aux.getName().equals(lst[1])) {
                met = aux;
                break;
            }
        }

        try {
            // when calling the method, it will be intercepted and the cache will be updated
            met.invoke(bean, args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbCacheException(e);
        }
    }

    /**
     * Check if cache must be automatically updated
     * @param dbCache the method annotation
     * @return true if the annotation indicates that the cache must be automatically updated
     */
    private boolean isAutoRefreshable(DbCache dbCache) {
        return !dbCache.updateAt().isEmpty() || !dbCache.updateIn().isEmpty();
    }
}
