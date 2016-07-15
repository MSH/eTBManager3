package org.msh.etbm.db.entities;

import org.msh.etbm.db.Synchronizable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "batchdispensing")
public class BatchDispensing extends Synchronizable {


    @ManyToOne
    @JoinColumn(name = "DISPENSING_ID")
    private MedicineDispensing dispensing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BATCH_ID")
    @NotNull
    private Batch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOURCE_ID")
    @NotNull
    private Source source;

    private int quantity;


    /**
     * @return the dispensing
     */
    public MedicineDispensing getDispensing() {
        return dispensing;
    }

    /**
     * @param dispensing the dispensing to set
     */
    public void setDispensing(MedicineDispensing dispensing) {
        this.dispensing = dispensing;
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


}
