package org.msh.etbm.services.offline.fileimporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the queries that must be executed when executing a parent table UPDATE.
 * As this list of dependent registers will come forward on the file, those stored commands should delete the dependent
 * registers of the updated parent table register.
 * Created by Mauricio on 07/12/2016.
 */
public class SQLUpdateChildTables {

    private Map<String, List<String>> parentTablesCommands;

    private final String MEDICINE_SUBSTANCES = "DELETE FROM medicine_substances WHERE medicine_id = ?";
    private final String MEDICINE_REGIMEN = "DELETE FROM medicineregimen WHERE regimen_id = ?";
    private final String USERPERMISSION = "DELETE FROM userpermission WHERE profile_id = ?";
    private final String USERWORKSPACE_PROFILES = "DELETE FROM userworkspace_profiles WHERE userworkspace_id = ?";

    public SQLUpdateChildTables() {
        initialize();
    }

    /**
     * Creates the Map of parent table name and its child commands.
     */
    private void initialize() {
        parentTablesCommands = new HashMap<>();

        addChild("product", MEDICINE_SUBSTANCES);
        addChild("regimen", MEDICINE_REGIMEN);
        addChild("userprofile", USERPERMISSION);
        addChild("userworkspace", USERWORKSPACE_PROFILES);
    }

    /**
     * Add a child command to a entry of the map, that represents a parent table.
     * @param parentTableName
     * @param childQuery
     */
    private void addChild(String parentTableName, String childQuery) {
        List list = parentTablesCommands.get(parentTableName);

        if (list == null) {
            list = new ArrayList<String>();
        }

        list.add(childQuery);
    }

    /**
     *
     * @param parentTableName
     * @return list of SQL commands that deletes the dependencies of the parent table.
     */
    public List<String> getParentCommands(String parentTableName) {
        List ret = parentTablesCommands.get(parentTableName);

        if (ret == null || ret.size() < 1) {
            return null;
        }

        return ret;
    }
}
