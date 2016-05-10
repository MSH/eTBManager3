package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.dao.EntityDAO;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.UserWorkspace;
import org.msh.etbm.db.enums.UserState;
import org.msh.etbm.services.admin.usersws.data.UserWsData;
import org.msh.etbm.services.admin.usersws.data.UserWsDetailedData;
import org.msh.etbm.services.admin.usersws.data.UserWsItemData;
import org.msh.etbm.services.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Date;
import java.util.UUID;

/**
 * Implementation of the {@link UserWsService} to handle CRUD operations for User workspace
 *
 * Created by rmemoria on 26/1/16.
 */
@Service
public class UserWsServiceImpl extends EntityServiceImpl<UserWorkspace, UserWsQueryParams> implements UserWsService {

    @Autowired
    UserRequestService userRequestService;

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
        if (uw.getAdminUnit() == null && uw.getUnit() != null) {
            uw.setAdminUnit(uw.getUnit().getAddress().getAdminUnit());
        }
    }

    @Override
    protected void beforeSave(UserWorkspace userWorkspace, Errors errors) {
        initNewUser(userWorkspace.getUser());
    }

    /**
     * Initialize the variables for a new user
     * @param user the new user to be registered
     */
    protected void initNewUser(User user) {
        // is a new user ?
        if (user.getId() != null) {
            return;
        }

        user.setState(UserState.VALIDATE_EMAIL);

        // generate new UUID token to change password
        UUID id = UUID.randomUUID();
        user.setPwdChangeRequest(id.toString());

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
}
