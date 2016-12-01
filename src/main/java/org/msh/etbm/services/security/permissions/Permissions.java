package org.msh.etbm.services.security.permissions;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Set of permission constants used to authorize a user to execute
 * system commands
 * <p>
 * Created by rmemoria on 22/10/15.
 */
@Service
public class Permissions {

    private static final String EDIT = "_EDT";

    /**
     * Administration module permissions
     */
    public static final String ADMIN = "ADMIN";

    public static final String ADMIN_TABLES = "TABLES";

    public static final String TABLE_USERS = "USERS";
    public static final String TABLE_USERS_EDT = TABLE_USERS + EDIT;

    public static final String TABLE_AGERANGES = "AGERANGES";
    public static final String TABLE_AGERANGES_EDT = TABLE_AGERANGES + EDIT;

    public static final String TABLE_ADMUNITS = "ADMINUNITS";
    public static final String TABLE_ADMUNITS_EDT = TABLE_ADMUNITS + EDIT;

    public static final String TABLE_SOURCES = "SOURCES";
    public static final String TABLE_SOURCES_EDT = TABLE_SOURCES + EDIT;

    public static final String TABLE_UNITS = "UNITS";
    public static final String TABLE_UNITS_EDT = TABLE_UNITS + EDIT;

    public static final String TABLE_PRODUCTS = "PRODUCTS";
    public static final String TABLE_PRODUCTS_EDT = TABLE_PRODUCTS + EDIT;

    public static final String TABLE_REGIMENS = "REGIMENS";
    public static final String TABLE_REGIMENS_EDT = TABLE_REGIMENS + EDIT;

    public static final String TABLE_SUBSTANCES = "SUBSTANCES";
    public static final String TABLE_SUBSTANCES_EDT = TABLE_SUBSTANCES + EDIT;

    public static final String TABLE_TAGS = "TAGS";
    public static final String TABLE_TAGS_EDT = TABLE_TAGS + EDIT;

    public static final String TABLE_USERPROFILES = "PROFILES";
    public static final String TABLE_USERPROFILES_EDT = TABLE_USERPROFILES + EDIT;

    public static final String TABLE_WORKSPACES = "WORKSPACES";
    public static final String TABLE_WORKSPACES_EDT = TABLE_WORKSPACES + EDIT;

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
    public static final String CASES_EXAM_XPERT_EDT = CASES_EXAM_XPERT + EDIT;
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
    public static final String CASES_EXAM_CULTURE_EDT = CASES_EXAM_CULTURE + EDIT;
    public static final String CASES_EXAM_MICROSCOPY = "EXAM_MICROSCOPY";
    public static final String CASES_EXAM_MICROSCOPY_EDT = CASES_EXAM_MICROSCOPY + EDIT;
    public static final String CASES_EXAM_DST = "EXAM_DST";
    public static final String CASES_EXAM_DST_EDT = CASES_EXAM_DST + EDIT;
    public static final String CASES_EXAM_XRAY = "EXAM_XRAY";
    public static final String CASES_EXAM_XRAY_EDT = CASES_EXAM_XRAY + EDIT;
    public static final String CASES_EXAM_HIV = "EXAM_HIV";
    public static final String CASES_EXAM_HIV_EDT = CASES_EXAM_HIV + EDIT;
    public static final String CASES_COMORBIDITIES = "COMIRBIDITIES";
    public static final String CASES_COMORBIDITIES_EDT = CASES_COMORBIDITIES + EDIT;
    public static final String CASES_CASE_CONTACT = "CASECONTACT";
    public static final String CASES_CASE_CONTACT_EDT = CASES_CASE_CONTACT + EDIT;
    public static final String CASES_ADV_EFFECTS = "ADV_EFFECTS";
    public static final String CASES_ADV_EFFECTS_EDT = CASES_ADV_EFFECTS + EDIT;
    public static final String CASES_MED_EXAM = "CASE_MED_EXAM";
    public static final String CASES_MED_EXAM_EDT = CASES_MED_EXAM + EDIT;
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
     *
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
     *
     * @param id the permission ID
     * @return instance of the permission
     */
    public Permission find(String id) {
        for (Permission perm : getList()) {
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
                add(CASES_TREAT, "cases.details.treatment"),
                addChangeable(CASES_INTAKEMED),
                addChangeable(CASES_EXAM_MICROSCOPY, "cases.exammicroscopy"),
                addChangeable(CASES_EXAM_XPERT, "cases.examxpert"),
                addChangeable(CASES_EXAM_CULTURE, "cases.examculture"),
                addChangeable(CASES_EXAM_DST, "cases.examdst"),
                addChangeable(CASES_EXAM_HIV, "cases.examhiv"),
                addChangeable(CASES_EXAM_XRAY, "cases.examxray"),
                add(CASES_ADDINFO, "cases.details.otherinfo"),
                add(CASES_VALIDATE, "cases.validate"),
                add(CASES_DEL_VAL),
                add(CASES_TRANSFER, "cases.move"),
                add(CASES_CLOSE, "cases.close"),
                add(CASES_REOPEN, "cases.reopen"),
                add(CASES_COMMENTS),
                add(CASES_REM_COMMENTS),
                add(CASES_TAG),
                addChangeable(CASES_COMORBIDITIES, "cases.comorbidities"),
                addChangeable(CASES_CASE_CONTACT, "cases.contacts"),
                addChangeable(CASES_ADV_EFFECTS, "cases.sideeffects"),
                addChangeable(CASES_MED_EXAM, "cases.details.medexam"),
                add(CASES_ISSUES, "cases.issues",
                        add(CASES_NEW_ISSUE, "cases.issues.new"),
                        add(CASES_ANSWER_ISSUE),
                        add(CASES_CLOSEDEL_ISSUE)));

        module(INVENTORY,
                add(INVENTORY_INIT, "inventory.start"),
                add(INV_STOCKADJ, "inventory.newadjust"),
                add(INV_RECEIV, "inventory.receiving"),
                add(INV_ORDERS, "inventory.orders",
                        add(INV_NEW_ORDER, "inventory.orders.new"),
                        add(INV_VAL_ORDER, "inventory.orders.autorize"),
                        add(INV_SEND_ORDER, "inventory.orders.shipment"),
                        add(INV_RECEIV_ORDER, "inventory.orders.receive"),
                        add(INV_ORDER_CANC, "inventory.orders.cancel")),
                add(INV_DISPENSING, "inventory.dispensing"),
                add(INV_TRANSFER, "inventory.transfer",
                        add(INV_NEW_TRANSFER, "inventory.transfer.new"),
                        add(INV_TRANSF_REC, "inventory.transfer.receive"),
                        add(INV_TRANSF_CANCEL, "inventory.transfer.cancel")));

        module(LABS,
                add(LABS_NEWREQUEST),
                add(LABS_EDTREQ),
                add(LABS_POSTRESULT),
                add(LABS_REMREQ));

        module(REPORTS,
                add(REP_DATA_ANALYSIS, "reports.dat"),
                add(REP_MOVEMENTS, "reports.movements"),
                add(REP_ORDER_LEADTIME, "reports.orderleadtime"));


        // define permissions of the administration module
        module(ADMIN,
                add(ADMIN_TABLES, "admin.tables",
                        addChangeable(TABLE_ADMUNITS, "admin.adminunits"),
                        addChangeable(TABLE_SOURCES, "admin.sources"),
                        addChangeable(TABLE_UNITS, "admin.units"),
                        addChangeable(TABLE_SUBSTANCES, "admin.substances"),
                        addChangeable(TABLE_PRODUCTS, "admin.products"),
                        addChangeable(TABLE_REGIMENS, "admin.regimens"),
                        addChangeable(TABLE_AGERANGES, "admin.ageranges"),
                        addChangeable(TABLE_USERPROFILES, "admin.profiles"),
                        addChangeable(TABLE_TAGS, "admin.tags"),
                        addChangeable(TABLE_USERS, "admin.users"),
                        addChangeable(TABLE_WORKSPACES, "admin.workspaces")
                ),
                add(ADMIN_REPORTS, "admin.reports",
                        add(ADMIN_REP_CMDHISTORY),
                        add(ADMIN_REP_CMDSTATISTICS),
                        add(ADMIN_REP_USERSONLINE),
                        add(ADMIN_REP_USERSESSIONS, "admin.reports.usersession"),
                        add(ADMIN_REP_ERRORLOG)
                ),
                add(ADMIN_SETUP_WORKSPACE),
                add(ADMIN_SETUP_SYSTEM),
                add(ADMIN_CHECKUPDT)
        );

    }

    /**
     * Add a root permission, i.e, a module of the system
     *
     * @param id
     * @param children
     * @return
     */
    protected Permission module(String id, Permission... children) {
        if (list == null) {
            list = new ArrayList<>();
        }

        Permission perm = add(id, children);
        list.add(perm);

        return perm;
    }

    private Permission add(String id, Permission... children) {
        return add(id, "Permission." + id, children);
    }

    private Permission add(String id, String messageKey, Permission... children) {
        Permission perm = new Permission(null, id, messageKey, false);
        if (children != null && children.length > 0) {
            for (Permission child : children) {
                perm.addPermission(child);
            }
        }
        return perm;
    }

    private Permission addChangeable(String id) {
        return new Permission(null, id, "Permission." + id, true);
    }

    private Permission addChangeable(String id, String messageKey) {
        return new Permission(null, id, messageKey, true);
    }
}
