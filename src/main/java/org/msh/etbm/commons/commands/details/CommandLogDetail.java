package org.msh.etbm.commons.commands.details;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Help creating an object containing the command log detail
 * Created by rmemoria on 7/3/16.
 */
public class CommandLogDetail {

    public static final String TYPE_STRING = "S";
    public static final String TYPE_DATETIME = "D";
    public static final String TYPE_NUMBER = "N";
    public static final String TYPE_TEMPLATE = "T";
    public static final String TYPE_BOOLEAN = "B";


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String text;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CommandLogItem> items;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CommandLogDiff> diffs;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<CommandLogItem> getItems() {
        return items;
    }

    public void setItems(List<CommandLogItem> items) {
        this.items = items;
    }

    public List<CommandLogDiff> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<CommandLogDiff> diffs) {
        this.diffs = diffs;
    }
}
