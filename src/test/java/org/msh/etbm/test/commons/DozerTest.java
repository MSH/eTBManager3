package org.msh.etbm.test.commons;

import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.msh.etbm.db.entities.Tbunit;
import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeries;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.usersession.UserRequestService;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rmemoria on 10/2/16.
 */
public class DozerTest extends AuthenticatedTest {

    @Autowired
    DozerBeanMapper mapper;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;


    @Test
    @Transactional
    public void testUnit() {
        // get the first unit
        UUID wsid = userRequestService.getUserSession().getWorkspaceId();
        List<Tbunit> lst = entityManager.createQuery("from Tbunit where workspace.id = :id")
                .setParameter("id", wsid)
                .setMaxResults(1)
                .getResultList();

        Tbunit unit = lst.get(0);

        UnitData data = mapper.map(unit, UnitData.class);
        assert(data instanceof UnitData);
        assertNotNull(data);
        assertEquals(data.getId(), unit.getId());
        assertEquals(data.getName(), unit.getName());
        assertEquals(data.getType(), UnitType.TBUNIT);

        assert(data.getAdminUnit() instanceof AdminUnitSeries);
    }
}
