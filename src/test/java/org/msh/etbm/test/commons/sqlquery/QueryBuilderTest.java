package org.msh.etbm.test.commons.sqlquery;

import org.junit.Test;
import org.msh.etbm.commons.sqlquery.SQLField;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the generation of SQL instruction based on table definition
 * Created by rmemoria on 17/8/16.
 */
public class QueryBuilderTest {

    @Test
    public void testGeneration() {
        SQLQueryBuilder builder = new SQLQueryBuilder("tbcase");

        builder.select("diagnosisDate, caseNumber, id")
                .join("patient", "patient.id = tbcase.patient_id")
                .select("id, name, middleName, lastName")
                .join("unit", "$parent.id = $root.owner_unit_id")
                .select("id, name")
                .join("administrativeunit", "$this.id = $parent.adminunit_id")
                .select("name, pname1, pname2, pname3, pname4, pid1, pid2, pid3, pid4")
                .restrict("tbcase.workspace_id = ?", 1234)
                .restrict("tbcase.caseNumber is not null");

        String sql = builder.generate();

        assertNotNull(sql);
        // must be replaced by table alias
        assertEquals(-1, sql.indexOf("$this."));
        assertEquals(-1, sql.indexOf("$parent."));

        // parameter must be replaced by param name
        assertEquals(-1, sql.indexOf(" = ?"));

        // table name must be replaced by alias
        assertEquals(-1, sql.indexOf("tbcase.caseNumber"));

        // check parameters
        assertNotNull(builder.getParameters());
        assertEquals(1, builder.getParameters().size());
        assertEquals(1234, builder.getParameters().get("p0"));
    }

    @Test
    public void testNestedJoins() {
        SQLQueryBuilder builder = new SQLQueryBuilder("test");

        builder.select("id, name")
                .join("test", "$this.id = $parent.parent_id")
                .select("id, name")
                .leftJoin("test", "$this.id = $parent.parent_id")
                .select("id, name");

        String sql = builder.generate();

        assertTrue(sql.startsWith("select "));

        assertTrue(sql.indexOf("from test ") > 0);

        assertTrue(sql.indexOf("join test ") > 0);

        // check if LEFT JOIN declaration was found
        assertTrue(sql.indexOf("left join") > 0);
    }

    @Test
    public void test() {
        SQLQueryBuilder builder = new SQLQueryBuilder("test");

        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        builder.restrict("field in (?, ?)", values.toArray());

        String sql = builder.generate();

//        System.out.println(sql);
    }


    /**
     * Search for field reference in the SQL query
     */
    @Test
    public void searchField() {
        SQLQueryBuilder builder = new SQLQueryBuilder("mytable");
        builder.select("field1, field2, field3")
                .join("tbl2", "$this.id = $root.tbl2_id").select("field4, field5");

        String sql = builder.generate();

        assertNotNull(sql);

        sql = sql.toLowerCase();

        SQLField f1 = builder.fieldByName("field1");
        assertNotNull(f1);

        SQLField f2 = builder.fieldByName("mytable.field1");
        assertNotNull(f2);

        assertEquals(f1, f2);

        SQLField f3 = builder.fieldByName("field4");
        assertNull(f3);

        f3 = builder.fieldByName("tbl2.field4");
        assertNotNull(f3);
    }

    @Test
    public void aggregationTest() {
        SQLQueryBuilder builder = new SQLQueryBuilder("mytable");

        builder.select("field1, field2, field3");
        builder.addGroupExpression("count(*)");

        String sql = builder.generate();

        assertNotNull(sql);

        sql = sql.toLowerCase();

        assertTrue("Must contain count(*) clause", sql.contains("count(*)"));
        assertTrue("Must contain group by clause", sql.contains("group by"));

        assertTrue("Group by declaration is wrong", sql.contains("group by"));

        String[] tokens = sql.split("[\\n ]");
        int index = Arrays.binarySearch(tokens, "group");
        assertTrue(index > 0);

        assertEquals("by", tokens[++index]);

        SQLField f1 = builder.fieldByName("field1");
        assertNotNull(f1);
        assertEquals(f1.getFieldAlias() + ",", tokens[++index]);
        assertEquals(builder.fieldByName("field2").getFieldAlias() + ",", tokens[++index]);
        assertEquals(builder.fieldByName("field3").getFieldAlias(), tokens[++index]);
        assertEquals("Must be the end of the tokens in SQL", ++index, tokens.length);
    }

    @Test
    public void simpleQuery() {
        SQLQueryBuilder qry = SQLQueryBuilder.from("mytable");

        qry.join("table2", "table2.id = mytable.table2_id")
                .restrict("table2.name like ?", "%test%");

        String sql = qry.generate();
        assertNotNull(sql);

        qry.setDisableFieldAlias(true);
        qry.select("table2.*");
        sql = qry.generate();
        assertNotNull(sql);
    }
}
