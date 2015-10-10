package org.msh.etbm.db.repositories;

import org.msh.etbm.db.entities.Workspace;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 10/10/15.
 */
public interface WorkspaceRepository extends Repository<Workspace, UUID> {
    List<Workspace> findByName(String name);
}
