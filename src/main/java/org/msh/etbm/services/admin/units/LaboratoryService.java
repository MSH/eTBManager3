package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.db.entities.Laboratory;
import org.msh.etbm.db.repositories.UnitRepository;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 28/10/15.
 */
@Service
public class LaboratoryService extends EntityService<Laboratory, UnitRepository> {
}
