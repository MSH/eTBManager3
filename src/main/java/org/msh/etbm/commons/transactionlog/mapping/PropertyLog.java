package org.msh.etbm.commons.transactionlog.mapping;

import org.msh.etbm.commons.transactionlog.Operation;

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
	 * If field is another entity, it indicates if log values of the entity will also be logged (true) or
	 * the value of the entity as a string (using toString) will be used for logging (false). Default value is false
	 * @return
	 */
	boolean logEntityFields() default false;

	/**
	 * Indicate the operations where the field will be logged. Default is the edit operation, i.e, 
	 * this field will be logged just when the operation is for editing 
	 * @return
	 */
	Operation[] operations() default {};
}
