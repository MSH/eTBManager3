package org.msh.etbm.entities.enums;

public enum MovementType {
	DRUGRECEIVING,
	ORDERSHIPPING,
	ORDERRECEIVING,
	DISPENSING,
	ADJUSTMENT,
	TRANSFERIN,
	TRANSFEROUT,
	INITIALIZE;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
	
	/**
	 * Returns the operation for the corresponding movement type
	 * @return
	 */
	public int getOper() {
		switch (this) {
		case DRUGRECEIVING: return 1;
		case ORDERRECEIVING: return 1;
		case ORDERSHIPPING: return -1;
		case DISPENSING: return -1;
		case ADJUSTMENT: return 1;
		case TRANSFERIN: return 1;
		case TRANSFEROUT: return -1;
		case INITIALIZE: return 1;
		}
		
		return 0;
	}
}
