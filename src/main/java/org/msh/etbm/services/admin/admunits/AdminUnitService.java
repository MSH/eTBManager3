package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.messages.MessageList;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.repositories.AdminUnitRepository;
import org.msh.etbm.services.admin.admunits.impl.CodeUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Set of services to handle basic CRUD operations in administrative unit
 * Created by rmemoria on 24/10/15.
 */
@Service
public class AdminUnitService extends EntityService<AdministrativeUnit, AdminUnitRepository> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected boolean isUniqueEntity(AdministrativeUnit entity) {
        return super.isUniqueEntity(entity);
    }

    @Override
    protected void prepareToSave(AdministrativeUnit entity, MessageList errors) {
        super.prepareToSave(entity, errors);
        if (errors.size() > 0) {
            return;
        }

        validateParent(entity);

        AdministrativeUnit parent = entity.getParent();

        checkCode(entity);
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
    protected void validateParent(AdministrativeUnit au) {
        AdministrativeUnit parent = au.getParent();

        if (parent == null && au.getCountryStructure().getLevel() > 1) {
            throw new IllegalArgumentException("parentId must be informed for the given country structure level");
        }

        // check if parent is in the same level of country structure
        if (parent != null && au.getCountryStructure().getLevel() != parent.getLevel() + 1) {
            throw new IllegalArgumentException("Country structure is not compactible with parent");
        }
    }

}
