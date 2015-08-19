package org.msh.etbm.db.entities;


import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.db.WSObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "batch")
public class Batch extends WSObject {

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date expiryDate;
	
	@Column(length=30)
	@NotNull
	private String batchNumber;
	
	@Column(length=80)
	private String manufacturer;
	
	@ManyToOne
	@JoinColumn(name="MEDICINE_ID")
	@NotNull
	private Medicine medicine;
	
	private int quantityReceived;

	private double unitPrice;
	

	/**
	 * Check if batch is 
	 * @return
	 */
	public boolean isExpired() {
		return (expiryDate != null) && (expiryDate.before(DateUtils.getDate()));
	}


	@Override
	public String toString() {
		return (batchNumber != null? batchNumber: super.toString());
	}


	/**
	 * Copy data from another batch. 
	 * @param
	 */
/*	public void copyFromBatch(Batch b) {
		batchNumber = b.getBatchNumber();
		container = b.getContainer();
		expiryDate = b.getExpiryDate();
		manufacturer = b.getManufacturer();
		medicine = b.getMedicine();
		quantityReceived = b.getQuantityReceived();
		quantityContainer = b.getQuantityContainer();
		unitPrice = b.getUnitPrice();
	}
*/
	
//	/**
//	 * Returns the number of containers based on the quantity and the quantity per box
//	 * @return
//	 */
//	public int getNumContainers() {
//		return (quantityContainer > 0)? (int) Math.ceil((double) quantityReceived / (double) quantityContainer): 0;
//	}
	
//	public void setNumContainers(int value) {
//		if (quantityContainer==0)
//			quantityContainer = (value != 0? quantityReceived/value: 0);
//	}
	
	public void setContainerPrice(float containerPrice) {
		return;
	}

//	public float getContainerPrice() {
//		return unitPrice*quantityContainer;
//	}
	
	
//	public Integer getQuantityContainer() {
//		return quantityContainer;
//	}
//
//	public void setQuantityContainer(Integer quantityContainer) {
//		this.quantityContainer = quantityContainer;
//	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public double getUnitPrice() {
		return unitPrice;
	}
	
	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public double getTotalPrice() {
		double tot = (double)(unitPrice*(double)quantityReceived);
		return tot;
	}

	public void setTotalPrice(double totalPrice) {
		if (quantityReceived != 0)
			unitPrice = (float)(totalPrice / quantityReceived);
		else
		if (unitPrice != 0)
			quantityReceived = Math.round((float) (totalPrice / unitPrice));
	}

	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @param quantityReceived the quantityReceived to set
	 */
	public void setQuantityReceived(int quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	/**
	 * @return the quantityReceived
	 */
	public int getQuantityReceived() {
		return quantityReceived;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

}
