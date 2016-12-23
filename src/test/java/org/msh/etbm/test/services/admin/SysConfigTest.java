package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.services.admin.sysconfig.SysConfigData;
import org.msh.etbm.services.admin.sysconfig.SysConfigFormData;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.services.session.usersession.UserSession;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by rmemoria on 8/4/16.
 */
public class SysConfigTest extends AuthenticatedTest {

    private static final String ADMIN_MAIL = "rmemoria@gmail.com";
    private static final boolean ALLOW_REG_PAGE = true;
    private static final String PAGE_ROOT = "https://www.etbmanager.org/etbm3";
    private static final boolean ULA_ACTIVE = true;


    @Autowired
    SysConfigService service;

    @Autowired
    UserRequestService userRequestService;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Simple test of configuration changes to check if the service load and write configuration
     */
    @Test
    public void testReadWrite() {
        // load the service to change it later
        SysConfigFormData cfg = service.loadFormConfig();

        UserSession session = userRequestService.getUserSession();
        UUID profileId = getUserProfile();

        assertTrue(cfg.getAdminMail().isPresent());
        assertTrue(cfg.getAllowRegPage().isPresent());

        cfg.setAdminMail(Optional.of(ADMIN_MAIL));
        cfg.setAllowRegPage(Optional.of(ALLOW_REG_PAGE));
        cfg.setSystemURL(Optional.of(PAGE_ROOT));
        cfg.setUlaActive(Optional.of(ULA_ACTIVE));
        cfg.setWorkspace(Optional.of(session.getWorkspaceId()));
        cfg.setUserProfile(Optional.of(profileId));
        cfg.setUnit(Optional.of(session.getUnitId()));

        // update the new configuration
        service.updateConfig(cfg);

        SysConfigFormData cfg2 = service.loadFormConfig();
        assertNotNull(cfg2);

        // check if configuration is equals the one saved
        assertEquals(cfg.getAdminMail(), cfg2.getAdminMail());
        assertEquals(cfg.getAllowRegPage(), cfg2.getAllowRegPage());
        assertEquals(cfg.getSystemURL(), cfg2.getSystemURL());
        assertEquals(cfg.getUlaActive(), cfg2.getUlaActive());
        assertEquals(session.getWorkspaceId(), cfg2.getWorkspace().orElse(null));
        assertEquals(session.getUnitId(), cfg2.getUnit().orElse(null));
        assertEquals(profileId, cfg2.getUserProfile().orElse(null));

        SysConfigData cfg3 = service.loadConfig();

        assertEquals(ADMIN_MAIL, cfg3.getAdminMail());
        assertEquals(ALLOW_REG_PAGE, cfg3.isAllowRegPage());
        assertEquals(PAGE_ROOT, cfg3.getSystemURL());
        assertEquals(ULA_ACTIVE, cfg3.isUlaActive());

        assertNotNull(cfg3.getWorkspace());
        assertNotNull(cfg3.getWorkspace().getId());
        assertNotNull(cfg3.getWorkspace().getName());
        assertEquals(session.getWorkspaceId(), cfg3.getWorkspace().getId());

        assertNotNull(cfg3.getUnit());
        assertNotNull(cfg3.getUnit().getId());
        assertNotNull(cfg3.getUnit().getName());

        assertEquals(session.getUnitId(), cfg3.getUnit().getId());
        assertNotNull(cfg3.getUserProfile());
        assertNotNull(cfg3.getUserProfile().getId());
        assertNotNull(cfg3.getUserProfile().getName());
        assertEquals(profileId, cfg3.getUserProfile().getId());
    }

    @Test(expected = EntityValidationException.class)
    public void testValidationFailure() {
        SysConfigFormData cfg = service.loadFormConfig();

        UserSession session = userRequestService.getUserSession();
        UUID profileId = getUserProfile();

        assertTrue(cfg.getAdminMail().isPresent());
        assertTrue(cfg.getAllowRegPage().isPresent());

        cfg.setAdminMail(Optional.of(ADMIN_MAIL));
        cfg.setAllowRegPage(Optional.of(ALLOW_REG_PAGE));
        cfg.setSystemURL(Optional.of(PAGE_ROOT));
        cfg.setUlaActive(Optional.of(ULA_ACTIVE));
        cfg.setWorkspace(Optional.empty());
        cfg.setUnit(Optional.empty());
        cfg.setUserProfile(Optional.empty());

        service.updateConfig(cfg);
    }

    private UUID getUserProfile() {
        UUID id = (UUID)entityManager.createQuery("select id from UserProfile where workspace.id = :id")
                .setParameter("id", userRequestService.getUserSession().getWorkspaceId())
                .setMaxResults(1)
                .getSingleResult();

        return id;
    }

}
