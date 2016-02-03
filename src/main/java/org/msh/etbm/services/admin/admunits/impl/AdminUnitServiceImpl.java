package org.msh.etbm.services.admin.admunits.impl;

import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.objutils.ObjectDiffs;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.services.admin.admunits.*;
import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeries;
import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.UUID;

/**
 * Set of services to handle basic CRUD operations in administrative unit
 * Created by rmemoria on 24/10/15.
 */
@Service
public class AdminUnitServiceImpl extends EntityServiceImpl<AdministrativeUnit, AdminUnitQueryParams>
    implements AdminUnitService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    CodeGeneratorService codeGeneratorService;

    @Autowired
    CountryStructureService countryStructureService;

    @Autowired
    AdminUnitSeriesService adminUnitSeriesService;

    /**
     * Query the administrative units
     * @param q the query parameters
     * @return the result list
     */
    public AdminUnitQueryResult findMany(AdminUnitQueryParams q) {
        QueryBuilder qry = queryBuilderFactory.createQueryBuilder(AdministrativeUnit.class, "a");

        if (!AdminUnitQueryParams.QUERY_PROFILE_ITEM.equals(qry.getProfile())) {
            qry.setHqlJoin("join fetch a.countryStructure cs left join fetch a.parent p");
        }
        else {
            qry.setHqlJoin("join fetch a.countryStructure cs");
        }

        // add profiles
        qry.addDefaultProfile(AdminUnitQueryParams.QUERY_PROFILE_DEFAULT, AdminUnitData.class);
        qry.addProfile(AdminUnitQueryParams.QUERY_PROFILE_DETAILED, AdminUnitDetailedData.class);
        qry.addProfile(AdminUnitQueryParams.QUERY_PROFILE_ITEM, AdminUnitItemData.class);

        // add order by
        qry.addDefaultOrderByMap("name", "a.name");
        qry.addOrderByMap("code", "a.code");
        qry.addOrderByMap("parent", "a.parent.id");

        qry.initialize(q);

        // filter by the key
        qry.addLikeRestriction("a.name", q.getKey());

        // filter by the name
        qry.addRestriction("a.name = :name", q.getName());

        // criteria for selection based on parent
        // parent was defined ?
        if (q.getParentId() != null) {
            // include all child administrative units?
            if (q.isIncludeChildren()) {
                // get parent because it needs the code
                AdministrativeUnit parent = findEntity(q.getParentId());
                if (parent == null) {
                    rejectFieldException(q, "parentId", "InvalidValue");
                }
                // get all sub units using the parent code
                qry.addRestriction("a.code like :code", parent.getCode() + "%");

                // but exclude the own parent
                qry.addRestriction("a.id <> :parentid", q.getParentId());
            }
            else {
                qry.addRestriction("a.parent.id = :pid", q.getParentId());
            }
        }
        else {
            if (q.isRootUnits()) {
                qry.addRestriction("a.parent.id is null");
            }
        }

        // generate the result
        QueryResult res = qry.createQueryResult();
        AdminUnitQueryResult aures = new AdminUnitQueryResult();
        aures.setCount(res.getCount());
        aures.setList(res.getList());

        // must include country structure ?
        if (q.isFetchCountryStructure()) {
            CountryStructureQueryParams csquery = new CountryStructureQueryParams();
            csquery.setOrderBy("level");
            res = countryStructureService.findMany(csquery);
            aures.setCsList(res.getList());
        }

        return aures;
    }

    @Override
    protected void saveEntity(AdministrativeUnit entity) {
        boolean isNew = entity.getId() == null;

        super.saveEntity(entity);

        // update number of children in the parent administrative unit
        if (isNew && entity.getParent() != null) {
            entityManager.createQuery("update AdministrativeUnit set unitsCount = unitsCount + 1 where id = :id")
                    .setParameter("id", entity.getParent().getId())
                    .executeUpdate();
        }
    }

    @Override
    protected void deleteEntity(AdministrativeUnit entity) {
        super.deleteEntity(entity);

        // update number of children in the parent administrative unit
        if (entity.getParent() != null) {
            entityManager.createQuery("update AdministrativeUnit set unitsCount = unitsCount - 1 where id = :id")
                    .setParameter("id", entity.getParent().getId())
                    .executeUpdate();
        }
    }

    /**
     * This method is override because calling 'generateNewMethod' from prepareToSave, an entityManager.flush()
     * is called internally
     * @param request the request object, argument in the create or update method
     * @param entity the entity to be filled with values in the request
     */
    @Override
    protected void mapRequest(Object request, AdministrativeUnit entity) {
        AdminUnitFormData req = (AdminUnitFormData)request;

        // check if parent has changed
        UUID pid =  entity.getParent() != null? entity.getParent().getId(): null;
        boolean parentChanged = entity.getId() == null || !ObjectDiffs.compareEquals(req.getParentId(), pid);

        String newCode = null;
        if (parentChanged) {
            newCode = codeGeneratorService.generateNewCode(req.getParentId());
        }

        // call standard map
        super.mapRequest(request, entity);

        // new code was changed ?
        if (newCode != null) {
            // must update the children ?
            if (entity.getId() != null) {
                String oldCode = entity.getCode();
                updateChildCodes(entity.getId(), oldCode, newCode);
            }
            entity.setCode(newCode);
        }
    }


    @Override
    protected void prepareToSave(AdministrativeUnit entity, BindingResult errors) throws EntityValidationException {
        super.prepareToSave(entity, errors);
        if (errors.hasErrors()) {
            return;
        }

        if (entity.getCountryStructure() == null) {
            errors.rejectValue("csId", ErrorMessages.REQUIRED);
            return;
        }

        validateParent(entity);

        if (!isUnique(entity)) {
            errors.rejectValue("name", ErrorMessages.NOT_UNIQUE);
        }
    }

    /**
     * Check if administrative unit is unique
     * @param au the administrative unit to check if is unique
     * @return true if there is no other administrative unit with the same name in the branch
     */
    protected boolean isUnique(AdministrativeUnit au) {
        String hql = "select count(*) from AdministrativeUnit " +
                "where workspace.id = :wsid " +
                "and upper(name) = :name ";

        // set the condition to check just items from the same parent
        if (au.getParent() != null) {
            hql += " and parent.id = :parentid";
        }
        else {
            hql += " and parent.id is null";
        }

        // if admin unit already exists, exclude it from the query
        if (au.getId() != null) {
            hql += " and id <> :id";
        }

        Query qry = entityManager.createQuery(hql)
                .setParameter("wsid", getWorkspaceId())
                .setParameter("name", au.getName().toUpperCase());

        if (au.getParent() != null) {
            qry.setParameter("parentid", au.getParent().getId());
        }

        if (au.getId() != null) {
            qry.setParameter("id", au.getId());
        }

        Number count = (Number)qry.getSingleResult();
        return count.intValue() == 0;
    }

    /**
     * Update number of units under parent units
     * @param prevParent the previous parent unit
     * @param newParent the new parent unit
     */
    protected void updateUnitsCount(AdministrativeUnit prevParent, AdministrativeUnit newParent) {
        if (prevParent == newParent) {
            return;
        }

        // adjust count of previous parent
        prevParent.setUnitsCount( prevParent.getUnitsCount() - 1);

        // adjust count of new parent
        newParent.setUnitsCount( newParent.getUnitsCount() + 1);
    }


    /**
     * Update the code of the child records. This method must be called when the parent
     * of the administrative unit is being changed
     * @param id is the administrative unit ID being updated
     * @param oldCode the previous code of the administrative unit
     * @param newCode the new code of the administrative unit
     */
    protected void updateChildCodes(UUID id, String oldCode, String newCode) {
        int ini = newCode.length() + 1;
        entityManager.createQuery("update AdministrativeUnit " +
                "set code = concat(:newcode, substring(code, :ini, 16)) " +
                "where code like :oldcode and id <> :id")
                .setParameter("oldcode", oldCode + '%')
                .setParameter("newcode", newCode)
                .setParameter("ini", ini)
                .setParameter("id", id)
                .executeUpdate();
    }



    /**
     * Validate the administrative unit parent
     * @param au instance of AdministrativeUnit to be saved
     */
    protected void validateParent(AdministrativeUnit au) throws EntityValidationException{
        AdministrativeUnit parent = au.getParent();

        if (parent == null && au.getCountryStructure().getLevel() > 1) {
            raiseRequiredFieldException(au, "parent");
        }

        // check if parent is in the same level of country structure
        if (parent != null && au.getCountryStructure().getLevel() != parent.getLevel() + 1) {
            rejectFieldException(au, "countryStructure", "validation.au.invalidparent");
        }
    }

    @Override
    protected <K> K mapResponse(AdministrativeUnit entity, Class<K> resultClass) {
        if (AdminUnitSeries.class.isAssignableFrom(resultClass)) {
            return (K)adminUnitSeriesService.getAdminUnitSeries(entity);
        }

        return super.mapResponse(entity, resultClass);
    }
}
