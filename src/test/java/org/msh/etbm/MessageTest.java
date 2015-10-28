package org.msh.etbm;

import org.junit.Test;
import org.msh.etbm.commons.messages.MessageKeyResolver;

/**
 * Created by rmemoria on 27/10/15.
 */
public class MessageTest {

    @Test
    public void test() {
        MessageKeyResolver res = new MessageKeyResolver();
        String s = "This {arg} will be replaced by {value}";

        res.evaluateExpression(s);
    }
}
