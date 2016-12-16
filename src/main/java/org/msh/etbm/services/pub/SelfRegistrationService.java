package org.msh.etbm.services.pub;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.mail.MailService;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.UserView;
import org.msh.etbm.services.admin.sysconfig.SysConfigData;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.security.ForbiddenException;
import org.msh.etbm.services.security.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Service to register a new user in the visitor page, requested from the public pages
 * Created by rmemoria on 13/6/16.
 */
@Service
public class SelfRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelfRegistrationService.class);

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    Messages messages;

    @Autowired
    MailService mailService;

    /**
     * Register a new user and send an e-mail with instructions to enter a password
     *
     * @param req the request with information about the user
     */
    @Transactional
    public void register(SelfRegistrationRequest req) {
        // check if registration is allowed (configuration is ok for that)
        SysConfigData cfg = sysConfigService.loadConfig();

        if (!cfg.isAllowRegPage() ||
                cfg.getWorkspace() == null ||
                cfg.getUnit() == null ||
                cfg.getUserProfile() == null) {
            raiseForbiddenException();
        }

        // check if login and e-mail are unique
        checkUnique(req);

        // register the user
        UserWorkspace uw = registerUser(req, cfg);

        sendEmailMessage(uw);
    }

    /**
     * Register the user and include him/her in a workspace
     *
     * @param req client request
     * @param cfg system configuration
     * @return object with information about the new user registered
     */
    protected UserWorkspace registerUser(SelfRegistrationRequest req, SysConfigData cfg) {
        // register a new user
        User user = new User();
        user.setName(req.getName());
        user.setEmailConfirmed(false);
        user.setRegistrationDate(new Date());
        user.setComments(req.getOrganization());
        user.setEmail(req.getEmail());
        user.setLogin(req.getLogin());
        user.setUlaAccepted(false);
        String token = UserUtils.generatePasswordToken();
        user.setPasswordResetToken(token);

        entityManager.persist(user);
        entityManager.flush();

        // recover workspace
        UUID wsid = cfg.getWorkspace().getId();
        Workspace workspace = entityManager.find(Workspace.class, wsid);

        // recover unit
        UUID unitId = cfg.getUnit().getId();
        Unit unit = entityManager.find(Unit.class, unitId);

        // recover user profile
        UUID profId = cfg.getUserProfile().getId();
        UserProfile profile = entityManager.find(UserProfile.class, profId);

        // check if entities exist (cannot register if any of them is null)
        if (wsid == null || unitId == null || profId == null) {
            raiseForbiddenException();
        }

        // include user in the workspace
        UserWorkspace uw = new UserWorkspace();
        uw.setWorkspace(workspace);
        uw.setAdminUnit(unit.getAddress().getAdminUnit());
        uw.setUnit(unit);
        uw.getProfiles().add(profile);
        uw.setPlayOtherUnits(true);
        uw.setAdministrator(false);
        uw.setUser(user);
        uw.setView(UserView.COUNTRY);

        entityManager.persist(uw);
        entityManager.flush();

        return uw;
    }

    /**
     * Raise exception if service is not allowed to execute
     */
    private void raiseForbiddenException() {
        throw new ForbiddenException("User registration is forbidden");
    }

    protected void sendEmailMessage(UserWorkspace uw) {
        User user = uw.getUser();

        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("name", user.getName());

        String subject = messages.get("userreg.mail.subject");

        mailService.send(user.getEmail(), subject, "self-registration.ftl", model);
    }

    /**
     * Check if there is any user already registered with the e-mail or login informed
     *
     * @param req The request to register a new user
     */
    protected void checkUnique(SelfRegistrationRequest req) {
        List<User> lst = entityManager
                .createQuery("from User where email = :email or login = :login")
                .setParameter("login", req.getLogin().trim().toUpperCase())
                .setParameter("email", req.getEmail().trim())
                .getResultList();

        if (lst.size() == 0) {
            return;
        }

        User user = lst.get(0);

        // check if e-mail is already registered
        if (req.getEmail().equals(user.getEmail())) {
            throw new EntityValidationException(req, "email", null, Messages.NOT_UNIQUE_USER);
        }

        if (req.getLogin().compareToIgnoreCase(user.getLogin()) == 0) {
            throw new EntityValidationException(req, "login", null, Messages.NOT_UNIQUE_USER);
        }
    }
}
