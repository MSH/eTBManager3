package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.enums.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="productorder")
public class Order extends WorkspaceEntity {

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date orderDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date approvingDate;
	
	@Temporal(TemporalType.DATE)
	private Date shippingDate;

	@Temporal(TemporalType.DATE)
	private Date receivingDate;

	private OrderStatus status;
	
	private Integer numDays;
	
	@ManyToOne
	@JoinColumn(name="UNIT_FROM_ID")
	@NotNull
	private Tbunit unitFrom;

	@ManyToOne
	@JoinColumn(name="UNIT_TO_ID")
	@NotNull
	private Tbunit unitTo;
	
	@Column(length=200)
	private String cancelReason;
	
	@ManyToOne
	@JoinColumn(name="USER_CREATOR_ID")
	@NotNull
	private User userCreator;

	@ManyToOne
	@JoinColumn(name="AUTHORIZER_UNIT_ID")
	private Tbunit authorizer;
	
	@OneToMany(mappedBy="order", cascade={CascadeType.ALL})
	private List<OrderItem> items = new ArrayList<OrderItem>();

	@Column(length=50)
	@PropertyLog(messageKey="form.customId")
	private String customId;
	
	@Column(length=200)
	private String shipAddress;

	@Column(length=200)
	private String shipAddressCont;

	@Column(length=200)
	private String shipContactName;

	@Column(length=200)
	private String shipContactPhone;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="SHIP_ADMINUNIT_ID")
	private AdministrativeUnit shipAdminUnit;
	
	@Column(length=50)
	private String shipZipCode;
	
	@Column(length=200)
	private String shipInstitutionName;
	

	@Transient
	public float getTotalPrice() {
		float tot = 0;
		for (OrderItem it: items) {
			tot += it.getTotalPrice();
		}
		return tot;
	}


	public boolean isHasShipAddress() {
		return (!checkEmpty(shipAddress)) || (!checkEmpty(shipZipCode));
	}
	
	private boolean checkEmpty(String s) {
		return (s == null) || (s.isEmpty());
	}
	
	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getReceivingDate() {
		return receivingDate;
	}

	public void setReceivingDate(Date receivingDate) {
		this.receivingDate = receivingDate;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public Date getApprovingDate() {
		return approvingDate;
	}

	public void setApprovingDate(Date approvingDate) {
		this.approvingDate = approvingDate;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
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

	public Integer getNumDays() {
		return numDays;
	}

	public void setNumDays(Integer numDays) {
		this.numDays = numDays;
	}

	public User getUserCreator() {
		return userCreator;
	}

	public void setUserCreator(User userCreator) {
		this.userCreator = userCreator;
	}

	public Tbunit getAuthorizer() {
		return authorizer;
	}

	public void setAuthorizer(Tbunit authorizer) {
		this.authorizer = authorizer;
	}

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    /**
	 * @return the shipAddress
	 */
	public String getShipAddress() {
		return shipAddress;
	}

	/**
	 * @param shipAddress the shipAddress to set
	 */
	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	/**
	 * @return the shipAddressCont
	 */
	public String getShipAddressCont() {
		return shipAddressCont;
	}

	/**
	 * @param shipAddressCont the shipAddressCont to set
	 */
	public void setShipAddressCont(String shipAddressCont) {
		this.shipAddressCont = shipAddressCont;
	}

	/**
	 * @return the shipContactName
	 */
	public String getShipContactName() {
		return shipContactName;
	}

	/**
	 * @param shipContactName the shipContactName to set
	 */
	public void setShipContactName(String shipContactName) {
		this.shipContactName = shipContactName;
	}

	/**
	 * @return the shipContactPhone
	 */
	public String getShipContactPhone() {
		return shipContactPhone;
	}

	/**
	 * @param shipContactPhone the shipContactPhone to set
	 */
	public void setShipContactPhone(String shipContactPhone) {
		this.shipContactPhone = shipContactPhone;
	}

	/**
	 * @return the shipAdminUnit
	 */
	public AdministrativeUnit getShipAdminUnit() {
		return shipAdminUnit;
	}

	/**
	 * @param shipAdminUnit the shipAdminUnit to set
	 */
	public void setShipAdminUnit(AdministrativeUnit shipAdminUnit) {
		this.shipAdminUnit = shipAdminUnit;
	}

	/**
	 * @return the shipZipCode
	 */
	public String getShipZipCode() {
		return shipZipCode;
	}

	/**
	 * @param shipZipCode the shipZipCode to set
	 */
	public void setShipZipCode(String shipZipCode) {
		this.shipZipCode = shipZipCode;
	}

	/**
	 * @return the shipInstitutionName
	 */
	public String getShipInstitutionName() {
		return shipInstitutionName;
	}

	/**
	 * @param shipInstitutionName the shipInstitutionName to set
	 */
	public void setShipInstitutionName(String shipInstitutionName) {
		this.shipInstitutionName = shipInstitutionName;
	}


    @Override
    public String getDisplayString() {
        return orderDate + " - " + unitFrom.getDisplayString();
    }
}
