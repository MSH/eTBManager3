package org.msh.etbm.commons.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that must be included in every method call in order to register the command
 * execution in the command history
 *
 * Created by rmemoria on 17/10/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandLog {
    /**
     * The type of the log to register
     * @return
     */
    String type();


    /**
     * Implementation of the CommandLogHandler that will convert the data
     * on how the command will be registered by the history
     * @return
     */
    Class<? extends CommandLogHandler> handler();
}
