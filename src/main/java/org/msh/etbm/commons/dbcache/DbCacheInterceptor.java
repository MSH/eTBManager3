package org.msh.etbm.commons.dbcache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
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

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DbCacheStore dbCacheStore;

    @Autowired
    DbCacheUtils dbCacheUtils;


    private List<CacheId> ids = new ArrayList<>();


    /**
     * Called around database DB cache
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(public * *(..)) && @annotation(org.msh.etbm.commons.dbcache.DbCache)")
    public Object aroundDbCache(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        CacheId cacheId = dbCacheUtils.createCacheId(method, pjp.getArgs());

        // check if data is in the cache
        Object res = dbCacheStore.loadFromCache(cacheId);

        // if there is anything back from cache, return it
        if (res != null) {
            return res;
        }

        cacheId = retrieveCacheId(cacheId);

        synchronized (cacheId) {
            // call it again inside synchronized to check if it was already saved
            // by another thread while blocked
            res = dbCacheStore.loadFromCache(cacheId);
            if (res != null) {
                return res;
            }

            // call the method
            res = pjp.proceed();

            // save its result to cache
            dbCacheStore.saveToCache(cacheId, res);
        }

        releaseCacheId(cacheId);

        return res;
    }


    private CacheId retrieveCacheId(CacheId cacheId) {
        synchronized (this) {
            int index = ids.indexOf(cacheId);
            if (index >= 0) {
                cacheId = ids.get(index);
            } else {
                ids.add(cacheId);
            }

            cacheId.addRefCount();
        }

        return cacheId;
    }


    private void releaseCacheId(CacheId cacheId) {
        synchronized (this) {
            cacheId.remRefCount();
            if (cacheId.getRefCount() == 0) {
                ids.remove(cacheId);
            }
        }
    }
}
