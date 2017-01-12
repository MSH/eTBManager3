package org.msh.etbm.commons.dbcache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.CachedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Aspect component that intercepts all execution calls of methods with the {@link DbCache} annotation,
 * implementing a database cache.
 *
 * Created by rmemoria on 9/1/17.
 */
@Aspect
@Component
public class DbCacheInterceptor {

    private final static Logger log = LoggerFactory.getLogger(DbCacheInterceptor.class);

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;


    /**
     * Called around database DB cache
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(public * *(..)) && @annotation(org.msh.etbm.commons.dbcache.DbCache)")
    @Transactional
    public Object aroundDbCache(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        // check if data is in the cache
        Object res = restoreFromCache(method, pjp.getArgs());

        // if there is anything back from cache, return it
        if (res != null) {
            return res;
        }

        // call the method
        res = pjp.proceed();

        // save its result to cache
        saveToCache(method, pjp.getArgs(), res);

        return res;
    }


    /**
     * Save data to cache
     * @param method the method with the {@link DbCache} annotation
     * @param args the arguments of the method call
     * @param res the data to be cached
     */
    private void saveToCache(Method method, Object[] args, Object res) {
        CachedData cd = new CachedData();

        String hash = ObjectUtils.hashSHA1(args);
        String entry = getEntryId(method);

        String data;
        try {
            data = objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e) {
            throw new DbCacheException(e);
        }

        cd.setEntryId(entry);
        cd.setHash(hash);
        cd.setDataClass(res.getClass().getCanonicalName());
        cd.setEntryDate(new Date());
        cd.setData(data);

        // save new cache entry
        entityManager.persist(cd);
        entityManager.flush();
    }


    /**
     * Get the cache entry by its method signature
     * @param method the method with the {@link DbCache} annotation
     * @return the entry ID of the cache
     */
    private String getEntryId(Method method) {
        // get the method signature
        String metName = method.getDeclaringClass().getCanonicalName() + "#" + method.getName();
        return metName;
    }

    /**
     * Restore a result type from DB cache, based on the method and its arguments
     * @param method the method with the {@link DbCache} annotation
     * @param args the method arguments
     * @return the object stored in cache (if still valid)
     */
    private Object restoreFromCache(Method method, Object[] args) {
        // get the hash of the arguments
        String argsHash = ObjectUtils.hashSHA1(args);

        // get the method signature
        String entryId = getEntryId(method);

        List<CachedData> lst = entityManager.createQuery("from CachedData " +
                "where hash = :hash and entryId = :entry")
                .setParameter("hash", argsHash)
                .setParameter("entry", entryId)
                .getResultList();

        if (lst.size() == 0) {
            return null;
        }

        CachedData cd = lst.get(0);

        // cache is invalid
        if (cd.getExpiryDate() != null && cd.getExpiryDate().before(new Date())) {
            // delete the cache data
            entityManager.remove(cd);
            entityManager.flush();
            return null;
        }

        String data = cd.getData();
        String className = cd.getDataClass();

        Class clazz = ObjectUtils.forClass(className);

        try {
            return objectMapper.readValue(data, clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DbCacheException(e);
        }
    }
}
