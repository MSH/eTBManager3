package org.msh.etbm.commons.sync.server;

import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.objutils.StringConverter;
import org.msh.etbm.commons.sync.SynchronizationException;

import java.util.Date;
import java.util.UUID;

/**
 * Convert a java object to a JSON compatible value
 *
 * Created by rmemoria on 13/11/16.
 */
public class CompactibleJsonConverter {

    private static final String STRING_PREFIX = "S";
    private static final String DATETIME_PREFIX = "D";
    private static final String BINARY_PREFIX = "B";
    private static final String UUID_PREFIX = "U";


    public static Object convertToJson(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Boolean) {
            return val;
        }

        if (val instanceof Number) {
            return val;
        }

        if (val instanceof String) {
            return STRING_PREFIX + (String)val;
        }

        if (val instanceof Date) {
            return DATETIME_PREFIX + StringConverter.dateToString((Date)val);
        }

        if (val instanceof byte[]) {
            return BINARY_PREFIX + StringConverter.bytesToString((byte[])val);
        }

        if (val instanceof UUID) {
            byte[] data = ObjectUtils.uuidAsBytes((UUID)val);
            // TODO: [MSANTOS] verificar com o ricardo
            //return UUID_PREFIX + StringConverter.bytesToString((byte[])data);
            return BINARY_PREFIX + StringConverter.bytesToString((byte[])data);
        }

        if (val instanceof Enum) {
            return ((Enum)val).ordinal();
        }

        throw new SynchronizationException("Not supported type " + val.getClass());
    }

    public static Object convertFromJson(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof String) {
            String s = (String)val;
            String prefix = s.substring(0, 1);
            String content = s.substring(1);
            switch (prefix) {
                case STRING_PREFIX: return content;
                case BINARY_PREFIX: return StringConverter.stringToBytes(content);
                case DATETIME_PREFIX: return StringConverter.stringToDate(content);
                case UUID_PREFIX:
                    byte[] data = StringConverter.stringToBytes(content);
                    return ObjectUtils.bytesToUUID(data);
                default: throw new SynchronizationException("Unsupported value: " + s);
            }
        }

        return val;
    }
}
