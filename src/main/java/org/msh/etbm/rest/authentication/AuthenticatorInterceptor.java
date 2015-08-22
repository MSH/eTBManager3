package org.msh.etbm.rest.authentication;

import org.msh.etbm.services.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Authenticator interceptor, called when a call is made to check if the request requires authentication
 *
 * Created by ricardo on 03/12/14.
 */
@Controller
public class AuthenticatorInterceptor extends HandlerInterceptorAdapter  {

    @Autowired
    AuthenticationService authenticationService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // check if route requires authentication
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();

        boolean authRequired = method.getAnnotation(Authenticated.class) != null ||
                method.getDeclaringClass().getAnnotation(Authenticated.class) != null;

        // if no authentication is required, so return
        if (!authRequired) {
            return true;
        }

        // get the authentication token in the request
        String token = request.getHeader(AuthConstants.AUTH_TOKEN_HEADERNAME);

        if (token == null &&
                request.getQueryString() != null &&
                request.getQueryString().contains(AuthConstants.AUTH_TOKEN_HEADERNAME))
        {
            token = request.getParameter(AuthConstants.AUTH_TOKEN_HEADERNAME);
        }

        // if there is no token, or token is invalid, return unauthorized
        if (token == null || !authenticationService.authenticateByToken(token)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Not authorized");
            return false;
        }

        return true;
    }

}
