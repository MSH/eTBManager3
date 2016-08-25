package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequestService;
import org.msh.etbm.services.admin.units.UnitQueryParams;
import org.msh.etbm.services.admin.units.UnitService;
import org.msh.etbm.services.admin.units.data.UnitDetailedData;
import org.msh.etbm.services.admin.units.data.UnitFormData;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * REST controller to handle units operations
 * Created by rmemoria on 31/10/15.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_UNITS_EDT})
public class UnitsREST {

    @Autowired
    UnitService unitService;

    @Autowired
    FormRequestService formRequestService;

    /**
     * Query units (labs or TB units) based on the request query
     *
     * @param qry search criterias to query the database
     * @return lisat of units
     */
    @RequestMapping(value = "/unit/query", method = RequestMethod.POST)
    @Authenticated
    public QueryResult queryUnits(@Valid @RequestBody UnitQueryParams qry) {
        return unitService.findMany(qry);
    }


    @RequestMapping(value = "/unit/{id}", method = RequestMethod.GET)
    @Authenticated
    public UnitDetailedData get(@PathVariable UUID id) {
        return unitService.findOne(id, UnitDetailedData.class);
    }

    @RequestMapping(value = "/unit/form/{id}", method = RequestMethod.GET)
    @Authenticated
    public UnitFormData getFormData(@PathVariable UUID id) {
        return unitService.findOne(id, UnitFormData.class);
    }

    @RequestMapping(value = "/unit", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody UnitFormData req) throws BindException {
        ServiceResult res = unitService.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/unit/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody UnitFormData req) throws BindException {
        ServiceResult res = unitService.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/unit/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) throws BindException {
        unitService.delete(id).getId();
        return new StandardResult(id, null, true);
    }
}
