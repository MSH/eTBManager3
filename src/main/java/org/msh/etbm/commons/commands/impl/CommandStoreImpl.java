package org.msh.etbm.commons.commands.impl;

import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandStoreService;
import org.msh.etbm.commons.commands.data.DataType;
import org.msh.etbm.db.dto.UserDTO;
import org.msh.etbm.db.dto.WorkspaceDTO;
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
        if (in.getUnit() != null) {
            Unit unit = entityManager.find(Unit.class, in.getUnit().getId());
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
        WorkspaceDTO ws = in.getWorkspace();
        if (ws == null) {
            return null;
        }

        WorkspaceLog wslog = entityManager.find(WorkspaceLog.class, ws.getId());

        if (wslog == null) {
            wslog = new WorkspaceLog();
            wslog.setId(ws.getId());
            wslog.setName(ws.getName());
            entityManager.persist(wslog);
        }
        return wslog;
    }

    /**
     * Return the user to be used in log operation
     * @return
     */
    protected UserLog getUserLog(CommandHistoryInput in) {
        UserDTO user = in.getUser();

        if (user == null) {
            return null;
        }

        UserLog userLog = entityManager.find(UserLog.class, user.getId());

        // if the user log doesn't exist, create a new one
        if (userLog == null) {
            // save new user log information
            userLog = new UserLog();
            userLog.setId(user.getId());
            userLog.setName(user.getName());
            entityManager.persist(userLog);
        }

        return userLog;
    }
}
