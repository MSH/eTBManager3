package org.msh.etbm.commons.entities.impl;

import org.dozer.CustomConverter;
import org.msh.etbm.db.Synchronizable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

/**
 * Dozer custom convert that simplify conversion from/to ID and entity object
 *
 * Created by rmemoria on 25/10/15.
 */
@Component
public class DozerEntityConverter implements CustomConverter {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Object convert(Object dest, Object source, Class<?> destClass, Class<?> sourceClass) {
        if (source == null) {
            return null;
        }

        // check if source is an optional value
        if (source instanceof Optional) {
            // unwrap value from optional
            Optional sourceOpt = (Optional)source;
            source = sourceOpt.isPresent() ? sourceOpt.get() : null;
        }

        // is an entity ID ?
        if (source instanceof UUID) {
            // from ID, get the source class
            return convertFromId((UUID) source, destClass);
        }

        // is the entity itself?
        if (source instanceof Synchronizable) {
            // get the ID from the entity
            return convertToId((Synchronizable)source, destClass);
        }

        return null;
    }

    /**
     * Convert from ID to entity object
     * @param id the ID of the entity
     * @param entityClass the class of the entity
     * @return
     */
    private Object convertFromId(UUID id, Class entityClass) {
        return entityManager.find(entityClass, id);
    }

    /**
     * Convert from entity to ID
     * @param source
     * @return
     */
    private Object convertToId(Synchronizable source, Class<?> destClass) {
        if (destClass == UUID.class) {
            return source.getId();
        }

        if (destClass == Optional.class) {
            return Optional.ofNullable(source.getId());
        }

        throw new RuntimeException("Destination class not supported: " + destClass);
    }
}
