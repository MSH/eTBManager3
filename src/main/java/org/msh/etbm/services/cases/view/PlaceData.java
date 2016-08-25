package org.msh.etbm.services.cases.view;

import java.util.UUID;

/**
 * Created by rmemoria on 17/6/16.
 */
public class PlaceData {

    public enum PlaceType {
        ADMINUNIT,
        UNIT;
    }

    private UUID id;
    private String name;
    private PlaceType type;
    private boolean hasChildren;
    private long suspectCount;
    private long tbCount;
    private long drtbCount;
    private long ntmCount;

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

    public PlaceType getType() {
        return type;
    }

    public void setType(PlaceType type) {
        this.type = type;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public long getSuspectCount() {
        return suspectCount;
    }

    public void setSuspectCount(long suspectCount) {
        this.suspectCount = suspectCount;
    }

    public long getTbCount() {
        return tbCount;
    }

    public void setTbCount(long tbCount) {
        this.tbCount = tbCount;
    }

    public long getDrtbCount() {
        return drtbCount;
    }

    public void setDrtbCount(long drtbCount) {
        this.drtbCount = drtbCount;
    }

    public long getNtmCount() {
        return ntmCount;
    }

    public void setNtmCount(long ntmCount) {
        this.ntmCount = ntmCount;
    }
}
