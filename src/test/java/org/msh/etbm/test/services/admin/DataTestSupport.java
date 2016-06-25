package org.msh.etbm.test.services.admin;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.services.admin.AddressRequest;
import org.msh.etbm.services.admin.admunits.AdminUnitData;
import org.msh.etbm.services.admin.admunits.AdminUnitFormData;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.admin.units.UnitService;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.admin.units.data.UnitFormData;
import org.msh.etbm.services.admin.userprofiles.UserProfileQueryParams;
import org.msh.etbm.services.admin.userprofiles.UserProfileService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by rmemoria on 24/6/16.
 */
@Component
public class DataTestSupport {

    @Autowired
    UnitService unitService;

    @Autowired
    AdminUnitService adminUnitService;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    UserProfileService userProfileService;


    /**
     * Create an administrative unit
     * @param name
     * @param parentId
     * @return
     */
    @Transactional
    public AdminUnitData createAdminUnit(String name, UUID parentId) {
        AdminUnitFormData au = new AdminUnitFormData();

        // select country structure
        List<CountryStructure> cslist = entityManager
                .createQuery("from CountryStructure where workspace.id = :id")
                .setParameter("id", userRequestService.getUserSession().getWorkspaceId())
                .getResultList();

        int level;
        if (parentId != null) {
            AdministrativeUnit parent = (AdministrativeUnit)entityManager
                    .createQuery("from AdministrativeUnit where id = :id")
                    .setParameter("id", parentId)
                    .getSingleResult();
            level = parent.getLevel() + 1;
        }
        else {
            level = 1;
        }

        CountryStructure cs = null;
        for (CountryStructure item: cslist) {
            if (item.getLevel() == level) {
                cs = item;
                break;
            }
        }

        if (cs == null) {
            throw new RuntimeException("No country structure found to insert admin unit");
        }

        au.setName(name);
        au.setCsId(cs.getId());
        au.setParentId(parentId);
        ServiceResult res = adminUnitService.create(au);

        return adminUnitService.findOne(res.getId(), AdminUnitData.class);
    }


    @Transactional
    public UnitData createTBUnit(String name, UUID adminUnitId) {
        UnitFormData unit = new UnitFormData();
        unit.setType(UnitType.TBUNIT);
        unit.setName(Optional.of(name));
        unit.setNumDaysOrder(Optional.of(90));
        unit.setNtmFacility(Optional.of(true));
        unit.setTbFacility(Optional.of(true));
        unit.setDrtbFacility(Optional.of(true));
        unit.setActive(Optional.of(true));

        AddressRequest addr = new AddressRequest();
        addr.setAdminUnitId(adminUnitId);
        addr.setAddress("Test");
        addr.setZipCode("20100-00");
        unit.setAddress(addr);

        ServiceResult res = unitService.create(unit);

        return unitService.findOne(res.getId(), UnitData.class);
    }

    @Transactional
    public SynchronizableItem getAdminProfile() {
        UserProfileQueryParams p = new UserProfileQueryParams();
        p.setProfile(UserProfileQueryParams.PROFILE_DEFAULT);
        QueryResult<SynchronizableItem> res = userProfileService.findMany(p);

        for (SynchronizableItem item: res.getList()) {
            if (item.getName().toUpperCase().startsWith("ADMIN")) {
                return item;
            }
        }

        return null;
    }
}
