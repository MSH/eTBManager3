package org.msh.etbm.test;

import org.dozer.CustomConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

import java.util.Optional;

/**
 * Created by rmemoria on 10/11/15.
 */
public class DozerOptionalConverter implements CustomConverter, MapperAware {

    private Mapper mapper;

    @Override
    public Object convert(Object dest, Object source, Class<?> destClass, Class<?> sourceClass) {
        // source is optional ?
        if (Optional.class.isAssignableFrom(sourceClass)) {
            Optional opt = (Optional)source;
            // if no optional is given, so dest should not be modified
            if (opt == null) {
                return dest;
            }

            // if optional is empty, return null
            if (opt == Optional.empty()) {
                return null;
            }

            Object objSource = ((Optional) source).get();

            if (objSource == null) {
                return null;
            }

            // source and dest are from different classes ?
            if (!destClass.isPrimitive() && !destClass.isAssignableFrom(objSource.getClass())) {
                return mapper.map(objSource, destClass);
            } else {
                return objSource;
            }
        } else {
            // dest must be optional
            return source == null ? Optional.empty() : Optional.of(source);
        }
    }

    @Override
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
}
