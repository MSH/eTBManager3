package org.msh.etbm.commons.sqlquery;

import org.msh.etbm.services.cases.search.CaseData;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Execute a query from an instance of {@link SQLQueryBuilder}
 * Created by rmemoria on 22/8/16.
 */
public class SQLQueryExec {

    private DataSource dataSource;

    public SQLQueryExec(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Execute the query and map the result set
     * @param builder
     * @param mapper
     * @param <E>
     * @return
     */
    public <E> List<E> query(SQLQueryBuilder builder, RowMapper<E> mapper) {
        String sql = builder.generate();

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);

        List<E> lst = template.query(sql, builder.getParameters(), new org.springframework.jdbc.core.RowMapper<E>() {
            @Override
            public E mapRow(ResultSet resultSet, int i) throws SQLException {
                return null;
            }
        });
        return lst;
    }
}
