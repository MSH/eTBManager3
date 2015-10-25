package org.msh.etbm.services.admin.admunits;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 24/10/15.
 */
public interface AdminUnitService {

    UUID create(@Valid AdminUnitRequest req);

    UUID update(UUID id, @Valid AdminUnitRequest req);

    AdminUnitData delete(UUID id);

    AdminUnitData get(UUID id);

    List<AdminUnitData> query(AdminUnitQuery qry);
}
