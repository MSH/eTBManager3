package org.msh.etbm.commons.models.impl;

import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.handlers.FieldHandler;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.io.InputStream;
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

    private Field createFieldInstance(Map<String, Object> props) {
        String ftypeName = (String)props.get("type");
        FieldHandler handler = FieldTypeManager.instance().getHandler(ftypeName);
        if (handler == null) {
            throw new ModelException("Invalid field type: " + ftypeName);
        }

        Class<? extends Field> fieldClass = handler.getFieldClass();

        props.remove("type");

        return ObjectUtils.newInstance(fieldClass);
    }

}
