package org.msh.etbm.web.api.exceptions;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.services.security.ForbiddenException;
import org.msh.etbm.web.api.Message;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exception handlers to display friendly and standard messages to the client
 *
 * Created by rmemoria on 22/8/15.
 */
@ControllerAdvice
public class ExceptionHandlingController {

    @Autowired
    Messages messages;


    /**
     * When a service call is not authorized by the current user
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ResponseBody
    public String forbiddenException(ForbiddenException e) {
        return e.getMessage();
    }

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
        return convertErrorsToStandardResult(e.getBindingResult());
    }

    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object handleInvalidArgument(InvalidArgumentException e) {
        return e.getProperty() + " - " + e.getMessage();
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

        Map<String, Message> errors = new HashMap<>();
        List<FieldError> lst1 = res.getFieldErrors();
        for (FieldError fld: lst1) {
            String message = messages.get(fld);
            errors.put(fld.getField(), new Message(message, fld.getCode()));
        }

        List<ObjectError> lst2 = res.getGlobalErrors();
        for (ObjectError fld: lst2) {
            String message = messages.get(fld);
            errors.put("global", new Message(message, fld.getCode())); //TODOMS: melhorar para permitir mais de uma mensagem global, o key nao pode ser repetido
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
