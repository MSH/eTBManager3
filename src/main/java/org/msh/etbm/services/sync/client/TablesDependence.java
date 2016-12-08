package org.msh.etbm.services.sync.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mauricio on 07/12/2016.
 */
public class TablesDependence {

    private Map<String, List<String>> tablesDependence;

    private final String MEDICINE_SUBSTANCES = "DELETE FROM medicine_substances WHERE medicine_id = ?";
    private final String MEDICINE_REGIMEN = "DELETE FROM medicineregimen WHERE regimen_id = ?";
    private final String USERPERMISSION = "DELETE FROM userpermission WHERE profile_id = ?";
    private final String USERWORKSPACE_PROFILES = "DELETE FROM userworkspace_profiles WHERE userworkspace_id = ?";

    public TablesDependence() {
        initialize();
    }

    private void initialize() {
        tablesDependence = new HashMap<>();

        addDependence("product", MEDICINE_SUBSTANCES);
        addDependence("regimen", MEDICINE_REGIMEN);
        addDependence("userprofile", USERPERMISSION);
        addDependence("userworkspace", USERWORKSPACE_PROFILES);
    }

    private void addDependence(String tableName, String childrenQuery) {
        List list = tablesDependence.get(tableName);

        if (list == null) {
            list = new ArrayList<String>();
        }

        list.add(childrenQuery);
    }

    public List<String> getDependentCommands(String tableName) {
        List ret = tablesDependence.get(tableName);

        if (ret == null || ret.size() < 1) {
            return null;
        }

        return ret;
    }
}
