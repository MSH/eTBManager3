package org.msh.etbm.web.api.units;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.units.UnitQuery;
import org.msh.etbm.services.admin.units.UnitQueryService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by rmemoria on 28/10/15.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated
public class UnitsQueryREST {

    @Autowired
    UnitQueryService unitQueryService;

    /**
     * Query units (labs or TB units) based on the request query
     * @param qry search criterias to query the database
     * @param profile identifies the kind of information to be returned by the query
     * @return
     */
    @RequestMapping(value = "/unit/query/{id}", method = RequestMethod.GET)
    public QueryResult queryUnits(@Valid @RequestBody UnitQuery qry, @RequestParam("p") String profile) {
        System.out.println("UNITS PROFILE = " + profile);
        return unitQueryService.queryUnits(qry);
    }
}
