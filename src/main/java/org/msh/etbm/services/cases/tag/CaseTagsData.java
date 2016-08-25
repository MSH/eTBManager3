package org.msh.etbm.services.cases.tag;

import org.msh.etbm.db.entities.Tag;

import java.util.UUID;

/**
 * Created by Mauricio on 25/07/2016.
 */
public class CaseTagsData {
    private UUID id;
    private String name;
    private Tag.TagType type;

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
}
