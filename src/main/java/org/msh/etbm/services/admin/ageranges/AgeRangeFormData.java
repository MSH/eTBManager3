package org.msh.etbm.services.admin.ageranges;

import java.util.Optional;

/**
 * Created by msantos on 22/03/16.
 */
public class AgeRangeFormData {
    private Optional<Integer> iniAge;
    private Optional<Integer> endAge;

    public Optional<Integer> getIniAge() {
        return iniAge;
    }

    public void setIniAge(Optional<Integer> iniAge) {
        this.iniAge = iniAge;
    }

    public Optional<Integer> getEndAge() {
        return endAge;
    }

    public void setEndAge(Optional<Integer> endAge) {
        this.endAge = endAge;
    }
}
