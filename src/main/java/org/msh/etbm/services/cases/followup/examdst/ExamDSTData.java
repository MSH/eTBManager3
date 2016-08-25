package org.msh.etbm.services.cases.followup.examdst;

import org.msh.etbm.db.enums.DstResult;
import org.msh.etbm.services.cases.followup.data.LaboratoryExamData;

/**
 * Created by msantos on 15/7/16.
 */
public class ExamDSTData extends LaboratoryExamData {

    private DstResult resultAm;
    private DstResult resultCfz;
    private DstResult resultCm;
    private DstResult resultCs;
    private DstResult resultE;
    private DstResult resultEto;
    private DstResult resultH;
    private DstResult resultLfx;
    private DstResult resultOfx;
    private DstResult resultR;
    private DstResult resultS;
    private DstResult resultZ;
    private String method;

    public DstResult getResultAm() {
        return resultAm;
    }

    public void setResultAm(DstResult resultAm) {
        this.resultAm = resultAm;
    }

    public DstResult getResultCfz() {
        return resultCfz;
    }

    public void setResultCfz(DstResult resultCfz) {
        this.resultCfz = resultCfz;
    }

    public DstResult getResultCm() {
        return resultCm;
    }

    public void setResultCm(DstResult resultCm) {
        this.resultCm = resultCm;
    }

    public DstResult getResultCs() {
        return resultCs;
    }

    public void setResultCs(DstResult resultCs) {
        this.resultCs = resultCs;
    }

    public DstResult getResultE() {
        return resultE;
    }

    public void setResultE(DstResult resultE) {
        this.resultE = resultE;
    }

    public DstResult getResultEto() {
        return resultEto;
    }

    public void setResultEto(DstResult resultEto) {
        this.resultEto = resultEto;
    }

    public DstResult getResultH() {
        return resultH;
    }

    public void setResultH(DstResult resultH) {
        this.resultH = resultH;
    }

    public DstResult getResultLfx() {
        return resultLfx;
    }

    public void setResultLfx(DstResult resultLfx) {
        this.resultLfx = resultLfx;
    }

    public DstResult getResultOfx() {
        return resultOfx;
    }

    public void setResultOfx(DstResult resultOfx) {
        this.resultOfx = resultOfx;
    }

    public DstResult getResultR() {
        return resultR;
    }

    public void setResultR(DstResult resultR) {
        this.resultR = resultR;
    }

    public DstResult getResultS() {
        return resultS;
    }

    public void setResultS(DstResult resultS) {
        this.resultS = resultS;
    }

    public DstResult getResultZ() {
        return resultZ;
    }

    public void setResultZ(DstResult resultZ) {
        this.resultZ = resultZ;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
