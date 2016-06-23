package org.msh.etbm.services.session.usersession;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.CacheConfiguration;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.services.security.permissions.Permission;
import org.msh.etbm.services.security.permissions.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service to return information about the on-going user session under the current request
 *
 * Created by rmemoria on 30/9/15.
 */
@Service
public class UserSessionService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    Permissions permissions;

    @Autowired
    UserRequestService userRequestService;


    /**
     * Register the new user session
     * @param userWorkspaceId the ID of the user workspace
     * @param ipAddress the ip address of the client requesting login
     * @param application the name of the client application requesting login. Usually it is the browser agent
     * @return authentication token, to be used in future authentications
     */
    @Transactional
    public UUID beginSession(UUID userWorkspaceId, String ipAddress, String application) {
        UserWorkspace uw = entityManager.find(UserWorkspace.class, userWorkspaceId);
        if (uw == null) {
            throw new EntityNotFoundException("Workspace not found");
        }

        // register new user session
        UserLogin login = new UserLogin();
        login.setWorkspace(uw.getWorkspace());
        login.setLoginDate(new Date());
        login.setIpAddress(ipAddress);
        login.setApplication(application);
        login.setUser(uw.getUser());

        // save the user session
        entityManager.persist(login);
        entityManager.flush();

        return login.getId();
    }


    /**
     * Return the user session information by its authentication token
     * @param authToken the authentication token
     * @return instance of the user session, or null if authentication token is invalid
     */
    @Transactional
    @Cacheable(value = CacheConfiguration.CACHE_SESSION_ID, unless = "#result == null")
    public UserSession recoverSession(UUID authToken) {
        UserLogin login = entityManager.find(UserLogin.class, authToken);
        if (login == null) {
            return null;
        }

        // is a valid session ?
        if (login.getLogoutDate() != null) {
            return null;
        }

        // recover the information of the user in the workspace
        UserWorkspace uw = getUserWorkspace(login.getUser(), login.getWorkspace());

        if (uw == null) {
            return null;
        }

        UserSession session = mapper.map(uw, UserSession.class);

        session.setUserLoginId(login.getId());

        if (!uw.isAdministrator()) {
            List<String> perms = createPermissionList(uw);
            session.setPermissions(perms);
        }

        return session;
    }


    /**
     * End a session by specifying its logout date. Once closed, the session cannot be recovered anymore
     * @param authToken the authentication token assigned to the session
     * @return
     */
    @Transactional
    @CacheEvict(value = CacheConfiguration.CACHE_SESSION_ID)
    public boolean endSession(UUID authToken) {
        // recover the user login assigned to the authentication token
        List<UserLogin> lst = entityManager
                .createQuery("from UserLogin  where id = :id and logoutDate is null")
                .setParameter("id", authToken)
                .getResultList();

        if (lst.size() == 0) {
            return false;
        }

        UserLogin login = lst.get(0);

        // register the logout date
        Date logoutDate = new Date();

        login.setLogoutDate(logoutDate);
        login.setLastAccess(logoutDate);

        entityManager.persist(login);
        entityManager.flush();
        return true;
    }


    /**
     * Return the client response to be sent back to the client with information about the user session
     * @return instance of {@link UserSessionResponse}
     */
    @Transactional
    public UserSessionResponse createClientResponse() {
        UserSession userSession = userRequestService.getUserSession();

        UserSessionResponse resp = mapper.map(userSession, UserSessionResponse.class);

        resp.setWorkspaces(getUserWorkspaces());

        return resp;
    }

    /**
     * Return the list of workspaces available for the user
     * @return List of {@link SynchronizableItem} containing id and name of the workspace
     */
    @Transactional
    public List<SynchronizableItem> getUserWorkspaces() {
        UserSession session = userRequestService.getUserSession();

        List<Object[]> lst = entityManager
                .createQuery("select uw.id, uw.workspace.name from UserWorkspace uw where uw.user.id = :id " +
                        "order by uw.workspace.name")
                .setParameter("id", session.getUserId())
                .getResultList();

        List<SynchronizableItem> workspaces = new ArrayList<>();
        for (Object[] vals: lst) {
            UUID id = (UUID)vals[0];
            String name = (String)vals[1];
            workspaces.add(new SynchronizableItem(id, name));
        }

        return workspaces;
    }

    /**
     * Create the list of permissions granted to the user
     * @param uw instance of {@link UserWorkspace}
     * @return list of permissions in an array of String values
     */
    private List<String> createPermissionList(UserWorkspace uw) {
        List<String> lst = new ArrayList<>();

        for (UserProfile prof: uw.getProfiles()) {
            for (UserPermission up: prof.getPermissions()) {
                String permID = up.getPermission();
                Permission perm = permissions.find(permID);

                if (perm != null) {
                    lst.add(permID);

                    // can change ?
                    if (perm.isChangeable() && up.isCanChange()) {
                        lst.add(permID + "_EDT");
                    }
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
        List<UserWorkspace> lst = entityManager.createQuery("from UserWorkspace uw "
                + "join fetch uw.user join fetch uw.workspace where uw.user.id = :userid "
                + "and uw.workspace.id = :wsid")
                .setParameter("userid", user.getId())
                .setParameter("wsid", workspace.getId())
                .getResultList();

        if (lst.size() == 0) {
            return null;
        }

        return lst.get(0);
    }

    @Transactional
    public void updateUserPrefLanguage(UserSession userSession, String newLocale) {
        User user = entityManager.find(User.class, userSession.getUserId());

        if (newLocale != null && !newLocale.equals(user.getLanguage())) {
            user.setLanguage(newLocale);
            entityManager.merge(user);
            userSession.setLanguage(newLocale);
        }
    }

}
