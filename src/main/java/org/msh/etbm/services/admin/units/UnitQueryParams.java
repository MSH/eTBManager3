package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

import java.util.UUID;

/**
 * Specific criterias to query units in the system
 *
 * Created by rmemoria on 28/10/15.
 */
public class UnitQueryParams extends EntityQueryParams {

    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_DETAILED = "detailed";
    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_ADMINUNIT = "admunit";


    private UnitType type;

    /**
     * Search for the exact name
     */
    private String name;

    /**
     * Search for part of the name (using LIKE)
     */
    private String key;

    /**
     * If true, will include non-active units
     */
    private boolean includeDisabled;

    private UUID adminUnitId;
    /**
     * If true and adminiUnitId is not null, it will also include all direct and indirect child units
     */
    private boolean includeSubunits;

    /** used when filtering TB units **/
    private Boolean tbFacility;
    private Boolean mdrFacility;
    private Boolean ntmFacility;
    private Boolean notificationUnit;

    /** used when filtering laboratories **/
    private Boolean performCulture;
    private Boolean performMicroscopy;
    private Boolean performDst;
    private Boolean performXpert;

    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

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

    public UUID getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(UUID adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public Boolean getTbFacility() {
        return tbFacility;
    }

    public void setTbFacility(Boolean tbFacility) {
        this.tbFacility = tbFacility;
    }

    public Boolean getMdrFacility() {
        return mdrFacility;
    }

    public void setMdrFacility(Boolean mdrFacility) {
        this.mdrFacility = mdrFacility;
    }

    public Boolean getNtmFacility() {
        return ntmFacility;
    }

    public void setNtmFacility(Boolean ntmFacility) {
        this.ntmFacility = ntmFacility;
    }

    public Boolean getNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(Boolean notificationUnit) {
        this.notificationUnit = notificationUnit;
    }

    public Boolean getPerformCulture() {
        return performCulture;
    }

    public void setPerformCulture(Boolean performCulture) {
        this.performCulture = performCulture;
    }

    public Boolean getPerformMicroscopy() {
        return performMicroscopy;
    }

    public void setPerformMicroscopy(Boolean performMicroscopy) {
        this.performMicroscopy = performMicroscopy;
    }

    public Boolean getPerformDst() {
        return performDst;
    }

    public void setPerformDst(Boolean performDst) {
        this.performDst = performDst;
    }

    public Boolean getPerformXpert() {
        return performXpert;
    }

    public void setPerformXpert(Boolean performXpert) {
        this.performXpert = performXpert;
    }

    public boolean isIncludeSubunits() {
        return includeSubunits;
    }

    public void setIncludeSubunits(boolean includeSubunits) {
        this.includeSubunits = includeSubunits;
    }

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }
}
