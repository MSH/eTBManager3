/**
 * 
 */
package org.msh.etbm.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Store information about an issue created, optionally, to a TB case.
 * An issue is a question that is posted, and it may contain several
 * follow-ups. Each follow-up is a user that collaborates to answer
 * the question
 * 
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="issue")
public class Issue  {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="case_id")
	private TbCase tbcase;

	private boolean closed;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date answerDate;

	@Column(length=200)
	private String title;

	@Lob
	private String description;
	
	@OneToMany(mappedBy="issue")
	private List<IssueFollowup> followups = new ArrayList<IssueFollowup>();
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="unit_id")
	@NotNull
	private Tbunit unit;

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * @return the tbcase
	 */
	public TbCase getTbcase() {
		return tbcase;
	}

	/**
	 * @param tbcase the tbcase to set
	 */
	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}

	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * @param closed the closed to set
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the answerDate
	 */
	public Date getAnswerDate() {
		return answerDate;
	}

	/**
	 * @param answerDate the answerDate to set
	 */
	public void setAnswerDate(Date answerDate) {
		this.answerDate = answerDate;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the followups
	 */
	public List<IssueFollowup> getFollowups() {
		return followups;
	}

	/**
	 * @param followups the followups to set
	 */
	public void setFollowups(List<IssueFollowup> followups) {
		this.followups = followups;
	}

	/**
	 * @return the unit
	 */
	public Tbunit getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Tbunit unit) {
		this.unit = unit;
	}
}
