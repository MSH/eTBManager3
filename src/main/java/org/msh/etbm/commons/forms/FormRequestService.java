package org.msh.etbm.commons.forms;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Services to handle form operations. Initialize field contents that need resources
 * from the server side, for example, list of units needed in a drop down
 * Created by rmemoria on 17/1/16.
 */
@Component
public class FormRequestService {

    private Map<String, FormRequestHandler> handlers = new HashMap<>();


    /**
     * Register a new form request handler. The request handler will be called to generate response
     * of form requests based on the command issued
     *
     * @param handler the request handler to be registered
     */
    public void registerRequestHandler(FormRequestHandler handler) {
        String cmdName = handler.getFormCommandName();
        if (handlers.containsKey(cmdName)) {
            throw new FormException("There is already a form request handler to the name " + cmdName);
        }

        // more than one name can be registered to the same handle using ; or , as separators
        String delimiters = ",\\s*|\\;\\s*";
        String[] names = cmdName.split(delimiters);

        // register each name
        for (String name : names) {
            handlers.put(name, handler);
        }
    }


    /**
     * Process form requests, invoking the specific handler for each request
     *
     * @param reqs list of requests of a form
     * @return form response, in a map format
     */
    public Map<String, Object> processFormRequests(List<FormRequest> reqs) {
        if (reqs == null) {
            return null;
        }

        Map<String, Object> resps = new HashMap<>();

        for (FormRequest req : reqs) {
            // get the handler for the given command
            FormRequestHandler handler = handlers.get(req.getCmd());

            // handler was found ?
            if (handler == null) {
                throw new FormException("No form handler found for command request: " + req.getCmd());
            }

            Object res = handler.execFormRequest(req);
            resps.put(req.getId(), res);
        }

        return resps;
    }
}
