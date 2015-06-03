package org.msh.etbm.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Stores information about the stock of medicine in a TB Unit in a specific date
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="stockposition")
public class StockPosition implements Serializable {
	private static final long serialVersionUID = -8474281715312681147L;
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="MEDICINE_ID")
	@NotNull
	private Medicine medicine;
	
	@ManyToOne
	@JoinColumn(name="UNIT_ID")
	@NotNull
	private Tbunit tbunit;
	
	@ManyToOne
	@JoinColumn(name="SOURCE_ID")
	@NotNull
	private Source source;

	private int quantity;
	private float totalPrice;
	
	// Average Monthly Consumption
	private Integer amc;
	
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date lastMovement;


	/**
	 * Return the unit price of the medicine
	 * @return
	 */
	public float getUnitPrice() {
		return (quantity != 0? totalPrice / quantity: 0);
	}


	/**
	 * Returns the id of the stock position object
	 * @return unique id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Changes the id of the stock position object
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Returns the medicine of the stock position
	 * @return instance of Medicine class
	 */
	public Medicine getMedicine() {
		return medicine;
	}

	/**
	 * Changes the medicine property 
	 * @param medicine
	 */
	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	/**
	 * Returns the quantity of the medicine at the moment in the stock position
	 * @return quantity of medicine
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Changes the quantity of medicine
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Returns the source of medicine
	 * @return instance of the Source class
	 */
	public Source getSource() {
		return source;
	}

	/**
	 * Changes the source 
	 * @param source to change
	 */
	public void setSource(Source source) {
		this.source = source;
	}

	/**
	 * Returns the TB unit of the stock position information
	 * @return instance of Tbunit class
	 */
	public Tbunit getTbunit() {
		return tbunit;
	}

	/**
	 * Changes the TB unit
	 * @param tbunit to be changed
	 */
	public void setTbunit(Tbunit tbunit) {
		this.tbunit = tbunit;
	}

	/**
	 * Changes the total price of the stock position information
	 * @return the totalPrice
	 */
	public float getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}


	/**
	 * @return the amc
	 */
	public Integer getAmc() {
		return amc;
	}


	/**
	 * @param amc the amc to set
	 */
	public void setAmc(Integer amc) {
		this.amc = amc;
	}


	/**
	 * @return the lastMovement
	 */
	public Date getLastMovement() {
		return lastMovement;
	}


	/**
	 * @param lastMovement the lastMovement to set
	 */
	public void setLastMovement(Date lastMovement) {
		this.lastMovement = lastMovement;
	}
}
