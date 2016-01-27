package org.msh.etbm.services.admin.tags;

import java.util.UUID;

/**
 * Created by rmemoria on 6/1/16.
 */
public class TagData {
    private UUID id;
    private String name;
    private String sqlCondition;
    private boolean consistencyCheck;
    private boolean active;
    private boolean dailyUpdate;

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

    public String getSqlCondition() {
        return sqlCondition;
    }

    public void setSqlCondition(String sqlCondition) {
        this.sqlCondition = sqlCondition;
    }

    public boolean isConsistencyCheck() {
        return consistencyCheck;
    }

    public void setConsistencyCheck(boolean consistencyCheck) {
        this.consistencyCheck = consistencyCheck;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDailyUpdate() {
        return dailyUpdate;
    }

    public void setDailyUpdate(boolean dailyUpdate) {
        this.dailyUpdate = dailyUpdate;
    }
}
