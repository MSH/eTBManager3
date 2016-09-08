package org.msh.etbm.commons.forms.controls;

/**
 * Created by rmemoria on 25/7/16.
 */
public class SubtitleControl extends Control {
    /**
     * The description label of the field
     */
    private String label;

    private int level;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
