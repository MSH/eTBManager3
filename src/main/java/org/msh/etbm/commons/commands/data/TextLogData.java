package org.msh.etbm.commons.commands.data;

/**
 * Store a text as a log data
 * Created by rmemoria on 17/10/15.
 */
public class TextLogData implements CommandData {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public DataType getType() {
        return DataType.TEXT;
    }

    @Override
    public Object getDataToSerialize() {
        return text;
    }
}
