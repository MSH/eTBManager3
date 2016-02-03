package org.msh.etbm.commons.entities;

/**
 * Exception thrown when a problem occurs during entity mapping in the {@link DozerEntityConverter} class
 *
 * Created by rmemoria on 2/2/16.
 */
public class EntityConverterException extends RuntimeException {
    private final transient Object source;
    private final transient Object dest;

    EntityConverterException(Object source, Object dest, String message) {
        super(message);
        this.source = source;
        this.dest = dest;
    }

    /**
     * Get the source object used in the mapping
     * @return Object instance
     */
    public Object getSource() {
        return source;
    }

    /**
     * Get the destination object used in the mapping
     * @return Object instance
     */
    public Object getDest() {
        return dest;
    }
}
