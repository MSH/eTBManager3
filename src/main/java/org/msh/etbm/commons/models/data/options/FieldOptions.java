package org.msh.etbm.commons.models.data.options;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.forms.impl.JSGeneratorValueWrapper;

import java.util.List;

/**
 * Base class for representation and generation of options of a field
 * Created by rmemoria on 7/7/16.
 */
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = FieldRangeOptions.class, name = "range"),
//        @JsonSubTypes.Type(value = FieldListOptions.class, name = "list")
//    })
public abstract class FieldOptions implements JSGeneratorValueWrapper {

    @JsonIgnore
    public abstract List<Item> getOptionsValues();

    public abstract boolean isValueInOptions(Object value);

    @Override
    @JsonIgnore
    public Object getValueToGenerateJSCode() {
        return getOptionsValues();
    }
}
