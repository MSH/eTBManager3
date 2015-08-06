package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.enums.CultureResult;
import org.msh.etbm.db.enums.SampleType;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="examculture")
public class ExamCulture extends LaboratoryExam implements Serializable {
	private static final long serialVersionUID = 1470269499087718301L;

	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private CultureResult result;

	@PropertyLog(operations={Operation.NEW})
	private Integer numberOfColonies;

	private SampleType sampleType;

    @Override
    public ExamResult getExamResult() {
        if (result == null) {
            return ExamResult.UNDEFINED;
        }

        if (result.isNegative()) {
            return ExamResult.NEGATIVE;
        }

        if (result.isPositive()) {
            return ExamResult.POSITIVE;
        }

        return ExamResult.UNDEFINED;
    }

    public CultureResult getResult() {
		return result;
	}

	public void setResult(CultureResult result) {
		this.result = result;
	}

	/**
	 * @return the numberOfColonies
	 */
	public Integer getNumberOfColonies() {
		return numberOfColonies;
	}

	/**
	 * @param numberOfColonies the numberOfColonies to set
	 */
	public void setNumberOfColonies(Integer numberOfColonies) {
		this.numberOfColonies = numberOfColonies;
	}


	/**
	 * @return the sampleType
	 */
	public SampleType getSampleType() {
		return sampleType;
	}

	/**
	 * @param sampleType the sampleType to set
	 */
	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
	}
}
