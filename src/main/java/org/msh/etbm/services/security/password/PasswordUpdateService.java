package org.msh.etbm.services.security.password;

import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.services.pub.PwdResetTokenResponse;
import org.msh.etbm.services.security.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 16/6/16.
 */
@Service
public class PasswordUpdateService {

    @Autowired
    EntityManager entityManager;

    /**
     * Change the user password using the password request token sent by e-mail to him
     *
     * @param req The client request containing password and the request token
     */
    @Transactional
    public void updatePassword(PasswordUpdateRequest req) {
        if (!UserUtils.isValidPassword(req.getPassword())) {
            throw new EntityValidationException(req, "password", null, "changepwd.invalidpassword");
        }

        List<User> lst = entityManager.createQuery("from User where passwordResetToken = :req")
                .setParameter("req", req.getToken())
                .getResultList();

        if (lst.size() == 0) {
            throw new EntityValidationException(req, "token", null, "changepwd.invalidtoken");
        }

        User user = lst.get(0);

        if (!user.isActive() && user.isEmailConfirmed()) {
            throw new EntityValidationException(req, "token", null, "changepwd.invalidtoken");
        }

        String hashPwd = UserUtils.hashPassword(req.getPassword());
        user.setPassword(hashPwd);
        user.setPasswordResetToken(null);
        user.setEmailConfirmed(true);
        user.setActive(true);

        entityManager.persist(user);
        entityManager.flush();
    }

    /**
     * Return information about the user from the password request token provided when user
     * requested a password reset. This method is just to check if token is valid
     *
     * @param token the password reset token
     * @return instance of {@link PwdResetTokenResponse} or null if token is not valid
     */
    public PwdResetTokenResponse getUserInfoByPasswordResetToken(String token) {
        List<User> lst = entityManager.createQuery("from User where passwordResetToken = :tk")
                .setParameter("tk", token)
                .getResultList();

        if (lst.size() == 0) {
            return null;
        }

        User user = lst.get(0);

        PwdResetTokenResponse resp = new PwdResetTokenResponse();
        resp.setName(user.getName());
        resp.setEmail(user.getEmail());
        resp.setLogin(user.getLogin());

        return resp;
    }

    /**
     * Change the user password using the user UUID
     * @param userId The UUID of the user that will have its password updated
     * @param newPassword The new password
     */
    @Transactional
    public void updatePassword(UUID userId, String newPassword) {
        User user = entityManager.find(User.class, userId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!UserUtils.isValidPassword(newPassword)) {
            throw new EntityValidationException(newPassword, "newPassword", null, "changepwd.invalidpassword");
        }

        String hashNewPwd = UserUtils.hashPassword(newPassword);
        user.setPassword(hashNewPwd);

        entityManager.persist(user);
        entityManager.flush();
    }
}
