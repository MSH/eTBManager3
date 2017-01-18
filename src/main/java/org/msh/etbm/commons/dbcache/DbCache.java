package org.msh.etbm.commons.dbcache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that the result of the method must be stored in a database cache.
 * The method signature is used as a cache record identification
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
