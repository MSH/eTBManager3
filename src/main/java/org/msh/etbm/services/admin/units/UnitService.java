package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Laboratory;
import org.msh.etbm.db.entities.Tbunit;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.repositories.UnitRepository;
import org.msh.etbm.services.admin.units.data.UnitItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 31/10/15.
 */
@Service
public class UnitService extends EntityService<Unit, UnitRepository> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    /**
     * Query units
     * @param qry
     * @return
     */
    public QueryResult<Unit> findMany(UnitQuery qry) {
        QueryBuilder<Unit> builder = queryBuilderFactory.createQueryBuilder(Unit.class);

        builder.addDefaultOrderByMap("name", "name");
        builder.addDefaultOrderByMap("adminUnit", "adminUnit.name");

        builder.addLikeRestriction("name", qry.getKey());

        if (qry.getName() != null && !qry.getName().isEmpty()) {
            builder.addRestriction("name = :name");
            builder.setParameter("name", qry.getName());
        }

        return builder.createQueryResult(UnitItemData.class);
    }

    @Override
    protected Unit createEntityInstance(Object req) {
        UnitRequest ureq = (UnitRequest)req;

        if (ureq.getType() == null) {
            raiseRequiredFieldException("type");
        }

        switch (ureq.getType()) {
            case LAB: return new Laboratory();
            case TBUNIT: return new Tbunit();
        }

        throw new EntityValidationException("type", "Unsupported type");
    }
}
