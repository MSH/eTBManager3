package org.msh.etbm.commons.commands.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.commons.commands.CommandStoreService;
import org.msh.etbm.services.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Aspect to surround the execution of a command
 * Created by rmemoria on 14/10/15.
 */
@Aspect
@Component
public class CommandInterceptor {

    @Autowired
    CommandStoreService commandStoreService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserRequestService userRequestService;


    /**
     * Around point cut called for every method that implements the CommandLog annotation
     * @param pjp information about the method to be invoked
     * @return the return of the method invocation
     * @throws Throwable
     */
    @Around("execution(public * *(..)) && @annotation(org.msh.etbm.commons.commands.CommandLog)")
    public Object aroundCommand(ProceedingJoinPoint pjp) throws Throwable {
        //  check if the current request is already under execution of a nested command call
        // command nesting is not supported, so if there is already a command being executed, just skip command log
        // and execute method
        if (userRequestService.isCommandExecuting()) {
            return pjp.proceed();
        }

        // execute the command and avoid other commands of being logged in a single request
        userRequestService.setCommandExecuting(true);
        try {
            return executeAndLog(pjp);
        } finally {
            userRequestService.setCommandExecuting(false);
        }
    }

    /**
     * Execute a command and log its execution in the command history
     * @param pjp information about method being invoked
     * @return the return of method invocation
     * @throws Throwable
     */
    protected Object executeAndLog(ProceedingJoinPoint pjp) throws Throwable {
        Method method = findAnnotatedMethod(pjp);
        CommandLog cmdLog = method.getAnnotation(CommandLog.class);

        if (cmdLog == null) {
            throw new CommandException("Annotation for command log not found in method " + method.toString());
        }

        Object res = pjp.proceed();

        storeCommand(method, cmdLog, pjp.getArgs(), res);

        return res;
    }

    protected Method findAnnotatedMethod(ProceedingJoinPoint pjp) {
        // try to get the command log annotation from the method
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        Method method = signature.getMethod();

        if (!method.getDeclaringClass().isInterface()) {
            return method;
        }

        Class clazz = pjp.getTarget().getClass();
        while (clazz != Object.class) {
            for (Method met: clazz.getDeclaredMethods()) {
                if (met.getName().equals(method.getName()) && met.getParameterCount() == method.getParameterCount()) {
                    return met;
                }
            }
            clazz = clazz.getSuperclass();
        }

        throw new CommandException("Annotated method not found: " + method.getName());
    }

    /**
     * Store the command in the command history
     * @param cmdlog the annotation found in the method about the command
     * @param args the arguments of the method call
     * @param result the return object of the method call
     */
    protected void storeCommand(Method method, CommandLog cmdlog, Object[] args, Object result) {
        String type = cmdlog.type();
        Class handlerClass = cmdlog.handler();

        // prepare command information to be filled by the handler
        CommandHistoryInput in = new CommandHistoryInput();
        in.setType(type);
        in.setMethod(method);

        // include information about the authenticated user
        if (userRequestService.isAuthenticated()) {
            in.setWorkspaceId(userRequestService.getUserSession().getWorkspaceId());
            in.setUserId(userRequestService.getUserSession().getUserId());
            in.setUnitId(userRequestService.getUserSession().getUnitId());
        }

        // call handler of the log
        if (handlerClass != null) {
            CommandLogHandler handler = (CommandLogHandler) applicationContext.getBean(handlerClass);
            Object p = args.length == 1 ? args[0] : args;
            handler.prepareLog(in, p, result);
        }

        // log operation was canceled?
        if (in.isCanceled()) {
            return;
        }

        // store the log
        commandStoreService.store(in);
    }
}
