package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.services.admin.sysconfig.SysConfigFormData;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

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

    /**
     * Simple test of configuration changes to check if the service load and write configuration
     */
    @Test
    public void testReadWrite() {
        // load the service to change it later
        SysConfigFormData cfg = service.loadConfig();

        assertTrue(cfg.getAdminMail().isPresent());
        assertTrue(cfg.getAllowRegPage().isPresent());

        cfg.setAdminMail(Optional.of(ADMIN_MAIL));
        cfg.setAllowRegPage(Optional.of(ALLOW_REG_PAGE));
        cfg.setPageRootURL(Optional.of(PAGE_ROOT));
        cfg.setUlaActive(Optional.of(ULA_ACTIVE));

        // update the new configuration
        service.updateConfig(cfg);

        SysConfigFormData cfg2 = service.loadConfig();
        assertNotNull(cfg2);

        // check if configuration is equals the one saved
        assertEquals(cfg.getAdminMail(), cfg2.getAdminMail());
        assertEquals(cfg.getAllowRegPage(), cfg2.getAllowRegPage());
        assertEquals(cfg.getPageRootURL(), cfg2.getPageRootURL());
        assertEquals(cfg.getUlaActive(), cfg2.getUlaActive());
    }
}
