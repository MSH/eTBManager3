package org.msh.etbm.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Store information about an available quantity of medicines from one specific batch in a TB unit
 * @author Ricardo Mem�ria
 *
 */
@Entity
@Table(name = "batchquantity")
public class BatchQuantity implements Serializable {
	private static final long serialVersionUID = -5948846203987021296L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="UNIT_ID")
	@NotNull
	private Tbunit tbunit;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="SOURCE_ID")
	@NotNull
	private Source source;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="BATCH_ID")
	@NotNull
	private Batch batch;

	private int quantity;

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
	 * @return the tbunit
	 */
	public Tbunit getTbunit() {
		return tbunit;
	}
	/**
	 * @param tbunit the tbunit to set
	 */
	public void setTbunit(Tbunit tbunit) {
		this.tbunit = tbunit;
	}
	/**
	 * @return the batch
	 */
	public Batch getBatch() {
		return batch;
	}
	/**
	 * @param batch the batch to set
	 */
	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * @return the source
	 */
	public Source getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(Source source) {
		this.source = source;
	}
	
	public double getTotalPrice() {
		Batch batch = getBatch();
		return (batch != null? batch.getUnitPrice() * (double)quantity: 0);
	}
	
	public float getContainerPrice() {
		return (batch != null? batch.getUnitPrice()*batch.getQuantityContainer().floatValue():0);
	}
	public void setContainerPrice(float containerPrice) {
		return;
	}

}
