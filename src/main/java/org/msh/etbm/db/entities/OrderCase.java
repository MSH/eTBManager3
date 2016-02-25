package org.msh.etbm.db.entities;


import org.msh.etbm.db.Synchronizable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ordercase")
public class OrderCase extends Synchronizable {


	@ManyToOne
	@JoinColumn(name = "CASE_ID")
	@NotNull
	private TbCase tbcase;
	
	@ManyToOne
	@JoinColumn(name = "ORDERITEM_ID")
	@NotNull
	private OrderItem item;
	
	private int estimatedQuantity;


	public TbCase getTbcase() {
		return tbcase;
	}

	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}

	public OrderItem getItem() {
		return item;
	}

	public void setItem(OrderItem item) {
		this.item = item;
	}

	public int getEstimatedQuantity() {
		return estimatedQuantity;
	}

	public void setEstimatedQuantity(int estimatedQuantity) {
		this.estimatedQuantity = estimatedQuantity;
	}
}
