package org.msh.etbm.commons.models.data.options;

import org.msh.etbm.commons.Item;

import java.util.List;

/**
 * Options of a field represented by a list that will come from a from server request
 *
 * Created by rmemoria on 2/8/16.
 */
public class FormRequestOptions extends FieldOptions {

    /**
     * The name of the list in the server side
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Item> getOptionsValues() {
        return null;
    }

    @Override
    public boolean isValueInOptions(Object value) {
        return false;
    }
}
