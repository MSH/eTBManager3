package org.msh.etbm.services.authentication;

import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.UserWorkspace;
import org.msh.etbm.db.enums.UserState;
import org.msh.etbm.services.users.UserUtils;
import org.msh.etbm.services.usersession.UserRequestService;
import org.msh.etbm.services.usersession.UserSession;
import org.msh.etbm.services.usersession.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

/**
 * Service to support user login and logout
 *
 * Created by rmemoria on 29/9/15.
 */
@Service
public class LoginService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    UserRequestService userRequestService;

    /**
     * Authenticate a user by its user name, password and workspace
     * @param username the user name
     * @param password the user password
     * @param workspaceId the workspace to log into
     * @return authentication token, to be reused in future requests, if the user was successfully authenticated
     */
    @Transactional
    public UUID login(String username, String password, UUID workspaceId, String ipAddress, String application) {
        // authenticate the user and password and return its userWorkspace information
        UserWorkspace uw = authenticate(username, password, workspaceId);

        // no information returned? so authentication failed
        if (uw == null) {
            return null;
        }

        UUID authToken = userSessionService.beginSession(uw.getId(), ipAddress, application);

        // include user session in cache
        UserSession ses = userSessionService.recoverSession(authToken);
        userRequestService.setUserSession(ses);

        return authToken;
    }


    /**
     * Log the user out of the system by its authentication token. Once logged out, the
     * authentication token will be invalid for future system calls, so another valid token
     * must be provided or a new login must be performed
     *
     * @param authToken Authentication token of the account to log out
     * @return true if user was successfully logged out
     */
    @Transactional
    public boolean logout(UUID authToken) {
        if (authToken == null) {
            return false;
        }

        return userSessionService.endSession(authToken);
    }



    /**
     * Authenticate the user and, in case of success, return the instance of the UserWorkspace related to it
     * @param username the user login name
     * @param password the user password
     * @param workspaceId the workspace ID to log in. If not informed, system will automatically select the user workspace
     * @return instance of UserWorkspace
     */
    public UserWorkspace authenticate(String username, String password, UUID workspaceId) {
        String pwdhash = UserUtils.hashPassword(password);

        // no workspace was defined ?
        if (workspaceId == null) {
            // authenticate user and password
            List<User> lst = entityManager.createQuery("from User u where u.login = :login " +
                    "and upper(u.password) = :pwd and u.state <> :blockstate")
                    .setParameter("login", username.toUpperCase())
                    .setParameter("pwd", pwdhash.toUpperCase())
                    .setParameter("blockstate", UserState.BLOCKED)
                    .getResultList();

            if (lst.isEmpty()) {
                return null;
            }

            User user = lst.get(0);
            return selectUserWorkspace(user);
        } else {
            // authenticate user, password and workspace
            List<UserWorkspace> lst = entityManager.createQuery("from UserWorkspace uw " +
                    "join fetch uw.user u " +
                    "join fetch uw.workspace w " +
                    "where u.login = :login " +
                    "and upper(u.password) = :pwd " +
                    "and u.state <> :blockstate " +
                    "and w.id = :wsid")
                    .setParameter("login", username.toUpperCase())
                    .setParameter("pwd", pwdhash.toUpperCase())
                    .setParameter("wsid", workspaceId)
                    .setParameter("blockstate", UserState.BLOCKED)
                    .getResultList();

            if (lst.isEmpty()) {
                return null;
            }

            return lst.get(0);
        }
    }


    /**
     * Select the user workspace by the given user
     * @param user the user object to search the workspace from
     * @return instance of UserWorkspace
     */
    private UserWorkspace selectUserWorkspace(User user) {
        if (user.getDefaultWorkspace() != null) {
            return user.getDefaultWorkspace();
        }

        List<UserWorkspace> lst = entityManager.createQuery("from UserWorkspace where id = (select min(id) " +
                "from UserWorkspace aux where aux.user.id = :userid)")
                .setParameter("userid", user.getId())
                .setMaxResults(1)
                .getResultList();

        return lst.isEmpty() ? null : lst.get(0);
    }

}
