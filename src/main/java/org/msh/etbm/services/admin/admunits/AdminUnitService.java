package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.messages.MessageList;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.db.repositories.AdminUnitRepository;
import org.msh.etbm.services.admin.admunits.impl.CodeUtils;
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

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    /**
     * Query the administrative units
     * @param q the query parameters
     * @return the result list
     */
    public QueryResult<AdminUnitData> findMany(AdminUnitQuery q) {
        QueryBuilder qry = queryBuilderFactory.createQueryBuilder(AdministrativeUnit.class);

        qry.addOrderByMap("name", "name", true);

        qry.initialize(q);

        // filter by the key
        qry.addLikeRestriction("name", q.getKey());

        // filter by the name
        if (q.getName() != null) {
            qry.addRestriction("name = :name");
            qry.setParameter("name", q.getName());
        }

        // criteria for selection based on parent
        // parent was defined ?
        if (q.getParentId() != null) {
            // include all child administrative units?
            if (q.isIncludeChildren()) {
                // get parent because it needs the code
                AdministrativeUnit parent = getCrudRepository().findOne(q.getParentId());
                if (parent == null) {
                    throw new IllegalArgumentException("Invalid parent id");
                }
                // get all sub units using the parent code
                qry.addRestriction("code like :code");
                qry.setParameter("code", parent.getCode() + "%");
                // but exclude the own parent
                qry.addRestriction("id <> :parentid");
                qry.setParameter("parentid", q.getParentId());
            }
            else {
                qry.addRestriction("parent.id = :pid");
                qry.setParameter("pid", q.getParentId());
            }
        }
        else {
            if (q.isRootUnits()) {
                qry.addRestriction("parent.id is null");
            }
        }


        QueryResult<AdminUnitData> res = qry.createQueryResult(AdminUnitData.class);

        return res;
    }


    @Override
    protected void prepareToSave(AdministrativeUnit entity, MessageList errors) throws EntityValidationException {
        super.prepareToSave(entity, errors);
        if (errors.size() > 0) {
            return;
        }

        validateParent(entity);

        AdministrativeUnit parent = entity.getParent();

        checkCode(entity);

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
        int ini = newCode.length();
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
    public void checkCode(AdministrativeUnit au) {
        AdministrativeUnit parent = au.getParent();

        // check if code must be generated
        if (au.getCode() == null || au.getCode().isEmpty() ||
                (parent != null && !au.isSameOrChildCode(parent.getCode())))
        {
            String code = generateNewCode(parent);
            // must update child records ?
            if (au.getId() == null) {
                String oldCode = au.getCode();
                updateChildCodes(au.getId(), oldCode, code);
            }
            au.setCode(code);
        }
    }

    /**
     * Generate a new code
     * @param parent
     * @return
     */
    protected String generateNewCode(AdministrativeUnit parent) {
        String cond;
        if (parent == null)
            cond = "where aux.parent is null";
        else cond = "where aux.parent.id = " + parent.getId();

        String code = (String)entityManager
                .createQuery("select max(aux.code) from AdministrativeUnit aux " + cond)
                .getSingleResult();

        if (code != null) {
            if (code.length() > 3) {
                int len = code.length();
                code = code.substring(len-3, len);
            }
            code = CodeUtils.incCode(code);
            if (code.length() > 3) {
                throw new IllegalArgumentException("Maximum code length reached");
            }

            if (parent != null)
                code = parent.getCode() + code;
        }
        else {
            if (parent == null)
                code = "001";
            else code = parent.getCode() + "001";
        }

        return code;
    }

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
            throw new EntityValidationException("csId", "Country structure is not compactible with parent");
        }
    }

}
