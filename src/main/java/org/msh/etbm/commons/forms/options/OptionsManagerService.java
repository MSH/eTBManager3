package org.msh.etbm.commons.forms.options;

import org.msh.etbm.commons.forms.FormException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rmemoria on 1/2/16.
 */
@Service
public class OptionsManagerService {

    private Map<String, OptionsProvider> resolvers = new HashMap<>();

    /**
     * Register a new options resolver
     * @param name
     * @param optionsProvider
     */
    public void register(String name, OptionsProvider optionsProvider) {
        if (resolvers.containsKey(name)) {
            throw new FormException("Options resolver already registered: " + name);
        }
        resolvers.put(name, optionsProvider);
    }

    public OptionsProvider get(String name) {
        return resolvers.get(name);
    }
}
