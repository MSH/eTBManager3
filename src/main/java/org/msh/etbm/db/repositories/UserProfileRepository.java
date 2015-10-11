package org.msh.etbm.db.repositories;

import org.msh.etbm.db.entities.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 10/10/15.
 */
public interface UserProfileRepository extends CrudRepository<UserProfile, UUID> {
    List<UserProfile> findByNameAndWorkspaceId(String name, UUID wsid);
}
