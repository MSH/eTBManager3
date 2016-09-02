package org.msh.etbm.services.admin.units.forms;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryParams;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryResult;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;
import org.msh.etbm.services.admin.units.UnitQueryParams;
import org.msh.etbm.services.admin.units.UnitService;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.admin.units.data.UnitItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Type handler for unit fields. Return the data necessary to initialize a unit field in the client side
 * <p>
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
        boolean unitsOnly = req.getBoolParam("units");

        return unitsOnly ? unitsResponse(req) : initResponse(req);
    }


    /**
     * Called when field needs to update the list of units
     *
     * @param req the request data
     * @return instance of {@link UnitFormResponse} containing the list of units
     */
    protected UnitFormResponse unitsResponse(FormRequest req) {
        // what will be sent back to the client
        UnitFormResponse res = new UnitFormResponse();

        // get the workspace ID (not required)
        UUID wsId = req.getIdParam("workspaceId");

        // get the selected administrative unit ID
        UUID auId = req.getIdParam("adminUnitId");

        // get the selected type of unit
        String unitType = req.getStringParam("type");

        List<UnitItemData> units = getUnits(auId, wsId, unitType);

        res.setUnits(units);

        res.setWorkspaceId(wsId);

        return res;
    }

    /**
     * Basic response, called when field control is being initialized
     *
     * @return instance of {@link UnitFormResponse} containing the data to initialize the field
     */
    protected UnitFormResponse initResponse(FormRequest req) {
        // get the selected unit ID, if any
        UUID unitId = req.getIdParam("value");

        // what will be sent back to the client
        UnitFormResponse res = new UnitFormResponse();

        // return the root list
        AdminUnitQueryParams qry = new AdminUnitQueryParams();

        // check if there is any workspace specified
        UUID wsId = req.getIdParam("workspaceId");
        if (wsId != null) {
            qry.setWorkspaceId(wsId);
        }
        qry.setRootUnits(true);
        qry.setProfile(AdminUnitQueryParams.QUERY_PROFILE_ITEM);
        AdminUnitQueryResult qr = (AdminUnitQueryResult) adminUnitService.findMany(qry);

        res.setAdminUnits(qr.getList());

        res.setWorkspaceId(wsId);

        // if there is no ID, then just return the root list
        if (unitId == null) {
            return res;
        }

        // get information about the unit
        UnitData unit = unitService.findOne(unitId, UnitData.class);

        // get the selected type of unit
        String unitType = req.getStringParam("type");

        AdminUnitData au = adminUnitService.findOne(unit.getAdminUnit().getId(), AdminUnitData.class);
        UUID auId = au.getP0() != null ? au.getP0().getId() : au.getId();

        // get selected administrative unit
        res.setAdminUnitId(auId);

        // get the list of units to be displayed
        res.setUnits(getUnits(auId, wsId, unitType));

        return res;
    }

    /**
     * Return the list of units for the give admin unit and workspace
     *
     * @param adminUnitId the administrative unit to get units from
     * @param workspaceId the workspace ID that admin unit belongs to (not required if it is the same of the
     *                    user session)
     * @return list of units
     */
    protected List<UnitItemData> getUnits(UUID adminUnitId, UUID workspaceId, String unitType) {
        // query the units of administrative unit as parent
        UnitQueryParams uqry = new UnitQueryParams();

        uqry.setAdminUnitId(adminUnitId);
        if (workspaceId != null) {
            uqry.setWorkspaceId(workspaceId);
        }
        if (unitType != null) {
            uqry.setType(UnitType.valueOf(unitType));
        }
        uqry.setIncludeSubunits(true);
        uqry.setProfile(UnitQueryParams.PROFILE_ITEM);
        QueryResult<UnitItemData> unitRes = unitService.findMany(uqry);
        return unitRes.getList();
    }

    @Override
    public String getFormCommandName() {
        return CMD_NAME;
    }
}
