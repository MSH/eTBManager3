package org.msh.etbm.services.pub;

import com.fasterxml.uuid.Generators;
import org.msh.etbm.Messages;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.mail.MailService;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.services.security.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service to reset user's password
 *
 * Created by rmemoria on 29/9/15.
 */
@Service
public class ForgotPwdService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MailService mailService;

    @Autowired
    Messages messages;

    /**
     * Start the process to change a password based on the user e-mail address
     * @param userid the user e-mail address or login
     * @return the ID to be informed to the user in order to change its password
     */
    @Transactional
    public String requestPasswordReset(String userid) {
        List<User> lst = entityManager.createQuery("from User where email = :userid or login = :userid")
                .setParameter("userid", userid)
                .getResultList();

        if (lst.size() == 0) {
            return null;
        }

        User user = lst.get(0);
        // if there is already a password change request, return it
        if (user.getPasswordResetToken() != null) {
            sendPwdRequestEmail(user);
            return user.getPasswordResetToken();
        }

        // create a new password change request ID
        UUID id = Generators.timeBasedGenerator().generate();
        String val = id.toString().replace("-", "");

        user.setPasswordResetToken(val);
        entityManager.persist(user);
        entityManager.flush();

        sendPwdRequestEmail(user);

        return val;
    }


    /**
     * Return information about the user from the password request token provided when user
     * requested a password reset. This method is just to check if token is valid
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

        return resp;
    }


    private void sendPwdRequestEmail(User user) {
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("name", user.getName());

        String subject = messages.get("mail.forgotpwd");

        mailService.send(user.getEmail(), subject, "forgotpwd.ftl", model);
    }


    /**
     * Change the user password using the password request token sent by e-mail to him
     * @param req The client request containing password and the request token
     */
    @Transactional
    public void updatePassword(PwdUpdateRequest req) {
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

        String hashPwd = UserUtils.hashPassword(req.getPassword());
        user.setPassword(hashPwd);
        user.setPasswordResetToken(null);

        entityManager.persist(user);
        entityManager.flush();
    }

}
