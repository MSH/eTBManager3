package org.msh.etbm.commons;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;

/**
 * Created by rmemoria on 4/9/15.
 */
@Component
public class Mapper {

    private DozerBeanMapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
    }

    /**
     * Copy the properties of an object to another of the given class
     * @param obj
     * @param <E>
     * @param clazz
     * @return
     */
    public <E> E map(Object obj, Class<E> clazz) {
        return mapper.map(obj, clazz);
    }

    /**
     * Create a clone of the object doing a deep copy of object properties
     * @param obj
     * @param <E>
     * @return
     */
    public <E> E deepCopy(E obj) {
        return mapper.map(obj, (Class<E>)obj.getClass());
    }

    /**
     * Make a shallow copy of the object, i.e, all properties will be copied as reference
     * @param obj the object to copy
     * @param <E>
     * @return
     */
    public <E> E shallowCopy(E obj) {
        try{
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(obj));
            }
            return (E)clone;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
