package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.dao.EntityDAO;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.mail.MailService;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.UserWorkspace;
import org.msh.etbm.services.admin.usersws.data.UserWsChangePwdFormData;
import org.msh.etbm.services.admin.usersws.data.UserWsData;
import org.msh.etbm.services.admin.usersws.data.UserWsDetailedData;
import org.msh.etbm.services.admin.usersws.data.UserWsItemData;
import org.msh.etbm.services.pub.ForgotPwdService;
import org.msh.etbm.services.security.UserUtils;
import org.msh.etbm.services.security.password.ChangePasswordResponse;
import org.msh.etbm.services.security.password.PasswordLogHandler;
import org.msh.etbm.services.security.password.PasswordUpdateService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the {@link UserWsService} to handle CRUD operations for User workspace
 * <p>
 * Created by rmemoria on 26/1/16.
 */
@Service
public class UserWsServiceImpl extends EntityServiceImpl<UserWorkspace, UserWsQueryParams> implements UserWsService {

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    MailService mailService;

    @Autowired
    Messages messages;

    @Autowired
    PasswordUpdateService passwordUpdateService;

    @Autowired
    ForgotPwdService forgotPwdService;

    @Override
    protected void buildQuery(QueryBuilder<UserWorkspace> builder, UserWsQueryParams queryParams) {
        builder.setEntityAlias("a");

        // add profiles
        builder.addProfile(UserWsQueryParams.PROFILE_ITEM, UserWsItemData.class);
        builder.addDefaultProfile(UserWsQueryParams.PROFILE_DEFAULT, UserWsData.class);
        builder.addProfile(UserWsQueryParams.PROFILE_DETAILED, UserWsDetailedData.class);

        // add order by
        builder.addDefaultOrderByMap(UserWsQueryParams.ORDERBY_NAME, "u.name");
        builder.addOrderByMap(UserWsQueryParams.ORDERBY_UNIT, "un.name");
        builder.addOrderByMap(UserWsQueryParams.ORDERBY_ADMINUNIT, "adminUnit.name");

        builder.setHqlJoin("join fetch a.user u join fetch a.unit un");
    }

    @Override
    public String getCommandType() {
        return CommandTypes.ADMIN_USERS;
    }

    @Override
    protected void beforeValidate(EntityServiceContext<UserWorkspace> context) {
        UserWorkspace uw = context.getEntity();

        // set login to lower case
        User user = uw.getUser();
        if (user != null && user.getLogin() != null) {
            uw.getUser().setLogin(uw.getUser().getLogin().toLowerCase());
        }

        if (uw.getAdminUnit() == null && uw.getUnit() != null) {
            uw.setAdminUnit(uw.getUnit().getAddress().getAdminUnit());
        }
    }

    @Override
    protected void beforeSave(EntityServiceContext<UserWorkspace> context, Errors errors) {
        UserWorkspace userWorkspace = context.getEntity();

        if (!checkUnique(User.class, userWorkspace.getUser(), "login", null)) {
            errors.rejectValue("login", Messages.NOT_UNIQUE);
        }

        if (!checkUnique(User.class, userWorkspace.getUser(), "email", null)) {
            errors.rejectValue("email", Messages.NOT_UNIQUE);
        }

        initNewUser(userWorkspace.getUser());
    }

    @Override
    protected void afterSave(EntityServiceContext<UserWorkspace> context, ServiceResult res) {
        // is a new entity ?
        if (context.getRequestedId() == null) {
            sendMessageToNewUser(context.getEntity());
        }
    }


    /**
     * Send an e-mail message to the user informing about its registration and link to change the password
     *
     * @param uw
     */
    protected void sendMessageToNewUser(UserWorkspace uw) {
        User user = uw.getUser();
        // send a message informing
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("name", user.getName());
        mailService.send(uw.getUser().getEmail(), messages.get("mail.newuser"), "new-user.ftl", model);

    }

    /**
     * Initialize the variables for a new user
     *
     * @param user the new user to be registered
     */
    protected void initNewUser(User user) {
        // is a new user ?
        if (user.getId() != null) {
            return;
        }

        user.setEmailConfirmed(false);

        // generate new UUID token to change password
        String token = UserUtils.generatePasswordToken();
        user.setPasswordResetToken(token);

        // set the date and time user was registered
        user.setRegistrationDate(new Date());

        // get the current user
        User puser = getEntityManager().find(User.class, userRequestService.getUserSession().getUserId());

        user.setParentUser(puser);

        // save the user
        EntityDAO<User> dao = createEntityDAO(User.class);
        dao.setEntity(user);

        // validate before save the entity
        dao.save();

        System.out.println(dao.getEntity().getId());
    }

    @Transactional
    @CommandLog(handler = PasswordLogHandler.class, type = "admin.users.changepwd")
    public ChangePasswordResponse changePassword(UserWsChangePwdFormData data) {
        UserWorkspace userws = getEntityManager().find(UserWorkspace.class, data.getUserWsId());

        if (userws == null) {
            throw new RuntimeException("UserWs not found");
        }

        // Update the user password on database
        passwordUpdateService.updatePassword(userws.getUser().getId(), data.getNewPassword());

        // Send email notifying the user
        Map<String, Object> model = new HashMap<>();
        model.put("adminUserName", userRequestService.getUserSession().getUserName());
        model.put("name", userws.getUser().getName());
        String subject = messages.get("mail.pwdchanged.subject");
        mailService.send(userws.getUser().getEmail(), subject, "userwschangepwd.ftl", model);

        // create data for command log
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("userModifiedName", userws.getUser().getName());
        ret.put("userModifiedId", userws.getUser().getId());
        ret.put("detail", "The user changed the password of another user throw administrative module.");

        return new ChangePasswordResponse(userws.getUser().getId(), userws.getUser().getName(), "The user changed the password of another user throw administrative module.");
    }

    @Transactional
    @CommandLog(handler = PasswordLogHandler.class, type = "admin.users.changepwd")
    public ChangePasswordResponse sendPwdResetLink(UserWsChangePwdFormData data) {
        UserWorkspace userws = getEntityManager().find(UserWorkspace.class, data.getUserWsId());
        forgotPwdService.requestPasswordReset(userws.getLogin());

        return new ChangePasswordResponse(userws.getUser().getId(), userws.getUser().getName(), "The user sent a reset password e-mail to another user throw administrative module.");
    }
}
