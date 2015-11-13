package org.msh.etbm.services.admin.products;

import org.dozer.BeanFactory;
import org.msh.etbm.db.entities.Medicine;
import org.msh.etbm.db.entities.Product;

import java.util.Optional;

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
            Optional<ProductType> opt = ((ProductRequest) source).getType();
            if (opt != null && opt.get() == ProductType.MEDICINE) {
                return new Medicine();
            }
            else {
                return new Product();
            }
        }

        boolean isMed = source instanceof Medicine;

        Class clazz;
        try {
            clazz = Class.forName(s);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Object obj = createInstance(clazz);

//        // target is a product item ?
//        if (obj instanceof ProductItem) {
//            ProductItem item = (ProductItem)obj;
//            item.setMedicine(isMed);
//            return item;
//        }

        // target is a request?
        if (clazz == ProductRequest.class) {
            ProductRequest req = new ProductRequest();
            req.setType(Optional.of(isMed ? ProductType.MEDICINE: ProductType.PRODUCT));
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
