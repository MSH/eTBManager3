package org.msh.etbm.services.admin.products;

import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Medicine;
import org.msh.etbm.db.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;

/**
 * Service component to handle CRUD operation on products and medicines
 *
 * Created by rmemoria on 11/11/15.
 */
@Service
public class ProductService extends EntityService<Product> {

    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_SHORTNAME = "shortName";

    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_DETAILED = "detailed";


    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    /**
     * Return a list of products according to the given query parameters
     * @param qry the parameters to generate the query
     * @return list of products
     */
    public QueryResult<Product> findMany(ProductQueryParams qry) {
        Class clazz = qry.getType() == ProductType.MEDICINE? Medicine.class: Product.class;

        QueryBuilder<Product> builder = queryBuilderFactory.createQueryBuilder(clazz);

        // order by options
        builder.addDefaultOrderByMap(ORDERBY_NAME, "name");
        builder.addOrderByMap(ORDERBY_SHORTNAME, "shortName");

        // profiles
        builder.addDefaultProfile(PROFILE_DEFAULT, ProductData.class);
        builder.addProfile(PROFILE_ITEM, ProductItem.class);
        builder.addProfile(PROFILE_DETAILED, ProductDetailedData.class);

        builder.initialize(qry);

        builder.addLikeRestriction("name", qry.getKey());

        if (!qry.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }

        return builder.createQueryResult();
    }

    @Override
    protected Product createEntityInstance(Object req) {
        if (req instanceof ProductFormData) {
            Optional<ProductType> optype = ((ProductFormData) req).getType();
            ProductType type = optype == null? null: optype.get();
            if (type == ProductType.MEDICINE) {
                return new Medicine();
            }
            else {
                return new Product();
            }
        }
        return super.createEntityInstance(req);
    }

    @Override
    protected void prepareToSave(Product entity, BindingResult bindingResult) {
        super.prepareToSave(entity, bindingResult);

        if (bindingResult.hasErrors()) {
            return;
        }

        if (!checkUnique(entity, "name")) {
            bindingResult.rejectValue("name", ErrorMessages.NOT_UNIQUE);
        }

        if (!checkUnique(entity, "shortName")) {
            bindingResult.rejectValue("shortName", ErrorMessages.NOT_UNIQUE);
        }
    }
}
