package org.msh.etbm.commons.indicators;

import javax.sql.DataSource;

public interface ReportResourceProvider {

    /**
     * Return a JDBC DataSource to be used by the report to access the data
     * @return
     */
    DataSource getDataSource();

}
