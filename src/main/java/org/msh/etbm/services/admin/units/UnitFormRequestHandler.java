package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryParams;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryResult;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.admin.units.data.UnitItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Type handler for unit fields. Return the data necessary to initialize a unit field in the client side
 *
 * Created by rmemoria on 18/1/16.
 */
@Component
public class UnitFormRequestHandler implements FormRequestHandler<UnitFormResponse> {

    // the supported type
    public static final String CMD_NAME = "unit";

    @Autowired
    AdminUnitService adminUnitService;

    @Autowired
    UnitService unitService;

    @Override
    public UnitFormResponse execFormRequest(FormRequest req) {
        String val = req.getStringParam("value");
        UUID unitId = val != null && !val.isEmpty() ? UUID.fromString(val) : null;

        // return the root list
        AdminUnitQueryParams qry = new AdminUnitQueryParams();
        qry.setRootUnits(true);
        qry.setProfile(AdminUnitQueryParams.QUERY_PROFILE_ITEM);
        AdminUnitQueryResult qr = (AdminUnitQueryResult)adminUnitService.findMany(qry);

        UnitFormResponse res = new UnitFormResponse();
        res.setAdminUnits(qr.getList());

        // if there is no ID, then just return the root list
        if (unitId == null) {
            return res;
        }

        // get information about the unit
        UnitData unit = unitService.findOne(unitId, UnitData.class);

        // get selected administrative unit
        res.setAdminUnitId( unit.getAdminUnit().getSelected().getId() );

        // query the units of administrative unit as parent
        UnitQueryParams uqry = new UnitQueryParams();
        uqry.setAdminUnitId(unit.getAdminUnit().getSelected().getId());
        uqry.setIncludeSubunits(true);
        uqry.setProfile(UnitQueryParams.PROFILE_ITEM);
        QueryResult<UnitItemData> unitRes = unitService.findMany(uqry);

        res.setUnits(unitRes.getList());

        return res;
    }

    @Override
    public String getFormCommandName() {
        return CMD_NAME;
    }
}
