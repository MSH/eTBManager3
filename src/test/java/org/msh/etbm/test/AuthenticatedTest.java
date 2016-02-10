package org.msh.etbm.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.msh.etbm.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.MalformedURLException;

/**
 * Base class to every test that requires authentication before execution
 * Created by rmemoria on 7/2/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public abstract class AuthenticatedTest {

    @Autowired
    TestSetup testSetup;

    @Before
    @BeforeTransaction
    public void beforeTesting() throws MalformedURLException {
        testSetup.checkAuthenticated();
    }
}
