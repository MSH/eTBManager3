package org.msh.etbm.services.usersession;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.db.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service to return information about the on-going user session under the current request
 *
 * Created by rmemoria on 30/9/15.
 */
@Service
@Configuration
public class UserSessionService {

    public static final String SESSION_KEY = "userSession";
    public static final String SESSION_ID = "userSessionID";

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * Return the user session information by its authentication token
     * @param authToken the authentication token
     * @return instance of the user session, or null if authentication token is invalid
     */
    @Transactional
    public UserSession getSessionByAuthToken(UUID authToken) {
        UserLogin login = entityManager.find(UserLogin.class, authToken);
        UserSession session = new UserSession();

        // recover the information of the user in the workspace
        UserWorkspace uw = getUserWorkspace(login.getUser(), login.getWorkspace());

        if (uw == null) {
            throw new IllegalArgumentException("User workspace not found");
        }

        session.setAdministrator(uw.isAdministrator());
        session.setUserWorkspaceId(uw.getId());
        session.setWorkspaceId(uw.getWorkspace().getId());
        session.setUserId(uw.getUser().getId());
        session.setUserLoginId(login.getId());

        if (!uw.isAdministrator()) {
            List<String> perms = createPermissionList(uw);
            session.setPermissions(perms);
        }

        return session;
    }


    /**
     * Create the list of permissions granted to the user
     * @param uw instance of {@link UserWorkspace}
     * @return list of permissions in an array of String values
     */
    private List<String> createPermissionList(UserWorkspace uw) {
        List<String> lst = new ArrayList<>();

        for (UserProfile prof: uw.getProfiles()) {
            for (UserPermission perm: prof.getPermissions()) {
                String roleName = perm.getUserRole().getName();
                lst.add(roleName);

                // can change ?
                if (perm.getUserRole().isChangeable() && perm.isCanChange()) {
                    lst.add(roleName + "_EDT");
                }
            }
        }

        return lst;
    }


    /**
     * Return the information about the user in the workspace based on the given user and workspace objects
     * @param user instance of the {@link User} object
     * @param workspace instance of the {@link Workspace} object
     * @return instance of the {@link UserWorkspace} object
     */
    private UserWorkspace getUserWorkspace(User user, Workspace workspace) {
        List<UserWorkspace> lst = entityManager.createQuery("from UserWorkspace uw " +
                "join fetch uw.user join fetch uw.workspace where uw.user.id = :userid " +
                "and uw.workspace.id = :wsid")
                .setParameter("userid", user.getId())
                .setParameter("wsid", workspace.getId())
                .getResultList();

        if (lst.size() == 0) {
            return null;
        }

        return lst.get(0);
    }


    /**
     * Return the current user session information of the given request
     * @param request the current HTTP request
     * @return the information about the user
     */
    @Bean
    @Scope("prototype")
    public UserSession userSession(HttpServletRequest request) {
        return (UserSession) request.getAttribute(SESSION_KEY);
    }

    /**
     * Return the UserWorkspace object based on the given user session. This function is a kind of factory
     * for the current user workspace
     * @param userSession information about the user session
     * @return instance of UserWorkspace
     */
    @Bean
    @Scope("request")
    @Transactional
    public UserWorkspaceDTO userWorkspace(UserSession userSession, DozerBeanMapper mapper) {
        if (userSession == null) {
            return null;
        }

        UserWorkspace uw = entityManager.find(UserWorkspace.class, userSession.getUserWorkspaceId());

        UserWorkspaceDTO res = mapper.map(uw, UserWorkspaceDTO.class);
        res.getUnit().setAdminUnit( mapper.map(uw.getUnit().getAddress().getAdminUnit(), AdministrativeUnitDTO.class));

        res.getUnit().setType(uw.getUnit().getTypeName());
        res.setPermissions(userSession.getPermissions());
        return res;
    }
}
