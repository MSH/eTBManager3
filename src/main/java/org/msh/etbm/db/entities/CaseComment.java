package org.msh.etbm.db.entities;

import org.msh.etbm.db.CaseData;
import org.msh.etbm.db.enums.CaseView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * Store comments about a case
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name = "casecomment")
public class CaseComment extends CaseData {

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	@NotNull
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="comment_date")
	@NotNull
	private Date date;
	
	@Lob
	@NotNull
	private String comment;

	@NotNull
	private CaseView view;


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
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the view
	 */
    public CaseView getView() {
		return view;
	}


	/**
	 * @param view the view to set
	 */
    public void setView(CaseView view) {
		this.view = view;
	}



}
