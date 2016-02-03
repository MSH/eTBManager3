package org.msh.etbm.commons.forms.options;

import org.msh.etbm.commons.Item;

import java.util.List;
import java.util.Map;

/**
 * An interface that all classes that want to expose a list of options to the UI in the client side
 * Created by rmemoria on 1/2/16.
 */
public interface OptionsResolver {

    /**
     * Return the list of options based on the given parameters
     * @param params list of parameters
     * @return list of options to be sent to the client side
     */
    List<Item> getOptions(Map<String, Object> params);
}
