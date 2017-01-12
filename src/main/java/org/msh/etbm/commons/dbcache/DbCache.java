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
}
