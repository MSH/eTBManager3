package org.msh.etbm.services.admin.workspaces;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.UserView;
import org.msh.etbm.services.admin.workspaces.impl.AdminUnitTemplate;
import org.msh.etbm.services.admin.workspaces.impl.NewWorkspaceTemplate;
import org.msh.etbm.services.admin.workspaces.impl.TbunitTemplate;
import org.msh.etbm.services.admin.workspaces.impl.UserProfileTemplate;
import org.msh.etbm.services.security.permissions.Permission;
import org.msh.etbm.services.security.permissions.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Spring service to create a new workspace based on a json template file, i.e, the minimum of entities are created
 * in order to use the workspace, like administrative units, units and profiles
 *
 * Created by rmemoria on 11/4/16.
 */
@Service
public class WorkspaceCreator {

    @Autowired
    DozerBeanMapper mapper;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    Permissions permissions;

    @Transactional
    public WorkspaceData create(String name) {

        // read the template data
        NewWorkspaceTemplate template = JsonParser.parseResource("/templates/json/new-workspace-template.json", NewWorkspaceTemplate.class);

        Workspace ws = insertWorkspace(name, template);

        List<AdministrativeUnit> lst = createAdminUnits(template);

        createUnits(template, lst);

        createProfiles(template);

        return mapper.map(ws, WorkspaceData.class);
    }


    /**
     * Add an user to a workspace. This service is used when it is necessary
     * @param userId
     * @param workspaceId
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public UUID addUserToWorkspace(UUID userId, UUID workspaceId) {
        User user = entityManager.find(User.class, userId);

        // get the first unit available
        List<Unit> units = entityManager
                .createQuery("from Unit where workspace.id = :id")
                .setParameter("id", workspaceId)
                .setMaxResults(1)
                .getResultList();

        if (units.isEmpty()) {
            throw new RuntimeException("No unit found");
        }
        Unit unit = units.get(0);

        Workspace workspace = entityManager.find(Workspace.class, workspaceId);

        UserWorkspace uw = new UserWorkspace();
        uw.setUser(user);
        uw.setUnit(unit);
        uw.setWorkspace(workspace);
        uw.setView(UserView.COUNTRY);
        uw.setAdministrator(true);
        uw.setPlayOtherUnits(true);
        uw.setAdminUnit(unit.getAddress().getAdminUnit());

        entityManager.persist(uw);
        entityManager.flush();

        return uw.getId();
    }


    /**
     * Insert a new workspace of a given name and with template
     * @param name
     * @param template
     * @return
     */
    private Workspace insertWorkspace(String name, NewWorkspaceTemplate template) {
        Workspace ws = template.getWorkspace();

        ws.setName(name);

        entityManager.persist(ws);

        WorkspaceLog wslog = new WorkspaceLog();
        wslog.setId(ws.getId());
        wslog.setName(ws.getName());
        entityManager.persist(wslog);
        entityManager.flush();

        return ws;
    }

    /**
     * Create the administrative units used in the new workspace
     * @return The list of administrative units created
     */
    private List<AdministrativeUnit> createAdminUnits(NewWorkspaceTemplate template) {
        for (CountryStructure cs: template.getCountryStructures()) {
            cs.setWorkspace(template.getWorkspace());
            entityManager.persist(cs);
            entityManager.flush();
        }

        DecimalFormat format = new DecimalFormat("000");
        int rootCount = 1;

        List<AdministrativeUnit> adminUnits = new ArrayList<>();

        // create all administrative units
        for (AdminUnitTemplate it: template.getAdminUnits()) {
            AdministrativeUnit au = new AdministrativeUnit();
            au.setName(it.getName());

            // get country structure
            for (CountryStructure cs: template.getCountryStructures()) {
                if (cs.getName().equals(it.getCountryStructure())) {
                    au.setCountryStructure(cs);
                    break;
                }
            }

            if (au.getCountryStructure() == null) {
                throw new RuntimeException("No country structure defined for admin unit " + it.getName());
            }

            // get parent
            if (it.getParent() != null) {
                AdministrativeUnit p = adminUnits
                        .stream()
                        .filter(x -> x.getName().equals(it.getParent()))
                        .findFirst()
                        .get();

                if (p == null) {
                    throw new RuntimeException("Error creating administrative unit " + it.getName() +
                            ". Invalid parent " + it.getParent());
                }

                au.setParent(p);
                p.setUnitsCount(p.getUnitsCount() + 1);
                p.getUnits().add(au);
                au.setCode( p.getCode() + format.format(p.getUnitsCount() + 1L));
            } else {
                au.setCode( format.format(rootCount));
                rootCount++;
            }

            au.setWorkspace(template.getWorkspace());

            entityManager.persist(au);
            entityManager.flush();
            adminUnits.add(au);
        }

        return adminUnits;
    }


    /**
     * Create the units from a template file
     */
    private void createUnits(NewWorkspaceTemplate template, List<AdministrativeUnit> adminUnits) {
        for (TbunitTemplate t: template.getTbunits()) {
            Tbunit unit = mapper.map(t, Tbunit.class);

            AdministrativeUnit au = adminUnits
                    .stream()
                    .filter(x -> x.getName().equals(t.getAdminUnitName()))
                    .findFirst()
                    .get();

            unit.getAddress().setAdminUnit(au);

            if (unit.getAddress().getAdminUnit() == null) {
                throw new RuntimeException("Administrative unit " + t.getAdminUnitName() + " not found for unit " + t.getName());
            }

            unit.setWorkspace(template.getWorkspace());

            entityManager.persist(unit);
            entityManager.flush();
        }
    }


    /**
     * Create the user profiles
     */
    private void createProfiles(NewWorkspaceTemplate template) {
        for (UserProfileTemplate t: template.getProfiles()) {
            UserProfile u = new UserProfile();
            u.setName(t.getName());
            u.setWorkspace(template.getWorkspace());

            if (t.isAllRoles()) {
                // include all roles
                for (Permission perm: permissions.getList()) {
                    String roleName = perm.getId();
                    if (perm.isChangeable()) {
                        roleName = "+" + roleName;
                    }
                    createPermission(u, roleName);
                }
            } else {
                // include selected roles
                if (t.getRoles() != null) {
                    for (String roleName: t.getRoles()) {
                        createPermission(u, roleName);
                    }
                }
            }

            entityManager.persist(u);
            entityManager.flush();
        }
    }

    /**
     * Create the permissions of the user profile
     * @param profile
     * @param permName
     */
    private void createPermission(UserProfile profile, String permName) {
        // create new permission
        UserPermission perm = new UserPermission();

        boolean canChange = permName.startsWith("+");
        String roleName = canChange ? permName.substring(1) : permName;

        Permission appPerm = permissions.find(roleName);
        if (appPerm == null) {
            throw new RuntimeException("Permission not defined: " + roleName + " profile: " + profile.getName());
        }

        perm.setPermission(roleName);
        if (appPerm.isChangeable()) {
            perm.setCanChange(canChange);
        }
        perm.setUserProfile(profile);

        profile.getPermissions().add(perm);
    }

}
