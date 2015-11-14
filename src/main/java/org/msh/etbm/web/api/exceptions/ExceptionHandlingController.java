package org.msh.etbm.web.api.exceptions;

import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.messages.Message;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
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
            Message msg = new Message(e.getField(), e.getMessage(), null);
            List<Message> lst = new ArrayList<>();
            lst.add(msg);
            return new StandardResult(null, lst, false);
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

        List<Message> errors = new ArrayList<>();
        List<FieldError> lst = res.getFieldErrors();
        for (FieldError fld: lst) {
            String message = messageSource.getMessage(fld, locale);
            errors.add(new Message(fld.getField(), message, fld.getCode()));
        }

        return new StandardResult(null, errors, false);
    }


//
//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(value = HttpStatus.OK)
//    @ResponseBody
//    public Object handleIllegalArgument(IllegalArgumentException e) {
//        StandardResult res = new StandardResult();
//        List<Message> errors = new ArrayList<>();
//        errors.add(new Message(null, e.getMessage(), null));
//
//        res.setErrors(errors);
//        return res;
//    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Object methodValidationError(MethodArgumentNotValidException e) {
        List<Message> errors = new ArrayList<>();

        for (FieldError fld: e.getBindingResult().getFieldErrors()) {
            errors.add(new Message(fld.getField(), fld.getDefaultMessage(), null));
        }

        StandardResult res = new StandardResult(null, errors, false);
        return res;
    }
}
