package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.msh.etbm.Application;
import org.msh.etbm.Messages;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLogUtils;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.objutils.ObjectValues;
import org.msh.etbm.commons.objutils.PropertyValue;
import org.msh.etbm.db.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;


/**
 * Test if messages to be logged are available in the resources file
 *
 * Created by rmemoria on 10/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class LogMessagesTest {

    Pattern MSGKEY_PATTERN = Pattern.compile("\\$([._a-zA-Z]+)");

    @Resource
    MessageSource messageSource;

    private boolean result;

    /**
     * Test the messages
     */
    @Test
    public void testMessages() {
        result = true;
        testObjectClas(Source.class);
        testObjectClas(Medicine.class);
        testObjectClas(CountryStructure.class);
        testObjectClas(AdministrativeUnit.class);
        testObjectClas(Tbunit.class);
        testObjectClas(Laboratory.class);
        testObjectClas(Product.class);
        testObjectClas(User.class);
        testObjectClas(UserWorkspace.class);
        testObjectClas(Workspace.class);
        testObjectClas(TbCase.class);
        testObjectClas(Patient.class);

        assertTrue("There are messages with no key", result);
    }

    /**
     * Test the messages of a given class
     * @param clazz the class to check property messages
     */
    protected void testObjectClas(Class clazz) {
        Object obj = ObjectUtils.newInstance(clazz);

        ObjectValues vals = PropertyLogUtils.generateLog(obj, clazz, Operation.ALL);

        for (String props: vals.getValues().keySet()) {
            if (!assertMessage(clazz, props)) {
                result = false;
            }
        }
    }

    protected boolean assertMessage(Class clazz, String msg) {
        Matcher matcher = MSGKEY_PATTERN.matcher(msg);

        boolean res = true;
        while (matcher.find()) {
            String p = matcher.group().substring(1);
            if (!assertMessageKey(clazz, p)) {
                res = false;
            }
        }

        return res;
    }


    public boolean assertMessageKey(Class clazz, String key) {
        try {
            messageSource.getMessage(key, null, Locale.ENGLISH);
            return true;
        } catch (NoSuchMessageException e) {
            System.out.println("NO MSG FOUND : " + clazz.getSimpleName() + " => " + key);
        }
        return false;
    }
}
