package org.msh.etbm.test.commons.indicators;

import org.junit.Test;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.indicators.query.IndicatorSqlBuilder;
import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.msh.etbm.test.commons.indicators.fixtures.SimpleFieldFilter;
import org.msh.etbm.test.commons.indicators.fixtures.SimpleFieldVariable;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Test the class {@link org.msh.etbm.commons.indicators.query.IndicatorSqlBuilder}
 * Created by rmemoria on 10/9/16.
 */
public class IndicatorSqlBuilderTest {

    @Test
    public void testGeneration() {
        IndicatorSqlBuilder builder = new IndicatorSqlBuilder(new SQLQueryBuilder("mytable"));

        builder.setFieldList("myfield1, myfield2");

        Map<Filter, Object> filters = new HashMap<>();
        Filter filter = new SimpleFieldFilter("myFilter", "My filter", "myfield1");
        filters.put(filter, "VALUE");

        builder.setFilters(filters);

        String sql = builder.createSql();
        assertNotNull(sql);

        // check SQL syntax (poor test)
        sql = sql.toLowerCase();
        assertTrue(sql.startsWith("select"));
        assertTrue(sql.contains("from mytable"));
        assertTrue(sql.contains("myfield1"));
        assertTrue(sql.contains("myfield2"));
        assertTrue(sql.contains("where"));

        // check parameters
        Map<String, Object> params = builder.getParameters();
        assertEquals(1, params.size());
        assertTrue(params.containsValue("VALUE"));
    }

    @Test
    public void testVariables() {
        IndicatorSqlBuilder builder = new IndicatorSqlBuilder(new SQLQueryBuilder("mytable"));

        Variable v1 = new SimpleFieldVariable("id1", "label1", "field1");
        Variable v2 = new SimpleFieldVariable("id2", "label2", "field2");

        builder.addVariable(v1);
        builder.addVariable(v2);

        String sql = builder.createSql();

        assertNotNull(sql);

        sql = sql.toLowerCase();

        assertTrue("Group by clause not found", sql.contains("group by"));
    }
}
