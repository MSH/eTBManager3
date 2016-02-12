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
public class Permissions {
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

    public static final String TABLE_TAGS = "TAGS";
    public static final String TABLE_TAGS_EDT = "TAGS_EDT";

    public static final String TABLE_USERPROFILES = "PROFILES";
    public static final String TABLE_USERPROFILES_EDT = "PROFILES_EDT";

    public static final String TABLE_WORKSPACES = "WORKSPACES";
    public static final String TABLE_WORKSPACES_EDT = "WORKSPACES_EDT";

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

    public static final String INVENTORY_INIT = "INVENTORY_INIT";
    public static final String INV_RECEIV = "RECEIV";
    public static final String INV_STOCKADJ = "STOCK_ADJ";
    public static final String INV_ORDERS = "ORDERS";
    public static final String INV_NEW_ORDER = "NEW_ORDER";
    public static final String INV_VAL_ORDER = "VAL_ORDER";
    public static final String INV_SEND_ORDER = "SEND_ORDER";
    public static final String INV_RECEIV_ORDER = "RECEIV_ORDER";
    public static final String INV_ORDER_CANC = "ORDER_CANC";
    public static final String INV_DISPENSING = "DISP_PAC";
    public static final String INV_TRANSFER = "TRANSFER";
    public static final String INV_TRANSF_REC = "TRANSF_REC";
    public static final String INV_NEW_TRANSFER = "NEW_TRANSFER";
    public static final String INV_TRANSF_CANCEL = "TRANSF_CANCEL";

    /**
     * Report module permissions
     */
    public static final String REPORTS = "REPORTS";

    public static final String REP_DATA_ANALYSIS = "DATA_ANALISYS";
    public static final String REP_ORDER_LEADTIME = "REP_ORDER_LEADTIME";
    public static final String REP_MOVEMENTS = "REP_MOVEMENTS";

    /**
     * Case management module
     */
    public static final String CASES = "CASEMAN";

    public static final String CASES_NEWSUSP = "NEWSUSP";
    public static final String CASES_NEWCASE = "NEWCASE";
    public static final String CASES_TREAT = "CASE_TREAT";
    public static final String CASES_INTAKEMED = "CASE_INTAKEMED";
    public static final String CASES_EXAM_XPERT = "EXAM_XPERT";
    public static final String CASES_ADDINFO = "CASE_ADDINFO";
    public static final String CASES_VALIDATE = "CASE_VALIDATE";
    public static final String CASES_DEL_VAL = "CASE_DEL_VAL";
    public static final String CASES_TRANSFER = "CASE_TRANSFER";
    public static final String CASES_CLOSE = "CASE_CLOSE";
    public static final String CASES_REOPEN = "CASE_REOPEN";
    public static final String CASES_COMMENTS = "CASE_COMMENTS";
    public static final String CASES_REM_COMMENTS = "REM_COMMENTS";
    public static final String CASES_TAG = "CASE_TAG";
    public static final String CASES_EXAM_CULTURE = "EXAM_CULTURE";
    public static final String CASES_EXAM_MICROSCOPY = "EXAM_MICROSCOPY";
    public static final String CASES_EXAM_DST = "EXAM_DST";
    public static final String CASES_EXAM_XRAY = "EXAM_XRAY";
    public static final String CASES_EXAM_HIV = "EXAM_HIV";
    public static final String CASES_COMORBIDITIES = "COMIRBIDITIES";
    public static final String CASES_TBCONTACT = "TBCONTACT";
    public static final String CASES_ADV_EFFECTS = "ADV_EFFECTS";
    public static final String CASES_MED_EXAM = "CASE_MED_EXAM";
    public static final String CASES_ISSUES = "ISSUES";
    public static final String CASES_NEW_ISSUE = "NEW_ISSUE";
    public static final String CASES_ANSWER_ISSUE = "ANSWER_ISSUE";
    public static final String CASES_CLOSEDEL_ISSUE = "CLOSEDEL_ISSUE";

    /**
     * Laboratory module
     */
    public static final String LABS = "LAB_MODULE";
    public static final String LABS_NEWREQUEST = "LAB_NEWREQUEST";
    public static final String LABS_POSTRESULT = "LAB_POSTRESULT";
    public static final String LABS_EDTREQ = "LAB_EDTREQ";
    public static final String LABS_REMREQ = "LAB_REMREQ";


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
     * @param id the permission ID
     * @return instance of the permission
     */
    public Permission find(String id) {
        for (Permission perm: getList()) {
            if (perm.getId().equals(id)) {
                return perm;
            }
            Permission aux = perm.find(id);
            if (aux != null) {
                return aux;
            }
        }
        return null;
    }


    /**
     * Initialize the list of permissions
     */
    private void initList() {
        // define permissions of the cases module
        module(CASES,
                add(CASES_NEWSUSP),
                add(CASES_NEWCASE),
                add(CASES_TREAT),
                add(CASES_INTAKEMED),
                add(CASES_EXAM_MICROSCOPY),
                add(CASES_EXAM_XPERT),
                add(CASES_EXAM_CULTURE),
                add(CASES_EXAM_DST),
                add(CASES_EXAM_HIV),
                add(CASES_EXAM_XRAY),
                add(CASES_ADDINFO),
                add(CASES_VALIDATE),
                add(CASES_DEL_VAL),
                add(CASES_TRANSFER),
                add(CASES_CLOSE),
                add(CASES_REOPEN),
                add(CASES_REM_COMMENTS),
                add(CASES_TAG),
                add(CASES_COMORBIDITIES),
                add(CASES_TBCONTACT),
                add(CASES_ADV_EFFECTS),
                add(CASES_MED_EXAM),
                add(CASES_ISSUES),
                add(CASES_NEW_ISSUE),
                add(CASES_ANSWER_ISSUE),
                add(CASES_CLOSEDEL_ISSUE));

        module(INVENTORY,
                add(INVENTORY_INIT),
                add(INV_STOCKADJ),
                add(INV_RECEIV),
                add(INV_ORDERS,
                        add(INV_NEW_ORDER),
                        add(INV_VAL_ORDER),
                        add(INV_SEND_ORDER),
                        add(INV_RECEIV_ORDER),
                        add(INV_ORDER_CANC)),
                add(INV_DISPENSING),
                add(INV_TRANSFER,
                        add(INV_NEW_TRANSFER),
                        add(INV_TRANSF_REC),
                        add(INV_TRANSF_CANCEL)));

        module(LABS,
                add(LABS_NEWREQUEST),
                add(LABS_EDTREQ),
                add(LABS_POSTRESULT),
                add(LABS_REMREQ));

        module(REPORTS,
                add(REP_DATA_ANALYSIS),
                add(REP_MOVEMENTS),
                add(REP_ORDER_LEADTIME));


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
                    addChangeable(TABLE_USERPROFILES),
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
        return new Permission(null, id, true);
    }
}
