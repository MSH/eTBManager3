package org.msh.etbm.services.cases.followup.examdst;

import org.msh.etbm.db.enums.CultureResult;
import org.msh.etbm.db.enums.DstResult;
import org.msh.etbm.db.enums.SampleType;
import org.msh.etbm.services.cases.followup.data.LaboratoryExamFormData;

import java.util.Optional;

/**
 * Created by msantos on 15/7/16.
 */
public class ExamDSTFormData extends LaboratoryExamFormData {

    private Optional<DstResult> resultAm;
    private Optional<DstResult> resultCfz;
    private Optional<DstResult> resultCm;
    private Optional<DstResult> resultCs;
    private Optional<DstResult> resultE;
    private Optional<DstResult> resultEto;
    private Optional<DstResult> resultH;
    private Optional<DstResult> resultLfx;
    private Optional<DstResult> resultOfx;
    private Optional<DstResult> resultR;
    private Optional<DstResult> resultS;
    private Optional<DstResult> resultZ;
    private Optional<String> method;

    public Optional<DstResult> getResultAm() {
        return resultAm;
    }

    public void setResultAm(Optional<DstResult> resultAm) {
        this.resultAm = resultAm;
    }

    public Optional<DstResult> getResultCfz() {
        return resultCfz;
    }

    public void setResultCfz(Optional<DstResult> resultCfz) {
        this.resultCfz = resultCfz;
    }

    public Optional<DstResult> getResultCm() {
        return resultCm;
    }

    public void setResultCm(Optional<DstResult> resultCm) {
        this.resultCm = resultCm;
    }

    public Optional<DstResult> getResultCs() {
        return resultCs;
    }

    public void setResultCs(Optional<DstResult> resultCs) {
        this.resultCs = resultCs;
    }

    public Optional<DstResult> getResultE() {
        return resultE;
    }

    public void setResultE(Optional<DstResult> resultE) {
        this.resultE = resultE;
    }

    public Optional<DstResult> getResultEto() {
        return resultEto;
    }

    public void setResultEto(Optional<DstResult> resultEto) {
        this.resultEto = resultEto;
    }

    public Optional<DstResult> getResultH() {
        return resultH;
    }

    public void setResultH(Optional<DstResult> resultH) {
        this.resultH = resultH;
    }

    public Optional<DstResult> getResultLfx() {
        return resultLfx;
    }

    public void setResultLfx(Optional<DstResult> resultLfx) {
        this.resultLfx = resultLfx;
    }

    public Optional<DstResult> getResultOfx() {
        return resultOfx;
    }

    public void setResultOfx(Optional<DstResult> resultOfx) {
        this.resultOfx = resultOfx;
    }

    public Optional<DstResult> getResultR() {
        return resultR;
    }

    public void setResultR(Optional<DstResult> resultR) {
        this.resultR = resultR;
    }

    public Optional<DstResult> getResultS() {
        return resultS;
    }

    public void setResultS(Optional<DstResult> resultS) {
        this.resultS = resultS;
    }

    public Optional<DstResult> getResultZ() {
        return resultZ;
    }

    public void setResultZ(Optional<DstResult> resultZ) {
        this.resultZ = resultZ;
    }

    public Optional<String> getMethod() {
        return method;
    }

    public void setMethod(Optional<String> method) {
        this.method = method;
    }

}
