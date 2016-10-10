package org.msh.etbm.commons.entities.cmdlog;

import org.msh.etbm.commons.commands.*;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.objutils.DiffValue;
import org.msh.etbm.commons.objutils.ObjectValues;
import org.msh.etbm.commons.objutils.PropertyValue;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Generic command log handler to support CUD operations
 * <p>
 * Created by rmemoria on 25/10/15.
 */
@Component
public class EntityCmdLogHandler implements CommandLogHandler<Object, ServiceResult> {

    @Override
    public void prepareLog(CommandHistoryInput in, Object request, ServiceResult result) {
        // any response ?
        if (result == null) {
            in.cancelLog();
            return;
        }

        // there were validation errors ?
        if (result.getOperation() != Operation.DELETE  && result.getLogDiffs() == null && result.getLogValues() == null) {
            in.cancelLog();
            return;
        }

        Class clazz = result.getEntityClass();
        String name = clazz.getSimpleName().toLowerCase();

        in.setType(name + "." + in.getType());
        in.setEntityId(result.getId());
        in.setEntityName(result.getEntityName());
        in.setParentId(result.getParentId());

        CommandType cmd = resolveCommandType(result);
        in.setType(cmd.getPath());

        switch (result.getOperation()) {
            case NEW:
                handleCreateCommand(in, result);
                return;
            case EDIT:
                handleUpdateCommand(in, result);
                return;
            case DELETE:
                handleDeleteCommand(in, result);
                return;
            default:
                throw new CommandException("Invalid log command operation");
        }
    }


    protected CommandType resolveCommandType(ServiceResult res) {
        CommandType cmd = res.getCommandType();
        switch (res.getOperation()) {
            case NEW: return cmd.find(CommandTypes.CMD_CREATE);
            case EDIT: return cmd.find(CommandTypes.CMD_UPDATE);
            case DELETE: return cmd.find(CommandTypes.CMD_DELETE);
            default: throw new CommandException("Invalid operation for log");
        }
    }

    /**
     * Handle command log when an entity is created
     *
     * @param in
     * @param res
     */
    protected void handleCreateCommand(CommandHistoryInput in, ServiceResult res) {
        addItems(in, res.getLogValues());
        in.setAction(CommandAction.CREATE);
    }


    /**
     * Handle command log when an entity is updated
     *
     * @param in
     * @param res
     */
    protected void handleUpdateCommand(CommandHistoryInput in, ServiceResult res) {
        Map<String, DiffValue> diffs = res.getLogDiffs().getValues();

        for (Map.Entry<String, DiffValue> it : diffs.entrySet()) {
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
     *
     * @param in
     * @param res
     */
    protected void handleDeleteCommand(CommandHistoryInput in, ServiceResult res) {
        addItems(in, res.getLogValues());
        in.setAction(CommandAction.DELETE);
    }

    /**
     * Add object values to the command history details
     *
     * @param in   the object containing the data to generate the command log
     * @param vals the list of values
     */
    private void addItems(CommandHistoryInput in, ObjectValues vals) {
        Map<String, PropertyValue> values = vals.getValues();
        for (Map.Entry<String, PropertyValue> it : values.entrySet()) {
            if (!it.getValue().isCollection()) {
                in.addItem(it.getKey(), it.getValue().get());
            }
        }
    }
}
