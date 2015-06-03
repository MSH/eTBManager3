package org.msh.etbm.commons.transactionlog.mapping;

import org.msh.etbm.commons.transactionlog.Operation;

import java.util.Arrays;


/**
 * Information about a property of a class that must be logged during a transaction
 * @author Ricardo Memoria
 *
 */
public class PropertyMapping {

	// the owner of this property
	private EntityLogMapping entity;
	
	// the name of this property
	private String name;
	
	// the message key assigned to this property
	private String messageKey;

	// entity mapping of the entity being mapped
	private EntityLogMapping entityProperty;
	
	private Operation[] operations;
	
	public PropertyMapping(EntityLogMapping ent, String name) {
		this.entity = ent;
		this.name = name;
	}


	/**
	 * Check if field should be logged in the operation informed
	 * @param oper
	 * @return
	 */
	public boolean isLoggedForOperation(Operation oper) {
		return ((Arrays.binarySearch(operations, Operation.ALL) >= 0) || (Arrays.binarySearch(operations, oper) >= 0));
	}


	/**
	 * @return the entity
	 */
	public EntityLogMapping getEntity() {
		return entity;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the primitiveValue
	 */
	public boolean isPrimitiveValue() {
		return entityProperty == null;
	}

	/**
	 * @return the entityProperty
	 */
	public EntityLogMapping getEntityProperty() {
		return entityProperty;
	}

	/**
	 * @param entityProperty the entityProperty to set
	 */
	public void setEntityProperty(EntityLogMapping entityProperty) {
		this.entityProperty = entityProperty;
	}

	/**
	 * @return the messageKey
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * @param messageKey the messageKey to set
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}


	/**
	 * @return the operations
	 */
	public Operation[] getOperations() {
		return operations;
	}


	/**
	 * @param operations the operations to set
	 */
	public void setOperations(Operation[] operations) {
		this.operations = operations;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PropertyMapping [entity=" + entity + ", name=" + name
				+ ", messageKey=" + messageKey + ", entityProperty="
				+ entityProperty + ", operations="
				+ Arrays.toString(operations) + "]";
	}
}
