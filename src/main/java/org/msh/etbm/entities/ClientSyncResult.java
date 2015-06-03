/**
 * 
 */
package org.msh.etbm.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="clientsyncresult")
public class ClientSyncResult {

	@Id
	@Column(length=32)
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date syncStart;

	@Temporal(TemporalType.TIMESTAMP)
	private Date syncEnd;
	
	@Column(length=250)
	private String errorMessage;

	@Column(length=100)
	private String answerFileName;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the syncStart
	 */
	public Date getSyncStart() {
		return syncStart;
	}

	/**
	 * @param syncStart the syncStart to set
	 */
	public void setSyncStart(Date syncStart) {
		this.syncStart = syncStart;
	}

	/**
	 * @return the syncEnd
	 */
	public Date getSyncEnd() {
		return syncEnd;
	}

	/**
	 * @param syncEnd the syncEnd to set
	 */
	public void setSyncEnd(Date syncEnd) {
		this.syncEnd = syncEnd;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	/**
	 * @return the answerFileName
	 */
	public String getAnswerFileName() {
		return answerFileName;
	}

	/**
	 * @param answerFileName the answerFileName to set
	 */
	public void setAnswerFileName(String answerFileName) {
		this.answerFileName = answerFileName;
	}
}
