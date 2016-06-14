package org.msh.etbm.test.services.pub;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.msh.etbm.Application;
import org.msh.etbm.services.pub.ForgotPwdService;
import org.msh.etbm.test.TestSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;

import static org.junit.Assert.*;


/**
 * Created by rmemoria on 14/6/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class ForgotPwdTest {

    @Autowired
    ForgotPwdService forgotPwdService;

    @Autowired
    TestSetup testSetup;

    @Value("${mail.from:}")
    String mailFrom;

    @Before
    @BeforeTransaction
    public void beforeTesting() throws MalformedURLException {
        testSetup.checkSystemInitialization();
    }

    @Test
    public void testRequest() throws Exception {
        GreenMail mailServer = new GreenMail(ServerSetupTest.SMTP);

        mailServer.start();

        String res = forgotPwdService.requestPasswordReset(TestSetup.ADMIN_EMAIL);
        assertNotNull(res);

        // check if message was sent
        MimeMessage[] msgs = mailServer.getReceivedMessages();

        // check message
        assertEquals(msgs.length, 1);

        MimeMessage msg = msgs[0];
        assertEquals(1, msg.getFrom().length);

        assertEquals(mailFrom, msg.getFrom()[0]);
        assertEquals(TestSetup.ADMIN_EMAIL, msg.getAllRecipients()[0]);

        mailServer.stop();
    }
}
