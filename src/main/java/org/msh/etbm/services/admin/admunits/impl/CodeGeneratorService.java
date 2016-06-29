package org.msh.etbm.services.admin.admunits.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.msh.etbm.services.admin.admunits.AdminUnitFormData;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service to manage the creation of new codes to updated or recently created administrative
 * units. This control is necessary due to the difficult to generate different codes in multi thread requests
 * <p>
 * Created by rmemoria on 31/10/15.
 */
@Component
@Aspect
public class CodeGeneratorService {

    /**
     * Default value to be used as a NULL value
     */
    private static final UUID NULL_ID = new UUID(0, 0);

    private enum Oper { ADD, REM }

    /**
     * List of parent codes being managed
     */
    private Map<UUID, CodeRef> parentCodes = new HashMap<>();

    @PersistenceContext
    EntityManager entityManager;


    /**
     * Generate a new code for the given parent
     *
     * @param parentId
     * @return
     */
    public String generateNewCode(UUID parentId) {
        synchronized (this) {
            CodeRef ref;
            if (parentId != null) {
                ref = parentCodes.get(parentId);
            } else {
                ref = parentCodes.get(NULL_ID);
            }

            // check if code is already loaded
            if (ref.getCode() == null) {
                // if no, load the code from the database
                String code = retrieveLastCode(parentId);

                // if there is no code, i.e, it is the first child of the parent
                // calculate a new code to be used in the calculation
                if (code == null) {
                    code = getAdminUnitCode(parentId) + "000";
                }
                ref.setCode(code);
            }

            // generate a new code and return
            return ref.generateNewCode();
        }
    }

    @Around("execution(public * org.msh.etbm.commons.entities.EntityServiceImpl.create(..)) && target(org.msh.etbm.services.admin.admunits.impl.AdminUnitServiceImpl))")
    public Object adminUnitCreateInterceptor(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        AdminUnitFormData req = (AdminUnitFormData) args[0];

        UUID pid = req.getParentId() != null && req.getParentId().isPresent() ? req.getParentId().get() : null;

        return aroundCall(pid, pjp);
    }

    @Around("execution(public * org.msh.etbm.commons.entities.EntityServiceImpl.update(..)) && target(org.msh.etbm.services.admin.admunits.impl.AdminUnitServiceImpl))")
    public Object adminUnitUpdateInterceptor(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        AdminUnitFormData req = (AdminUnitFormData) args[1];

        UUID pid = req.getParentId() != null && req.getParentId().isPresent() ? req.getParentId().get() : null;

        return aroundCall(pid, pjp);
    }

    /**
     * Called around the invoking of update and create methods in AdminUnitService service
     *
     * @param parentId
     * @param pjp
     */
    private Object aroundCall(UUID parentId, ProceedingJoinPoint pjp) throws Throwable {
        addRef(parentId);
        try {
            return pjp.proceed();
        } finally {
            remRef(parentId);
        }
    }

    /**
     * Add a reference to a given parent ID being processed
     *
     * @param parentId the ID of the parent admin unit
     */
    protected void addRef(UUID parentId) {
        toggleRef(parentId, Oper.ADD);
    }

    /**
     * Remove the reference of a parent ID being processed
     *
     * @param parentId
     */
    protected void remRef(UUID parentId) {
        toggleRef(parentId, Oper.REM);
    }

    /**
     * Toggle the number of references to a parent ID. This operation is synchronized to avoid that
     * double references or invalid removals are done
     *
     * @param parentId
     * @param oper
     */
    protected void toggleRef(UUID parentId, Oper oper) {
        if (parentId == null) {
            parentId = NULL_ID;
        }

        // run synchronized to avoid that list is messed by concurrent calls
        synchronized (this) {
            CodeRef ref = parentCodes.get(parentId);

            if (ref == null) {
                ref = new CodeRef(parentId);
                parentCodes.put(parentId, ref);
            }

            if (oper == Oper.ADD) {
                ref.addRef();
            } else {
                ref.removeRef();
            }

            // if there is no reference count, so remove it from the "cache"
            if (ref.getRefCount() == 0) {
                parentCodes.remove(parentId);
            }
        }
    }

    /**
     * Retrieve the code from the parent ID
     *
     * @param parentId the parent ID to retrieve the code from
     * @return the code of the parent
     */
    private String retrieveLastCode(UUID parentId) {
        String cond;
        if (parentId == null) {
            cond = "where aux.parent is null";
        } else {
            cond = "where aux.parent.id = :id";
        }

        Query qry = entityManager
                .createQuery("select max(aux.code) from AdministrativeUnit aux " + cond);

        if (parentId != null) {
            qry.setParameter("id", parentId);
        }

        String code = (String) qry
                .getSingleResult();

        return code;
    }

    private String getAdminUnitCode(UUID id) {
        if (id == null) {
            return "";
        }

        String code = (String) entityManager
                .createQuery("select code from AdministrativeUnit  where id = :id")
                .setParameter("id", id)
                .getSingleResult();

        return code;
    }

}
