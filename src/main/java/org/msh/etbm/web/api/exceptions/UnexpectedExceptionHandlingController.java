package org.msh.etbm.web.api.exceptions;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.mail.MailService;
import org.msh.etbm.db.entities.ErrorLog;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.services.session.usersession.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    private final static Logger LOGGER = LoggerFactory.getLogger(UnexpectedExceptionHandlingController.class);

    @Autowired
    Messages messages;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    MailService mailService;

    @PersistenceContext
    EntityManager entityManager;

    @Value("${development:false}")
    boolean development;

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
        if (session != null) {
            error.setUserName(session.getUserLoginName() + " - " + session.getUserName());
            error.setUserId(session.getUserId());
            error.setWorkspace(session.getWorkspaceName());
        }

        //mount request field
        StringBuilder reqdata = new StringBuilder();
        reqdata.append("ip address = " + req.getRemoteAddr());
        reqdata.append("\n* browser = " + req.getHeader("user-Agent"));
        reqdata.append("\n* method = " + req.getMethod());
        reqdata.append("\n* context path = " + req.getContextPath());
        error.setRequest(reqdata.toString());

        //mount exception message field
        String s = e.getMessage();
        if (s == null) {
            s = "No message in exception";
        } else if (s.length() > 200) {
            s = s.substring(0, 199);
        }
        error.setExceptionMessage(s);

        //mount stack trace field
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        error.setStackTrace(writer.toString());

        entityManager.persist(error);
        entityManager.flush();

        if (!development) {
            notifyAdministrators(error);
        }

        e.printStackTrace();
    }

    /**
     * Send email to administrator notifying that an unexpected error occourred
     * @param error
     */
    private void notifyAdministrators(ErrorLog error) {
        //Get admin mails address
        String to = (String) entityManager.createQuery("select adminMail from SystemConfig group by id having id = min(id)").getSingleResult();
        if (to == null || to.isEmpty()) {
            throw new RuntimeException("Missing admin email");
        }
        to = to.replace(" ", "");
        String[] emails = to.split(",");

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


        //Mount subject
        String subject = messages.get("error.title") + " " + error.getId() + " - " + error.getExceptionMessage();

        try {
            //send email
            for (String email : emails) {
                mailService.send(email, subject, "unexpectedexception.ftl", model);
            }
        } catch (Exception e) {
            LOGGER.info("Error when trying to send e-mail: " + e.getClass().toString() + ": " + e.getMessage());
        }
    }
}
