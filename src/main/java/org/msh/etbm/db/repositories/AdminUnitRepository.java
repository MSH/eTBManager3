package org.msh.etbm.db.repositories;

import org.msh.etbm.db.entities.AdministrativeUnit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 10/10/15.
 */
public interface AdminUnitRepository extends CrudRepository<AdministrativeUnit, UUID> {
    /**
     * Search an administrative unit by its name and workspace ID
     *
     * @param name
     * @param wsId
     * @return list of administrative units found
     */
    List<AdministrativeUnit> findByNameAndWorkspaceId(String name, UUID wsId);
}
