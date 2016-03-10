package org.msh.etbm.commons.commands.details;

import org.msh.etbm.commons.commands.impl.CommandException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Help creation of command history details
 *
 * Created by rmemoria on 7/3/16.
 */
public class DetailWriter {
    public CommandLogDetail detail = new CommandLogDetail();


    public DetailWriter setText(String title) {
        detail.setText(title);
        return this;
    }

    public DetailWriter addItem(String s, Object value) {
        CommandLogItem item = new CommandLogItem();
        item.setTitle(s);
        item.setValue( convertToString(value) );

        List<CommandLogItem> items = detail.getItems();
        if (items == null) {
            items = new ArrayList<>();
            detail.setItems(items);
        }

        items.add(item);
        return this;
    }

    public DetailWriter addDiff(String title, Object prevValue, Object newValue) {
        CommandLogDiff diff = new CommandLogDiff();
        diff.setTitle(title);
        diff.setNewValue( convertToString(newValue) );
        diff.setPrevValue( convertToString(prevValue) );

        List<CommandLogDiff> diffs = detail.getDiffs();
        if (diffs == null) {
            diffs = new ArrayList<>();
            detail.setDiffs(diffs);
        }

        diffs.add(diff);
        return this;
    }


    /**
     * Convert a value to a string representation
     * @param value
     * @return
     */
    private String convertToString(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return CommandLogDetail.TYPE_STRING + value.toString();
        }

        if (value instanceof Date) {
            return CommandLogDetail.TYPE_DATETIME + StringConverter.dateToString((Date)value);
        }

        if (value instanceof Enum) {
            return CommandLogDetail.TYPE_TEMPLATE + "$" + StringConverter.enumToString((Enum)value);
        }

        if (value instanceof Number) {
            Number num = (Number)value;
            return CommandLogDetail.TYPE_NUMBER + StringConverter.floatToString(num.doubleValue());
        }

        if (value instanceof Boolean) {
            return CommandLogDetail.TYPE_BOOLEAN + StringConverter.boolToString((Boolean)value);
        }

        if (value instanceof Collection) {
            return convertCollection((Collection)value);
        }

        throw new CommandException("Type not supported for string convertion: " + value.getClass());
    }

    private String convertCollection(Collection lst) {
        StringBuilder s = new StringBuilder();
        for (Object obj: lst) {
            if (s.length() > 0) {
                s.append(", ");
            }
            s.append(obj.toString());
        }

        return s.toString();
    }

    public CommandLogDetail getDetail() {
        return detail;
    }

    public void setDetail(CommandLogDetail detail) {
        this.detail = detail;
    }
}
