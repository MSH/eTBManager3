package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Store information about a tag. A tag may be assigned to cases. This
 * assignment may be done automatically or manually according to the
 * content of the sqlCondition - If null, the tag is assigned manually
 * by a user to a case, if not null, so the tag is maintained by
 * the system. Manual tags are represented by the blue tag.
 * <p>
 * The <code>consistencyCheck</code> field is used only in automatic
 * tags and indicate if the tag is used for checking validation
 * problems (red tag) or if this is just for grouping cases (green tag)
 *
 * @author Ricardo Memoria
 */
@Entity
@Table(name = "tag")
public class Tag extends WorkspaceEntity {

    public enum TagType { MANUAL, AUTO, AUTODANGER }

    @Column(length = 100)
    @NotNull
    @PropertyLog(messageKey = "form.name")
    private String name;

    @Lob
    private String sqlCondition;

    private boolean consistencyCheck;

    @PropertyLog(messageKey = "UserState.ACTIVE")
    private boolean active;

    private boolean dailyUpdate;

    /**
     * Return true if tag is auto generated and maintained by the system
     *
     * @return
     */
    public boolean isAutogenerated() {
        return (sqlCondition != null) && (!sqlCondition.isEmpty());
    }


    /**
     * Return the tag type
     *
     * @return
     */
    public TagType getType() {
        if (!isAutogenerated()) {
            return TagType.MANUAL;
        }

        return consistencyCheck ? TagType.AUTODANGER : TagType.AUTO;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the sqlCondition
     */
    public String getSqlCondition() {
        return sqlCondition;
    }

    /**
     * @param sqlCondition the sqlCondition to set
     */
    public void setSqlCondition(String sqlCondition) {
        this.sqlCondition = sqlCondition;
    }

    /**
     * @return the consistencyCheck
     */
    public boolean isConsistencyCheck() {
        return consistencyCheck;
    }

    /**
     * @param consistencyCheck the consistencyCheck to set
     */
    public void setConsistencyCheck(boolean consistencyCheck) {
        this.consistencyCheck = consistencyCheck;
    }


    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }


    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the dailyUpdate
     */
    public boolean isDailyUpdate() {
        return dailyUpdate;
    }


    /**
     * @param dailyUpdate the dailyUpdate to set
     */
    public void setDailyUpdate(boolean dailyUpdate) {
        this.dailyUpdate = dailyUpdate;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", active=" + active +
                ", consistencyCheck=" + consistencyCheck +
                '}';
    }

    @Override
    public String getDisplayString() {
        return name;
    }
}
