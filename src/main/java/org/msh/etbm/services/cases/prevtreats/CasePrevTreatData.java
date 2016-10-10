package org.msh.etbm.services.cases.prevtreats;

import org.msh.etbm.commons.Item;

import java.util.UUID;

/**
 * Created by Mauricio on 18/08/2016.
 */
public class CasePrevTreatData {
    private UUID id;
    private Integer month;
    private Integer year;
    private Integer outcomeMonth;
    private Integer outcomeYear;
    private String outcome;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
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
