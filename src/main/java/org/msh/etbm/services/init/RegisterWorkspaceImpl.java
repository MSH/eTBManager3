package org.msh.etbm.services.init;

import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.mail.MailService;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.UserLog;
import org.msh.etbm.services.admin.sysconfig.SysConfigFormData;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.admin.workspaces.WorkspaceCreator;
import org.msh.etbm.services.admin.workspaces.WorkspaceData;
import org.msh.etbm.services.init.demodata.DemonstrationDataCreator;
import org.msh.etbm.services.security.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Register a new workspace. Only valid during initialization of the system, and no other workspace should be available
 * Created by rmemoria on 1/9/15.
 */
@Service
public class RegisterWorkspaceImpl implements RegisterWorkspaceService {

    public static final String ADMIN_NAME = "Administrator";
    public static final String ADMIN_LOGIN = "ADMIN";

    @Autowired
    SysConfigService sysConfigService;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MailService mailService;

    @Autowired
    WorkspaceCreator workspaceCreator;

    @Autowired
    DemonstrationDataCreator demonstrationDataCreator;

    /**
     * Register a new workspace during the initialization process
     *
     * @param form contains information about the workspace to be registered
     */
    @Transactional
    @CommandLog(type = CommandTypes.INIT_REGWORKSPACE, handler = RegisterWorkspaceLog.class)
    @Override
    public UUID run(@Valid @NotNull RegisterWorkspaceRequest form) {
        User user = createAdminUser(form);

        WorkspaceData ws = workspaceCreator.create(form.getWorkspaceName(), user.getId());

        updateConfiguration(form);

        if (form.isDemoData()) {
            demonstrationDataCreator.create(ws.getId());
        }

        return ws.getId();
    }


    /**
     * Create the account of the administrator user called admin using the given password
     */
    private User createAdminUser(RegisterWorkspaceRequest form) {
        User user = new User();
        user.setLogin(ADMIN_LOGIN);
        user.setName(ADMIN_NAME);
        user.setSendSystemMessages(true);
        user.setActive(true);
        user.setUlaAccepted(false);
        user.setPassword(UserUtils.hashPassword(form.getAdminPassword()));
        user.setEmail(form.getAdminEmail());
        user.setRegistrationDate(new Date());

        entityManager.persist(user);
        entityManager.flush();

        // create user log
        UserLog ulog = new UserLog();
        ulog.setId(user.getId());
        ulog.setName(user.getName());
        entityManager.persist(ulog);
        entityManager.flush();

        return user;
    }


    protected void updateConfiguration(RegisterWorkspaceRequest form) {
        SysConfigFormData cfg = sysConfigService.loadConfig();
        cfg.setAdminMail(Optional.of(form.getAdminEmail()));

        sysConfigService.updateConfig(cfg);
    }
}
