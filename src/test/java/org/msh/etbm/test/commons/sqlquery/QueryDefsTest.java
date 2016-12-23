package org.msh.etbm.test.commons.sqlquery;

import org.junit.Test;
import org.msh.etbm.commons.sqlquery.SQLParseUtils;

import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by rmemoria on 23/12/16.
 */
public class QueryDefsTest {

    @Test
    public void testFieldSplit() {
        String s = "table.field";
        int pos = SQLParseUtils.parseFirstField(s);
        assertTrue(pos > 0);
        String f = s.substring(0, pos);
        assertEquals(s, f);

        s = "field1, field2";
        pos = SQLParseUtils.parseFirstField(s);
        assertTrue(pos > 0);
        f = s.substring(0, pos);
        assertEquals("field1", f);

        s = "expr(field1, field2), field3";
        pos = SQLParseUtils.parseFirstField(s);
        assertTrue(pos > 0);
        f = s.substring(0, pos);
        assertEquals("expr(field1, field2)", f);

        s = "";
        pos = SQLParseUtils.parseFirstField(s);
        assertEquals(0, pos);
    }


    @Test
    public void testFieldsParse() {
        String s = "field1,  field2  , field3 ";
        List<String> lst = SQLParseUtils.parseFields(s);
        assertNotNull(lst);
        assertEquals(3, lst.size());
        assertEquals("field1", lst.get(0));
        assertEquals("field2", lst.get(1));
        assertEquals("field3", lst.get(2));

        lst = SQLParseUtils.parseFields("field1");
        assertNotNull(lst);
        assertEquals(1, lst.size());
        assertEquals("field1", lst.get(0));

        lst = SQLParseUtils.parseFields("field1, max(field2, field3) , field4");
        assertNotNull(lst);
        assertEquals(3, lst.size());
        assertEquals("field1", lst.get(0));
        assertEquals("max(field2, field3)", lst.get(1));
        assertEquals("field4", lst.get(2));

        s = "max(field1, (field2 * (field3 - field4))), select min(*) from value";
        lst = SQLParseUtils.parseFields(s);
        assertNotNull(lst);
        assertEquals(2, lst.size());
        assertEquals("max(field1, (field2 * (field3 - field4)))", lst.get(0));
        assertEquals("select min(*) from value", lst.get(1));

    }
}
