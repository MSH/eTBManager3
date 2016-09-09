package org.msh.etbm.commons.indicators;

import java.sql.Connection;

public interface ReportResourceProvider {

    /**
     * Return a JDBC connection to be used by the report to access the data
     * @return
     */
    Connection getConnection();


    /**
     * Resolve a name to a value. In web application it's the injection dependency approach that references an
     * object by a name. For example, in SEAM framework, the resolution is to resolve a name to a component
     * (using the Component.getInstance method), but can be easily implemented to other platforms that doesn't
     * use SEAM (or even not WEB)
     *
     * @param name
     * @return value corresponding to its name
     */
    Object resolveName(String name);

}
