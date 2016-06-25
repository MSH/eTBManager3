package org.msh.etbm.test.services.admin;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Test;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.entities.UserWorkspace;
import org.msh.etbm.db.enums.UserView;
import org.msh.etbm.services.admin.admunits.AdminUnitData;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.admin.usersws.UserWsService;
import org.msh.etbm.services.admin.usersws.data.UserViewData;
import org.msh.etbm.services.admin.usersws.data.UserWsData;
import org.msh.etbm.services.admin.usersws.data.UserWsDetailedData;
import org.msh.etbm.services.admin.usersws.data.UserWsFormData;
import org.msh.etbm.test.services.CommonEntityServiceTests;
import org.msh.etbm.test.services.TestResult;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by rmemoria on 24/6/16.
 */
public class UserWsTest extends CommonEntityServiceTests {

    private static final String USER_NAME = "Bruce Dickinson";
    private static final String USER_LOGIN = "bdickinson";
    private static final String USER_EMAIL = "test@test.com";

    private GreenMail mailServer;

    @Autowired
    UserWsService userWsService;

    @Autowired
    DataTestSupport dataTestSupport;

    public UserWsTest() {
        super(UserWorkspace.class, UserWsFormData.class, UserWsData.class);
    }

    @Test
    public void execute() throws Exception {
        setEntityService(userWsService);

        mailServer = new GreenMail(ServerSetupTest.SMTP);

        // start server mail
        mailServer.start();
        try {
            UUID id = createUser();

            checkNewUserMessage(id);

            blockUser(id);
        } finally {
            mailServer.stop();
        }
    }

    /**
     * Test the creation of a new user
     * @return
     */
    protected UUID createUser() {
        Map<String, Object> props = new HashMap<>();
        props.put("name", USER_NAME);
        props.put("login", USER_LOGIN);
        props.put("email", USER_EMAIL);

        // create auxiliary data
        AdminUnitData region = dataTestSupport.createAdminUnit("Minas Gerais", null);
        AdminUnitData city = dataTestSupport.createAdminUnit("Caxambu", region.getId());
        UnitData unit = dataTestSupport.createTBUnit("Unit 1", city.getId());
        SynchronizableItem profile = dataTestSupport.getAdminProfile();

        props.put("unitId", unit.getId());
        props.put("active", true);
        props.put("administrator", true);

        List<UUID> profs = new ArrayList<>();
        profs.add(profile.getId());
        props.put("profiles", profs);

        UserViewData view = new UserViewData();
        view.setView(Optional.of(UserView.COUNTRY));
        props.put("view", view);
        props.put("sendSystemMessages", true);


        List<String> uniqueProps = Arrays.asList("login", "email");

        TestResult res = testCreateAndFindOne(props, uniqueProps, "view,state");
        assertNotNull(res.getData());
        return res.getId();
    }


    protected void checkNewUserMessage(UUID userid) throws Exception {
        UserWsDetailedData user = userWsService.findOne(userid, UserWsDetailedData.class);
        // check if message was sent
        MimeMessage[] msgs = mailServer.getReceivedMessages();

        // check message
        assertEquals(msgs.length, 1);

        MimeMessage msg = msgs[0];
        assertEquals(1, msg.getFrom().length);

        assertEquals(user.getEmail(), getEmail(msg.getAllRecipients()[0]));

        // check if the message contains the token to be used
        String txt = GreenMailUtil.getBody(msg);
        assertNotNull(txt);
    }

    private String getEmail(Address addr) {
        if (addr instanceof InternetAddress) {
            return ((InternetAddress) addr).getAddress();
        }

        return addr.toString();
    }


    protected void blockUser(UUID userid) {
        UserWsFormData req = new UserWsFormData();
        req.setActive(Optional.of(false));
        userWsService.update(userid, req);

        UserWsData data = userWsService.findOne(userid, UserWsData.class);
        assertNotNull(data);
        assertFalse(data.isActive());
        // just in case
        assertNotNull(data.getUnit());
        assertNotNull(data.getName());
    }
}
