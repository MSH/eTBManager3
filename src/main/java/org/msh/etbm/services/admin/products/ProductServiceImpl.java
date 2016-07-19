package org.msh.etbm.services.admin.products;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.Medicine;
import org.msh.etbm.db.entities.Product;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

/**
 * Service component to handle CRUD operation on products and medicines
 * <p>
 * Created by rmemoria on 11/11/15.
 */
@Service
public class ProductServiceImpl extends EntityServiceImpl<Product, ProductQueryParams>
        implements ProductService {


    @Override
    protected void buildQuery(QueryBuilder<Product> builder, ProductQueryParams queryParams) {
        Class clazz = queryParams.getType() == ProductType.MEDICINE ? Medicine.class : Product.class;

        // order by options
        builder.addDefaultOrderByMap(ProductQueryParams.ORDERBY_NAME, "name");
        builder.addOrderByMap(ProductQueryParams.ORDERBY_SHORTNAME, "shortName");

        // profiles
        builder.addDefaultProfile(ProductQueryParams.PROFILE_DEFAULT, ProductData.class);
        builder.addProfile(ProductQueryParams.PROFILE_ITEM, ProductItem.class);
        builder.addProfile(ProductQueryParams.PROFILE_DETAILED, ProductDetailedData.class);

        builder.initialize(queryParams);

        builder.addLikeRestriction("name", queryParams.getKey());

        if (!queryParams.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }
    }


    @Override
    protected Product createEntityInstance(Object req) {
        if (req instanceof ProductFormData) {
            Optional<ProductType> optype = ((ProductFormData) req).getType();
            ProductType type = optype == null ? null : optype.get();
            if (type == ProductType.MEDICINE) {
                return new Medicine();
            } else {
                return new Product();
            }
        }
        return super.createEntityInstance(req);
    }

    @Override
    public String getCommandType() {
        return CommandTypes.ADMIN_PRODUCTS;
    }

    @Override
    protected void beforeSave(EntityServiceContext<Product> context, Errors errors) {
        Product product = context.getEntity();

        if (!checkUnique(product, "name")) {
            errors.rejectValue("name", Messages.NOT_UNIQUE);
        }

        if (!checkUnique(product, "shortName")) {
            errors.rejectValue("shortName", Messages.NOT_UNIQUE);
        }
    }
}
