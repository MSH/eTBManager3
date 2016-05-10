package org.msh.etbm.commons.entities.cmdlog;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.objutils.DiffValue;
import org.msh.etbm.commons.objutils.ObjectValues;
import org.msh.etbm.commons.objutils.PropertyValue;
import org.springframework.stereotype.Component;

import java.util.Map;

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
        // any response ?
        if (response == null) {
            in.cancelLog();
            return;
        }

        String cmd = in.getType();

        // there were validation errors ?
        ServiceResult result = (ServiceResult)response;
        if (!DELETE.equals(cmd) && result.getLogDiffs() == null && result.getLogValues() == null) {
            in.cancelLog();
            return;
        }

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


    /**
     * Handle command log when an entity is created
     * @param in
     * @param res
     */
    protected void handleCreateCommand(CommandHistoryInput in, ServiceResult res) {
        addItems(in, res.getLogValues());
        in.setAction(CommandAction.CREATE);
    }


    /**
     * Handle command log when an entity is updated
     * @param in
     * @param res
     */
    protected void handleUpdateCommand(CommandHistoryInput in, ServiceResult res) {
        Map<String, DiffValue> diffs = res.getLogDiffs().getValues();

        for (Map.Entry<String, DiffValue> it: diffs.entrySet()) {
            DiffValue diff = it.getValue();
            if (diff.isCollection()) {
                handleCollectionUpdate(in, it.getKey(), diff);
            } else {
                in.addDiff(it.getKey(), it.getValue().getPrevValue(), it.getValue().getNewValue());
            }
        }

        in.setAction(CommandAction.UPDATE);
    }


    private void handleCollectionUpdate(CommandHistoryInput in, String key, DiffValue diffValue) {
        if (diffValue.getAddedItems() != null) {
            in.addItem("+" + key, diffValue.getAddedItems());
        }

        if (diffValue.getRemovedItems() != null) {
            in.addItem("-" + key, diffValue.getRemovedItems());
        }

        if (diffValue.getChangedItems() != null) {
            in.addItem(key + " ($action.changed)", diffValue.getChangedItems());
        }
    }

    /**
     * Handle command log when an entity is deleted
     * @param in
     * @param res
     */
    protected void handleDeleteCommand(CommandHistoryInput in, ServiceResult res) {
        addItems(in, res.getLogValues());
        in.setAction(CommandAction.DELETE);
    }

    /**
     * Add object values to the command history details
     * @param in the object containing the data to generate the command log
     * @param vals the list of values
     */
    private void addItems(CommandHistoryInput in, ObjectValues vals) {
        Map<String, PropertyValue> values = vals.getValues();
        for (Map.Entry<String, PropertyValue> it: values.entrySet()) {
            if (!it.getValue().isCollection()) {
                in.addItem(it.getKey(), it.getValue().get());
            }
        }
    }
}
