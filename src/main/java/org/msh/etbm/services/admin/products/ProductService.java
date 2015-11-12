package org.msh.etbm.services.admin.products;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Medicine;
import org.msh.etbm.db.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 11/11/15.
 */
@Service
public class ProductService extends EntityService<Product> {

    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_SHORTNAME = "shortName";

    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    /**
     * Return a list of products according to the given query parameters
     * @param qry the parameters to generate the query
     * @return list of products
     */
    public QueryResult<Product> findMany(ProductQuery qry) {
        Class clazz = qry.isMedicinesOnly()? Medicine.class: Product.class;

        QueryBuilder<Product> builder = queryBuilderFactory.createQueryBuilder(clazz);

        // order by options
        builder.addDefaultOrderByMap(ORDERBY_NAME, "name");
        builder.addOrderByMap(ORDERBY_SHORTNAME, "shortName");

        // profiles
        builder.addDefaultProfile(PROFILE_ITEM, ProductItem.class);
        builder.addProfile(PROFILE_DEFAULT, ProductData.class);

        builder.addLikeRestriction("name", qry.getKey());

        return builder.createQueryResult();
    }
}
