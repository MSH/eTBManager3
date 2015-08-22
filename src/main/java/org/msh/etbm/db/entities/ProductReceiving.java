package org.msh.etbm.db.entities;

import org.msh.etbm.commons.date.DateUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Inheritance(strategy= InheritanceType.JOINED)
@Table(name="productreceiving")
public class ProductReceiving {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;

	@ManyToOne
	@JoinColumn(name="UNIT_ID")
	@NotNull
	private Tbunit tbunit;
	
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date receivingDate;
	
	@ManyToOne
	@JoinColumn(name="SOURCE_ID")
	@NotNull
	private Source source;

	@Lob
	private String comments;
	
	private double totalPrice;
	
	@ManyToMany
	@JoinTable(name="movements_receiving",
			joinColumns={@JoinColumn(name="RECEIVING_ID")},
			inverseJoinColumns={@JoinColumn(name="MOVEMENT_ID")})
	private List<Movement> movements = new ArrayList<Movement>();

	
	/**
	 * Search for a movement by its medicine
	 * @param prod the product to find the movement from
	 * @return
	 */
	public Movement movementByProduct(Product prod) {
		for (Movement mov: movements) {
			if (mov.getProduct().equals(prod))
				return mov;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (source != null? source.toString() + " - " + DateUtils.formatDate(receivingDate, "dd-MMM-yyyy"): super.toString());
	}

	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Date getReceivingDate() {
		return receivingDate;
	}

	public void setReceivingDate(Date receivingDate) {
		this.receivingDate = receivingDate;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Tbunit getTbunit() {
		return tbunit;
	}

	public void setTbunit(Tbunit tbunit) {
		this.tbunit = tbunit;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the movements
	 */
	public List<Movement> getMovements() {
		return movements;
	}

	/**
	 * @param movements the movements to set
	 */
	public void setMovements(List<Movement> movements) {
		this.movements = movements;
	}

	/**
	 * @return the totalPrice
	 */
	public double getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
