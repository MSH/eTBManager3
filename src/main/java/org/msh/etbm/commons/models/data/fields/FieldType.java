package org.msh.etbm.commons.models.data.fields;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Store the type name of the Field class
 * Created by rmemoria on 1/7/16.
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
public @interface FieldType {

    /**
     * Store the type name representing this type
     * @return
     */
    String value();
}
