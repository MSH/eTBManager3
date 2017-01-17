package org.msh.etbm.commons.dbcache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.Tuple;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility functions used throughout the db cache library
 *
 * Created by rmemoria on 11/1/17.
 */
@Component
public class DbCacheUtils {

    @Autowired
    ObjectMapper objectMapper;


    public CacheId createCacheId(Method method, Object[] args) {
        CacheId cacheId = new CacheId();
        cacheId.setMethod(method);
        cacheId.setArgs(args);

        String entry = getEntryId(method);
        cacheId.setEntry(entry);

        Tuple<String, String> t = calcHash(args);
        String hash = t.getValue1();
        String jsonArgs = t.getValue2();
        cacheId.setHash(hash);
        cacheId.setArgsJson(jsonArgs);

        return cacheId;
    }

    /**
     * Get the cache entry by its method signature
     * @param method the method with the {@link DbCache} annotation
     * @return the entry ID of the cache
     */
    protected String getEntryId(Method method) {
        return methodToString(method);
    }

    /**
     * Return the method signature as a string value
     * @param method the method
     * @return the string representation of the method
     */
    public String methodToString(Method method) {
        // get the method signature
        String metName = method.getDeclaringClass().getCanonicalName() + "#" + method.getName();
        return metName;
    }

    /**
     * Calculate the hash of the method call arguments used in the DbCache annotation
     * @param args the arguments used in the function call
     * @return tuple containing the hash of the arguments and its json representation
     */
    protected Tuple<String, String> calcHash(Object[] args) {
        String s = JsonArgumentsHandler.generateJson(objectMapper, args);
        return Tuple.of(ObjectUtils.hashSHA1(s), s);
    }

    /**
     * Calculate when is the expiry date of the cached data based on information in the
     * {@link DbCache} annotation
     * @param method
     * @return
     */
    public Date calcExpiryDate(Method method) {
        DbCache cacheAnnot = method.getAnnotation(DbCache.class);
        if (cacheAnnot == null) {
            return null;
        }

        Date expiryDate = null;

        String sUpdateAt = cacheAnnot.updateAt();
        if (sUpdateAt != null && !sUpdateAt.isEmpty()) {
            expiryDate = parseDateByExactTime(sUpdateAt);
        }

        String sUpdateIn = cacheAnnot.updateIn();
        if (sUpdateIn != null && !sUpdateIn.isEmpty()) {
            Date dt = parseDateByDuration(sUpdateIn);
            if (expiryDate == null || dt.before(expiryDate)) {
                expiryDate = dt;
            }
        }

        String sExpireAt = cacheAnnot.expireAt();
        if (sExpireAt != null && !sExpireAt.isEmpty()) {
            Date dt = parseDateByExactTime(sExpireAt);
            if (expiryDate == null || dt.before(expiryDate)) {
                expiryDate = dt;
            }
        }

        String sExpiresIn = cacheAnnot.expireIn();
        if (sExpiresIn != null && !sExpiresIn.isEmpty()) {
            Date dt = parseDateByExactTime(sExpiresIn);
            if (expiryDate == null || dt.before(expiryDate)) {
                expiryDate = dt;
            }
        }

        return expiryDate;
    }

    /**
     * Parse the date calculated from now plus the given period in the format HH:MM:SS
     * @param s period of time in the format HH:MM:SS
     * @return
     */
    protected Date parseDateByDuration(String s) {
        int[] res = parseDate(s);
        Date dt = DateUtils.incHours(new Date(), res[0]);
        dt = DateUtils.incMinutes(dt, res[1]);
        dt = DateUtils.incSeconds(dt, res[2]);
        return dt;
    }

    /**
     * Parse the given time set to the current date (today). If the time has already passed,
     * it is returned the tomorrow's date with the given time
     * @param s the exactly time in the format HH:MM:SS
     * @return
     */
    protected Date parseDateByExactTime(String s) {
        int[] res = parseDate(s);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, res[0]);
        c.set(Calendar.MINUTE, res[1]);
        c.set(Calendar.SECOND, res[2]);
        Date dt = c.getTime();
        if (dt.before(new Date())) {
            int day = c.get(Calendar.DAY_OF_YEAR) + 1;
            c.set(Calendar.DAY_OF_YEAR, day);
            dt = c.getTime();
        }
        return dt;
    }

    /**
     * Parse the date and return an array containing hours, minutes and seconds, respectively
     * @param sdate the date in the format HH:MM:SS
     * @return
     */
    protected int[] parseDate(String sdate) {
        String[] s = sdate.split(":");

        int index = 0;
        int[] res = new int[3];
        res[0] = Integer.parseInt(s[index++]);
        res[1] = s.length > 1 ? Integer.parseInt(s[index++]) : 0;
        res[2] = s.length > 2 ? Integer.parseInt(s[index]) : 0;

        return res;
    }
}
