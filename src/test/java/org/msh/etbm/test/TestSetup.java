package org.msh.etbm.test;

import org.msh.etbm.services.init.RegisterWorkspaceRequest;
import org.msh.etbm.services.init.RegisterWorkspaceService;
import org.msh.etbm.services.security.authentication.LoginService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.services.session.usersession.UserSession;
import org.msh.etbm.services.sys.info.SystemInfoService;
import org.msh.etbm.services.sys.info.SystemInformation;
import org.msh.etbm.services.sys.info.SystemState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.util.UUID;

import static org.junit.Assert.*;


/**
 * Initialize database and check proper user authentication
 * Created by rmemoria on 7/2/16.
 */
@Component
public class TestSetup {

    // write logging information
    private final static Logger LOGGER = LoggerFactory.getLogger(TestSetup.class);

    // the administrator user name
    public static final String ADMIN_LOGIN = "admin";
    // the password of the administrator
    public static final String ADMIN_PWD = "pwd123";
    // administrator e-mail address
    public static final String ADMIN_EMAIL = "rmemoria@msh.org";
    // the name of the workspace that will be registered
    public static final String WORKSPACE_NAME = "Test";
    // the IP address of the application that is requesting login
    public static final String CLIENT_IP = "127.0.0.1";
    // the name of the app that is starting the test
    public static final String CLIENT_APP = "JUnit test suite";

    // script name to run in order to initialize database
    public static final String DB_SCRIPT = "setup-database.sql";

    @Autowired
    LoginService loginService;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    SystemInfoService systemInfoService;

    @Autowired
    RegisterWorkspaceService registerWorkspaceService;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DataSource dataSource;


    private boolean initialized;


    /**
     * Check if user is authenticated, if not, a login will be performed using the standard user name and password
     * @throws MalformedURLException
     */
    public void checkAuthenticated() throws MalformedURLException {
        checkSystemInitialization();

        // check if user is already authenticated
        if (userRequestService.isAuthenticated()) {
            return;
        }

        UUID authToken = loginService.login(ADMIN_LOGIN, ADMIN_PWD, null, CLIENT_IP, CLIENT_APP);
        assertNotNull(authToken);

        UserSession userSession = userRequestService.getUserSession();
        assertNotNull(userSession);

        assertEquals(userSession.isAdministrator(), true);
        assertTrue(userRequestService.isAuthenticated());

        LOGGER.info("User admin authenticated");
    }


    /**
     * Check if system was already initialized. It is supposed to be called before any test.
     * It clear all database tables and register a new workspace for testing
     *
     * @throws MalformedURLException
     */
    public void checkSystemInitialization() throws MalformedURLException {
        if (initialized) {
            return;
        }

        // clear database in order to force registration
        runIniScript();

        // assert that system state is correct
        SystemInformation info = systemInfoService.getInformation(false);
        assertNotNull(info);

        assertEquals(info.getState(), SystemState.NEW);

        // register workspace
        registerWorkspace();

        initialized = true;
    }

    /**
     * Run initialization script that will prepare the database
     */
    private void runIniScript() throws MalformedURLException {
        LOGGER.info("Initializing database: running " + DB_SCRIPT);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(DB_SCRIPT));

        populator.execute(this.dataSource);
    }

    /**
     * Initialize system, which is the registration of a new workspace
     */
    private void registerWorkspace() {
        LOGGER.info("Initializing workspace and admin user");

        RegisterWorkspaceRequest req = new RegisterWorkspaceRequest();
        req.setAdminEmail(ADMIN_EMAIL);
        req.setAdminPassword(ADMIN_PWD);
        req.setWorkspaceName(WORKSPACE_NAME);

        UUID wsid = registerWorkspaceService.run(req);

        assertNotNull(wsid);
    }
}
