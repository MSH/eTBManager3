package org.msh.etbm.services.admin.admunits.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.msh.etbm.commons.SynchronizableItem;

import java.util.UUID;

/**
 * Created by rmemoria on 21/10/15.
 */
public class AdminUnitData extends AdminUnitItemData {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p3;

    private SynchronizableItem countryStructure;


    /**
     * Return the level of the administrative unit being referenced.
     *
     * @return an integer value from 0 (no admin unit) until 5 (deepest level)
     */
    @JsonIgnore
    public int getLevel() {
        return 4 - (p3 == null ? 1 : 0)
                - (p2 == null ? 1 : 0)
                - (p1 == null ? 1 : 0)
                - (p0 == null ? 1 : 0);
    }

    public SynchronizableItem getItemAtLevel(int reqLevel) {
        int level = getLevel();
        if (reqLevel > level) {
            return null;
        }

        if (reqLevel == level) {
            return new SynchronizableItem(getId(), getName());
        }

        switch (reqLevel) {
            case 0: return p0;
            case 1: return p1;
            case 2: return p2;
            case 3: return p3;
            default: return null;
        }
    }

    public SynchronizableItem getCountryStructure() {
        return countryStructure;
    }

    public void setCountryStructure(SynchronizableItem countryStructure) {
        this.countryStructure = countryStructure;
    }

    public SynchronizableItem getP0() {
        return p0;
    }

    public void setP0(SynchronizableItem p0) {
        this.p0 = p0;
    }

    public SynchronizableItem getP1() {
        return p1;
    }

    public void setP1(SynchronizableItem p1) {
        this.p1 = p1;
    }

    public SynchronizableItem getP2() {
        return p2;
    }

    public void setP2(SynchronizableItem p2) {
        this.p2 = p2;
    }

    public SynchronizableItem getP3() {
        return p3;
    }

    public void setP3(SynchronizableItem p3) {
        this.p3 = p3;
    }
}
