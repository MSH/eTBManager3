package org.msh.etbm;

import org.junit.Test;
import org.msh.etbm.commons.messages.MessageKeyResolver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple test of local data
 * Created by rmemoria on 27/10/15.
 */
public class MessageTest {

    @Test
    public void test() {
        MessageKeyResolver res = new MessageKeyResolver();
        String s = "This {arg} will be replaced by {value}";

        res.evaluateExpression(s);
    }

    @Test
    public void test2() {
        Pattern p = Pattern.compile("\\:([_a-zA-Z]+)");

        String s = "name = :name and lsatname = :lastname";

        Matcher m = p.matcher(s);
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}
