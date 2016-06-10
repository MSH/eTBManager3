package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.enums.SearchableType;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Store records that may be searched
 * Created by rmemoria on 9/6/16.
 */
@Entity
@Table(name = "searchable")
public class Searchable {

    @Id
    private UUID id;
    /**
     * The workspace of this entity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_ID")
    @NotNull
    private Workspace workspace;

    @NotNull
    private SearchableType type;

    @Max(100)
    @NotNull
    private String title;

    @Max(200)
    private String subtitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT_ID")
    private Unit unit;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public SearchableType getType() {
        return type;
    }

    public void setType(SearchableType type) {
        this.type = type;
    }
}
