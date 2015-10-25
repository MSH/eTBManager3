package org.msh.etbm.services.admin.admunits;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Interface of a service to handle CRUD operations in a country structure
 * Created by rmemoria on 24/10/15.
 */
public interface CountryStructureService {

    UUID create(@Valid CountryStructureRequest req);

    UUID update(UUID id, @Valid CountryStructureRequest req);

    CountryStructureData delete(UUID id);

    CountryStructureData get(UUID id);

    List<CountryStructureData> query();

}
