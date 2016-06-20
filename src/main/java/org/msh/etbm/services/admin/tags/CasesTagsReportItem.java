package org.msh.etbm.services.admin.tags;

import org.msh.etbm.db.entities.Tag;

import java.util.UUID;

/**
 * Store temporary information about information of a tag and its quantity of cases.
 * Used by {@link CasesTagsReportService} to generate a report about the quantity of cases per tag
 *
 * Created by rmemoria on 17/6/16.
 */
public class CasesTagsReportItem {
    private UUID id;
    private String name;
    private Tag.TagType type;
    private int count;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
