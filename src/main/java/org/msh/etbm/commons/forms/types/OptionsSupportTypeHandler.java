package org.msh.etbm.commons.forms.types;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.forms.FieldInitRequest;
import org.msh.etbm.commons.forms.FormException;
import org.msh.etbm.commons.forms.options.OptionsManagerService;
import org.msh.etbm.commons.forms.options.OptionsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Type handler to assist on field types that need remote initialization of lists
 * Created by rmemoria on 1/2/16.
 */
@Component
public class OptionsSupportTypeHandler implements TypeHandler {

    public static final String PARAM_OPTIONS = "options";

    @Autowired
    OptionsManagerService optionsManagerService;

    @Autowired
    TypesManagerService typesManagerService;


    @PostConstruct
    public void init() {
        typesManagerService.register("string", this);
        typesManagerService.register("multi-select", this);
    }

    /**
     * Initialize the field
     * @param req
     * @return
     */
    @Override
    public Object initField(FieldInitRequest req) {
        String optionsName = req.getParams() != null ? (String)req.getParams().get(PARAM_OPTIONS) : null;

        if (optionsName == null) {
            return null;
        }

        return resolveOptions(optionsName, req.getParams());
    }

    /**
     * Resolve the options given the name and the list of params
     * @param name the name of the options list
     * @param params the optional list of parameters
     * @return list of items to be serialized and sent back to the client
     */
    protected List<Item> resolveOptions(String name, Map<String, Object> params) {
        // find option resolver
        OptionsProvider resolver = optionsManagerService.get(name);

        // no resolver found ?
        if (resolver == null) {
            throw new FormException("Invalid options name: " + name);
        }

        // get the list of options
        return resolver.getOptions(params);
    }
}
