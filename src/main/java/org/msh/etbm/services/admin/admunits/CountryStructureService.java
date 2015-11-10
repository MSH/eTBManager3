package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.messages.MessageList;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.db.repositories.CountryStructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;


/**
 * Interface of a service to handle CRUD operations in a country structure
 * Created by rmemoria on 24/10/15.
 */
@Service
public class CountryStructureService extends EntityService<CountryStructure, CountryStructureRepository> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;


    @Override
    protected void prepareToSave(CountryStructure entity, BindingResult bindingResult) throws EntityValidationException {
        super.prepareToSave(entity, bindingResult);

        // there are error messages ?
        if (bindingResult.hasErrors()) {
            return;
        }

        if (!checkUnique(entity, "name")) {
            bindingResult.rejectValue("name", "validation.duplicatedname");
        }
    }



    /**
     * Query the database based on the given query criterias
     * @param q the query criteria
     * @return instance of {@link QueryResult}
     */
    @Transactional
    public QueryResult<CountryStructureData> query(CountryStructureQuery q) {
        QueryBuilder<CountryStructure> qry = queryBuilderFactory.createQueryBuilder(CountryStructure.class);

        qry.addDefaultOrderByMap("name", "name");
        qry.addOrderByMap("level", "level, name");
        qry.addOrderByMap("level desc", "level desc, name desc");

        qry.initialize(q);

        // filter by the level
        if (q.getLevel() != null) {
            qry.addRestriction("level = :level");
            qry.setParameter("level", q.getLevel());
        }

        // filter by the name
        if (q.getName() != null) {
            qry.addRestriction("name = :name");
            qry.setParameter("name", q.getName());
        }

        QueryResult<CountryStructureData> res = qry.createQueryResult(CountryStructureData.class);

        return res;
    }
}
