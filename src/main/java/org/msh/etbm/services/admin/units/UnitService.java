package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.Laboratory;
import org.msh.etbm.db.entities.Tbunit;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.repositories.AdminUnitRepository;
import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeriesService;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.admin.units.data.UnitDetailedData;
import org.msh.etbm.services.admin.units.data.UnitItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * CRUD service to handle units (laboratories and TB units)
 *
 * Created by rmemoria on 31/10/15.
 */
public interface UnitService extends EntityService<UnitQueryParams> {

}
