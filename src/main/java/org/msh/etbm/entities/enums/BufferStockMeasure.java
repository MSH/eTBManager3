package org.msh.etbm.entities.enums;

/**
 * Buffer stock measures units for minimum quantity allowed
 * @author Ricardo Memoria
 *
 */
public enum BufferStockMeasure {
	DAYS,
	MONTHS,
	UNITS;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
