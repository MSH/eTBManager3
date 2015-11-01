package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.entities.query.EntityQuery;

import java.util.UUID;

/**
 * Specific criterias to query units in the system
 *
 * Created by rmemoria on 28/10/15.
 */
public class UnitQuery extends EntityQuery {

    private UnitType type;
    private String name;
    private String key;

    private UUID adminUnitId;

    /** used when filtering TB units **/
    private boolean tbUnit;
    private boolean mdrUnit;
    private boolean ntmUnit;
    private boolean notificationUnit;

    /** used when filtering laboratories **/
    private boolean performCulture;
    private boolean performMicroscopy;
    private boolean performDst;
    private boolean performXpert;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public UUID getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(UUID adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public boolean isTbUnit() {
        return tbUnit;
    }

    public void setTbUnit(boolean tbUnit) {
        this.tbUnit = tbUnit;
    }

    public boolean isMdrUnit() {
        return mdrUnit;
    }

    public void setMdrUnit(boolean mdrUnit) {
        this.mdrUnit = mdrUnit;
    }

    public boolean isNtmUnit() {
        return ntmUnit;
    }

    public void setNtmUnit(boolean ntmUnit) {
        this.ntmUnit = ntmUnit;
    }

    public boolean isNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(boolean notificationUnit) {
        this.notificationUnit = notificationUnit;
    }

    public boolean isPerformCulture() {
        return performCulture;
    }

    public void setPerformCulture(boolean performCulture) {
        this.performCulture = performCulture;
    }

    public boolean isPerformMicroscopy() {
        return performMicroscopy;
    }

    public void setPerformMicroscopy(boolean performMicroscopy) {
        this.performMicroscopy = performMicroscopy;
    }

    public boolean isPerformDst() {
        return performDst;
    }

    public void setPerformDst(boolean performDst) {
        this.performDst = performDst;
    }

    public boolean isPerformXpert() {
        return performXpert;
    }

    public void setPerformXpert(boolean performXpert) {
        this.performXpert = performXpert;
    }
}
