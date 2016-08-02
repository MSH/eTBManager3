package org.msh.etbm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.msh.etbm.Application;
import org.msh.etbm.commons.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Simple test of local data
 * Created by rmemoria on 27/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class MessageTest {

    private static final String KEY = "CaseClassification";

    @Autowired
    Messages messages;

    @Test
    public void test() {
        String msg = messages.get(KEY);

        assertNotNull(msg);

        String s = "${" + KEY + "}";

        String res = messages.eval(s);

        assertEquals(msg, res);

        s = "--- ${" + KEY + "} ----";

        res = messages.eval(s);

        assertTrue(res.contains(msg));
    }

}
