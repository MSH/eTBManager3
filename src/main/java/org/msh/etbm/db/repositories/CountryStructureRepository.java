package org.msh.etbm.db.repositories;

import org.msh.etbm.db.entities.CountryStructure;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 10/10/15.
 */
public interface CountryStructureRepository extends CrudRepository<CountryStructure, UUID> {
    List<CountryStructure> findByNameAndWorkspaceIdAndLevel(String name, UUID ws, int level);
}
