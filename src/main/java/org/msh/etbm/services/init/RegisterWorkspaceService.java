package org.msh.etbm.services.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.CaseValidationOption;
import org.msh.etbm.db.enums.DisplayCaseNumber;
import org.msh.etbm.db.enums.NameComposition;
import org.msh.etbm.db.enums.TreatMonitoringInput;
import org.msh.etbm.services.init.impl.AdminUnitTemplate;
import org.msh.etbm.services.init.impl.NewWorkspaceTemplate;
import org.msh.etbm.services.users.UserUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
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

    private NewWorkspaceTemplate template;
    private List<AdministrativeUnit> adminUnits;
    private Workspace workspace;

    /**
     * Register a new workspace during the initialization process
     * @param form contains information about the workspace to be registered
     */
    @Transactional
    public UUID register(@Valid @NotNull RegisterWorkspaceForm form) {
        Number count = (Number) entityManager.createQuery("select count(*) from Workspace").getSingleResult();

        // if there is a workspace registered, so it cannot be initialized again by registering the workspace
        if (count.intValue() > 0) {
            throw new InitializationException("Cannot initialize. Workspace was already registered");
        }

        template = loadTemplate();

        Workspace ws = createWorkspace(form);

        entityManager.flush();

        return ws.getId();
    }


    /**
     * Load the object templates to create the workspace
     * @return template objects
     */
    private NewWorkspaceTemplate loadTemplate() {
        ClassPathResource res = new ClassPathResource("/templates/json/new-workspace-template.json");
        try {
            InputStream in = res.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, NewWorkspaceTemplate.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Create the account of the administrator user called admin using the given password
     * @return The administrator user object
     */
    private User createAdminUser(RegisterWorkspaceForm form) {
        User user = template.getUser();

        user.setPassword(UserUtils.hashPassword(form.getAdminPassword()));
        user.setEmail(form.getAdminEmail());
        user.setRegistrationDate(new Date());

        entityManager.persist(user);

        return user;
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

    }
}
