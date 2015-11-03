package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.impl.ObjectUtils;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.messages.MessageList;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.repositories.AdminUnitRepository;
import org.msh.etbm.services.admin.admunits.impl.CodeGeneratorService;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.UUID;

/**
 * Set of services to handle basic CRUD operations in administrative unit
 * Created by rmemoria on 24/10/15.
 */
@Service
public class AdminUnitService extends EntityService<AdministrativeUnit, AdminUnitRepository> {

    private static final String QUERY_PROFILE_ITEM = "item";
    private static final String QUERY_PROFILE_DEFAULT = "default";
    private static final String QUERY_PROFILE_EXT = "ext";

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    CodeGeneratorService codeGeneratorService;

    /**
     * Query the administrative units
     * @param q the query parameters
     * @return the result list
     */
    public QueryResult<AdminUnitData> findMany(AdminUnitQuery q) {
        QueryBuilder qry = queryBuilderFactory.createQueryBuilder(AdministrativeUnit.class, "a");

        if (!QUERY_PROFILE_ITEM.equals(qry.getProfile())) {
            qry.setHqlJoin("join fetch a.countryStructure cs left join fetch a.parent p");
        }
        else {
            qry.setHqlJoin("join fetch a.countryStructure cs");
        }

        // add profiles
        qry.addDefaultProfile(QUERY_PROFILE_DEFAULT, AdminUnitData.class);
        qry.addProfile(QUERY_PROFILE_EXT, AdminUnitExData.class);
        qry.addProfile(QUERY_PROFILE_ITEM, AdminUnitItemData.class);

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
                AdministrativeUnit parent = getCrudRepository().findOne(q.getParentId());
                if (parent == null) {
                    throw new EntityValidationException("Invalid parent id");
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

        QueryResult<AdminUnitData> res = qry.createQueryResult();

        return res;
    }

    /**
     * This method is override because calling 'generateNewMethod' from prepareToSave, an entityManager.flush()
     * is called internally
     * @param request
     * @param entity
     */
    @Override
    protected void mapRequest(Object request, AdministrativeUnit entity) {
        AdminUnitRequest req = (AdminUnitRequest)request;

        // check if parent has changed
        UUID pid =  entity.getParent() != null? entity.getParent().getId(): null;
        boolean parentChanged = entity.getId() == null || !ObjectUtils.isEqualValues(req.getParentId(), pid);

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
    protected void prepareToSave(AdministrativeUnit entity, MessageList errors) throws EntityValidationException {
        super.prepareToSave(entity, errors);
        if (errors.size() > 0) {
            return;
        }

        if (entity.getCountryStructure() == null) {
            errors.addRequired("csId");
            return;
        }

        validateParent(entity);

//        AdministrativeUnit parent = entity.getParent();

//        checkCode(entity);

        if (!isUnique(entity)) {
            errors.addNotUnique("name");
        }
    }

    /**
     * Check if administrative unit is unique
     * @param au
     * @return
     */
    protected boolean isUnique(AdministrativeUnit au) {
        String hql = "select count(*) from AdministrativeUnit " +
                "where workspace.id = :wsid " +
                "and upper(name) = :name " +
                "and countryStructure.level = :level";

        if (au.getId() != null) {
            hql += " and id <> :id";
        }

        Query qry = entityManager.createQuery(hql)
                .setParameter("wsid", getWorkspaceId())
                .setParameter("name", au.getName().toUpperCase())
                .setParameter("level", au.getCountryStructure().getLevel());

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
     * Check if the parent unit is being changed
     * @param au the administrative unit to be saved
     */
//    public void checkCode(AdministrativeUnit au) {
//        AdministrativeUnit parent = au.getParent();
//
//        if (au.getId() != null) {
//            System.out.println(" --> PARENT code = "+ (parent != null? parent.getCode(): "") + ", ID = " + (parent != null? parent.getId(): null));
//            System.out.println(" --> AU code = " + au.getCode());
//        }
//
//        // check if code must be generated
//        if (au.getCode() == null || au.getCode().isEmpty() ||
//                (parent != null && !parent.isSameOrChildCode(au.getCode())))
//        {
//            System.out.println("Calling new code");
//            String code = codeGeneratorService.generateNewCode(parent != null? parent.getId(): null);
//            System.out.println("New code = " + code);
//
//            // must update child records ?
//            if (au.getId() != null) {
//                String oldCode = au.getCode();
//                System.out.println("   ***  TRANSFER CODE " + oldCode + " -> " + code);
//                updateChildCodes(au.getId(), oldCode, code);
//            }
//            au.setCode(code);
//        }
//    }


    /**
     * Validate the administrative unit parent
     * @param au instance of AdministrativeUnit to be saved
     */
    protected void validateParent(AdministrativeUnit au) throws EntityValidationException{
        AdministrativeUnit parent = au.getParent();

        if (parent == null && au.getCountryStructure().getLevel() > 1) {
            throw new EntityValidationException("parentId", "parentId must be informed for the given country structure level");
        }

        // check if parent is in the same level of country structure
        if (parent != null && au.getCountryStructure().getLevel() != parent.getLevel() + 1) {
            System.out.println("ADMIN UNIT -> " + au.getName());
            System.out.println("COUNTRY STRUCTURE -> " + au.getCountryStructure());
            System.out.println("PARENT -> " + parent.getName() + ", level = " + parent.getLevel());
            throw new EntityValidationException("csId", "Country structure is not compactible with parent");
        }
    }

}
