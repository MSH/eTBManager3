package org.msh.etbm.db.repositories;

import org.msh.etbm.db.entities.Unit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 10/10/15.
 */
public interface UnitRepository extends CrudRepository<Unit, UUID> {

    /**
     * Return all units that match the given name and workspace ID
     * @param name the unit name
     * @param wsId the workspace ID
     * @return
     */
    List<Unit> findByNameAndWorkspaceId(String name, UUID wsId);
}
