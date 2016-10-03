package org.msh.etbm.services.admin.admunits.impl;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.objutils.DiffsUtils;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.services.admin.admunits.*;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;
import org.msh.etbm.services.admin.admunits.data.AdminUnitFormData;
import org.msh.etbm.services.admin.admunits.data.AdminUnitItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Set of services to handle basic CRUD operations in administrative unit
 * Created by rmemoria on 24/10/15.
 */
@Service
public class AdminUnitServiceImpl extends EntityServiceImpl<AdministrativeUnit, AdminUnitQueryParams>
        implements AdminUnitService {

    private static final String NAME_CHANGED = "nameChanged";
    private static final String PARENT_CHANGED = "parentChanged";
    private static final String PREVIOUS_PARENT = "previousParent";
    private static final String PREVIOUS_LEVEL = "prevLevel";

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    CountryStructureService countryStructureService;


    @Override
    protected void buildQuery(QueryBuilder<AdministrativeUnit> builder, AdminUnitQueryParams queryParams) {

    }

    /**
     * Query the administrative units
     *
     * @param q the query parameters
     * @return the result list
     */
    public AdminUnitQueryResult findMany(AdminUnitQueryParams q) {
        QueryBuilder qry = queryBuilderFactory.createQueryBuilder(AdministrativeUnit.class, "a");

        qry.setHqlJoin("join fetch a.countryStructure cs");

        // add profiles
        qry.addDefaultProfile(AdminUnitQueryParams.QUERY_PROFILE_DEFAULT, AdminUnitData.class);
        qry.addProfile(AdminUnitQueryParams.QUERY_PROFILE_ITEM, AdminUnitItemData.class);

        // add order by
        qry.addDefaultOrderByMap("name", "a.name");
        qry.addOrderByMap("code", "a.code");
        qry.addOrderByMap("parent", "a.pid0, a.pid1, a.pid2");

        qry.initialize(q);

        if (q.getWorkspaceId() != null) {
            qry.setWorkspaceId(q.getWorkspaceId());
        }

        // filter by the key
        qry.addLikeRestriction("a.name", q.getKey());

        // filter by the name
        qry.addRestriction("a.name = :name", q.getName());

        // criteria for selection based on parent
        // parent was defined ?
        if (q.getParentId() != null) {
            // get parent because it needs the code
            AdministrativeUnit parent = findEntity(q.getParentId());
            if (parent == null) {
                rejectFieldException(q, "parentId", "InvalidValue");
            }

            int level = parent.getLevel();

            // include all child administrative units?
            if (q.isIncludeChildren()) {
                if (level < 4) {
                    // get all sub units using the parent code (include children of children)
                    qry.addRestriction("a.pid" + level + " = :pid", q.getParentId());

                } else {
                    // TEMP -> When user asks the children of a parent of level 4 (last level)
                    qry.addRestriction("1 = 0");
                }
            } else {
                qry.addRestriction("a.pid" + parent.getLevel() + " = :pid and a.pid" +
                        (parent.getLevel() + 1) + " is null", q.getParentId());
            }
        } else {
            if (q.isRootUnits()) {
                qry.addRestriction("a.pid0 is null");
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
    public String getCommandType() {
        return CommandTypes.ADMIN_ADMINUNITS;
    }

    @Override
    protected void beforeSave(EntityServiceContext<AdministrativeUnit> context, Errors errors) {
        AdministrativeUnit entity = context.getEntity();

        validateParent(entity);

        if (!isUnique(entity)) {
            errors.rejectValue("name", Messages.NOT_UNIQUE);
        }

        boolean parentChanged = (boolean)context.getParameters().get(PARENT_CHANGED);
        if (parentChanged && entity.getUnitsCount() > 0 && entity.getLevel() == 4) {
            errors.rejectValue("parentId", null, "Cannot move administrative unit to this parent because it doesn' fit the children in this level");
        }
    }

    @Override
    protected void afterSave(EntityServiceContext<AdministrativeUnit> context, ServiceResult res) {
        boolean parentChanged = (boolean)context.getParameters().get(PARENT_CHANGED);
        boolean nameChanged = (boolean)context.getParameters().get(NAME_CHANGED);
        UUID prevParentId = (UUID)context.getParameters().get(PREVIOUS_PARENT);
        boolean isNew = context.getRequestedId() == null;

        AdministrativeUnit entity = context.getEntity();

        if (isNew || parentChanged) {
            // update number of children in the parent administrative unit
            entityManager.createQuery("update AdministrativeUnit set unitsCount = unitsCount + 1 where id = :id")
                    .setParameter("id", entity.getParentId())
                    .executeUpdate();
        }

        if (parentChanged) {
            // if parent changed, reduce the number of units in previous parent
            if (prevParentId != null) {
                entityManager.createQuery("update AdministrativeUnit set unitsCount = unitsCount - 1 where id = :id")
                        .setParameter("id", prevParentId)
                        .executeUpdate();
            }

        }

        if (parentChanged || nameChanged) {
            updateChildren(context);
        }

        super.afterSave(context, res);
    }

    /**
     * Update children of the entity
     * @param context
     */
    private void updateChildren(EntityServiceContext<AdministrativeUnit> context) {
        AdministrativeUnit entity = context.getEntity();

        // create list of parameters to set
        String[] expr = new String[8];
        Map<String, Object> params = new HashMap<>();

        int newLevel = context.getEntity().getLevel();
        int prevLevel = (int)context.getParameters().get(PREVIOUS_LEVEL);

        if (newLevel == prevLevel) {
            updateChildrenSameLevel(entity);
            return;
        }

        if (newLevel < prevLevel) {
            updateChildrenLowerLevel(prevLevel, entity);
            return;
        }

        updateChildrenUpperLevel(prevLevel, entity);
    }

    private void updateChildrenLowerLevel(int prevLevel, AdministrativeUnit adminUnit) {
        throw new EntityValidationException(adminUnit, "parentId", Messages.NOT_VALID, null);
    }

    private void updateChildrenUpperLevel(int prevLevel, AdministrativeUnit adminUnit) {
        throw new EntityValidationException(adminUnit, "parentId", Messages.NOT_VALID, null);
    }

    /**
     * Update children when new parent is in the same level
     * @param adminUnit
     */
    private void updateChildrenSameLevel(AdministrativeUnit adminUnit) {
        // create hql
        StringBuilder s = new StringBuilder();
        s.append("update AdministrativeUnit set");
        List<SynchronizableItem> lst = adminUnit.getParentsList(true);

        char delim = '\n';
        for (int index = 0; index < lst.size(); index ++) {
            String fieldId = "pid" + index;
            String fieldName = "pname" + index;
            s.append(delim).append(fieldId).append(" = :").append(fieldId)
                    .append(",").append(fieldName).append(" = :").append(fieldName);
            delim = ',';
        }
        s.append("\nwhere pid").append(adminUnit.getLevel()).append(" = :id");

        Query query = entityManager.createQuery(s.toString())
                .setParameter("id", adminUnit.getId());

        // pass parameters
        int i = 0;
        for (SynchronizableItem item: lst) {
            query.setParameter("pid" + i, item.getId())
                    .setParameter("pname" + i, item.getName());
        }

        query.executeUpdate();
    }

    @Override
    protected void afterDelete(EntityServiceContext<AdministrativeUnit> context, ServiceResult res) {
        AdministrativeUnit entity = context.getEntity();

        // update number of children in the parent administrative unit
        if (entity.getParentId() != null) {
            entityManager.createQuery("update AdministrativeUnit set unitsCount = unitsCount - 1 where id = :id")
                    .setParameter("id", entity.getParentId())
                    .executeUpdate();
        }
    }

    /**
     * This method is override because calling 'generateNewMethod' from prepareToSave, an entityManager.flush()
     * is called internally
     *
     * @param context object shared between interceptors during operation
     */
    @Override
    protected void mapRequest(EntityServiceContext<AdministrativeUnit> context) {
        AdministrativeUnit entity = context.getEntity();
        AdminUnitFormData req = (AdminUnitFormData) context.getRequest();

        boolean isNew = context.getRequestedId() == null;

        // check if name has changed
        String newName = req.getName() != null && req.getName().isPresent() ? req.getName().get() : null;
        boolean nameChanged = !isNew && newName != null ? !newName.equals(entity.getName()) : false;

        // check if parent has changed
        UUID newpid = req.getParentId() != null && req.getParentId().isPresent() ? req.getParentId().get() : null;
        UUID pid = entity.getParentId();
        boolean parentChanged = !isNew && !DiffsUtils.compareEquals(newpid, pid);

        context.getParameters().put(PREVIOUS_PARENT, entity.getParentId());
        context.getParameters().put(PARENT_CHANGED, parentChanged);
        context.getParameters().put(NAME_CHANGED, nameChanged);
        context.getParameters().put(PREVIOUS_LEVEL, entity.getLevel());

        // call standard map
        super.mapRequest(context);

        if (req.getParentId() != null && req.getParentId().isPresent()) {
            AdministrativeUnit parent = entityManager.find(AdministrativeUnit.class, req.getParentId().get());
            entity.setParent(parent);
        }
    }


    /**
     * Check if administrative unit is unique
     *
     * @param au the administrative unit to check if is unique
     * @return true if there is no other administrative unit with the same name in the branch
     */
    protected boolean isUnique(AdministrativeUnit au) {
        String hql = "select count(*) from AdministrativeUnit "
                + "where workspace.id = :wsid "
                + "and upper(name) = :name ";

        // set the condition to check just items from the same parent
        if (au.getParentId() != null) {
            hql += " and pid" + au.getLevel() + " = :parentid";
        } else {
            hql += " and pid0 is null";
        }

        // if admin unit already exists, exclude it from the query
        if (au.getId() != null) {
            hql += " and id <> :id";
        }

        Query qry = entityManager.createQuery(hql)
                .setParameter("wsid", getWorkspaceId())
                .setParameter("name", au.getName().toUpperCase());

        if (au.getParentId() != null) {
            qry.setParameter("parentid", au.getParentId());
        }

        if (au.getId() != null) {
            qry.setParameter("id", au.getId());
        }

        Number count = (Number) qry.getSingleResult();
        return count.intValue() == 0;
    }



    /**
     * Validate the administrative unit parent
     *
     * @param au instance of AdministrativeUnit to be saved
     */
    protected void validateParent(AdministrativeUnit au) throws EntityValidationException {
        AdministrativeUnit parent = au.getParentId() != null ? entityManager.find(AdministrativeUnit.class, au.getParentId()) : null;

        if (parent == null && au.getCountryStructure().getLevel() > 1) {
            raiseRequiredFieldException(au, "parentId");
        }

        // check if parent is in the same level of country structure
        if (parent != null && au.getCountryStructure().getLevel() != parent.getLevel() + 2) {
            rejectFieldException(au, "countryStructure", "validation.au.invalidparent");
        }
    }
}
