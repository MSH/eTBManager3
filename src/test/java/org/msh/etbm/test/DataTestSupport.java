package org.msh.etbm.test;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.services.admin.AddressRequest;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;
import org.msh.etbm.services.admin.admunits.data.AdminUnitFormData;
import org.msh.etbm.services.admin.units.UnitService;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.admin.units.data.UnitFormData;
import org.msh.etbm.services.admin.userprofiles.UserProfileQueryParams;
import org.msh.etbm.services.admin.userprofiles.UserProfileService;
import org.msh.etbm.services.admin.usersws.UserWsService;
import org.msh.etbm.services.admin.usersws.data.UserWsData;
import org.msh.etbm.services.admin.usersws.data.UserWsFormData;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Simple component to generate data for testing support
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

    @Autowired
    UserWsService userWsService;


    /**
     * Create an administrative unit
     *
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
            AdministrativeUnit parent = (AdministrativeUnit) entityManager
                    .createQuery("from AdministrativeUnit where id = :id")
                    .setParameter("id", parentId)
                    .getSingleResult();
            level = parent.getLevel() + 2;
        } else {
            level = 1;
        }

        CountryStructure cs = null;
        for (CountryStructure item : cslist) {
            if (item.getLevel() == level) {
                cs = item;
                break;
            }
        }

        if (cs == null) {
            throw new RuntimeException("No country structure found to insert admin unit");
        }

        au.setName(Optional.of(name));
        au.setCountryStructure(Optional.of(cs.getId()));
        au.setParentId(parentId != null ? Optional.of(parentId) : Optional.<UUID>empty());
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

        for (SynchronizableItem item : res.getList()) {
            if (item.getName().toUpperCase().startsWith("ADMIN")) {
                return item;
            }
        }

        return null;
    }

    @Transactional
    public UserWsData createUser(String login, String email, String name) {
        UserWsFormData req = new UserWsFormData();
        req.setActive(Optional.of(true));
        req.setAdministrator(Optional.of(true));
        req.setEmail(Optional.of(email));
        req.setLogin(Optional.of(login));
        req.setName(Optional.of(name));

        List<UUID> profs = Arrays.asList(getAdminProfile().getId());
        req.setProfiles(Optional.of(profs));
        req.setPlayOtherUnits(Optional.of(true));
        req.setSendSystemMessages(Optional.of(true));

        AdminUnitData region = createAdminUnit("Region A", null);
        AdminUnitData city = createAdminUnit("City A", region.getId());
        UnitData unit = createTBUnit("Unit Test", city.getId());
        req.setUnitId(Optional.of(unit.getId()));

        ServiceResult res = userWsService.create(req);

        return userWsService.findOne(res.getId(), UserWsData.class);
    }
}
