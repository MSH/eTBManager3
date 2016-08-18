package org.msh.etbm.commons.entities;

import org.dozer.ConfigurableCustomConverter;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.db.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Dozer custom convert that simplify conversion from/to Item and enum object
 * <p>
 * Created by mauricio on 11/08/16.
 */
@Component
public class DozerEnumConverter implements ConfigurableCustomConverter {

    @Autowired
    Messages messages;

    private String param;

    @Override
    public Object convert(Object dest, Object sourceVal, Class<?> destClass, Class<?> sourceClass) {
        Object source;
        if (Optional.class.isAssignableFrom(sourceClass)) {
            // if source is a null pointer of an optional, so it is considered that
            // the value was not informed. So return the dest value in order not to change
            // the destination value
            if (sourceVal == null) {
                return dest;
            }

            // unwrap optional
            source = ((Optional) sourceVal).isPresent() ? ((Optional) sourceVal).get() : null;
        } else {
            source = sourceVal;
        }

        if (source == null) {
            return null;
        }

        if (source instanceof MessageKey) {
            Object ret = new Item(source, messages.get(((MessageKey) source).getMessageKey()));
            return ret;
        }

        // maybe can remove this if
        if (source instanceof String && destClass.isAssignableFrom(Enum.class)) {
            return Enum.valueOf((Class<Enum>)destClass, (String)source);
        }

        throw new EntityConverterException(dest, source, "Invalid source type used in entity conversion: " + source.getClass());
    }

    public static <T extends Enum<T>> T getEnumInstance(final String value, final Class<T> enumClass) {
        return Enum.valueOf(enumClass, value);
    }

    @Override
    public void setParameter(String s) {
        this.param = s;
    }
}
