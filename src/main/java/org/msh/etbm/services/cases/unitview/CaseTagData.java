package org.msh.etbm.services.cases.unitview;

import org.msh.etbm.db.entities.Tag;

/**
 * Created by rmemoria on 2/6/16.
 */
public class CaseTagData {
    private String name;
    private Tag.TagType type;
    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tag.TagType getType() {
        return type;
    }

    public void setType(Tag.TagType type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
