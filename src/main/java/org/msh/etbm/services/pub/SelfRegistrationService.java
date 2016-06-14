package org.msh.etbm.services.pub;

import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by rmemoria on 13/6/16.
 */
@Service
public class SelfRegistrationService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    @Transactional
    public void registerUser(SelfRegistrationRequest req) {

    }
}
