package org.msh.etbm.commons.indicators;

import javax.sql.DataSource;


/**
 * This is a singleton class to configure the resource provider
 * used in the report engine
 *
 * @author Ricardo Memoria
 *
 */
public final class ReportConfiguration {

    private static ReportConfiguration reportConfiguration;

    private ReportResourceProvider resourceProvider;

    /**
     * Private constructor to guaranty singleton instance
     */
    private ReportConfiguration() {
        super();
    }

    /**
     * Register an implementation of the {@link ReportResourceProvider} interface. This interface
     * will integrate the report library with the system
     * @param resourceProvider
     * @return
     */
    public void registerResourceProvider(ReportResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    /**
     * Return an instance of the DataSource to run the queries
     * @return
     */
    public DataSource getDataSource() {
        return checkResourceProvider().getDataSource();
    }

    /**
     * @return
     */
    private ReportResourceProvider checkResourceProvider() {
        if (resourceProvider == null) {
            throw new RuntimeException("No NameResolver interface provided to " + getClass().getSimpleName());
        }
        return resourceProvider;
    }

    /**
     * Create the instance of {@link ReportConfiguration}
     * @return
     */
    private static synchronized ReportConfiguration createSingletonInstance() {
        if (reportConfiguration == null) {
            reportConfiguration = new ReportConfiguration();
        }
        return reportConfiguration;
    }

    /**
     * Return an unique instance of the {@link ReportConfiguration} class
     * @return
     */
    public static ReportConfiguration instance() {
        if (reportConfiguration == null) {
            createSingletonInstance();
        }
        return reportConfiguration;
    }

}
