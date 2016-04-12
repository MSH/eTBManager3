package org.msh.etbm.services.init;

import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.mail.MailService;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.UserState;
import org.msh.etbm.db.enums.UserView;
import org.msh.etbm.services.admin.sysconfig.SysConfigFormData;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.admin.workspaces.WorkspaceData;
import org.msh.etbm.services.admin.workspaces.impl.RegisterWorkspaceLog;
import org.msh.etbm.services.admin.workspaces.WorkspaceCreator;
import org.msh.etbm.services.users.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Register a new workspace. Only valid during initialization of the system, and no other workspace should be available
 * Created by rmemoria on 1/9/15.
 */
@Service
public class RegisterWorkspaceImpl implements RegisterWorkspaceService {


    @Autowired
    SysConfigService sysConfigService;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MailService mailService;

    @Autowired
    WorkspaceCreator workspaceCreator;



    /**
     * Register a new workspace during the initialization process
     * @param form contains information about the workspace to be registered
     */
    @Transactional
    @CommandLog(type = "init.regworkspace", handler = RegisterWorkspaceLog.class)
    @Override
    public UUID run(@Valid @NotNull RegisterWorkspaceRequest form) {

        WorkspaceData ws = workspaceCreator.create(form.getWorkspaceName());

        createAdminUser(form, ws);

        updateConfiguration(form);

        sendSuccessMailMessage(ws);

        return ws.getId();
    }


    /**
     * Send an e-mail message to the user to inform him about the new workspace created
     * @param ws the created workspace
     */
    protected void sendSuccessMailMessage(WorkspaceData ws) {
        // TODO Implement e-mail delivery of successful workspace registration
        // Map<String, Object> data = new HashMap<>();
        // data.put("name", "Ricardo");

        // mailService.send("ricardo@rmemoria.com.br", "Hello world", "test.ftl", data);
    }



    /**
     * Create the account of the administrator user called admin using the given password
     */
    private void createAdminUser(RegisterWorkspaceRequest form, WorkspaceData ws) {
        User user = new User();
        user.setLogin("ADMIN");
        user.setName("Administrator");
        user.setSendSystemMessages(true);
        user.setState(UserState.ACTIVE);
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

        workspaceCreator.addUserToWorkspace(user.getId(), ws.getId());
    }


    protected void updateConfiguration(RegisterWorkspaceRequest form) {
        SysConfigFormData cfg = sysConfigService.loadConfig();
        cfg.setAdminMail(Optional.of(form.getAdminEmail()));

        sysConfigService.updateConfig(cfg);
    }
}
