package org.msh.etbm.services.db;


import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.db.entities.UserRole;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Initialize the user roles in the database
 * Created by rmemoria on 2/9/15.
 */

public class InitRoles {

    private List<UserRole> roles;

    public void execute(EntityManager entityManager) {
        roles = entityManager.createQuery("from UserRole").getResultList();

        UserRole[] lst = JsonParser.parseResource("/db/data/userroles.json", UserRole[].class);

        for (UserRole role: lst) {
            // role exists ?
            if (findRoleByName(role.getName()) == null) {
                entityManager.persist(role);
            }
        }
    }

    /**
     * Find role by alias
     * @param name
     * @return
     */
    private UserRole findRoleByName(String name) {
        for (UserRole role: roles) {
            if (role.getName().equals(name)) {
                return role;
            }
        }
        return null;
    }

}
