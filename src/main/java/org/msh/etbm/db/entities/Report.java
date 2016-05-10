package org.msh.etbm.db.entities;

import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Store data about a report in the data analysis tool
 * 
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name = "report")
public class Report extends WorkspaceEntity {

	/**
	 * The title of the report, displayed to the user
	 */
	@Column(length = 200)
	@NotNull
    private String title;
	
	/**
	 * If true, the report will also be available to other users
	 */
	private boolean published;
	
	/**
	 * When the report was created
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDate;
	
	/**
	 * The user that is the owner of the report, i.e, just the user can change or delete the report
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_ID")
	private User owner;
	
	/**
	 * If true, the report will be displayed in the dashboard
	 */
	private boolean dashboard;
	
	/**
	 * JSON data containing the variables, filters and other information about the report
	 */
	@Lob
	private String data;

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
	 * @return the published
	 */
	public boolean isPublished() {
		return published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(boolean published) {
		this.published = published;
	}

	/**
	 * @return the registrationDate
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * @return the dashboard
	 */
	public boolean isDashboard() {
		return dashboard;
	}

	/**
	 * @param dashboard the dashboard to set
	 */
	public void setDashboard(boolean dashboard) {
		this.dashboard = dashboard;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

    @Override
    public String getDisplayString() {
        return title;
    }
}
