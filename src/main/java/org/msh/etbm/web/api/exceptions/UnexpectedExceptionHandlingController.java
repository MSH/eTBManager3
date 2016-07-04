package org.msh.etbm.web.api.exceptions;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.mail.MailService;
import org.msh.etbm.db.entities.ErrorLog;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Exception handler to send an e-mail to administrators
 * <p>
 * Created by msantos on 4/7/2016
 */
@ControllerAdvice
public class UnexpectedExceptionHandlingController {

    @Autowired
    Messages messages;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    MailService mailService;

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void methodValidationError(Exception e, HttpServletRequest req) {
        //Save error on database
        ErrorLog error = new ErrorLog();

        error.setErrorDate(new Date());
        error.setExceptionClass(e.getClass().getCanonicalName());
        error.setExceptionMessage(e.getMessage());
        error.setUrl(req.getRequestURL().toString());
        error.setUserName(userRequestService.getUserSession().getUserName());
        error.setUserId(userRequestService.getUserSession().getUserId());
        error.setStackTrace(e.getStackTrace().toString());
        error.setWorkspace(userRequestService.getUserSession().getWorkspaceName());

        entityManager.persist(error);
        entityManager.flush();
        entityManager.merge(error);

        System.out.println(error.getId());
        System.out.println("foi");

        /*Send e-mail to administrator
        Map<String, Object> model = new HashMap<>();
        model.put("errorDate", DateUtils.FormatDateTime("yyyy.MM.dd G 'at' HH:mm:ss z", error.getErrorDate()));
        model.put("exceptionClass", error.getExceptionClass());
        model.put("exceptionMessage", error.getExceptionMessage());
        model.put("url", error.getUrl());
        model.put("userName", error.getUserName());
        model.put("stackTrace", error.getStackTrace());
        model.put("workspace", error.getWorkspace());
        model.put("request", error.getRequest());

        String subject = messages.get("error.title");

        mailService.send("msantos.msh@gmail.com", subject, "unexpectedexception.ftl", model);*/
    }
}
