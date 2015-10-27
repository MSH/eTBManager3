package org.msh.etbm.services.admin.admunits.impl;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.admin.admunits.AdminUnitData;
import org.msh.etbm.services.admin.admunits.AdminUnitQuery;
import org.msh.etbm.services.admin.admunits.AdminUnitRequest;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.usersession.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Set of services to handle basic CRUD operations in administrative unit
 * Created by rmemoria on 21/10/15.
 */
//@Service
public class AdminUnitImpl {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    UserSession userSession;

    @Transactional
    @CommandLog(type = AdminUnitLogHandler.CREATE, handler = AdminUnitLogHandler.class)
    public UUID create(@Valid AdminUnitRequest req) {
        AdministrativeUnit adm = new AdministrativeUnit();

        updateValues(req, adm);

        return adm.getId();
    }


    @Transactional
    @CommandLog(type = AdminUnitLogHandler.UPDATE, handler = AdminUnitLogHandler.class)
    public UUID update(UUID id, @Valid AdminUnitRequest req) {
        AdministrativeUnit adm = entityManager.find(AdministrativeUnit.class, id);

        updateValues(req, adm);
        return id;
    }

    /**
     * Update the values from the request to the given administrative unit
     * @param req
     * @param adm
     */
    private UUID updateValues(AdminUnitRequest req, AdministrativeUnit adm) {
        // is a new record ?
        boolean bNew = adm.getId() == null;

        adm.setName(req.getName());

        CountryStructure cs = entityManager.find(CountryStructure.class, req.getCsId());

        // validate and get new parent
        AdministrativeUnit prevParent = adm.getParent();
        AdministrativeUnit parent = validateParent(cs, req.getParentId());

        adm.setParent(parent);

        // must create new code?
        boolean bCreateNewCode = bNew || isNewParent(prevParent, req.getParentId());
        if (bCreateNewCode) {
            String code = generateNewCode(parent);
            // must update child records ?
            if (!bNew) {
                String oldCode = adm.getCode();
                updateChildCodes(adm.getId(), oldCode, code);
            }

            adm.setCode(code);
        }

        // adjust units count
        updateUnitsCount(prevParent, parent);

        // check workspace for existing admin unit and set the workspace for new admin unit
        updateWorkspace(adm);

        entityManager.persist(adm);
        entityManager.flush();

        return adm.getId();
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
                .executeUpdate();
    }

    /**
     * Check if the parent unit is being changed
     * @param currentParent the current parent administrative unit
     * @param newParentId the new ID for the parent unit
     * @return true if the administrative unit is changing
     */
    public boolean isNewParent(AdministrativeUnit currentParent, UUID newParentId) {
        if (currentParent == null) {
            return newParentId != null;
        }

        return !currentParent.getId().equals(newParentId);
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
     * Update the workspace of the record being created or updated
     * @param adm instance of {@link AdministrativeUnit}
     */
    protected void updateWorkspace(AdministrativeUnit adm) {
        // set the administrative unit workspace
        Workspace ws = entityManager.find(Workspace.class, userSession.getUserWorkspace().getWorkspace().getId());
        if (adm.getWorkspace() == null) {
            adm.setWorkspace(ws);
        }
        else {
            if (!ws.getId().equals(userSession.getUserWorkspace().getWorkspace().getId())) {
                throw new IllegalArgumentException("Invalid workspace");
            }
        }
    }

    /**
     * Validate the administrative unit parent
     * @param cs the country structure
     * @param parentId the parent id
     * @return the parent, or
     */
    protected AdministrativeUnit validateParent(CountryStructure cs, UUID parentId) {
        AdministrativeUnit parent;
        if (parentId != null) {
            parent = entityManager.find(AdministrativeUnit.class, parentId);
        }
        else {
            if (cs.getLevel() > 1) {
                throw new IllegalArgumentException("parentId must be informed for the given country structure level");
            }
            return null;
        }

        // check if parent is in the same level of country structure
        if (cs.getLevel() != parent.getLevel() + 1) {
            throw new IllegalArgumentException("Country structure is not compactible with parent");
        }

        return parent;
    }

    /**
     * Delete the administrative unit
     * @param id the ID of the administrative unit to be removed
     * @return data of the deleted administrative unit
     */
    @Transactional
    @CommandLog(type = AdminUnitLogHandler.DELETE, handler = AdminUnitLogHandler.class)
    public AdminUnitData delete(UUID id) {
        AdministrativeUnit adm = entityManager.find(AdministrativeUnit.class, id);

        entityManager.remove(adm);
        AdminUnitData data = mapper.map(adm, AdminUnitData.class);

        return data;
    }


    /**
     * Return data about an specific administrative unit
     * @param id
     * @return
     */
    public AdminUnitData get(UUID id) {
        AdministrativeUnit adm = entityManager.find(AdministrativeUnit.class, id);
        AdminUnitData data = mapper.map(adm, AdminUnitData.class);
        return data;
    }

    public List<AdminUnitData> query(AdminUnitQuery qry) {
        List<AdministrativeUnit> lst = entityManager.createQuery("from AdministrativeUnit ")
                .getResultList();

        List<AdminUnitData> res = new ArrayList<>();
        mapper.map(lst, res);

        return res;
    }
}
