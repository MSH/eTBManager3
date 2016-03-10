package org.msh.etbm.commons.commands.details;

/**
 * Created by rmemoria on 7/3/16.
 */
public class CommandLogDiff {
    private String title;
    private String prevValue;
    private String newValue;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrevValue() {
        return prevValue;
    }

    public void setPrevValue(String prevValue) {
        this.prevValue = prevValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
