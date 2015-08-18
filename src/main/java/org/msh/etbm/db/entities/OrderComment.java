package org.msh.etbm.db.entities;


import org.msh.etbm.db.enums.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "ordercomment")
public class OrderComment {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name="ORDER_ID")
	@NotNull
	private Order order;
	
	@Lob
	private String comment;
	
	@ManyToOne
	@JoinColumn(name="USER_CREATOR_ID")
	@NotNull
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date date;
	
	private OrderStatus statusOnComment;

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
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
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
	 * @return the statusOnComment
	 */
	public OrderStatus getStatusOnComment() {
		return statusOnComment;
	}

	/**
	 * @param statusOnComment the statusOnComment to set
	 */
	public void setStatusOnComment(OrderStatus statusOnComment) {
		this.statusOnComment = statusOnComment;
	}

}
