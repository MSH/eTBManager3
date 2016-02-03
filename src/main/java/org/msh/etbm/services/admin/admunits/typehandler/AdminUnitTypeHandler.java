package org.msh.etbm.services.admin.admunits.typehandler;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.forms.FieldInitRequest;
import org.msh.etbm.commons.forms.types.TypeHandler;
import org.msh.etbm.commons.forms.types.TypesManagerService;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryParams;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryResult;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeries;
import org.msh.etbm.services.admin.admunits.typehandler.AdminUnitFieldResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

/**
 * Administrative unit type handler, for field initialization support in the client side
 *
 * Created by rmemoria on 18/1/16.
 */
@Component
public class AdminUnitTypeHandler implements TypeHandler<AdminUnitFieldResponse[]> {

    public static final String TYPE_NAME = "adminUnit";

    @Autowired
    AdminUnitService adminUnitService;

    @Autowired
    TypesManagerService typesManagerService;

    @PostConstruct
    public void register() {
        typesManagerService.register(TYPE_NAME, this);
    }


    protected String getLevelName(int level, List<CountryStructureData> lst) {
        String delim = "";
        String name = "";
        for (CountryStructureData cs: lst) {
            if (cs.getLevel() == level) {
                name += delim + cs.getName();
                delim = ", ";
            }
        }
        return name;
    }

    @Override
    public AdminUnitFieldResponse[] initField(FieldInitRequest req) {
        String val = (String)req.getValue();

        UUID id = val != null && !val.isEmpty() ? UUID.fromString(val) : null;

        // return the root list
        AdminUnitQueryParams qry = new AdminUnitQueryParams();
        qry.setRootUnits(true);
        qry.setProfile(AdminUnitQueryParams.QUERY_PROFILE_ITEM);
        qry.setFetchCountryStructure(true);
        AdminUnitQueryResult qr = adminUnitService.findMany(qry);

        // calc max level
        int level = 0;
        for (CountryStructureData cs: qr.getCsList()) {
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
        vals[0].setList( qr.getList() );

        // if there is no ID, then just return the root list
        if (id == null) {
            return vals;
        }

        // recover administrative unit (with parent series)
        AdminUnitSeries adminUnit = adminUnitService.findOne(id, AdminUnitSeries.class);

        // prepare query for other levels
        qry.setRootUnits(false);
        qry.setFetchCountryStructure(false);

        // number of levels in the admin unit series
        int seriesLevels = adminUnit.getLevel();
        int l = 1;
        while (l <= seriesLevels) {
            SynchronizableItem item = adminUnit.getAdminUnitLevel(l);

            vals[l - 1].setSelected(item.getId());

            if (l < level) {
                qry.setParentId(item.getId());
                qr = adminUnitService.findMany(qry);
                vals[l].setList(qr.getList());
            }
            l++;
        }

        return vals;
    }
}
