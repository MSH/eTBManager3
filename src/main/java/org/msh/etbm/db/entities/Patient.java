package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.enums.Gender;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Store information of patient personal information
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="patient")
public class Patient extends WorkspaceEntity {

	@Column(length=100)
	@NotNull
	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private String name;
	
	@Column(length=100)
	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private String middleName;
	
	@Column(length=100)
	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private String lastName;
	
	@Column(length=50)
	private String securityNumber;
	
	@Column(length=100)
	private String motherName;
	
	@Temporal(TemporalType.DATE)
	@PropertyLog(operations={Operation.NEW})
	private Date birthDate;

	private Integer recordNumber;

	@NotNull
	@PropertyLog(operations={Operation.NEW})
	private Gender gender;
	
	@Column(length=50)
	@PropertyLog(messageKey="form.customId")
	private String customId;
	
	@OneToMany(mappedBy="patient")
	private List<TbCase> cases = new ArrayList<TbCase>();

	@Column(length=100)
	private String fatherName;
	


/*
	public String getFullName() {
		Workspace ws;
		if (getWorkspace() != null)
			 ws = getWorkspace();
		else ws = (Workspace)Component.getInstance("defaultWorkspace", true);
		return compoundName(ws);
	}
*/

/*
	public String compoundName(Workspace ws) {
		NameComposition comp = ws.getPatientNameComposition();

		String result;
		switch (comp) {
		case FIRST_MIDDLE_LASTNAME:
			   result = (name != null? name: "") + (middleName != null? " " + middleName: "") + (lastName != null? " " + lastName: "");
			   break;

		case FULLNAME:
			result = name;
			break;
		
		case FIRSTSURNAME:
			result = (name != null? name: "") + (lastName != null? ", " + lastName: "");
			break;
			
		case LAST_FIRST_MIDDLENAME:
			result = (lastName != null? lastName + ", ": "") + (name != null? name: "") + ((middleName != null) && (!middleName.isEmpty())? ", " + middleName: "");
			break;
		case LAST_FIRST_MIDDLENAME_WITHOUT_COMMAS:
			result = (lastName != null? lastName + " ": "") + (name != null? name: "") + ((middleName != null) && (!middleName.isEmpty())? " " + middleName: "");
			break;
			
		case SURNAME_FIRSTNAME:
			result = (middleName != null? middleName + ", ":"") + (name != null? name: "");
			break;
		default:
		   result = (name != null? name: "") + (middleName != null? " " + middleName: "") + (lastName != null? " " + lastName: "");
		}
		
		return result.trim();
	}
*/

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getSecurityNumber() {
		return securityNumber;
	}

	public void setSecurityNumber(String securityNumber) {
		this.securityNumber = securityNumber;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public Integer getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    /**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the cases
	 */
	public List<TbCase> getCases() {
		return cases;
	}

	/**
	 * @param cases the cases to set
	 */
	public void setCases(List<TbCase> cases) {
		this.cases = cases;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

    @Override
    public String getDisplayString() {
        return name +
                (middleName != null && !middleName.isEmpty()? " " + middleName + " ": "") +
                (lastName != null && !lastName.isEmpty()? " " + lastName + " ": "");
    }
}
