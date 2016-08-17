package org.msh.etbm.test.commons.sqlquery;

import org.junit.Test;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;

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
}
