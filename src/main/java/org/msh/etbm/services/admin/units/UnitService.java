package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.Laboratory;
import org.msh.etbm.db.entities.Tbunit;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.repositories.AdminUnitRepository;
import org.msh.etbm.db.repositories.UnitRepository;
import org.msh.etbm.services.admin.admunits.AdminUnitRequest;
import org.msh.etbm.services.admin.units.data.UnitItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * CRUD service to handle units (laboratories and TB units)
 *
 * Created by rmemoria on 31/10/15.
 */
@Service
public class UnitService extends EntityService<Unit, UnitRepository> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @PersistenceContext
    AdminUnitRepository adminUnitRepository;

    /**
     * Search for units based on the given query
     * @param qry unit query filters
     * @return list of units
     */
    public QueryResult<UnitItemData> findMany(UnitQuery qry) {
        Class clazz;
        if (qry.getType() != null) {
            clazz = qry.getType() == UnitType.TBUNIT? Tbunit.class : Laboratory.class;
        }
        else {
            clazz = Unit.class;
        }
        QueryBuilder<Unit> builder = queryBuilderFactory.createQueryBuilder(clazz);

        builder.addDefaultOrderByMap("name", "name");
        builder.addDefaultOrderByMap("adminUnit", "adminUnit.name");

        builder.addLikeRestriction("name", qry.getKey());

        builder.addRestriction("name = :name", qry.getName());

        // restrictions administrative unit
        if (qry.getAdminUnitId() != null) {
            // include children?
            if (qry.isAdminUnitChildren()) {
                AdministrativeUnit au = adminUnitRepository.findOne(qry.getAdminUnitId());
                if (au == null || !au.getWorkspace().getId().equals(getWorkspaceId())) {
                    throw new EntityValidationException("adminUnitId", "Invalid administrative unit");
                }
                // search for all administrative units
                builder.addRestriction("adminUnit.code like :code", au.getCode() + "%");
            }
            else {
                // search for units directly registered in this administrative unit
                builder.addRestriction("address.adminUnit.id = :auid", qry.getAdminUnitId());
            }
        }

        // laboratory filters
        builder.addRestriction("performCulture = :pefculture", qry.getPerformCulture());
        builder.addRestriction("performMicroscopy = :pefmic", qry.getPerformMicroscopy());
        builder.addRestriction("performDst = :pefdst", qry.getPerformDst());
        builder.addRestriction("performXpert = :pefxpert", qry.getPerformXpert());

        // TB unit filters
        builder.addRestriction("tbFacility = :tbfacility", qry.getTbFacility());
        builder.addRestriction("mdrFacility = :mdrfacility", qry.getMdrFacility());
        builder.addRestriction("ntmFacility = :ntmfacility", qry.getNtmFacility());
        builder.addRestriction("notificationUnit = :notifunit", qry.getNotificationUnit());

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
