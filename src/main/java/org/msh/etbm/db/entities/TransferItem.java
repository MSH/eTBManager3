package org.msh.etbm.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name="transferitem")
public class TransferItem {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;

	@ManyToOne
	@JoinColumn(name="TRANSFER_ID",nullable=false)
	@NotNull
	private Transfer transfer;
	
	@ManyToOne
	@JoinColumn(name="SOURCE_ID",nullable=false)
	@NotNull
	private Source source;
	
	@ManyToOne
	@JoinColumn(name="product_id",nullable=false)
	@NotNull
	private Product product;

	@ManyToOne
	@JoinColumn(name="MOV_OUT_ID")
	private Movement movementOut;
	
	@ManyToOne
	@JoinColumn(name="MOV_IN_ID")
	private Movement movementIn;
	
	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="TRANSFERITEM_ID")
	private List<TransferBatch> batches = new ArrayList<TransferBatch>();
	
	@Transient
	private Object data;

	/**
	 * Search for a specific batch 
	 * @param b
	 * @return
	 */
	public TransferBatch findByBatch(Batch b) {
		for (TransferBatch tb: getBatches()) {
			if (tb.getBatch().equals(b))
				return tb;
		}
		
		return null;
	}
	
	public int getQuantity() {
		Integer val = 0;
		for (TransferBatch b: getBatches()) {
			val += b.getQuantity();
		}
		return val;
	}
	
	public int getQuantityReceived() {
		Integer val = 0;
		for (TransferBatch b: getBatches()) {
			if (b.getQuantityReceived() != null)
				val += b.getQuantityReceived();
		}
		return val;
	}
	
	public float getTotalPrice() {
		float val = 0;
		for (TransferBatch b: getBatches()) {
			val += b.getTotalPrice();
		}
		return val;
	}
	
	public float getTotalPriceReceived() {
		float val = 0;
		for (TransferBatch b: getBatches()) {
			val += b.getTotalPriceReceived();
		}
		return val;
	}
	
	public float getUnitPrice() {
		int val = getQuantity();
		if (val == 0)
			 return 0;
		else return getTotalPrice() / val;
	}

	public Movement getMovementOut() {
		return movementOut;
	}

	public void setMovementOut(Movement movementOut) {
		this.movementOut = movementOut;
	}

	public Movement getMovementIn() {
		return movementIn;
	}

	public void setMovementIn(Movement movementIn) {
		this.movementIn = movementIn;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public List<TransferBatch> getBatches() {
		return batches;
	}

	public void setBatches(List<TransferBatch> batches) {
		this.batches = batches;
	}

	public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
