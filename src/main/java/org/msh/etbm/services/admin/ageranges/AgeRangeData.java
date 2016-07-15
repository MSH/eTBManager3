package org.msh.etbm.services.admin.ageranges;

import java.util.UUID;

/**
 * Age range data returned to the client from the service layer
 * <p>
 * Created by rmemoria on 6/1/16.
 */
public class AgeRangeData {

    private UUID id;
    private String name;
    private int iniAge;
    private int endAge;

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

    public int getIniAge() {
        return iniAge;
    }

    public void setIniAge(int iniAge) {
        this.iniAge = iniAge;
    }

    public int getEndAge() {
        return endAge;
    }

    public void setEndAge(int endAge) {
        this.endAge = endAge;
    }
}
