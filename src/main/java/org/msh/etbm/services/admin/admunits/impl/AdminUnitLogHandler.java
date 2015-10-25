package org.msh.etbm.services.admin.admunits.impl;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.services.admin.admunits.AdminUnitData;
import org.msh.etbm.services.admin.admunits.AdminUnitRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by rmemoria on 21/10/15.
 */
@Component
public class AdminUnitLogHandler implements CommandLogHandler {

    public static final String CREATE = "admunit.create";
    public static final String UPDATE = "admunit.update";
    public static final String DELETE = "admunit.delete";

    @Override
    public CommandHistoryInput prepareLog(CommandHistoryInput in, Object request, Object response) {
        if (in.getType().endsWith("create")) {
            in.setAction(CommandAction.CREATE);
            in.setEntityId((UUID)response);
            in.setEntityName(((AdminUnitRequest)request).getName());
        }

        if (in.getType().endsWith("update")) {
            in.setAction(CommandAction.UPDATE);
            in.setEntityId((UUID)response);
            Object[] args = (Object[])request;
            in.setEntityName(((AdminUnitRequest)args[1]).getName());
        }

        if (in.getType().endsWith("delete")) {
            in.setAction(CommandAction.DELETE);
            AdminUnitData data = (AdminUnitData)response;
            in.setEntityId(data.getId());
            in.setEntityName(data.getName());
        }

        return in;
    }
}
