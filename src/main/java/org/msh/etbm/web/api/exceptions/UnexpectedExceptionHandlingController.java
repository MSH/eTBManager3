package org.msh.etbm.web.api.exceptions;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.mail.MailService;
import org.msh.etbm.db.entities.ErrorLog;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.services.session.usersession.UserSession;
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
import java.io.PrintWriter;
import java.io.StringWriter;
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
        ErrorLog error = new ErrorLog();

        UserSession session = userRequestService.getUserSession();

        error.setErrorDate(new Date());
        error.setExceptionClass(e.getClass().getName());
        error.setUrl(req.getRequestURL().toString());
        error.setUserName(session.getUserLoginName() + " - " + session.getUserName());
        error.setUserId(session.getUserId());
        error.setWorkspace(session.getWorkspaceName());
        error.setRequest("TODOMS: nao consegui montar o request. exemplo abaixo, é realmente necessario?");

        /*
        ip address = 127.0.0.1
        * browser = Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36
        * method = POST
        * auth type = null
        * context path = /etbmanager
        * path info = null
        * path translated = null
        * query string = null
        * remote user = null
        * requested session id = 18B37C1FC0C05B780DA64ECBA24A3215
        * request URI = /etbmanager/login.seam
        * request URL = http://127.0.0.1:8080/etbmanager/login.seam
        * servlet path = /login.seam
        * is Request session id valid = true
        */

        String s = e.getMessage();
        if (s == null) {
            s = "No message in exception";
        } else if (s.length() > 500) {
            s = s.substring(0, 499);
        }
        error.setExceptionMessage(s);

        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        error.setStackTrace(writer.toString());

        entityManager.persist(error);
        entityManager.flush();

        notifyAdministrators(error);
    }

    /**
     * Send email to administrator notifying that an unexpected error occourred
     * @param error
     */
    private void notifyAdministrators(ErrorLog error) {
        //Mount list of parameter used on email render
        Map<String, Object> model = new HashMap<>();
        model.put("errorDate", DateUtils.FormatDateTime("yyyy.MM.dd G 'at' HH:mm:ss z", error.getErrorDate()));
        model.put("exceptionClass", error.getExceptionClass());
        model.put("exceptionMessage", error.getExceptionMessage());
        model.put("excepionUrl", error.getUrl());
        model.put("userName", error.getUserName());
        model.put("stackTrace", error.getStackTrace());
        model.put("workspace", error.getWorkspace());

        // 'Name' of who is receiving the email
        model.put("name", "Administrator");

        //Get admin mail address
        String to = (String) entityManager.createQuery("select adminMail from SystemConfig group by id having id = min(id)").getSingleResult();

        //Mount subject
        String subject = messages.get("error.title") + " " + error.getId() + " - " + error.getExceptionMessage();
        //TODOMS: Deveria colocar alguma proteção para nao enviar emails quando em dev?
        //send email
        mailService.send(to, subject, "unexpectedexception.ftl", model);
    }
}
