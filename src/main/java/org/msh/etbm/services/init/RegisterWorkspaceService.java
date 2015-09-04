package org.msh.etbm.services.init;

import com.fasterxml.uuid.Generators;
import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.Mapper;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.services.init.impl.*;
import org.msh.etbm.services.users.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Register a new workspace. Only valid during initialization of the system, and no other workspace should be available
 * Created by rmemoria on 1/9/15.
 */
@Service
@Scope("prototype")
public class RegisterWorkspaceService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    Mapper mapper;

    private NewWorkspaceTemplate template;
    private List<AdministrativeUnit> adminUnits;
    private List<UserRole> roles;

    /**
     * Register a new workspace during the initialization process
     * @param form contains information about the workspace to be registered
     */
    @Transactional
    public UUID register(@Valid @NotNull RegisterWorkspaceForm form) {
//        Number count = (Number) entityManager.createQuery("select count(*) from Workspace").getSingleResult();
//
//        // if there is a workspace registered, so it cannot be initialized again by registering the workspace
//        if (count.intValue() > 0) {
//            throw new InitializationException("Cannot initialize. Workspace was already registered");
//        }

        template = JsonParser.parseResource("/templates/json/new-workspace-template.json", NewWorkspaceTemplate.class);

        Workspace ws = createWorkspace(form);

        createAdminUser(form);

        entityManager.flush();

        return ws.getId();
    }


    /**
     * Load the object templates to create the workspace
     * @return template objects
     */
//    private NewWorkspaceTemplate loadTemplate() {
//        ClassPathResource res = new ClassPathResource("/templates/json/new-workspace-template.json");
//        try {
//            InputStream in = res.getInputStream();
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(in, NewWorkspaceTemplate.class);
//        }
//        catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


    /**
     * Create the account of the administrator user called admin using the given password
     * @return The administrator user object
     */
    private UserWorkspace createAdminUser(RegisterWorkspaceForm form) {
        UserWorkspaceTemplate templ = template.getUser();

        User user = templ.getUser();
        user.setId(Generators.timeBasedGenerator().generate());
        user.setPassword(UserUtils.hashPassword(form.getAdminPassword()));
        user.setEmail(form.getAdminEmail());
        user.setRegistrationDate(new Date());

        entityManager.persist(user);

        // get TB unit
        Tbunit unit = (Tbunit) entityManager.createQuery("from Tbunit where name = :name and workspace.id=:id")
                .setParameter("name", templ.getUnitName())
                .setParameter("id", template.getWorkspace().getId())
                .getSingleResult();

        UserWorkspace uw = new UserWorkspace();
        uw.setId(Generators.timeBasedGenerator().generate());
        uw.setUser(user);
        uw.setUnit(unit);
        uw.setWorkspace(template.getWorkspace());
        uw.setView(templ.getUserView());

        // get administrative unit
        if (templ.getAdminUnitName() != null) {
            AdministrativeUnit au = (AdministrativeUnit) entityManager
                    .createQuery("from AdministrativeUnit where name = :name and workspace.id=:id")
                    .setParameter("name", templ.getAdminUnitName())
                    .setParameter("id", template.getWorkspace().getId())
                    .getSingleResult();
            uw.setAdminUnit(au);
        }

        // get user profiles
        if (templ.getProfiles() != null) {
            for (String pname: templ.getProfiles()) {
                UserProfile profile = (UserProfile) entityManager
                        .createQuery("from UserProfile where name = :name and workspace.id=:id")
                        .setParameter("name", pname)
                        .setParameter("id", template.getWorkspace().getId())
                        .getSingleResult();
                uw.getProfiles().add(profile);
            }
        }

        // save
        entityManager.persist(user);
        entityManager.persist(uw);
        entityManager.flush();

        return uw;
    }


    /**
     * Create the workspace based on the form data and the template data
     * @param form fields to set in the new workspace
     * @return the instance of the workspace
     */
    private Workspace createWorkspace(RegisterWorkspaceForm form) {
        Workspace ws = template.getWorkspace();

        UUID id = Generators.timeBasedGenerator().generate();
        ws.setId(id);
        ws.setName(form.getWorkspaceName());
        ws.setDescription(form.getWorkspaceDescription());

        entityManager.persist(ws);

        createAdminUnits();
        createUnits();
        createProfiles();

        return ws;
    }


    /**
     * Create the administrative units used in the new workspace
     */
    private void createAdminUnits() {
        for (CountryStructure cs: template.getCountryStructures()) {
            cs.setId(Generators.timeBasedGenerator().generate());
            cs.setWorkspace(template.getWorkspace());
            entityManager.persist(cs);
        }

        DecimalFormat format = new DecimalFormat("000");
        int rootCount = 1;

        adminUnits = new ArrayList<>();

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
                AdministrativeUnit p = findAdminUnit(it.getParent());
                if (p == null) {
                    throw new RuntimeException("Error creating administrative unit " + it.getName() + ". Invalid parent " + it.getParent());
                }

                au.setParent(p);
                p.setUnitsCount(p.getUnitsCount() + 1);
                p.getUnits().add(au);
                au.setCode( p.getCode() + format.format(p.getUnitsCount() + 1));
            }
            else {
                au.setCode( format.format(rootCount));
                rootCount++;
            }

            au.setId(Generators.timeBasedGenerator().generate());
            au.setWorkspace(template.getWorkspace());

            entityManager.persist(au);
            adminUnits.add(au);
        }
    }


    /**
     * Search for the administrative unit by its name
     * @param name the name of the administrative unit
     * @return Administrative unit object that matches the given name, or null
     */
    private AdministrativeUnit findAdminUnit(String name) {
        for (AdministrativeUnit item: adminUnits) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }


    /**
     * Create the units from a template file
     */
    private void createUnits() {
        for (TbunitTemplate t: template.getTbunits()) {
            Tbunit unit = mapper.map(t, Tbunit.class);
            unit.getAddress().setAdminUnit(findAdminUnit(t.getAdminUnitName()));

            if (unit.getAddress().getAdminUnit() == null) {
                throw new RuntimeException("Administrative unit " + t.getAdminUnitName() + " not found for unit " + t.getName());
            }

            unit.setId(Generators.timeBasedGenerator().generate());
            unit.setWorkspace(template.getWorkspace());

            entityManager.persist(unit);
            entityManager.flush();
        }
    }

    /**
     * Create the user profiles
     */
    private void createProfiles() {
        // load all roles
        roles = entityManager.createQuery("from UserRole").getResultList();

        for (UserProfileTemplate t: template.getProfiles()) {
            UserProfile u = new UserProfile();
            u.setName(t.getName());
            u.setWorkspace(template.getWorkspace());
            u.setId(Generators.timeBasedGenerator().generate());

            if (t.isAllRoles()) {
                // include all roles
                for (UserRole role: roles) {
                    String roleName = role.getName();
                    if (role.isChangeable()) {
                        roleName = "+" + roleName;
                    }
                    createPermission(u, roleName);
                }
            }
            else {
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
     * @param roleName
     */
    private void createPermission(UserProfile profile, String roleName) {
        // create new permission
        UserPermission perm = new UserPermission();

        boolean canChange = roleName.startsWith("+");
        if (canChange) {
            roleName = roleName.substring(1);
        }

        // search for role
        for (UserRole r: roles) {
            if (r.getName().equals(roleName)) {
                perm.setUserRole(r);
                break;
            }
        }

        // user role not found ?
        if (perm.getUserRole() == null) {
            throw new RuntimeException("User role not defined: " + roleName + " profile: " + profile.getName());
        }

        if (perm.getUserRole().isChangeable()) {
            perm.setCanChange(canChange);
        }
        perm.setCanExecute(true);
        perm.setUserProfile(profile);
        perm.setId(Generators.timeBasedGenerator().generate());

        profile.getPermissions().add(perm);
    }
}
