package org.msh.etbm.db.entities;

import org.msh.etbm.db.CaseEntity;
import org.msh.etbm.db.enums.PrevTBTreatmentOutcome;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "prevtbtreatment")
public class PrevTBTreatment extends CaseEntity {

    @Column(name = "TREATMENT_MONTH")
    private Integer month;

    @Column(name = "TREATMENT_YEAR")
    private Integer year;

    @Column(name = "OUTCOME_MONTH")
    private Integer outcomeMonth;

    @Column(name = "OUTCOME_YEAR")
    private Integer outcomeYear;

    @NotNull
    private PrevTBTreatmentOutcome outcome;

    private boolean am;
    private boolean cfz;
    private boolean cm;
    private boolean cs;
    private boolean e;
    private boolean eto;
    private boolean h;
    private boolean lfx;
    private boolean ofx;
    private boolean r;
    private boolean s;
    private boolean z;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public PrevTBTreatmentOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(PrevTBTreatmentOutcome outcome) {
        this.outcome = outcome;
    }

    public boolean isAm() {
        return am;
    }

    public void setAm(boolean am) {
        this.am = am;
    }

    public boolean isCfz() {
        return cfz;
    }

    public void setCfz(boolean cfz) {
        this.cfz = cfz;
    }

    public boolean isCm() {
        return cm;
    }

    public void setCm(boolean cm) {
        this.cm = cm;
    }

    public boolean isCs() {
        return cs;
    }

    public void setCs(boolean cs) {
        this.cs = cs;
    }

    public boolean isE() {
        return e;
    }

    public void setE(boolean e) {
        this.e = e;
    }

    public boolean isEto() {
        return eto;
    }

    public void setEto(boolean eto) {
        this.eto = eto;
    }

    public boolean isH() {
        return h;
    }

    public void setH(boolean h) {
        this.h = h;
    }

    public boolean isLfx() {
        return lfx;
    }

    public void setLfx(boolean lfx) {
        this.lfx = lfx;
    }

    public boolean isOfx() {
        return ofx;
    }

    public void setOfx(boolean ofx) {
        this.ofx = ofx;
    }

    public boolean isR() {
        return r;
    }

    public void setR(boolean r) {
        this.r = r;
    }

    public boolean isS() {
        return s;
    }

    public void setS(boolean s) {
        this.s = s;
    }

    public boolean isZ() {
        return z;
    }

    public void setZ(boolean z) {
        this.z = z;
    }
}
