package org.msh.etbm.db.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name="medicinedispensingcase")
public class MedicineDispensingCase {

	@Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "uuid2", strategy = "uuid2", parameters = { @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	private UUID id;

	@ManyToOne
	@JoinColumn(name="DISPENSING_ID")
	@NotNull
	private MedicineDispensing dispensing;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="CASE_ID")
	@NotNull
	private TbCase tbcase;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="SOURCE_ID")
	@NotNull
	private Source source;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="BATCH_ID")
	@NotNull
	private Batch batch;
	
	@NotNull
	private int quantity;



	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

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
	 * @return the tbcase
	 */
	public TbCase getTbcase() {
		return tbcase;
	}

	/**
	 * @param tbcase the tbcase to set
	 */
	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
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
