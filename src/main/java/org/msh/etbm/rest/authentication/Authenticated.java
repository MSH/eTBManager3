package org.msh.etbm.rest.authentication;


import java.lang.annotation.*;

/**
 * All methods or classes that implements a REST API that must be authenticated
 * first, must implement this annotation
 *
 * Created by rmemoria on 5/5/15.
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticated {
}
