package org.msh.etbm.services.session.changepassword;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.dao.EntityDAOFactory;
import org.msh.etbm.commons.objutils.Diffs;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.services.security.UserUtils;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.services.session.usersettings.UserSettingsFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Change users password
 * Created by msantos on 27/06/16.
 */
@Service
public class ChangePasswordService {

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    DozerBeanMapper mapper;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    EntityDAOFactory entityDAOFactory;

    /**
     * Update the settings of the current user
     *
     * @param data instance of {@link UserSettingsFormData} containing the settings
     * @return The list of changed fields
     */
    @Transactional
    @CommandLog(handler = ChangePasswordLogHandler.class, type = "changePassword")
    public Diffs changePassword(ChangePasswordFormData data) {
        List<User> lst = entityManager.createQuery("from User where id = :id")
                .setParameter("id", userRequestService.getUserSession().getUserId())
                .getResultList();

        User user = lst.get(0);
        String hashPwd = UserUtils.hashPassword(data.getPassword());

        if (!user.getPassword().equals(hashPwd)) {
            throw new EntityValidationException(data.getPassword(), "password", "changepwd.wrongpass", "changepwd.wrongpassword");
        }

        if (!UserUtils.isValidPassword(data.getNewPassword())) {
            throw new EntityValidationException(data.getNewPassword(), "newPassword", "changepwd.wrongpass", "changepwd.invalidpassword");
        }

        String hashNewPwd = UserUtils.hashPassword(data.getNewPassword());
        user.setPassword(hashNewPwd);

        entityManager.persist(user);
        entityManager.flush();

        return null;
    }
}
