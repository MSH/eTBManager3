package org.msh.etbm.services.init.demodata;

import org.msh.etbm.services.admin.substances.SubstanceData;
import org.msh.etbm.services.admin.tags.TagData;
import org.msh.etbm.services.init.demodata.data.CaseDemoData;
import org.msh.etbm.services.init.demodata.data.MedicineDemoData;
import org.msh.etbm.services.init.demodata.data.RegimenDemoData;

import java.util.List;

/**
 * Template class that will be filled by the json file located in the resources folder
 * Created by Mauricio on 19/10/2016.
 */
public class DemonstrationDataTemplate {

    List<SubstanceData> substances;

    List<MedicineDemoData> medicines;

    List<RegimenDemoData> regimens;

    List<CaseDemoData> tbcases;

    List<TagData> tags;

    public List<SubstanceData> getSubstances() {
        return substances;
    }

    public void setSubstances(List<SubstanceData> substances) {
        this.substances = substances;
    }

    public List<MedicineDemoData> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<MedicineDemoData> medicines) {
        this.medicines = medicines;
    }

    public List<RegimenDemoData> getRegimens() {
        return regimens;
    }

    public void setRegimens(List<RegimenDemoData> regimens) {
        this.regimens = regimens;
    }

    public List<CaseDemoData> getTbcases() {
        return tbcases;
    }

    public void setTbcases(List<CaseDemoData> tbcases) {
        this.tbcases = tbcases;
    }

    public List<TagData> getTags() {
        return tags;
    }

    public void setTags(List<TagData> tags) {
        this.tags = tags;
    }
}
