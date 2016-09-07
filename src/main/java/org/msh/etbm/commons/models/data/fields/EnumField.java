package org.msh.etbm.commons.models.data.fields;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.data.options.FieldListOptions;
import org.msh.etbm.commons.models.data.options.FieldOptions;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.MessageKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a field with enumerated values
 *
 * Created by rmemoria on 8/8/16.
 */
@FieldType("enum")
public class EnumField extends SingleField {

    private String enumClass;

    private FieldListOptions options;

    public String getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(String enumClass) {
        this.enumClass = enumClass;
    }

    public Class<? extends Enum> resolveEnumClass() {
        return ObjectUtils.forClass(getEnumClass());
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
        Class<? extends Enum> enumType = ObjectUtils.forClass(getEnumClass());

        if (enumType != null) {
            Enum[] enums = enumType.getEnumConstants();
            for (Enum val: enums) {
                String key = val instanceof MessageKey ?
                        ((MessageKey)val).getMessageKey() :
                        enumType.getSimpleName() + "." + val.toString();

                String txt = "${" + key + "}";
                lst.add(new Item(val, txt));
            }
        }

        FieldListOptions options = new FieldListOptions();
        options.setList(lst);

        return options;
    }


}
