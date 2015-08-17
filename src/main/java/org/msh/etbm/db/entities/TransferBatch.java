package org.msh.etbm.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="transferbatch")
public class TransferBatch implements Serializable {
	private static final long serialVersionUID = -3578501257933133181L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	@NotNull
	private Batch batch;
	
	@ManyToOne
	@JoinColumn(name="TRANSFERITEM_ID")
	@NotNull
	private TransferItem transferItem;

	private int quantity;

	private Integer quantityReceived;

	public float getUnitPrice() {
		return (getBatch() == null? 0: batch.getUnitPrice());
	}
	
	public float getTotalPrice() {
		return (getBatch() == null? 0: quantity * batch.getUnitPrice());
	}
	
	public float getTotalPriceReceived() {
		return (getBatch() == null? 0: quantityReceived * batch.getUnitPrice());
	}
	
	public Integer getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(Integer quantityReceived) {
		this.quantityReceived = quantityReceived;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public TransferItem getTransferItem() {
		return transferItem;
	}

	public void setTransferItem(TransferItem transferItem) {
		this.transferItem = transferItem;
	}

	public void setNumContainersRec(int numContainersRec){
		return;
	}
}
