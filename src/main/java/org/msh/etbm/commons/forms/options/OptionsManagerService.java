package org.msh.etbm.commons.forms.options;

import org.msh.etbm.commons.forms.FormException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rmemoria on 1/2/16.
 */
@Component
public class OptionsManagerService {

    private Map<String, OptionsResolver> resolvers = new HashMap<>();

    /**
     * Register a new options resolver
     * @param name
     * @param optionsResolver
     */
    public void register(String name, OptionsResolver optionsResolver) {
        if (resolvers.containsKey(name)) {
            throw new FormException("Options resolver already registered: " + name);
        }
        resolvers.put(name, optionsResolver);
    }

    public OptionsResolver get(String name) {
        return resolvers.get(name);
    }
}
