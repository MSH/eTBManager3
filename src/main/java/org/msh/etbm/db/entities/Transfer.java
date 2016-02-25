package org.msh.etbm.db.entities;

import org.hibernate.annotations.GenericGenerator;
import org.msh.etbm.db.enums.TransferStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy =  InheritanceType.JOINED)
@Table(name = "transfer")
public class Transfer  {

	@Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "uuid2", strategy = "uuid2", parameters = { @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	private UUID id;

	@Temporal(TemporalType.DATE)
	@NotNull
	private Date shippingDate;
	
	@Temporal(TemporalType.DATE)
	private Date receivingDate;

	@NotNull
	private TransferStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNIT_ID_FROM")
	@NotNull
	private Tbunit unitFrom;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNIT_ID_TO")
	@NotNull
	private Tbunit unitTo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_FROM_ID")
	@NotNull
	private User userFrom;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_TO_ID")
	private User userTo;

	@Lob
	private String commentsFrom;
	
	@Lob
	private String commentsTo;
	
	@Column(length = 200)
	private String cancelReason;
	
	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name = "TRANSFER_ID")
	private List<TransferItem> items = new ArrayList<TransferItem>();

	private String consignmentNumber;
	
	@Override
	public String toString() {
		return (id != null? id.toString() : super.toString());
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public Date getReceivingDate() {
		return receivingDate;
	}

	public void setReceivingDate(Date receivingDate) {
		this.receivingDate = receivingDate;
	}

	public Tbunit getUnitFrom() {
		return unitFrom;
	}

	public void setUnitFrom(Tbunit unitFrom) {
		this.unitFrom = unitFrom;
	}

	public Tbunit getUnitTo() {
		return unitTo;
	}

	public void setUnitTo(Tbunit unitTo) {
		this.unitTo = unitTo;
	}

	public String getCommentsFrom() {
		return commentsFrom;
	}

	public void setCommentsFrom(String commentsFrom) {
		this.commentsFrom = commentsFrom;
	}

	public String getCommentsTo() {
		return commentsTo;
	}

	public void setCommentsTo(String commentsTo) {
		this.commentsTo = commentsTo;
	}

	public List<TransferItem> getItems() {
		return items;
	}

	public void setItems(List<TransferItem> items) {
		this.items = items;
	}

	public TransferStatus getStatus() {
		return status;
	}

	public void setStatus(TransferStatus status) {
		this.status = status;
	}

	public User getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(User userFrom) {
		this.userFrom = userFrom;
	}

	public User getUserTo() {
		return userTo;
	}

	public void setUserTo(User userTo) {
		this.userTo = userTo;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public void setConsignmentNumber(String consignmentNumber) {
		this.consignmentNumber = consignmentNumber;
	}

	public String getConsignmentNumber() {
		return consignmentNumber;
	}
}
