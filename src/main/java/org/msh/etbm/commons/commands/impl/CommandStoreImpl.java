package org.msh.etbm.commons.commands.impl;

import org.msh.etbm.commons.JsonUtils;
import org.msh.etbm.commons.commands.*;
import org.msh.etbm.commons.commands.details.CommandLogDetail;
import org.msh.etbm.db.entities.*;
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
    public Integer store(CommandHistoryInput in) {
        checkCommandType(in.getType());
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
        CommandLogDetail data = in.getDetailData();
        if (data != null) {
            String json = JsonUtils.objectToJSONString(data, false);
            cmd.setData(json);
        }

        entityManager.persist(cmd);

        return cmd.getId();
    }


    /**
     * Check if it is a valid command type
     * @param type
     */
    public void checkCommandType(String type) {
        CommandType cmd = CommandTypes.ROOT.find(type);

        if (cmd == null) {
            throw new CommandException("Command type not registered: " + type);
        }

        if (cmd.getChildCount() > 0) {
            throw new CommandException("Cannot use a group command type: " + type);
        }
    }

    /**
     * Return the workspace to be used in log operations
     *
     * @param in
     * @return
     */
    protected WorkspaceLog getWorkspaceLog(CommandHistoryInput in) {
        if (in.getWorkspaceId() == null) {
            return null;
        }

        WorkspaceLog wslog = entityManager.find(WorkspaceLog.class, in.getWorkspaceId());

        // workspace log does not exist ?
        if (wslog == null) {
            // register a new workspace log
            Workspace ws = entityManager.find(Workspace.class, in.getWorkspaceId());

            wslog = new WorkspaceLog();
            wslog.setName(ws.getName());
            wslog.setId(ws.getId());

            entityManager.persist(wslog);
            entityManager.flush();
        }

        return wslog;
    }

    /**
     * Return the user to be used in log operation
     *
     * @return
     */
    protected UserLog getUserLog(CommandHistoryInput in) {
        if (in.getUserId() == null) {
            return null;
        }

        UserLog userLog = entityManager.find(UserLog.class, in.getUserId());

        // user log does not exist ?
        if (userLog == null) {
            // register a new user log
            User user = entityManager.find(User.class, in.getUserId());

            userLog = new UserLog();
            userLog.setName(user.getName());
            userLog.setId(user.getId());
            entityManager.persist(userLog);
            entityManager.flush();
        }

        return userLog;
    }
}
