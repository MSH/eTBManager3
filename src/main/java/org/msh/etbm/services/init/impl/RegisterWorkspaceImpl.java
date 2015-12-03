package org.msh.etbm.services.init.impl;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.db.dto.SystemConfigDTO;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.repositories.*;
import org.msh.etbm.services.init.InitializationException;
import org.msh.etbm.services.init.RegisterWorkspaceRequest;
import org.msh.etbm.services.init.RegisterWorkspaceService;
import org.msh.etbm.services.sys.ConfigurationService;
import org.msh.etbm.services.users.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RegisterWorkspaceImpl implements RegisterWorkspaceService {

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    AdminUnitRepository adminUnitRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UserWorkspaceRepository userWorkspaceRepository;

    @Autowired
    CountryStructureRepository countryStructureRepository;

    @Autowired
    UserRoleRepositories userRoleRepositories;

    @PersistenceContext
    EntityManager entityManager;


    private NewWorkspaceTemplate template;
    private List<AdministrativeUnit> adminUnits;
    private List<UserRole> roles;

    /**
     * Register a new workspace during the initialization process
     * @param form contains information about the workspace to be registered
     */
    @Transactional
    @CommandLog(type = "init.regworkspace", handler = RegisterWorkspaceLog.class)
    public UUID run(@Valid @NotNull RegisterWorkspaceRequest form) {
        // check if workspace is alread initialized
        Long count = workspaceRepository.count();

        // if there is a workspace registered, so it cannot be initialized again by registering the workspace
        if (count.intValue() > 0) {
            throw new InitializationException("Cannot initialize. Workspace was already registered");
        }

        // read the template data
        template = JsonParser.parseResource("/templates/json/new-workspace-template.json", NewWorkspaceTemplate.class);

        Workspace ws = createWorkspace(form);

        createAdminUser(form);

        updateConfiguration(form);

        return ws.getId();
    }



    /**
     * Create the account of the administrator user called admin using the given password
     * @return The administrator user object
     */
    private UserWorkspace createAdminUser(RegisterWorkspaceRequest form) {
        UserWorkspaceTemplate templ = template.getUser();

        User user = templ.getUser();
        user.setPassword(UserUtils.hashPassword(form.getAdminPassword()));
        user.setEmail(form.getAdminEmail());
        user.setRegistrationDate(new Date());

        userRepository.save(user);

        // create user log
        UserLog ulog = new UserLog();
        ulog.setId(user.getId());
        ulog.setName(user.getName());
        entityManager.persist(ulog);
        entityManager.flush();

        // get TB unit
        List<Unit> units = unitRepository.findByNameAndWorkspaceId(templ.getUnitName(), template.getWorkspace().getId());
        if (units.size() == 0) {
            throw new RuntimeException("No unit found with name " + templ.getUnitName());
        }
        Unit unit = units.get(0);

        UserWorkspace uw = new UserWorkspace();
        uw.setUser(user);
        uw.setUnit(unit);
        uw.setWorkspace(template.getWorkspace());
        uw.setView(templ.getUserView());
        uw.setAdministrator(true);

        // get administrative unit
        if (templ.getAdminUnitName() != null) {
            List<AdministrativeUnit> admunits = adminUnitRepository
                    .findByNameAndWorkspaceId(templ.getAdminUnitName(), template.getWorkspace().getId());
            AdministrativeUnit au = adminUnits.get(0);
            uw.setAdminUnit(au);
        }

        // get user profiles
        if (templ.getProfiles() != null) {
            for (String pname: templ.getProfiles()) {
                List<UserProfile> profs = userProfileRepository.findByNameAndWorkspaceId(pname, template.getWorkspace().getId());
                UserProfile profile = profs.get(0);
                uw.getProfiles().add(profile);
            }
        }

        userWorkspaceRepository.save(uw);

        return uw;
    }


    /**
     * Create the workspace based on the form data and the template data
     * @param form fields to set in the new workspace
     * @return the instance of the workspace
     */
    private Workspace createWorkspace(RegisterWorkspaceRequest form) {
        Workspace ws = template.getWorkspace();

        ws.setName(form.getWorkspaceName());

        entityManager.persist(ws);
//        workspaceRepository.save(ws);

        WorkspaceLog wslog = new WorkspaceLog();
        wslog.setId(ws.getId());
        wslog.setName(ws.getName());
        entityManager.persist(wslog);
        entityManager.flush();

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
            cs.setWorkspace(template.getWorkspace());
            countryStructureRepository.save(cs);
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

            au.setWorkspace(template.getWorkspace());

            adminUnitRepository.save(au);
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

            unit.setWorkspace(template.getWorkspace());

            unitRepository.save(unit);
        }
    }

    /**
     * Create the user profiles
     */
    private void createProfiles() {
        // load all roles
        roles = Lists.newArrayList(userRoleRepositories.findAll());

        for (UserProfileTemplate t: template.getProfiles()) {
            UserProfile u = new UserProfile();
            u.setName(t.getName());
            u.setWorkspace(template.getWorkspace());

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

            userProfileRepository.save(u);
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

        profile.getPermissions().add(perm);
    }


    protected void updateConfiguration(RegisterWorkspaceRequest form) {
        SystemConfigDTO cfg = configurationService.systemConfig();
        cfg.setAdminMail(form.getAdminEmail());

        configurationService.updateConfiguration();
    }
}
