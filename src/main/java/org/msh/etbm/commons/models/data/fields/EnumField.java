package org.msh.etbm.commons.models.data.fields;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.data.options.FieldListOptions;
import org.msh.etbm.commons.models.data.options.FieldOptions;
import org.msh.etbm.db.MessageKey;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a field with enumerated values
 *
 * Created by rmemoria on 8/8/16.
 */
@FieldType("enum")
public class EnumField extends SingleField {

    private Class<? extends Enum> enumClass;

    private FieldListOptions options;

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public FieldOptions getOptions() {
        if (options == null) {
            options = createOptions();
        }
        FieldListOptions options = new FieldListOptions();
        return super.getOptions();
    }

    protected FieldListOptions createOptions() {
        List<Item> lst = new ArrayList<>();

        if (enumClass != null) {
            Enum[] enums = enumClass.getEnumConstants();
            for (Enum val: enums) {
                String key = val instanceof MessageKey ?
                        ((MessageKey)val).getMessageKey() :
                        enumClass.getSimpleName() + "." + val.toString();

                String txt = "${" + key + "}";
                lst.add(new Item(val, txt));
            }
        }

        FieldListOptions options = new FieldListOptions();
        options.setList(lst);

        return options;
    }
}
