package org.msh.etbm.commons.commands.impl;

import org.msh.etbm.commons.commands.CommandType;
import org.msh.etbm.commons.commands.CommandTypes;

/**
 * Created by rmemoria on 29/6/16.
 */
public class RootCommandType extends CommandType {

    public RootCommandType() {
        super(null);
    }

    @Override
    public String getPath() {
        return "";
    }

    public CommandType add(String path) {
        int pos = path.lastIndexOf(".");
        // break the path in two
        String subpath = pos > 0 ? path.substring(0, pos) : path;
        String id = pos > 0 ? path.substring(pos + 1, path.length()) : path;

        // create a new command
        CommandType cmd = new CommandType(id);

        if (subpath.equals(path)) {
            addChild(cmd);
            return cmd;
        }

        CommandType parent = find(subpath);
        if (parent == null) {
            throw new RuntimeException("Invalid path " + path);
        }

        parent.addChild(cmd);

        return cmd;
    }

    public CommandType add(String path, String  messageKey) {
        CommandType cmd = add(path);
        cmd.setMessageKey(messageKey);
        return cmd;
    }

    public CommandType addCRUD(String path) {
        return addCRUD(path, null);
    }

    public CommandType addCRUD(String path, String messageKey) {
        CommandType cmd = add(path);
        cmd.setMessageKey(messageKey);
        cmd.addChild(new CommandType(CommandTypes.CMD_CREATE));
        cmd.addChild(new CommandType(CommandTypes.CMD_UPDATE));
        cmd.addChild(new CommandType(CommandTypes.CMD_DELETE));

        return cmd;
    }
}
