package org.msh.etbm.commons.models.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.forms.controls.Control;
import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.handlers.FieldHandler;
import org.msh.etbm.commons.models.data.options.FieldListOptions;
import org.msh.etbm.commons.models.data.options.FieldOptions;
import org.msh.etbm.commons.models.data.options.FieldRangeOptions;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Parse a JSON data into an instance of a {@link Model} class
 *
 * Created by rmemoria on 7/8/16.
 */
public class JsonModelParser extends StandardJSONParser<Model> {

    /**
     * Parse a JSON representation of a {@link Model} from an input stream
     * @param in input stream delivering the JSON data
     * @return instance of {@link Model}
     */
    public Model parse(InputStream in) {
        Model model = parseInputStream(in);
        return model;
    }


    @Override
    protected <K> K createObjectInstance(Map<String, Object> props, Class<K> beanClass) {
        if (Field.class.isAssignableFrom(beanClass)) {
            return (K)createFieldInstance(props);
        }

        return super.createObjectInstance(props, beanClass);
    }

    @Override
    protected <E> E convertValue(Object value, Class<E> targetClass, java.lang.reflect.Field field) {
        if (targetClass == FieldOptions.class) {
            return (E)convertToFieldOptions(value);
        }
        return super.convertValue(value, targetClass, field);
    }

    private Field createFieldInstance(Map<String, Object> props) {
        String ftypeName = (String)props.get("type");
        FieldHandler handler = FieldTypeManager.instance().getHandler(ftypeName);
        Class<? extends Field> fieldClass = handler.getFieldClass();

        props.remove("type");

        return ObjectUtils.newInstance(fieldClass);
    }

    private FieldOptions convertToFieldOptions(Object source) {
        // is a list of options ?
        if (source instanceof Collection) {
            return collectionToFieldOptions((Collection<Map>)source);
        }

        if (source instanceof Map) {
            return mapToFieldOptions((Map)source);
        }

        throw new ModelException("Invalid field options representation: " + source);
    }

    private FieldOptions mapToFieldOptions(Map<String, Object> props) {
        if (props.size() == 2 && props.containsKey("from") && props.containsKey("to")) {
            FieldRangeOptions range = new FieldRangeOptions();
            range.setIni((Integer)props.get("from"));
            range.setEnd((Integer)props.get("to"));
            return range;
        }

        throw new ModelException("Invalid options properties: " + props);
    }

    private FieldOptions collectionToFieldOptions(Collection<Map> collection) {
        FieldListOptions options = new FieldListOptions();

        List<Item> list = new ArrayList<>();
        for (Map props: collection) {
            Item item = new Item(props.get("id"), (String)props.get("name"));
            list.add(item);
        }

        options.setList(list);

        return options;
    }
}
