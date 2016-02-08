package org.msh.etbm.commons.forms;

/**
 * Interface that must be implemented by components that want to answer form requests
 * Created by rmemoria on 5/2/16.
 */
public interface FormRequestHandler<R> {

    /**
     * Return the name on which this form request handler will be referenced in the command field of the request.
     * This method will be called on initialization time, and the command name must be unique, otherwise a
     * {@link FormException} will be thrown
     * @return the command name
     */
    String getFormCommandName();

    /**
     * Execute a form request and return the response of the command
     * @param req the form request
     * @return the response of the request
     */
    R execFormRequest(FormRequest req);
}
