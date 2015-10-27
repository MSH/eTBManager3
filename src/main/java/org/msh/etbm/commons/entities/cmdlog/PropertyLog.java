package org.msh.etbm.commons.entities.cmdlog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation that gives extra information about the field being logged. By default all fields in the entity
 * will be logged if changed, but additional information may be included depending on the situation
 * @author Ricardo Memoria
 *
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Documented
public @interface PropertyLog {

	/**
	 * field value will be ignored and its value won't be logged
	 * @return
	 */
	boolean ignore() default false;

	/**
	 * Force log value to use this key when logging value. This key must match a key in the messages file
	 * @return
	 */
	String messageKey() default "";

	/**
	 * Indicate the operations that the field will be logged
	 * @return
	 */
	Operation[] operations() default { Operation.EDIT };
}
