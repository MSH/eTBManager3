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

        if (source instanceof Collection) {
            return handleCollection(dest, (Collection)source, destClass);
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
     * @return Collection with the mapped objects
     */
    private Object handleCollection(Object dest, Collection source, Class<?> destClass) {
        boolean optional = destClass == Optional.class;

        if (!optional && !Collection.class.isAssignableFrom(destClass)) {
            throw new EntityConverterException(source, dest, "When a source is a collection, destination object must be a collection");
        }

        // get the list that will receive mapped values
        List list = dest != null ? (List)dest : new ArrayList();
        list.clear();

        if (param == null) {
            throw new EntityConverterException(source, dest, "Dozer mapping - field converter - Must inform custom-converter-param with the entity class");
        }

        Class entityClass = ObjectUtils.forClass(param);

        convertCollection(source, list, entityClass);

        return optional ? Optional.of(list) : list;
    }

    /**
     * Convert the items from one collection to another. <code>entityClass</code> is the class that objects will
     * be converted from or to, depending on the items in source. If source contains UUID instances, so entityClass
     * will be used as the destination class, otherwise, entityClass will be used to convert from the items
     * in source to UUID. The conversion is done using the method {@link DozerEntityConverter#convert(Object, Object, Class, Class)}
     *
     * @param source the source collection where items to be converted are
     * @param dest the destination collection, where converted items will be included
     * @param entityClass the entity class to convert or be converted from source
     */
    private void convertCollection(Collection source, Collection dest, Class entityClass) {
        for (Object obj: source) {
            Object result = obj instanceof UUID ?
                    convert(null, obj, entityClass, UUID.class) :
                    convert(null, obj, UUID.class, entityClass);

            if (result != null) {
                dest.add(result);
            }
        }
    }

    @Override
    public void setParameter(String s) {
        this.param = s;
    }
}
