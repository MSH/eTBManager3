package org.msh.etbm.commons.commands.impl;

import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandStoreService;
import org.msh.etbm.commons.commands.data.DataType;
import org.msh.etbm.db.entities.CommandHistory;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.entities.UserLog;
import org.msh.etbm.db.entities.WorkspaceLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * Implementation of the CommandStoreService, to store command execution in the database
 * Created by rmemoria on 17/10/15.
 */
@Service
public class CommandStoreImpl implements CommandStoreService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public void store(CommandHistoryInput in) {
        CommandHistory cmd = new CommandHistory();

        cmd.setType(in.getType());
        cmd.setExecDate(new Date());
        cmd.setEntityName(in.getEntityName());

        // get the user and workspace involved in the command
        cmd.setUser(getUserLog(in));
        cmd.setWorkspace(getWorkspaceLog(in));

        // get the unit involved in the command
        if (in.getUnitId() != null) {
            Unit unit = entityManager.find(Unit.class, in.getUnitId());
            cmd.setUnit(unit);
        }

        cmd.setEntityName(in.getEntityName());
        cmd.setEntityId(in.getEntityId());
        cmd.setParentId(in.getParentId());
        cmd.setAction(in.getAction());

        // parse the data to json format
        if (in.getData() != null) {
            Object data = in.getData().getDataToSerialize();
            String json = JsonParser.objectToJSONString(data);
            DataType dttype = in.getData().getType();
            cmd.setData(json);
            cmd.setDataType(dttype);
        }

        entityManager.persist(cmd);
    }


    /**
     * Return the workspace to be used in log operations
     * @param in
     * @return
     */
    protected WorkspaceLog getWorkspaceLog(CommandHistoryInput in) {
        if (in.getWorkspaceId() == null) {
            return null;
        }

        WorkspaceLog wslog = entityManager.find(WorkspaceLog.class, in.getWorkspaceId());

        return wslog;
    }

    /**
     * Return the user to be used in log operation
     * @return
     */
    protected UserLog getUserLog(CommandHistoryInput in) {
        if (in.getUserId() == null) {
            return null;
        }

        UserLog userLog = entityManager.find(UserLog.class, in.getUserId());

        return userLog;
    }
}
