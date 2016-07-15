package org.msh.etbm.commons.entities;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by rmemoria on 17/6/16.
 */
public class EntityUtils {

    private EntityUtils() {

    }

    /**
     * Convert an array of bytes to an UUID object
     *
     * @param val an array of bytes
     * @return instance of UUID
     */
    public static UUID bytesToUUID(byte[] val) {
        ByteBuffer bb = ByteBuffer.wrap(val);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }
}
