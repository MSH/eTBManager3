package org.msh.etbm.services.security.authentication;

import com.fasterxml.uuid.Generators;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.services.security.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
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

    /**
     * Start the process to change a password based on the user e-mail address
     * @param email the user e-mail address
     * @return the ID to be informed to the user in order to change its password
     */
    @Transactional
    public String initPasswordChange(String email) {
        List<User> lst = entityManager.createQuery("from User where email = :email")
                .setParameter("email", email)
                .getResultList();

        if (lst.size() == 0) {
            return null;
        }

        User user = lst.get(0);
        // if there is already a password change request, return it
        if (user.getPwdChangeRequest() != null) {
            return user.getPwdChangeRequest();
        }

        // create a new password change request ID
        UUID id = Generators.timeBasedGenerator().generate();
        String val = id.toString().replace("-", "");

        user.setPwdChangeRequest(val);
        entityManager.persist(user);
        entityManager.flush();

        // TODO Send e-mail to the user on password change request

        return val;
    }


    /**
     * Change the user password by the
     * @param reqId the password change request ID sent initially to the user by e-mail
     * @param newpwd the new password
     */
    @Transactional
    public void changePassword(String reqId, String newpwd) {
        if (!UserUtils.isValidPassword(newpwd)) {
            throw new IllegalArgumentException("Invalid password");
        }

        List<User> lst = entityManager.createQuery("from User where pwdChangeRequest = :req")
                .setParameter("req", reqId)
                .getResultList();

        if (lst.size() == 0) {
            throw new IllegalArgumentException("Invalid password change request");
        }

        User user = lst.get(0);

        String hashPwd = UserUtils.hashPassword(newpwd);
        user.setPwdChangeRequest(hashPwd);

        entityManager.persist(user);
        entityManager.flush();
    }

}
