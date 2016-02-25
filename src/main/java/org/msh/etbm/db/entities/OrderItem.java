package org.msh.etbm.db.entities;


import org.msh.etbm.db.Synchronizable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Store data about the medicines ordered in an order
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name = "orderitem")
public class OrderItem extends Synchronizable {

	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "ORDER_ID")
	@NotNull
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	@NotNull
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "SOURCE_ID")
	@NotNull
	private Source source;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOVEMENT_IN_ID")
	private Movement MovementIn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOVEMENT_OUT_ID")
	private Movement MovementOut;
	
	private int estimatedQuantity;
	private int requestedQuantity;
	private Integer approvedQuantity;
	private Integer shippedQuantity;
	private Integer receivedQuantity;
	
	private Integer stockQuantity;
	
	private Integer numPatients;
	
	@Column(length = 200)
	private String comment;

	@OneToMany(mappedBy = "orderItem", cascade = {CascadeType.ALL})
	private List<OrderBatch> batches = new ArrayList<OrderBatch>();

	@OneToMany(mappedBy = "item", cascade = {CascadeType.ALL})
	private List<OrderCase> cases = new ArrayList<OrderCase>();

	/**
	 * Used internally to check if item is validated
	 */
	@Transient
	private Object data;

	
	/**
	 * Return the quantity to be shipped. If approved quantity is entered, it's returned, otherwise
	 * the requested quantity is
	 * @return
	 */
	public Integer getQuantityToShip() {
		return (approvedQuantity != null ? approvedQuantity : requestedQuantity);
	}


	/**
	 * Return the total price of the medicine to be shipped
	 * @return
	 */
	public float getTotalPrice() {
		float total = 0;
		for (OrderBatch b: batches) {
			total += b.getBatch().getUnitPrice() * b.getQuantity();
		}
		
		return total;
	}


	/**
	 * Get the unit price
	 * @return
	 */
	public float getUnitPrice() {
		float total = 0;
		float qtd = 0;
		for (OrderBatch b: batches) {
			int aux = (b.getReceivedQuantity() != null ? b.getReceivedQuantity() : b.getQuantity());
			qtd += aux;
			total += b.getBatch().getUnitPrice() * aux;
		}
		
		return qtd > 0 ? total / qtd : 0;
	}


	/**
	 * Add a new case that uses the medicine in the order item
	 * @param c
	 * @param estimatedQtd
	 */
	public void addCase(TbCase c, int estimatedQtd) {
		for (OrderCase oc: cases) {
			if (oc.getTbcase().equals(c)) {
				oc.setEstimatedQuantity( oc.getEstimatedQuantity() + estimatedQtd );
				return;
			}
		}

		OrderCase oc = new OrderCase();
		oc.setTbcase(c);
		oc.setItem(this);
		oc.setEstimatedQuantity(estimatedQtd);

		cases.add(oc);
		numPatients = cases.size();
	}

	
	/**
	 * Search for an instance of {@link OrderBatch} of this item by its {@link Batch}
	 * @param batch Batch to be used to search for an instance of {@link OrderBatch} class
	 * @return object {@link OrderBatch} of this item, or null if no OrderBatch found
	 */
	public OrderBatch findOrderBatchByBatch(Batch batch) {
		for (OrderBatch ob: batches) {
			if (ob.getBatch().equals(batch)) {
                return ob;
            }
		}
		return null;
	}
	
	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

    public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public int getRequestedQuantity() {
		return requestedQuantity;
	}

	public void setRequestedQuantity(int requestedQuantity) {
		this.requestedQuantity = requestedQuantity;
	}

    public Integer getReceivedQuantity() {
		return receivedQuantity;
	}

    public void setReceivedQuantity(Integer receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

    public Source getSource() {
		return source;
	}

    public void setSource(Source source) {
		this.source = source;
	}

    public Movement getMovementIn() {
		return MovementIn;
	}

    public void setMovementIn(Movement movementIn) {
		MovementIn = movementIn;
	}

    public Movement getMovementOut() {
		return MovementOut;
	}

    public void setMovementOut(Movement movementOut) {
		MovementOut = movementOut;
	}

    public List<OrderBatch> getBatches() {
		return batches;
	}

    public void setBatches(List<OrderBatch> batches) {
		this.batches = batches;
	}

    public Order getOrder() {
		return order;
	}

    public void setOrder(Order order) {
		this.order = order;
	}

    public int getEstimatedQuantity() {
		return estimatedQuantity;
	}

    public void setEstimatedQuantity(int estimatedQuantity) {
		this.estimatedQuantity = estimatedQuantity;
	}

    public Integer getShippedQuantity() {
		return shippedQuantity;
	}

    public void setShippedQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

    public String getComment() {
		return comment;
	}

    public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getNumPatients() {
		return numPatients;
	}

	public void setNumPatients(Integer numPatients) {
		this.numPatients = numPatients;
	}

	public List<OrderCase> getCases() {
		return cases;
	}

	public void setCases(List<OrderCase> cases) {
		this.cases = cases;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


	/**
	 * @return the stockQuantity
	 */
	public Integer getStockQuantity() {
		return stockQuantity;
	}

	/**
	 * @param stockQuantity the stockQuantity to set
	 */
	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
