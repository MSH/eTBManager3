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
    private SynchronizableItem p1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p3;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p4;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SynchronizableItem p5;

    /**
     * Return the administrative unit represented by the series, i.e, the
     * unit in the lowest level
     * @return the administrative unit id and name
     */
    @JsonIgnore
    public SynchronizableItem getSelected() {
        if (p5 != null) {
            return p5;
        }

        if (p4 != null) {
            return p4;
        }

        if (p3 != null) {
            return p3;
        }

        if (p2 != null) {
            return p2;
        }

        return p1;
    }

    /**
     * Return the administrative unit by the given level
     * @param level the administrative level, from 1 to 5
     * @return the administrative unit info at the given level, or null if no information found or invalid level
     */
    @JsonIgnore
    public SynchronizableItem getAdminUnitLevel(int level) {
        int itemPos = getLevel() - level;
        switch (itemPos) {
            case 0: return p1;
            case 1: return p2;
            case 2: return p3;
            case 3: return p4;
            case 4: return p5;
            default: return null;
        }
    }

    /**
     * Return the level of the administrative unit being referenced.
     * @return an integer value from 0 (no admin unit) until 5 (deepest level)
     */
    @JsonIgnore
    public int getLevel() {
        int level = 5;
        return 5 - (p5 == null ? 1 : 0) -
                (p4 == null ? 1 : 0) -
                (p3 == null ? 1 : 0) -
                (p2 == null ? 1 : 0) -
                (p1 == null ? 1 : 0);
    }

    public SynchronizableItem getP5() {
        return p5;
    }

    public void setP5(SynchronizableItem p5) {
        this.p5 = p5;
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
