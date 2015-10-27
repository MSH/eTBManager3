package org.msh.etbm.commons.entities.cmdlog;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.commons.entities.*;
import org.msh.etbm.commons.transactionlog.RoleAction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Generic command log handler to support CUD operations
 *
 * Created by rmemoria on 25/10/15.
 */
@Component
public class EntityCmdLogHandler implements CommandLogHandler {
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    @Override
    public void prepareLog(CommandHistoryInput in, Object request, Object response) {
        if (response == null) {
            in.cancelLog();
            return;
        }

        String cmd = in.getType();
        ServiceResult result = (ServiceResult)response;

        Class clazz = result.getEntityClass();
        String name = clazz.getSimpleName().toLowerCase();

        in.setType(name + "." + in.getType());
        in.setEntityId(result.getId());
        in.setEntityName(result.getEntityName());

        if (CREATE.equals(cmd)) {
            handleCreateCommand(in, result);
            return;
        }

        if (UPDATE.equals(cmd)) {
            handleUpdateCommand(in, result);
            return;
        }

        if (DELETE.equals(cmd)) {
            handleDeleteCommand(in, result);
            return;
        }

        throw new RuntimeException("Action to log command not supported");
    }


    protected void handleCreateCommand(CommandHistoryInput in, ServiceResult res) {
        in.beginListLog().put(res.getLogValues());
        in.setAction(CommandAction.CREATE);
    }


    protected void handleUpdateCommand(CommandHistoryInput in, ServiceResult res) {
        in.beginDiffLog().setValues(res.getLogDiffs().getValues());
        in.setAction(CommandAction.UPDATE);
    }

    protected void handleDeleteCommand(CommandHistoryInput in, ServiceResult res) {
        in.beginListLog().put(res.getLogValues());
        in.setAction(CommandAction.DELETE);
    }

}
