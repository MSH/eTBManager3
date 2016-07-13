package org.msh.etbm.db.entities;

import org.msh.etbm.db.CaseEntity;
import org.msh.etbm.db.enums.PrevTBTreatmentOutcome;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "prevtbtreatment")
public class PrevTBTreatment extends CaseEntity {

    @Column(name = "TREATMENT_MONTH")
    private Integer month;

    @Column(name = "TREATMENT_YEAR")
    private int year;

    @Column(name = "OUTCOME_MONTH")
    private Integer outcomeMonth;

    @Column(name = "OUTCOME_YEAR")
    private Integer outcomeYear;

    @NotNull
    private PrevTBTreatmentOutcome outcome;

    private boolean Am;
    private boolean Cfz;
    private boolean Cm;
    private boolean Cs;
    private boolean E;
    private boolean Eto;
    private boolean H;
    private boolean Lfx;
    private boolean Ofx;
    private boolean R;
    private boolean S;
    private boolean Z;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public PrevTBTreatmentOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(PrevTBTreatmentOutcome outcome) {
        this.outcome = outcome;
    }

    public Integer getOutcomeMonth() {
        return outcomeMonth;
    }

    public void setOutcomeMonth(Integer outcomeMonth) {
        this.outcomeMonth = outcomeMonth;
    }

    public Integer getOutcomeYear() {
        return outcomeYear;
    }

    public void setOutcomeYear(Integer outcomeYear) {
        this.outcomeYear = outcomeYear;
    }

    public boolean hasOutcomeDate() {
        return getOutcomeMonth() != null && getOutcomeYear() != null;
    }

    public boolean isAm() {
        return Am;
    }

    public void setAm(boolean am) {
        Am = am;
    }

    public boolean isCfz() {
        return Cfz;
    }

    public void setCfz(boolean cfz) {
        Cfz = cfz;
    }

    public boolean isCm() {
        return Cm;
    }

    public void setCm(boolean cm) {
        Cm = cm;
    }

    public boolean isCs() {
        return Cs;
    }

    public void setCs(boolean cs) {
        Cs = cs;
    }

    public boolean isE() {
        return E;
    }

    public void setE(boolean e) {
        E = e;
    }

    public boolean isEto() {
        return Eto;
    }

    public void setEto(boolean eto) {
        Eto = eto;
    }

    public boolean isH() {
        return H;
    }

    public void setH(boolean h) {
        H = h;
    }

    public boolean isLfx() {
        return Lfx;
    }

    public void setLfx(boolean lfx) {
        Lfx = lfx;
    }

    public boolean isOfx() {
        return Ofx;
    }

    public void setOfx(boolean ofx) {
        Ofx = ofx;
    }

    public boolean isR() {
        return R;
    }

    public void setR(boolean r) {
        R = r;
    }

    public boolean isS() {
        return S;
    }

    public void setS(boolean s) {
        S = s;
    }

    public boolean isZ() {
        return Z;
    }

    public void setZ(boolean z) {
        Z = z;
    }
}
