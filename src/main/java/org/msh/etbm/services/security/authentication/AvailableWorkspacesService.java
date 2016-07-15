package org.msh.etbm.services.security.authentication;

import org.msh.etbm.db.entities.UserWorkspace;
import org.msh.etbm.services.security.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Return the list of available workspaces for the given user credential
 * Created by rmemoria on 29/9/15.
 */
@Service
public class AvailableWorkspacesService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    LoginService loginService;

    /**
     * Return the list of workspaces for the given user
     *
     * @param login the user login
     * @param pwd   the user password
     * @return list of available workspaces
     */
    public List<WorkspaceInfo> getWorkspaces(String login, String pwd) {
        // check if user and password are valid
        UserWorkspace userWorkspace = loginService.authenticate(login, pwd, null);

        // if not valid, throw an exception
        if (userWorkspace == null) {
            throw new ForbiddenException("Invalid username/password");
        }

        // return the list of
        List<Object[]> lst = entityManager.createQuery("select w.id, w.name, uw.unit.name " +
                "from UserWorkspace uw join uw.workspace w where uw.user.id = :id " +
                "order by w.name")
                .setParameter("id", userWorkspace.getUser().getId())
                .getResultList();

        List<WorkspaceInfo> res = new ArrayList<WorkspaceInfo>();
        for (Object[] vals : lst) {
            res.add(new WorkspaceInfo((UUID) vals[0],
                    (String) vals[1],
                    (String) vals[2]));
        }

        return res;
    }
}
