package org.msh.etbm.commons.entities;

import org.dozer.ConfigurableCustomConverter;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.Synchronizable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Dozer custom convert that simplify conversion from/to ID and entity object
 *
 * Created by rmemoria on 25/10/15.
 */
@Component
public class DozerEntityConverter implements ConfigurableCustomConverter {

    @PersistenceContext
    EntityManager entityManager;

    private String param;

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

        if (source instanceof Collection) {
            return handleCollection(dest, (Collection)source, destClass, sourceClass);
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

        throw new EntityConverterException(dest, source, "Invalid source type used in entity conversion: " + source.getClass());
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

    /**
     * Handle mapping of entities in a collection
     * @param dest the destination object
     * @param source the source object
     * @param destClass the destination class
     * @param sourceClass the source class
     * @return Collection with the mapped objects
     */
    private Object handleCollection(Object dest, Collection source, Class<?> destClass, Class<?> sourceClass) {
        boolean optional = destClass == Optional.class;

        if (!optional && !Collection.class.isAssignableFrom(destClass)) {
            throw new EntityConverterException(source, dest, "When a source is a collection, destination object must be a collection");
        }

        // get the list that will receive mapped values
        List list = dest != null ? (List)dest : new ArrayList<>();
        list.clear();

        if (param == null) {
            throw new EntityConverterException(source, dest, "Dozer mapping - field converter - Must inform custom-converter-param with the entity class");
        }

        Class entityClass = ObjectUtils.forClass(param);
        Class destType = null; // ObjectUtils.getGenericType(destClass, 0);
        Class sourceType = null; // ObjectUtils.getGenericType(sourceClass, 0);

        for (Object obj: source) {
            Object result = obj instanceof UUID ?
                    convert(null, obj, entityClass, UUID.class) :
                    convert(null, obj, UUID.class, entityClass);

            if (result != null) {
                list.add(result);
            }
        }

        return optional ? Optional.of(list) : list;
    }

    @Override
    public void setParameter(String s) {
        this.param = s;
    }
}
