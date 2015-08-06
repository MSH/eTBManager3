package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.enums.MicroscopyResult;
import org.msh.etbm.db.enums.SampleType;
import org.msh.etbm.db.enums.VisualAppearance;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="exammicroscopy")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("gen")
public class ExamMicroscopy extends LaboratoryExam implements Serializable {
	private static final long serialVersionUID = 1514632458011926044L;

	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private MicroscopyResult result;
	
	private Integer numberOfAFB;

	private SampleType sampleType;

    private String otherSampleType;

    private VisualAppearance visualAppearance;

    @Override
    public ExamResult getExamResult() {
        if (result == null) {
            return ExamResult.UNDEFINED;
        }
        if (result.isPositive()) {
            return ExamResult.POSITIVE;
        }

        if (result.isNegative()) {
            return ExamResult.NEGATIVE;
        }
        return ExamResult.UNDEFINED;
    }

    public MicroscopyResult getResult() {
		return result;
	}

	public void setResult(MicroscopyResult result) {
		this.result = result;
	}

	/**
	 * @param numberOfAFB the numberOfAFB to set
	 */
	public void setNumberOfAFB(Integer numberOfAFB) {
		this.numberOfAFB = numberOfAFB;
	}

	/**
	 * @return the numberOfAFB
	 */
	public Integer getNumberOfAFB() {
		return numberOfAFB;
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

    public String getOtherSampleType() {
        return otherSampleType;
    }

    public void setOtherSampleType(String otherSampleType) {
        this.otherSampleType = otherSampleType;
    }

    public VisualAppearance getVisualAppearance() {
        return visualAppearance;
    }

    public void setVisualAppearance(VisualAppearance visualAppearance) {
        this.visualAppearance = visualAppearance;
    }
}
