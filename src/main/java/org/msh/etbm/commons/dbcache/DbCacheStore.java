package org.msh.etbm.commons.dbcache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.CachedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Service responsible for loading and saving data to cache in database
 * Created by rmemoria on 11/1/17.
 */
@Service
public class DbCacheStore {

    private final static Logger log = LoggerFactory.getLogger(DbCacheStore.class);

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DbCacheUtils dbCacheUtils;


    /**
     * Restore a result type from DB cache, based on the method and its arguments
     * @param cacheId object containing information about the method, entry ID and arguments
     * @return the object stored in cache (if still valid)
     */
    @Transactional
    public Object loadFromCache(CacheId cacheId) {

        List<CachedData> lst = entityManager.createQuery("from CachedData " +
                "where hash = :hash and entryId = :entry")
                .setParameter("hash", cacheId.getHash())
                .setParameter("entry", cacheId.getEntry())
                .getResultList();

        if (lst.isEmpty()) {
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

    /**
     * Save data to cache
     * @param cacheId object containing information about the cache method and arguments
     * @param res the data to be cached
     */
    @Transactional
    public void saveToCache(CacheId cacheId, Object res) {
        CachedData cd = new CachedData();

        String data;
        try {
            data = objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e) {
            throw new DbCacheException(e);
        }

        cd.setEntryId(cacheId.getEntry());
        cd.setHash(cacheId.getHash());
        cd.setDataClass(res.getClass().getCanonicalName());
        cd.setEntryDate(new Date());
        cd.setData(data);
        cd.setArgs(cacheId.getArgsJson());
        cd.setArgsClass(cacheId.getArgs().getClass().getCanonicalName());
        cd.setMethod(dbCacheUtils.methodToString(cacheId.getMethod()));

        Date expiryDate = dbCacheUtils.calcExpiryDate(cacheId.getMethod());
        cd.setExpiryDate(expiryDate);

        // save new cache entry
        entityManager.persist(cd);
        entityManager.flush();
    }
}
