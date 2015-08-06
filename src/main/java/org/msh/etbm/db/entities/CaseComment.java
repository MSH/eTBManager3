package org.msh.etbm.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * Store comments about a case
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name = "casecomment")
public class CaseComment implements Serializable {
	private static final long serialVersionUID = 4605350882229307203L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="CASE_ID")
	@NotNull
	private TbCase tbcase;

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

/*
	@NotNull
	private CaseView view;
*/


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
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
/*
	public CaseView getView() {
		return view;
	}
*/

	/**
	 * @param view the view to set
	 */
/*
	public void setView(CaseView view) {
		this.view = view;
	}
*/


}
