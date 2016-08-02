package org.msh.etbm.services.pub;

import org.msh.etbm.commons.Messages;
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

/**
 * Service to reset user's password
 * <p>
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
     *
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
        String val = UserUtils.generatePasswordToken();

        user.setPasswordResetToken(val);
        entityManager.persist(user);
        entityManager.flush();

        sendPwdRequestEmail(user);

        return val;
    }


    private void sendPwdRequestEmail(User user) {
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("name", user.getName());

        String subject = messages.get("mail.forgotpwd");

        mailService.send(user.getEmail(), subject, "forgotpwd.ftl", model);
    }


}
