package org.msh.etbm.services.admin.tags;

import java.util.Optional;

/**
 * Created by rmemoria on 6/1/16.
 */
public class TagFormData {
    private Optional<String> name;
    private Optional<String> sqlCondition;
    private Optional<Boolean> consistencyCheck;
    private Optional<Boolean> active;
    private Optional<Boolean> dailyUpdate;

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getSqlCondition() {
        return sqlCondition;
    }

    public void setSqlCondition(Optional<String> sqlCondition) {
        this.sqlCondition = sqlCondition;
    }

    public Optional<Boolean> getConsistencyCheck() {
        return consistencyCheck;
    }

    public void setConsistencyCheck(Optional<Boolean> consistencyCheck) {
        this.consistencyCheck = consistencyCheck;
    }

    public Optional<Boolean> getActive() {
        return active;
    }

    public void setActive(Optional<Boolean> active) {
        this.active = active;
    }

    public Optional<Boolean> getDailyUpdate() {
        return dailyUpdate;
    }

    public void setDailyUpdate(Optional<Boolean> dailyUpdate) {
        this.dailyUpdate = dailyUpdate;
    }
}
