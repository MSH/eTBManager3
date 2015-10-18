package org.msh.etbm.db.entities;


import org.msh.etbm.db.Synchronizable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="orderbatch")
public class OrderBatch extends Synchronizable {

	@ManyToOne
	@JoinColumn(name="ORDERITEM_ID")
	@NotNull
	private OrderItem orderItem;
	
	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	@NotNull
	private Batch batch;

	private int quantity;
	private Integer receivedQuantity;

	@Transient
	public float getTotalPrice() {
		float qtd = (receivedQuantity != null? receivedQuantity: quantity);
		return (batch != null? (float)batch.getUnitPrice() * qtd: 0);
	}
	
	public Integer getReceivedQuantity() {
		return receivedQuantity;
	}
	public void setReceivedQuantity(Integer receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
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

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}
	

	public void setNumContainersRec(int numContainersRec){
		return;
	}
}
