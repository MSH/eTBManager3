package org.msh.etbm.services.admin.products;

import org.dozer.BeanFactory;
import org.msh.etbm.db.entities.Medicine;
import org.msh.etbm.db.entities.Product;

/**
 * Bean factory to create new request/medicine according to the condition if
 * the source represents a product or a medicine
 *
 * Created by rmemoria on 11/11/15.
 */
public class DozerProductFactory implements BeanFactory {
    @Override
    public Object createBean(Object source, Class<?> aClass, String s) {

        // source is a product request
        if (source instanceof ProductRequest) {
            Boolean ismed = ((ProductRequest) source).getMedicine();
            if (ismed != null && ismed == Boolean.TRUE) {
                return new Medicine();
            }
            else {
                return new Product();
            }
        }

//        if (source instanceof Product) {
//            throw new RuntimeException("invalid type " + source.getClass());
//        }

        boolean isMed = source instanceof Medicine;

        // target is a product item ?
        if (ProductItem.class.isAssignableFrom(aClass)) {
            ProductItem item = (ProductItem)createInstance(aClass);
            item.setMedicine(isMed);
            return item;
        }

        // target is a request?
        if (aClass == ProductRequest.class) {
            ProductRequest req = new ProductRequest();
            req.setMedicine(isMed);
            return req;
        }

        return null;
    }

    /**
     * Create a new instance of the given class without worrying about exceptions
     * @param clazz the class to create an instance of
     * @return instance of the given class
     */
    protected <E> E createInstance(Class<E> clazz) {
        try {
            return (E)clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
