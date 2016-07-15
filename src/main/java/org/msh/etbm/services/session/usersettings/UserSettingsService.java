package org.msh.etbm.services.session.usersettings;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.dao.EntityDAO;
import org.msh.etbm.commons.entities.dao.EntityDAOFactory;
import org.msh.etbm.commons.objutils.Diffs;
import org.msh.etbm.commons.objutils.ObjectDiffGenerator;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Handle the current user settings
 * Created by rmemoria on 14/5/16.
 */
@Service
public class UserSettingsService {

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    DozerBeanMapper mapper;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    EntityDAOFactory entityDAOFactory;


    /**
     * Recover the settings from the current user
     *
     * @return instance of {@link UserSettingsFormData} containing the settings
     */
    @Transactional
    public UserSettingsFormData get() {
        EntityDAO<User> userDAO = getCurrentUser();

        UserSettingsFormData data = userDAO.mapFromEntity(UserSettingsFormData.class);

        return data;
    }


    /**
     * Update the settings of the current user
     *
     * @param data instance of {@link UserSettingsFormData} containing the settings
     * @return The list of changed fields
     */
    @Transactional
    @CommandLog(handler = UserSettingsLogHandler.class, type = CommandTypes.SESSION_USER_SETTINGS)
    public Diffs update(UserSettingsFormData data) {
        EntityDAO<User> userDAO = getCurrentUser();

        // store the initial state of the object
        ObjectDiffGenerator diffs = new ObjectDiffGenerator(userDAO.getEntity());

        userDAO.mapToEntity(data);

        if (!userDAO.validate()) {
            userDAO.raiseValidationError();
        }

        userDAO.save();

        // generate the list of differences
        return diffs.generate();
    }


    /**
     * Return the current user inside an EntityDAO instance
     *
     * @return instance of {@link EntityDAO} object
     */
    protected EntityDAO<User> getCurrentUser() {
        EntityDAO<User> userDAO = entityDAOFactory.newDAO(User.class);

        userDAO.setId(userRequestService.getUserSession().getUserId());

        return userDAO;
    }

}
