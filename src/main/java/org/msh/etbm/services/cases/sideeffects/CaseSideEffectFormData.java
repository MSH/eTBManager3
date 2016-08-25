package org.msh.etbm.services.cases.sideeffects;

import org.msh.etbm.services.cases.CaseEntityFormData;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Mauricio on 12/08/2016.
 */
public class CaseSideEffectFormData extends CaseEntityFormData {

    private Optional<String> sideEffect;

    private Optional<Integer> month;

    private Optional<UUID> substanceId;

    private Optional<UUID> substance2Id;

    private String comment;

    public Optional<String> getSideEffect() {
        return sideEffect;
    }

    public void setSideEffect(Optional<String> sideEffect) {
        this.sideEffect = sideEffect;
    }

    public Optional<Integer> getMonth() {
        return month;
    }

    public void setMonth(Optional<Integer> month) {
        this.month = month;
    }

    public Optional<UUID> getSubstanceId() {
        return substanceId;
    }

    public void setSubstanceId(Optional<UUID> substanceId) {
        this.substanceId = substanceId;
    }

    public Optional<UUID> getSubstance2Id() {
        return substance2Id;
    }

    public void setSubstance2Id(Optional<UUID> substance2Id) {
        this.substance2Id = substance2Id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
