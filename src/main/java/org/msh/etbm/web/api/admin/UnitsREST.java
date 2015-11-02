package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.units.data.UnitDetailedData;
import org.msh.etbm.services.admin.units.data.UnitItemData;
import org.msh.etbm.services.admin.units.UnitQuery;
import org.msh.etbm.services.admin.units.UnitRequest;
import org.msh.etbm.services.admin.units.UnitService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.web.api.authentication.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
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
@Authenticated(permissions = {Permissions.ADMIN_UNITS_EDT})
public class UnitsREST {

    @Autowired
    UnitService unitService;

    /**
     * Query units (labs or TB units) based on the request query
     * @param qry search criterias to query the database
     * @param profile identifies the kind of information to be returned by the query
     * @return
     */
    @RequestMapping(value = "/unit/query/{id}", method = RequestMethod.GET)
    @Authenticated
    public QueryResult queryUnits(@Valid @RequestBody UnitQuery qry, @RequestParam("p") String profile) {
        return unitService.findMany(qry);
    }


    @RequestMapping(value = "/unit/{id}", method = RequestMethod.GET)
    @Authenticated
    public StandardResult get(@PathVariable UUID id) {
        UnitDetailedData data = unitService.findOne(id, UnitDetailedData.class);
        return new StandardResult(data, null, data != null);
    }

    @RequestMapping(value = "/unit", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody UnitRequest req) {
        ServiceResult res = unitService.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/unit/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody UnitRequest req) {
        ServiceResult res = unitService.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/unit/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        ServiceResult res = unitService.delete(id);
        return new StandardResult(res);
    }
}
