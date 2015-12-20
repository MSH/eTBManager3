package org.msh.etbm.services.admin.admunits.parents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.msh.etbm.commons.SynchronizableItem;

/**
 * Store information about an administrative unit and all its parents.
 * It starts from the first level (p0) and goes up to the lowest level, that depends on the
 * reference level
 * Created by rmemoria on 16/12/15.
 */
public class AdminUnitSeries {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p3;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p4;

    /**
     * Return the administrative unit represented by the series, i.e, the
     * unit in the lowest level
     * @return the administrative unit id and name
     */
    @JsonIgnore
    public SynchronizableItem getAdminUnit() {
        if (p4 != null) {
            return p4;
        }

        if (p3 != null) {
            return p3;
        }

        if (p2 != null) {
            return p2;
        }

        if (p1 != null) {
            return p1;
        }

        return p0;
    }

    /**
     * Return the level of the administrative unit being referenced.
     * @return an integer value from 0 (no admin unit) until 5 (deepest level)
     */
    @JsonIgnore
    public int getLevel() {
        int level = 5;
        return 5 - (p4 == null? 1: 0) -
                (p3 == null? 1: 0) -
                (p2 == null? 1: 0) -
                (p1 == null? 1: 0) -
                (p0 == null? 1: 0);
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

    public SynchronizableItem getP4() {
        return p4;
    }

    public void setP4(SynchronizableItem p4) {
        this.p4 = p4;
    }
}
