package org.msh.etbm.services.permissions;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Set of permission constants used to authorize a user to execute
 * system commands
 *
 * Created by rmemoria on 22/10/15.
 */
@Service
public class    Permissions {
    /**
     * Administration module permissions
     */
    public static final String ADMIN = "ADMIN";

    public static final String ADMIN_TABLES = "TABLES";
    
    public static final String TABLE_USERS = "USERS";
    public static final String TABLE_USERS_EDT = "USERS_EDT";

    public static final String TABLE_AGERANGES = "AGERANGES";
    public static final String TABLE_AGERANGES_EDT = "AGERANGES_EDT";

    public static final String TABLE_ADMUNITS = "ADMINUNITS";
    public static final String TABLE_ADMUNITS_EDT = "ADMINUNITS_EDT";

    public static final String TABLE_SOURCES = "SOURCES";
    public static final String TABLE_SOURCES_EDT = "SOURCES_EDT";

    public static final String TABLE_UNITS = "UNITS";
    public static final String TABLE_UNITS_EDT = "UNITS_EDT";

    public static final String TABLE_PRODUCTS = "PRODUCTS";
    public static final String TABLE_PRODUCTS_EDT = "PRODUCTS_EDT";

    public static final String TABLE_REGIMENS = "REGIMENS";
    public static final String TABLE_REGIMENS_EDT = "REGIMENS_EDT";

    public static final String TABLE_SUBSTANCES = "SUBSTANCES";
    public static final String TABLE_SUBSTANCES_EDT = "SUBSTANCES_EDT";

//    public static final String ADMIN_FIELDVALUES = "FIELDS";
//    public static final String ADMIN_FIELDVALUES_EDT = "FIELDS_EDT";

    public static final String TABLE_TAGS = "TAGS";
    public static final String TABLE_TAGS_EDT = "TAGS_EDT";

    public static final String TABLE_USERPROFILES = "PROFILES";
    public static final String TABLE_USERPROFILES_EDT = "PROFILES_EDT";

    public static final String TABLE_WORKSPACES = "WORKSPACES";
    public static final String TABLE_WORKSPACES_EDT = "WORKSPACES_EDT";

    public static final String ADMIN_WEEKFREQ = "WEEKFREQ";


    public static final String ADMIN_REPORTS = "ADMREP";
    public static final String ADMIN_REP_USERSONLINE = "ONLINE";
    public static final String ADMIN_REP_USERSESSIONS = "USERSESREP";
    public static final String ADMIN_REP_CMDHISTORY = "CMDHISTORY";
    public static final String ADMIN_REP_CMDSTATISTICS = "CMDSTATISTICS";
    public static final String ADMIN_REP_ERRORLOG = "ERRORLOGREP";

    public static final String ADMIN_SETUP_WORKSPACE = "SETUPWS";
    public static final String ADMIN_SETUP_SYSTEM = "SYSSETUP";
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
    public static final String CASES = "CASEMAN";

    public static final String CASES_NEWSUSP = "NEWSUSP";
    public static final String CASES_NEWCASE = "NEWCASE";

    /**
     * Laboratory module
     */
    public static final String LABS = "LABS";



    private List<Permission> list;

    /**
     * Return the list of permissions in a structured way, i.e, group permissions
     * @return list of group permissions
     */
    public List<Permission> getList() {
        if (list == null) {
            initList();
        }
        return list;
    }

    /**
     * Search for a permission by its ID
     * @param id
     * @return
     */
    public Permission find(String id) {
        return null;
    }


    /**
     * Initialize the list of permissions
     */
    private void initList() {
        // define permissions of the cases module
        module(CASES);

        module(INVENTORY);

        module(LABS);

        module(REPORTS);


        // define permissions of the administration module
        module(ADMIN,
                add(ADMIN_TABLES,
                    addChangeable(TABLE_ADMUNITS),
                    addChangeable(TABLE_SOURCES),
                    addChangeable(TABLE_UNITS),
                    addChangeable(TABLE_SUBSTANCES),
                    addChangeable(TABLE_PRODUCTS),
                    addChangeable(TABLE_REGIMENS),
                    addChangeable(TABLE_AGERANGES),
                    addChangeable(TABLE_TAGS),
                    addChangeable(TABLE_USERS),
                    addChangeable(TABLE_WORKSPACES)
                ),
                add(ADMIN_REPORTS,
                    add(ADMIN_REP_CMDHISTORY),
                    add(ADMIN_REP_CMDSTATISTICS),
                    add(ADMIN_REP_USERSONLINE),
                    add(ADMIN_REP_USERSESSIONS),
                    add(ADMIN_REP_ERRORLOG)
                ),
                add(ADMIN_SETUP_WORKSPACE),
                add(ADMIN_SETUP_SYSTEM),
                add(ADMIN_CHECKUPDT)
        );

    }

    /**
     * Add a root permission, i.e, a module of the system
     * @param id
     * @param children
     * @return
     */
    protected Permission module(String id, Permission ...children) {
        if (list == null) {
            list = new ArrayList<>();
        }

        Permission perm = add(id, children);
        list.add(perm);

        return perm;
    }

    private Permission add(String id, Permission ...children) {
        Permission perm = new Permission(null, id, false);
        if (children != null && children.length > 0) {
            for (Permission child: children) {
                perm.addPermission(child);
            }
        }
        return perm;
    }

    private Permission addChangeable(String id) {
        return new Permission();
    }
}
