package org.msh.etbm.commons.models.data.options;

import org.msh.etbm.commons.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 7/7/16.
 */
public class FieldRangeOptions extends FieldOptions {

    private int from;
    private int to;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Override
    public List<Item> getOptionsValues() {
        List<Item> lst = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            lst.add(new Item(i, Integer.toString(i)));
        }
        return lst;
    }

    @Override
    public boolean isValueInOptions(Object value) {
        if (!(value instanceof Number)) {
            return false;
        }

        int number = ((Number)value).intValue();
        if (number >= from && number <= to) {
            return true;
        }

        return false;
    }
}
