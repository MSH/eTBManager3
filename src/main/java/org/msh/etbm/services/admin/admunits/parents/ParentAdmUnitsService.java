package org.msh.etbm.services.admin.admunits.parents;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Service to support the resolution of parent administrative units in an efficient way
 *
 * Created by rmemoria on 16/12/15.
 */
@Service
public class ParentAdmUnitsService {

    @PersistenceContext
    EntityManager entityManager;

    public AdminUnitSeries getAdminUnitSeries(AdministrativeUnit adm) {
        return adm != null ? getAdminUnitSeries(adm.getId()) : null;
    }

    /**
     * Return the full list of parent administrative units (including the own unit)
     * @param adminUnitID
     * @return
     */
    public AdminUnitSeries getAdminUnitSeries(UUID adminUnitID) {
        if (adminUnitID == null) {
            return null;
        }

        Object[] vals = (Object[])entityManager
                .createQuery("select a.id, a.name, b.id, b.name, " +
                        "c.id, c.name, d.id, d.name, " +
                        "e.id, e.name " +
                        "from AdministrativeUnit a " +
                        "left JOIN a.parent b " +
                        "left JOIN b.parent c " +
                        "LEFT JOIN c.parent d " +
                        "LEFT JOIN d.parent e " +
                        "WHERE a.id=:id")
                .setParameter("id", adminUnitID)
                .getSingleResult();

        return createParentSeries(vals);
    }

    private AdminUnitSeries createParentSeries(Object[] vals) {
        AdminUnitSeries res = new AdminUnitSeries();
        if (vals[0] != null) {
            res.setP0(new SynchronizableItem((UUID)vals[0], (String)vals[1]));
        }

        if (vals[2] != null) {
            res.setP1(new SynchronizableItem((UUID)vals[2], (String)vals[3]));
        }

        if (vals[4] != null) {
            res.setP2(new SynchronizableItem((UUID)vals[4], (String)vals[5]));
        }

        if (vals[6] != null) {
            res.setP3(new SynchronizableItem((UUID)vals[6], (String)vals[7]));
        }

        if (vals[8] != null) {
            res.setP4(new SynchronizableItem((UUID)vals[8], (String)vals[9]));
        }

        return res;
    }
}
