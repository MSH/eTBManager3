package org.msh.etbm.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by rmemoria on 22/8/15.
 */
@Controller
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
}
