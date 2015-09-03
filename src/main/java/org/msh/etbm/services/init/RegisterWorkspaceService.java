package org.msh.etbm.services.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.CaseValidationOption;
import org.msh.etbm.db.enums.DisplayCaseNumber;
import org.msh.etbm.db.enums.NameComposition;
import org.msh.etbm.db.enums.TreatMonitoringInput;
import org.msh.etbm.services.init.impl.NewWorkspaceTemplate;
import org.msh.etbm.services.users.UserUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Register a new workspace. Only valid during initialization of the system, and no other workspace should be available
 * Created by rmemoria on 1/9/15.
 */
@Service
public class RegisterWorkspaceService {

    @PersistenceContext
    EntityManager entityManager;

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

        NewWorkspaceTemplate template = loadTemplate();

        // set the parameters send from the client side
        template.getWorkspace().setName(form.getWorkspaceName());
        template.getWorkspace().setDescription(form.getWorkspaceDescription());
        template.getUser().setEmail(form.getAdminEmail());

        Workspace ws = createWorkspace(form, template);

        return new UUID(12345, 67890);
//        User user = createAdminUser(form.getAdminPassword(), form.getAdminEmail());
//        Workspace ws = createWorkspace(form.getWorkspaceName(), form.getWorkspaceDescription());
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
     * @param pwd the administrator password
     * @return The administrator user object
     */
    private User createAdminUser(String pwd, String email) {
        User user = new User();

        user.setName("Administrator");
        user.setComments("The administrator of the system");
        user.setLogin("admin");
        user.setPassword(UserUtils.hashPassword(pwd));
        user.setEmail(email);
        user.setRegistrationDate(new Date());
        user.setSendSystemMessages(true);
        user.setUlaAccepted(false);

        entityManager.persist(user);

        return user;
    }

    /**
     * Create the workspace based on the form data and the template data
     * @param form fields to set in the new workspace
     * @param template Template containing default workspace data
     * @return the instance of the workspace
     */
    private Workspace createWorkspace(RegisterWorkspaceForm form, NewWorkspaceTemplate template) {
        Workspace ws = template.getWorkspace();

        List<UserRole> roles =  entityManager.createQuery("from UserRole").getResultList();
        for (UserRole role: roles) {
            System.out.println(role.getName());
        }

        ws.setName(form.getWorkspaceName());
        ws.setDescription(form.getWorkspaceDescription());

        entityManager.persist(ws);

        createAdminUnits(template);

        return ws;
    }

    /**
     * Create the administrative units used in the new workspace
     * @param template
     */
    private void createAdminUnits(NewWorkspaceTemplate template) {
        for (CountryStructure cs: template.getCountryStructures()) {
            entityManager.persist(cs);
        }

        for (AdministrativeUnit au: template.getRegions()) {
            entityManager.persist(au);
        }
    }
}
