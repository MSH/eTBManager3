package org.msh.etbm.db.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "transferbatch")
public class TransferBatch {

	@Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "uuid2", strategy = "uuid2", parameters = { @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "BATCH_ID")
	@NotNull
	private Batch batch;
	
	@ManyToOne
	@JoinColumn(name = "TRANSFERITEM_ID")
	@NotNull
	private TransferItem transferItem;

	private int quantity;

	private Integer quantityReceived;

	public float getUnitPrice() {
		return (getBatch() == null? 0: (float)batch.getUnitPrice());
	}
	
	public float getTotalPrice() {
		return (getBatch() == null? 0: quantity * (float)batch.getUnitPrice());
	}
	
	public float getTotalPriceReceived() {
		return (getBatch() == null? 0: quantityReceived * (float)batch.getUnitPrice());
	}
	
	public Integer getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(Integer quantityReceived) {
		this.quantityReceived = quantityReceived;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
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
