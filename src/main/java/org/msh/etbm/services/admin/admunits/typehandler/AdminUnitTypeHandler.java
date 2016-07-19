package org.msh.etbm.services.admin.admunits.typehandler;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryParams;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryResult;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Administrative unit type handler, for field initialization support in the client side
 * <p>
 * Created by rmemoria on 18/1/16.
 */
@Component
public class AdminUnitTypeHandler implements FormRequestHandler<AdminUnitFieldResponse[]> {

    public static final String CMD_NAME = "adminUnit";
    // the ID of the administrative unit being referenced
    public static final String PARAM_VALUE = "value";

    @Autowired
    AdminUnitService adminUnitService;


    protected String getLevelName(int level, List<CountryStructureData> lst) {
        String delim = "";
        String name = "";
        for (CountryStructureData cs : lst) {
            if (cs.getLevel() == level) {
                name += delim + cs.getName();
                delim = ", ";
            }
        }
        return name;
    }

    @Override
    public AdminUnitFieldResponse[] execFormRequest(FormRequest req) {
        String val = req.getStringParam("value");

        UUID id = val != null && !val.isEmpty() ? UUID.fromString(val) : null;

        // return the root list
        AdminUnitQueryParams qry = new AdminUnitQueryParams();
        qry.setRootUnits(true);
        qry.setProfile(AdminUnitQueryParams.QUERY_PROFILE_ITEM);
        qry.setFetchCountryStructure(true);
        AdminUnitQueryResult qr = (AdminUnitQueryResult) adminUnitService.findMany(qry);

        // calc max level
        int level = 0;
        for (CountryStructureData cs : qr.getCsList()) {
            if (cs.getLevel() > level) {
                level = cs.getLevel();
            }
        }

        // mount result list using country structure
        AdminUnitFieldResponse[] vals = new AdminUnitFieldResponse[level];
        for (int i = 0; i < level; i++) {
            String csname = getLevelName(i + 1, qr.getCsList());
            AdminUnitFieldResponse fld = new AdminUnitFieldResponse();
            fld.setLevel(i + 1);
            fld.setLabel(csname);
            vals[i] = fld;
        }

        // set the root list
        vals[0].setList(qr.getList());

        // if there is no ID, then just return the root list
        if (id == null) {
            return vals;
        }

        // recover administrative unit (with parent series)
        AdminUnitData adminUnit = adminUnitService.findOne(id, AdminUnitData.class);

        // prepare query for other levels
        qry.setRootUnits(false);
        qry.setFetchCountryStructure(false);

        // number of levels in the admin unit series
        int seriesLevels = adminUnit.getLevel();
        int l = 1;
        while (l <= seriesLevels) {
            SynchronizableItem item = adminUnit.getItemAtLevel(l);

            vals[l - 1].setSelected(item.getId());

            if (l < level) {
                qry.setParentId(item.getId());
                qr = (AdminUnitQueryResult) adminUnitService.findMany(qry);
                vals[l].setList(qr.getList());
            }
            l++;
        }

        return vals;
    }

    @Override
    public String getFormCommandName() {
        return CMD_NAME;
    }

}
