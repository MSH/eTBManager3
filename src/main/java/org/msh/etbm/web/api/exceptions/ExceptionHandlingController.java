package org.msh.etbm.web.api.exceptions;

import org.msh.etbm.commons.messages.Message;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.http.HttpStatus;
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

/**
 * Created by rmemoria on 22/8/15.
 */
@ControllerAdvice
public class ExceptionHandlingController {

    /**
     * Handle errors when the entity was not found in the database
     * @param req
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Information not found")
    public void entityNotFound(HttpServletRequest req) {
        // nothing to do
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
