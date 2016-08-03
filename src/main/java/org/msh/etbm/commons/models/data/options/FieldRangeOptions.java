package org.msh.etbm.commons.models.data.options;

import org.msh.etbm.commons.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 7/7/16.
 */
public class FieldRangeOptions extends FieldOptions {

    private int ini;
    private int end;

    public int getIni() {
        return ini;
    }

    public void setIni(int ini) {
        this.ini = ini;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public List<Item> getOptionsValues() {
        List<Item> lst = new ArrayList<>();
        for (int i = ini; i <= end; i++) {
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
        if (number >= ini && number <= end) {
            return true;
        }

        return false;
    }
}
