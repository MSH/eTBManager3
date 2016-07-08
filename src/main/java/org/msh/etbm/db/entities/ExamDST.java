package org.msh.etbm.db.entities;

import org.msh.etbm.db.enums.DstResult;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;

@Entity
@Table(name = "examdst")
public class ExamDST extends LaboratoryExam {

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

    @Column(length = 50)
    private String method;

    @Transient
    private HashMap<String, DstResult> results;

    @Override
    public ExamResult getExamResult() {
        boolean allNull = true;

        for (String key : getResults().keySet()) {
            if (getResults().get(key) != null) {
                allNull = false;
                break;
            }

            if (getResults().get(key) != null && getResults().get(key).equals(DstResult.RESISTANT)) {
                return ExamResult.POSITIVE;
            }
        }

        if (allNull) {
            return ExamResult.UNDEFINED;
        }

        return ExamResult.NEGATIVE;
    }
    
    public HashMap<String, DstResult> getResults() {
        if (this.results == null) {
            this.results = new HashMap<>();
            results.put("Am", this.resultAm);
            results.put("Cfz", this.resultCfz);
            results.put("Cm", this.resultCm);
            results.put("Cs", this.resultCs);
            results.put("E", this.resultE);
            results.put("Eto", this.resultEto);
            results.put("H", this.resultH);
            results.put("Lfx", this.resultLfx);
            results.put("Ofx", this.resultOfx);
            results.put("R", this.resultR);
            results.put("S", this.resultS);
            results.put("Z", this.resultZ);
        }
        return this.results;
    }

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
