package org.msh.etbm.services.admin.admunits;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.EntityQuery;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.messages.MessageList;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.db.repositories.CountryStructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


/**
 * Interface of a service to handle CRUD operations in a country structure
 * Created by rmemoria on 24/10/15.
 */
@Service
public class CountryStructureService extends EntityService<CountryStructure, CountryStructureRepository> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    DozerBeanMapper mapper;

    @Override
    protected void prepareToSave(CountryStructure entity, MessageList msgs) throws EntityValidationException {
        super.prepareToSave(entity, msgs);

        // there are error messages ?
        if (msgs.size() > 0) {
            return;
        }

        if (!checkUnique(entity, "name")) {
            msgs.addNotUnique("name");
        }
    }

    /**
     * Check if the given entity is unique in the database
     * @param cs
     * @return
     */
//    protected boolean isUniqueEntity(CountryStructure cs) {
//        CountryStructureRepository rep = getCrudRepository();
//        List<CountryStructure> lst = rep.findByNameAndWorkspaceIdAndLevel(cs.getName(),
//                getWorkspaceId(),
//                cs.getLevel());
//
//        UUID id = cs.getId();
//
//        if (id == null && lst.size() > 0) {
//            return false;
//        }
//
//        for (CountryStructure aux: lst) {
//            if (!aux.getId().equals(id)) {
//                return false;
//            }
//        }
//        return true;
//    }


    /**
     * Query the database based on the given query criterias
     * @param q the query criteria
     * @return instance of {@link QueryResult}
     */
    @Transactional
    public QueryResult<CountryStructureData> query(CountryStructureQuery q) {
        QueryBuilder<CountryStructure> qry = queryBuilderFactory.createQueryBuilder(CountryStructure.class);

        qry.addOrderByMap("name", "name", true);
        qry.addOrderByMap("level", "level, name", false);
        qry.addOrderByMap("level desc", "level desc, name desc", false);

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
