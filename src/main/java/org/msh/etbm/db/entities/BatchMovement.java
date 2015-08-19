package org.msh.etbm.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "batchmovement")
public class BatchMovement implements Serializable {
	private static final long serialVersionUID = -2778240504031969875L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;

	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	@NotNull
	private Batch batch;
	
	private int quantity;

    private int availableQuantity;

    @ManyToOne
    @JoinColumn(name="MOVEMENT_ID")
    @NotNull
    private Movement movement;

    private boolean header;

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

	public int getQtdOperation() {
		return getQuantity() * movement.getType().getOper();
	}
	
	public float getTotalPrice() {
		return (getBatch( )!= null? quantity * (float)batch.getUnitPrice(): 0);
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

	public Movement getMovement() {
		return movement;
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
	}
}
