package org.msh.etbm.db.entities;

import org.hibernate.validator.constraints.Length;
import org.msh.etbm.db.enums.MovementType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Stores information about a medicine transaction
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="movement")
public class Movement {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;

	@Temporal(TemporalType.DATE)
	@Column(name="mov_date")
	@NotNull
	private Date date;
	
	private MovementType type;
	
	@ManyToOne
	@JoinColumn(name="PRODUCT_ID")
	@NotNull
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="UNIT_ID")
	@NotNull
	private Tbunit tbunit;
	
	@ManyToOne
	@JoinColumn(name="SOURCE_ID")
	@NotNull
	private Source source;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date recordDate;

	@Length(max=250)
	private String comment;
	
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="movement")
	private List<BatchMovement> batches = new ArrayList<BatchMovement>();

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="ADJUSTMENT_ID")
	private FieldValue adjustmentType;


    /**
     * The available quantity in the inventory
     */
    private int availableQuantity;

    /**
     * The total price of the inventory
     */
    private float totalPriceInventory;

    private boolean header;

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public float getTotalPriceInventory() {
        return totalPriceInventory;
    }

    public void setTotalPriceInventory(float totalPriceInventory) {
        this.totalPriceInventory = totalPriceInventory;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    /**
	 * Returns adjustment type of the transaction
	 * @return a FieldValue representing the adjustment type
	 */
	public FieldValue getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(FieldValue adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	
	/**
	 * Returns the remark of the transaction
	 * @return String containing the remarks of the transaction
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Changes the remark of the transaction
	 * @param comment - the new remark of the transaction
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Returns the date of the transaction
	 * @return instance of the Date class
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Changes the date of the transaction
	 * @param date to be changed
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Returns the unique id of the transaction
	 * @return Integer instance representing the id of the transaction
	 */
	public UUID getId() {
		return id;
	}

	
	/**
	 * Changes the id of the transaction
	 * @param id to be changed
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Returns the source of the transaction
	 * @return instance of the Source class 
	 */
	public Source getSource() {
		return source;
	}

	/**
	 * Changes the source of the transaction
	 * @param source - the new source of the transaction
	 */
	public void setSource(Source source) {
		this.source = source;
	}

	/**
	 * Returns the TB unit of the transaction
	 * @return instance of Tbunit class
	 */
	public Tbunit getTbunit() {
		return tbunit;
	}

	/**
	 * Changes the TB unit of the transaction
	 * @param tbunit - the new TB Unit
	 */
	public void setTbunit(Tbunit tbunit) {
		this.tbunit = tbunit;
	}

	/**
	 * Returns the type of the transaction
	 * @return MovementType instance
	 */
	public MovementType getType() {
		return type;
	}

	/**
	 * Changes the type of the transaction
	 * @param type - new type of movement
	 */
	public void setType(MovementType type) {
		this.type = type;
	}

	/**
	 * Returns the date and time that the transaction was generated
	 * @return Date instance
	 */
	public Date getRecordDate() {
		return recordDate;
	}

	/**
	 * Changes the date and time the record was generated
	 * @param recordDate - the new record date
	 */
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	/**
	 * Returns the batches movements involved in this transaction
	 * @return the list of instances of BachMovement class 
	 */
	public List<BatchMovement> getBatches() {
		return batches;
	}

	/**
	 * Changes the batches involved in the transaction
	 * @param batches - the list of BachMovement objects of the transaction
	 */
	public void setBatches(List<BatchMovement> batches) {
		this.batches = batches;
	}

	public boolean isAdjustment() {
		if(this.type != null)
			return type.equals(MovementType.ADJUSTMENT);
		else
			return false;
	}
	
	public boolean isDispensing() {
		if(this.type != null)
			return type.equals(MovementType.DISPENSING);
		else
			return false;
	}

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
