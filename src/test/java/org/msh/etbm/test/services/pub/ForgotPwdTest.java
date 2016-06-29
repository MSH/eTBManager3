package org.msh.etbm.test.services.pub;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.msh.etbm.Application;
import org.msh.etbm.services.init.RegisterWorkspaceImpl;
import org.msh.etbm.services.pub.ForgotPwdService;
import org.msh.etbm.services.pub.PasswordUpdateService;
import org.msh.etbm.services.pub.PwdResetTokenResponse;
import org.msh.etbm.test.TestSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.Assert.*;


/**
 * Created by rmemoria on 14/6/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class ForgotPwdTest {

    private GreenMail mailServer;

    @Autowired
    ForgotPwdService forgotPwdService;

    @Autowired
    PasswordUpdateService passwordUpdateService;

    @Autowired
    TestSetup testSetup;

    @Value("${mail.from:}")
    String mailFrom;

    @Before
    @BeforeTransaction
    public void beforeTesting() throws MalformedURLException {
        testSetup.checkSystemInitialization();
    }

    /**
     * Test "forgot password" process
     *
     * @throws Exception
     */
    @Test
    public void testForgotPwd() throws Exception {
        mailServer = new GreenMail(ServerSetupTest.SMTP);

        mailServer.start();
        try {
            testRequest();
        } finally {
            mailServer.stop();
        }
    }

    /**
     * Test the request
     *
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    private String testRequest() throws MessagingException, IOException {
        String res = forgotPwdService.requestPasswordReset(TestSetup.ADMIN_EMAIL);
        assertNotNull(res);

        // check if message was sent
        MimeMessage[] msgs = mailServer.getReceivedMessages();

        // check message
        assertEquals(msgs.length, 1);

        MimeMessage msg = msgs[0];
        assertEquals(1, msg.getFrom().length);

        assertEquals(mailFrom, getEmail(msg.getFrom()[0]));
        assertEquals(TestSetup.ADMIN_EMAIL, getEmail(msg.getAllRecipients()[0]));

        // check if the message contains the token to be used
        String txt = GreenMailUtil.getBody(msg);
        assertTrue(txt.contains(res));

        return res;
    }

    private void testRequestToken(String token) {
        PwdResetTokenResponse resp = passwordUpdateService.getUserInfoByPasswordResetToken(token);
        assertNotNull(resp);
        assertEquals(resp.getName(), RegisterWorkspaceImpl.ADMIN_NAME);
    }

    private String getEmail(Address addr) {
        if (addr instanceof InternetAddress) {
            return ((InternetAddress) addr).getAddress();
        }

        return addr.toString();
    }
}
