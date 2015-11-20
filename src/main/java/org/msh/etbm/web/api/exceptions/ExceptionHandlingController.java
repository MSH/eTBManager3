package org.msh.etbm.web.api.exceptions;

import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.web.api.Message;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Exception handlers to display friendly and standard messages to the client
 *
 * Created by rmemoria on 22/8/15.
 */
@ControllerAdvice
public class ExceptionHandlingController {

    @Autowired
    MessageSource messageSource;

    /**
     * Handle errors when the entity was not found in the database
     * @param req
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void entityNotFound(HttpServletRequest req) {
        // nothing to do
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleBindException(BindException e) {
        return convertErrorsToStandardResult(e.getBindingResult());
    }

    @ExceptionHandler(EntityValidationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleEntityValidationError(EntityValidationException e) {
        if (e.getBindingResult() == null) {
            // get the selected locale
            Locale locale = LocaleContextHolder.getLocale();

            // create the message
            Map<String, Message> msgs = new HashMap<>();
            String msg;
            if (e.getCode() != null && e.getMessage() == null) {
                msg = messageSource.getMessage(e.getCode(), null, locale);
            }
            else {
                msg = e.getMessage();
            }
            msgs.put(e.getField(), new Message(msg, e.getCode()));
            return new StandardResult(null, msgs, false);
        }
        return convertErrorsToStandardResult(e.getBindingResult());
    }

    /**
     * Convert a list of error messages in the given bindingResult object to be serialized in a standard
     * way to the client
     * @param res list of error messages
     * @return instance of {@link StandardResult}
     */
    protected StandardResult convertErrorsToStandardResult(BindingResult res) {
        if (!res.hasErrors()) {
            return new StandardResult(null, null, false);
        }

        Locale locale = LocaleContextHolder.getLocale();

        Map<String, Message> errors = new HashMap<>();
        List<FieldError> lst = res.getFieldErrors();
        for (FieldError fld: lst) {
            String message = messageSource.getMessage(fld, locale);
            errors.put(fld.getField(), new Message(message, fld.getCode()));
        }

        return new StandardResult(null, errors, false);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Object methodValidationError(MethodArgumentNotValidException e) {
        Map<String, Message> errors = new HashMap<>();

        for (FieldError fld: e.getBindingResult().getFieldErrors()) {
            errors.put(fld.getField(), new Message(fld.getDefaultMessage(), null));
        }

        StandardResult res = new StandardResult(null, errors, false);
        return res;
    }
}
