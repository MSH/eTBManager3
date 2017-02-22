package org.msh.etbm.commons.dbcache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotation level to store in the database the result of complex and time consuming
 * method execution, working as a cache to speed up execution of forthcoming method calls.
 * Method execution is intercepted and if data is not in the
 * cache or cached data is expired, the method is executed and its result recorded in the database
 * (table cacheddata). The method signature and arguments are used as a key to look up for
 * cached data.
 * <p/>
 * About concurrency, i.e, several simultaneous request to the same method: Method execution
 * is synchronized only for methods with same arguments. If same method is called with
 * different arguments, then its execution is not synchronized, promoting maximum performance.
 * Access to the cache is also not synchronized.
 *
 * Created by rmemoria on 9/1/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DbCache {

    /**
     * How long the data in cache will last, in the format HH:MM:SS (hour, minute, second)
     * @return
     */
    String expireIn() default "";

    /**
     * The exactly time the data will be expired
     * @return
     */
    String expireAt() default "";

    /**
     * Indicate the time it will take for the data to be updated again, in the format HH:MM:SS,
     * counting from the time it was generated
     * @return
     */
    String updateIn() default "";

    /**
     * The exactly time of the day that the data will be updated
     * @return
     */
    String updateAt() default "";

    /**
     * Optionally define an entry name used to identify the cached data
     * @return
     */
    String entry() default "";
}
