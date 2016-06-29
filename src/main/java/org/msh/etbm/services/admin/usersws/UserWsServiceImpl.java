package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.dao.EntityDAO;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.mail.MailService;
import org.msh.etbm.commons.objutils.Diffs;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.UserWorkspace;
import org.msh.etbm.services.admin.usersws.data.UserWsChangePwdFormData;
import org.msh.etbm.services.admin.usersws.data.UserWsData;
import org.msh.etbm.services.admin.usersws.data.UserWsDetailedData;
import org.msh.etbm.services.admin.usersws.data.UserWsItemData;
import org.msh.etbm.services.security.UserUtils;
import org.msh.etbm.services.security.password.PasswordLogHandler;
import org.msh.etbm.services.security.password.PasswordUpdateService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.services.session.usersettings.UserSettingsFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    protected void beforeValidate(UserWorkspace uw, Object request) {
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
    protected void beforeSave(UserWorkspace userWorkspace, Errors errors) {
        if (!checkUnique(User.class, userWorkspace.getUser(), "login", null)) {
            errors.rejectValue("login", Messages.NOT_UNIQUE);
        }

        if (!checkUnique(User.class, userWorkspace.getUser(), "email", null)) {
            errors.rejectValue("email", Messages.NOT_UNIQUE);
        }

        initNewUser(userWorkspace.getUser());
    }

    @Override
    protected void afterSave(UserWorkspace entity, ServiceResult res, boolean isNew) {
        if (isNew) {
            sendMessageToNewUser(entity);
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

    /**
     * Update the settings of the current user
     * @param data instance of {@link UserSettingsFormData} containing the settings
     * @return The list of changed fields
     */
    @Transactional
    @CommandLog(handler = PasswordLogHandler.class, type = "userWsChangePassword")
    public Diffs changePassword(UserWsChangePwdFormData data) {
        UserWorkspace userws = getEntityManager().find(UserWorkspace.class, data.getUserWsId());

        if (userws == null) {
            throw new RuntimeException("UserWs not found");
        }

        passwordUpdateService.updatePassword(userws.getUser().getId(), data.getNewPassword());

        Diffs diff = new Diffs();
        diff.put("userPwdChanged", null, userws.getUser().getId());

        return diff;
    }
}
