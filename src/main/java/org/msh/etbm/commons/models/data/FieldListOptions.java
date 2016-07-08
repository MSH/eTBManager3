package org.msh.etbm.commons.models.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.data.FieldOptions;

import java.util.List;

/**
 * Created by rmemoria on 7/7/16.
 */
public class FieldListOptions extends FieldOptions {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Item> list;

    @Override
    public List<Item> getOptionsValues() {
        return list;
    }

    @Override
    public boolean isValueInOptions(Object value) {
        if (list == null) {
            return false;
        }

        for (Item item: list) {
            if (item.getId().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void setList(List<Item> options) {
        this.list = options;
    }

    public List<Item> getList() {
        return list;
    }
}
