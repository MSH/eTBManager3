package org.msh.etbm.commons.forms;

/**
 * Base exception when dealing with forms
 * Created by rmemoria on 29/1/16.
 */
public class FormException extends RuntimeException {

    public FormException(String message) {
        super(message);
    }

    public FormException(Throwable cause) {
        super(cause);
    }
}
