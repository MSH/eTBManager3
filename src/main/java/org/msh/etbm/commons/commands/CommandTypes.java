package org.msh.etbm.commons.commands;

import org.msh.etbm.commons.commands.impl.RootCommandType;

/**
 * Declare the list of command types available in the system
 *
 * Created by rmemoria on 29/6/16.
 */
public class CommandTypes {

    private CommandTypes() {
        // avoid this class being instantiated inadvertently
    }

    // main group commands
    public static final String INIT = "init";
    public static final String PUBLIC = "pub";
    public static final String CASES = "cases";
    public static final String INVENTORY = "inventory";
    public static final String ADMIN = "admin";
    public static final String SESSION = "session";

    // initialization command types
    public static final String INIT_REGWORKSPACE = "init.regworkspace";


    // admin command types
    public static final String ADMIN_USERS = "admin.users";
    public static final String ADMIN_USERS_CHANGE_PWD = "admin.users.changepwd";
    public static final String ADMIN_UNITS = "admin.units";
    public static final String ADMIN_ADMINUNITS = "admin.adminunits";
    public static final String ADMIN_COUNTRYSTRUCTURES = "admin.countrystr";
    public static final String ADMIN_SOURCES = "admin.sources";
    public static final String ADMIN_AGERANGES = "admin.ageranges";
    public static final String ADMIN_PRODUCTS = "admin.products";
    public static final String ADMIN_REGIMENS = "admin.regimens";
    public static final String ADMIN_SUBSTANCES = "admin.substances";
    public static final String ADMIN_TAGS = "admin.tags";
    public static final String ADMIN_USERPROFILES = "admin.profiles";
    public static final String ADMIN_WORKSPACES = "admin.workspaces";

    public static final String SYSTEM_SETUP = "admin.syssetup";

    // case module
    public static final String CASES_CASE = "cases.case";
    public static final String CASES_CASE_CLOSE = "cases.case.close";
    public static final String CASES_CASE_REOPEN = "cases.case.reopen";
    public static final String CASES_CASE_TAG = "cases.case.manualtags";
    public static final String CASES_CASE_VALIDATE = "cases.case.validate";
    public static final String CASES_CASE_TRANSFER_OUT = "cases.case.transferout";
    public static final String CASES_CASE_TRANSFER_IN = "cases.case.transferin";
    public static final String CASES_CASE_TRANSFER_ROLLBACK = "cases.case.transferrollback";
    public static final String CASES_CASE_COMMENT = "cases.case.comment";
    public static final String CASES_CASE_ISSUE = "cases.case.issues";
    public static final String CASES_CASE_ISSUEFOLLOWUP = "cases.case.issuefollowups";
    public static final String CASES_CASE_PREVTREAT = "cases.case.prevtreat";
    public static final String CASES_CASE_CONTACT = "cases.case.contact";
    public static final String CASES_CASE_SIDEEFFECT = "cases.case.sideeffect";
    public static final String CASES_CASE_MED_EXAM = "cases.case.medexam";
    public static final String CASES_CASE_EXAM_MIC = "cases.case.exammic";
    public static final String CASES_CASE_EXAM_CUL = "cases.case.examcul";
    public static final String CASES_CASE_EXAM_XPERT = "cases.case.examxpert";
    public static final String CASES_CASE_EXAM_DST = "cases.case.examdst";
    public static final String CASES_CASE_EXAM_HIV = "cases.case.examhiv";
    public static final String CASES_CASE_EXAM_XRAY = "cases.case.examxray";

    public static final String CASES_TREAT_FOLLOWUP = "cases.treatfollowup";
    public static final String CASES_TREAT_INI = "cases.treatini";
    public static final String CASES_TREAT_UNDO = "cases.treatundo";

    // user session commands
    public static final String SESSION_USER_SETTINGS = "session.usersettings";
    public static final String SESSION_CHANGE_PWD = "session.changepwd";

    // commands used in CRUD operations
    public static final String CMD_CREATE = "create";
    public static final String CMD_UPDATE = "update";
    public static final String CMD_DELETE = "delete";

    // the root of all command types (commands are organized in a tree way)
    public static final RootCommandType ROOT = new RootCommandType();

    /**
     * Initialize the list of command types
     */
    static {
        // main groups
        ROOT.add(INIT);
        ROOT.add(PUBLIC);
        ROOT.add(CASES);
        ROOT.add(INVENTORY);
        ROOT.add(ADMIN);
        ROOT.add(SESSION);

        // initialization
        ROOT.add(INIT_REGWORKSPACE);

        // administration module
        ROOT.addCRUD(ADMIN_ADMINUNITS);
        ROOT.addCRUD(ADMIN_COUNTRYSTRUCTURES);
        ROOT.addCRUD(ADMIN_UNITS);
        ROOT.addCRUD(ADMIN_SOURCES);
        ROOT.addCRUD(ADMIN_PRODUCTS);
        ROOT.addCRUD(ADMIN_REGIMENS);
        ROOT.addCRUD(ADMIN_SUBSTANCES);
        ROOT.addCRUD(ADMIN_TAGS);
        ROOT.addCRUD(ADMIN_USERS);
        ROOT.add(ADMIN_USERS_CHANGE_PWD);
        ROOT.addCRUD(ADMIN_USERPROFILES);
        ROOT.addCRUD(ADMIN_WORKSPACES);
        ROOT.add(SYSTEM_SETUP);

        // cases module
        ROOT.addCRUD(CASES_CASE);
        ROOT.add(CASES_CASE_CLOSE);
        ROOT.add(CASES_CASE_REOPEN);
        ROOT.add(CASES_CASE_TAG);
        ROOT.add(CASES_CASE_VALIDATE);
        ROOT.add(CASES_CASE_TRANSFER_OUT);
        ROOT.add(CASES_CASE_TRANSFER_IN);
        ROOT.add(CASES_CASE_TRANSFER_ROLLBACK);
        ROOT.addCRUD(CASES_CASE_PREVTREAT);
        ROOT.addCRUD(CASES_CASE_CONTACT);
        ROOT.addCRUD(CASES_CASE_SIDEEFFECT);
        ROOT.addCRUD(CASES_CASE_COMMENT);
        ROOT.addCRUD(CASES_CASE_ISSUE);
        ROOT.addCRUD(CASES_CASE_ISSUEFOLLOWUP);
        ROOT.addCRUD(CASES_CASE_MED_EXAM);
        ROOT.addCRUD(CASES_CASE_EXAM_MIC);
        ROOT.addCRUD(CASES_CASE_EXAM_CUL);
        ROOT.addCRUD(CASES_CASE_EXAM_XPERT);
        ROOT.addCRUD(CASES_CASE_EXAM_DST);
        ROOT.addCRUD(CASES_CASE_EXAM_HIV);
        ROOT.addCRUD(CASES_CASE_EXAM_XRAY);
        ROOT.add(CASES_TREAT_FOLLOWUP);
        ROOT.add(CASES_TREAT_UNDO);
        ROOT.add(CASES_TREAT_INI);

        // user sessions
        ROOT.add(SESSION_USER_SETTINGS);
        ROOT.add(SESSION_CHANGE_PWD, "changepwd");
    }


    /**
     * Return the {@link CommandType} by its name
     * @param name the full name of the command (path)
     * @return The instance of {@link CommandType} or raise a {@link CommandException} if command type is not found
     */
    public static CommandType get(String name) {
        CommandType cmd = ROOT.find(name);
        if (cmd == null) {
            throw new CommandException("Invalid command type: " + name);
        }
        return cmd;
    }
}
