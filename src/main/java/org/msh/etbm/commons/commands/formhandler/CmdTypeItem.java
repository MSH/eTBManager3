package org.msh.etbm.commons.commands.formhandler;

import java.util.List;

/**
 * Information about a command type to be sent to the client, used as a list of available command types
 * Created by rmemoria on 29/6/16.
 */
public class CmdTypeItem {

    private String id;
    private String name;
    private List<CmdTypeItem> children;

    public CmdTypeItem() {
        super();
    }

    public CmdTypeItem(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CmdTypeItem> getChildren() {
        return children;
    }

    public void setChildren(List<CmdTypeItem> children) {
        this.children = children;
    }
}
