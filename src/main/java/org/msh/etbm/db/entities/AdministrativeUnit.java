package org.msh.etbm.db.entities;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "administrativeunit")
public class AdministrativeUnit extends WorkspaceEntity {

    @PropertyLog(messageKey = "form.name", operations = {Operation.NEW, Operation.DELETE})
    @NotNull
    private String name;

    /**
     * A sequence with parent ids
     */
    @PropertyLog(ignore = true)
    private UUID pid0;
    @PropertyLog(ignore = true)
    private UUID pid1;
    @PropertyLog(ignore = true)
    private UUID pid2;
    @PropertyLog(ignore = true)
    private UUID pid3;

    /**
     * The name of the parents
     */
    private String pname0;
    private String pname1;
    private String pname2;
    private String pname3;

    @Column(length = 50)
    @PropertyLog(messageKey = "form.customId")
    private String customId;

    // properties to help dealing with trees
    private int unitsCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRYSTRUCTURE_ID")
    @PropertyLog(operations = {Operation.ALL})
    @NotNull
    private CountryStructure countryStructure;


    /**
     * Return the display name of the administrative unit concatenated with its parent units
     *
     * @return
     */
    public String getFullDisplayName() {
        String s = getName();

        String delim = "";
        if (pname3 != null) {
            s += delim + pname3;
            delim = ", ";
        }

        if (pname2 != null) {
            s += delim + pname2;
            delim = ", ";
        }

        if (pname1 != null) {
            s += delim + pname1;
            delim = ", ";
        }

        if (pname0 != null) {
            s += delim + pname0;
        }

        return s;
    }


    /**
     * Return the direct administrative unit parent ID
     * @return instance of UUID, or null if there is no parent
     */
    public UUID getParentId() {
        if (pid3 != null) {
            return pid3;
        }

        if (pid2 != null) {
            return pid2;
        }

        if (pid1 != null) {
            return pid1;
        }

        return pid0;
    }

    public void setParent(AdministrativeUnit parent) {
        pid0 = parent.getPid0();
        pid1 = parent.getPid1();
        pid2 = parent.getPid2();
        pid3 = parent.getPid3();
        pname0 = parent.getPname0();
        pname1 = parent.getPname1();
        pname2 = parent.getPname2();
        pname3 = parent.getPname3();

        switch (parent.getLevel()) {
            case 0:
                pid0 = parent.getId();
                pname0 = parent.getPname0();
                break;
            case 1:
                pid1 = parent.getPid1();
                pname1 = parent.getPname1();
                break;
            case 2:
                pid2 = parent.getPid2();
                pname2 = parent.getPname2();
                break;
            case 3:
                pid3 = parent.getPid3();
                pname3 = parent.getPname3();
                break;
        }
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

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public String toString() {
        return getFullDisplayName();
    }

    /**
     * @return the unitsCount
     */
    public int getUnitsCount() {
        return unitsCount;
    }

    /**
     * @param unitsCount the unitsCount to set
     */
    public void setUnitsCount(int unitsCount) {
        this.unitsCount = unitsCount;
    }

    /**
     * @return the countryStructure
     */
    public CountryStructure getCountryStructure() {
        return countryStructure;
    }

    /**
     * @param countryStructure the countryStructure to set
     */
    public void setCountryStructure(CountryStructure countryStructure) {
        this.countryStructure = countryStructure;
    }

    /**
     * Zero-based index indicating the administrative unit level, calculated from
     * its parent units. Level is a value from 0 to 4, where 0 means the administrative unit
     * has no parent and 4 means it has 4 parents
     * @return integer value
     */
    public int getLevel() {
        int level = 4;
        if (pid3 == null) {
            level--;
        }

        if (pid2 == null) {
            level--;
        }

        if (pid1 == null) {
            level--;
        }

        if (pid0 == null) {
            level--;
        }

        return level;
    }


    /**
     * Return the parent ID by its level
     * @param level the parent ID level, between 0 and the admin unit level
     * @return
     */
    public UUID getParentIdByLevel(int level) {
        switch (level) {
            case 0: return pid0;
            case 1: return pid1;
            case 2: return pid2;
            case 3: return pid3;
            default: return null;
        }
    }

    /**
     * Return the parent name by its level
     * @param level the parent ID level, between 0 and the admin unit level
     * @return String value
     */
    public String getParentNameByLevel(int level) {
        switch (level) {
            case 0: return pname0;
            case 1: return pname1;
            case 2: return pname2;
            case 3: return pname3;
            default: return null;
        }
    }

    /**
     * Create a list with all parent elements. The list is
     * @param includeItself if true, the list will contain the own unit
     * @return
     */
    public List<SynchronizableItem> getParentsList(boolean includeItself) {
        List<SynchronizableItem> lst = new ArrayList<>();

        if (pid0 != null) {
            lst.add(new SynchronizableItem(pid0, pname0));
        }

        if (pid1 != null) {
            lst.add(new SynchronizableItem(pid1, pname1));
        }

        if (pid2 != null) {
            lst.add(new SynchronizableItem(pid2, pname2));
        }

        if (pid3 != null) {
            lst.add(new SynchronizableItem(pid3, pname3));
        }

        if (includeItself) {
            lst.add(new SynchronizableItem(getId(), getName()));
        }

        return lst;
    }

    @Override
    public String getDisplayString() {
        return name;
    }

    public UUID getPid0() {
        return pid0;
    }

    public void setPid0(UUID pid0) {
        this.pid0 = pid0;
    }

    public UUID getPid1() {
        return pid1;
    }

    public void setPid1(UUID pid1) {
        this.pid1 = pid1;
    }

    public UUID getPid2() {
        return pid2;
    }

    public void setPid2(UUID pid2) {
        this.pid2 = pid2;
    }

    public UUID getPid3() {
        return pid3;
    }

    public void setPid3(UUID pid3) {
        this.pid3 = pid3;
    }

    public String getPname0() {
        return pname0;
    }

    public void setPname0(String pname0) {
        this.pname0 = pname0;
    }

    public String getPname1() {
        return pname1;
    }

    public void setPname1(String pname1) {
        this.pname1 = pname1;
    }

    public String getPname2() {
        return pname2;
    }

    public void setPname2(String pname2) {
        this.pname2 = pname2;
    }

    public String getPname3() {
        return pname3;
    }

    public void setPname3(String pname3) {
        this.pname3 = pname3;
    }
}
