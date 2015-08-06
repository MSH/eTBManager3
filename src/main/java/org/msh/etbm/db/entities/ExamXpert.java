package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.enums.XpertResult;
import org.msh.etbm.db.enums.XpertRifResult;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("gen")
@Table(name="examxpert")
public class ExamXpert extends LaboratoryExam implements Serializable {
	private static final long serialVersionUID = 7672681749376963359L;

    @PropertyLog(operations={Operation.ALL})
	private XpertResult result;
	
	private XpertRifResult rifResult;

    @Override
    public ExamResult getExamResult() {
        if (result == null) {
            return ExamResult.UNDEFINED;
        }

        return result == XpertResult.TB_DETECTED? ExamResult.POSITIVE: ExamResult.NEGATIVE;
    }

	public XpertResult getResult() {
		return result;
	}

	public void setResult(XpertResult result) {
		this.result = result;
	}

	/**
	 * @return the rifResult
	 */
	public XpertRifResult getRifResult() {
		return rifResult;
	}

	/**
	 * @param rifResult the rifResult to set
	 */
	public void setRifResult(XpertRifResult rifResult) {
		this.rifResult = rifResult;
	}
}
