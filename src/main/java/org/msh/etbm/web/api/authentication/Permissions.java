package org.msh.etbm.web.api.authentication;

/**
 * Set of permission constants used to authorize a user to execute
 * system commands
 *
 * Created by rmemoria on 22/10/15.
 */
public class Permissions {
    /**
     * Administration module permissions
     */
    public static final String ADMIN = "ADMIN";

    public static final String ADMIN_USERS = "USERS";
    public static final String ADMIN_USERS_EDT = "USERS_EDT";

    public static final String ADMIN_AGERANGES = "AGERANGES";
    public static final String ADMIN_AGERANGES_EDT = "AGERANGES_EDT";

    public static final String ADMIN_ADMUNITS = "ADMINUNITS";
    public static final String ADMIN_ADMUNITS_EDT = "ADMINUNITS_EDT";

    public static final String ADMIN_SOURCES = "SOURCES";
    public static final String ADMIN_SOURCES_EDT = "SOURCES_EDT";

    public static final String ADMIN_UNITS = "UNITS";
    public static final String ADMIN_UNITS_EDT = "UNITS_EDT";

    public static final String ADMIN_MEDICINES = "MEDICINES";
    public static final String ADMIN_MEDICINES_EDT = "MEDICINES_EDT";

    public static final String ADMIN_REGIMENS = "REGIMENS";
    public static final String ADMIN_REGIMENS_EDT = "REGIMENS_EDT";

    public static final String ADMIN_SUBSTANCES = "SUBSTANCES";
    public static final String ADMIN_SUBSTANCES_EDT = "SUBSTANCES_EDT";

    public static final String ADMIN_FIELDVALUES = "FIELDS";
    public static final String ADMIN_FIELDVALUES_EDT = "FIELDS_EDT";

    public static final String ADMIN_TAGS = "TAGS";
    public static final String ADMIN_TAGS_EDT = "TAGS_EDT";

    public static final String ADMIN_USERPROFILES = "PROFILES";
    public static final String ADMIN_USERPROFILES_EDT = "PROFILES_EDT";

    public static final String ADMIN_WEEKFREQ = "WEEKFREQ";

    public static final String ADMIN_REPORTS = "ADMREP";
    public static final String ADMIN_REP_USERSONLINE = "ONLINE";
    public static final String ADMIN_REP_USERSESSIONS = "USERSESSIONS";
    public static final String ADMIN_REP_CMDHISTORY = "CMDHISTORY";
    public static final String ADMIN_REP_CMDSTATISTICS = "CMDSTATISTICS";
    public static final String ADMIN_REP_ERRORLOG = "ERRORLOGREP";

    public static final String ADMIN_SETUP_WORKSPACE = "SETUPWS";
    public static final String ADMIN_SETUP_SYSTEM = "SYSSETUP";
    public static final String ADMIN_WORKSPACES = "WORKSPACES";
    public static final String ADMIN_CHECKUPDT = "CHECKUPDT";

    /**
     * Inventory management module permissions
     */
    public static final String INVENTORY = "INVENTORY";

    /**
     * Report module permissions
     */
    public static final String REPORTS = "REPORTS";

    /**
     * Case management module
     */
    public static final String CASES = "CASES";
}
