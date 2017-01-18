package org.msh.etbm.commons.dbcache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.CachedData;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.services.session.usersession.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
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

    private final static Logger log = LoggerFactory.getLogger(DbCacheUpdate.class);

    private EntityManager entityManager;

    private DbCacheUtils utils;

    private ObjectMapper objectMapper;

    private ConfigurableApplicationContext applicationContext;

    private List<Method> cachedMethods;


    public DbCacheUpdate(@NotNull ObjectMapper objectMapper,
                         @NotNull ConfigurableApplicationContext applicationContext,
                         @NotNull DbCacheUtils utils,
                         @NotNull EntityManager entityManager) {
        this.objectMapper = objectMapper;
        this.applicationContext = applicationContext;
        this.utils = utils;
        this.entityManager = entityManager;
    }

    /**
     * Search for cached data that must be updated and start the update process
     */
    @Transactional
    public void execute() {
        if (cachedMethods == null) {
            initialize();
        }

        updateExpiredData();
    }


    /**
     * Search for beans with methods annotated with {@link DbCache}
     */
    private void initialize() {
        cachedMethods = new ArrayList<>();

        String[] lst = applicationContext.getBeanDefinitionNames();

        ConfigurableListableBeanFactory factory = applicationContext.getBeanFactory();

        // search for methods with DbCache annotation
        for (String id: lst) {
            BeanDefinition beanDef = factory.getBeanDefinition(id);
            String clazzName = beanDef.getBeanClassName();
            if (clazzName != null && !clazzName.startsWith("org.spring")) {
                Class clazz = ObjectUtils.forClass(clazzName);
                searchAnnotatedMethods(clazz);
            }
        }
    }


    /**
     * Search for methods with the {@link DbCache} annotation and include them in the
     * list of methods to constantly check expired data
     * @param clazz the component class
     */
    private void searchAnnotatedMethods(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method: methods) {
            DbCache dbCache = method.getDeclaredAnnotation(DbCache.class);
            if (dbCache != null) {
                cachedMethods.add(method);
            }
        }
    }

    /**
     * Update the cached data that requires automatic update on data expired
     */
    private void updateExpiredData() {
        List<CachedData> lst = collectCachedData();

        if (lst.isEmpty()) {
            return;
        }

        for (CachedData cd: lst) {
            prepareRequestContext(cd);
            updateCache(cd);
        }
    }

    /**
     * Collect the list of {@link CachedData} entities that must be updated
     * @return list of {@link CachedData} entities
     */
    private List<CachedData> collectCachedData() {
        List<Method> methods = getMethodsWithAutomaticUpdate();
        if (methods.isEmpty()) {
            log.info("No cached data to update");
            return Collections.emptyList();
        }

        StringBuilder hql = new StringBuilder();
        hql.append("from CachedData where expiryDate <= :dt and method in (");

        String sep = "";
        for (int i = 0; i < methods.size(); i++) {
            hql.append(sep).append(":m" + i);
            sep = ",";
        }
        hql.append(")");

        Query qry = entityManager
                .createQuery(hql.toString())
                .setParameter("dt", new Date());

        int index = 0;
        for (Method method: methods) {
            qry.setParameter("m" + index++, utils.methodToString(method));
        }

        return qry.getResultList();
    }

    /**
     * Return the list of methods where cache must be automatically update
     * in case of expired data
     * @return list of methods
     */
    private List<Method> getMethodsWithAutomaticUpdate() {
        List<Method> methods = new ArrayList<>();

        for (Method method: cachedMethods) {
            DbCache dbcache = method.getAnnotation(DbCache.class);
            if (isAutoRefreshable(dbcache)) {
                methods.add(method);
            }
        }

        return methods;
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

    /**
     * Update a give cached data invoking its execution. It is expected that once it is executed,
     * that the cached data will be updated again
     * @param cd
     */
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
