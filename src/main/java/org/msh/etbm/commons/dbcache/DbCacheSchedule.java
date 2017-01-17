package org.msh.etbm.commons.dbcache;

import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.CachedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Component to check for expired data that must be updated again
 * Created by rmemoria on 12/1/17.
 */
@Service
public class DbCacheSchedule {

    private final static Logger log = LoggerFactory.getLogger(DbCacheSchedule.class);

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Autowired
    DbCacheStore dbCacheStore;

    @Autowired
    DbCacheUpdate dbCacheUpdate;


    private List<Method> cachedMethods;


    /**
     * Execute the scheduled checking of expired cached data
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 1000)
    public void execute() {
        if (cachedMethods == null) {
            initialize();
        }
        checkExpiredCachedData();
    }

    /**
     * Check if there is any expired cached data in the DB that must be updated
     */
    private void checkExpiredCachedData() {
        for (Method method: cachedMethods) {
            dbCacheUpdate.updateByMethod(method);
        }
    }



    /**
     * Initialize the list of methods with the {@link DbCache} annotation
     */
    private void initialize() {
        log.info("Schedule is alive...");

        cachedMethods = new ArrayList<>();

        String[] lst = applicationContext.getBeanDefinitionNames();

        ConfigurableListableBeanFactory factory = applicationContext.getBeanFactory();

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
}
