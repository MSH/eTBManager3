package org.msh.etbm.commons.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Store information about a command type
 *
 * Created by rmemoria on 29/6/16.
 */
public class CommandType {
    private String id;
    private CommandType parent;
    private String messageKey;
    private List<CommandType> children;


    public CommandType(String id) {
        this.id = id;
    }

    public CommandType(String id, String messageKey) {
        super();
        this.id = id;
        this.messageKey = messageKey;
    }

    /**
     * Add a command as a child of this command
     * @param child
     */
    public void addChild(CommandType child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        child.parent = this;
    }

    /**
     * Resolve the message key to display the item content
     * @return
     */
    public String resolveMessageKey() {
        if (messageKey != null) {
            return messageKey;
        }

        return getPath();
    }


    public String getId() {
        return id;
    }

    public CommandType getParent() {
        return parent;
    }

    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Return the child commands of the command
     * @return
     */
    public List<CommandType> getChildren() {
        return children != null ? Collections.unmodifiableList(children) : Collections.emptyList();
    }

    /**
     * Return the number of child commands
     * @return int value
     */
    public int getChildCount() {
        return children != null ? children.size() : 0;
    }

    /**
     * Get the path name of the command, which is a composition of the command ID plus all the parents ID
     * separated by dots (.)
     * @return
     */
    public String getPath() {
        String parentPath = parent != null ? parent.getPath() : "";

        return parentPath.isEmpty() ? id : parentPath + "." + id;
    }

    /**
     * Find a child command type by its path
     * @param path
     * @return
     */
    public CommandType find(String path) {
        if (children == null) {
            return null;
        }

        int pos = path.indexOf('.');
        String id = pos > 0 ? path.substring(0, pos) : path;

        for (CommandType cmd: children) {
            if (cmd.getId().equals(id)) {
                String remain = pos > 0 ? path.substring(pos + 1, path.length()) : null;
                return remain == null ? cmd : cmd.find(remain);
            }
        }
        return null;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
