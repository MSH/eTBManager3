package org.msh.etbm.services.admin.admunits.impl;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.services.admin.admunits.AdminUnitData;
import org.msh.etbm.services.admin.admunits.AdminUnitRequest;
import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.CountryStructureRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by rmemoria on 24/10/15.
 */
@Service
public class CountryStructureLogHandler implements CommandLogHandler {

    public static final String ENTITY_PREFIX = "countryStructure";

    @Override
    public CommandHistoryInput prepareLog(CommandHistoryInput in, Object request, Object response) {

        if (in.getType().equals("create")) {
            in.setAction(CommandAction.CREATE);
            in.setEntityId((UUID)response);
            in.setEntityName(((CountryStructureRequest)request).getName());
        }

        if (in.getType().equals("update")) {
            in.setAction(CommandAction.UPDATE);
            in.setEntityId((UUID)response);
            Object[] args = (Object[])request;
            in.setEntityName(((CountryStructureRequest)args[1]).getName());
        }

        if (in.getType().equals("delete")) {
            in.setAction(CommandAction.DELETE);
            CountryStructureData data = (CountryStructureData)response;
            in.setEntityId(data.getId());
            in.setEntityName(data.getName());
        }

        in.setType( ENTITY_PREFIX + "." + in.getType());

        return in;
    }
}
