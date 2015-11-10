package org.msh.etbm.test;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
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
        if (source == null) {
            return null;
        }

        if (source instanceof Optional) {
            Object objSource = ((Optional) source).get();

            if (objSource == null) {
                return null;
            }

            // source and dest are from different classes ?
            if (!destClass.isPrimitive() && !destClass.isAssignableFrom(objSource.getClass())) {
                return mapper.map(objSource, destClass);
            }
            else {
                return objSource;
            }
        }
        else {
            return Optional.of(dest);
        }
    }

    @Override
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
}
