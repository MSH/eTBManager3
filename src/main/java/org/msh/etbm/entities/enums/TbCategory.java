package org.msh.etbm.entities.enums;

/**
 * TB Category, according to WHO standards
 * @author Ricardo Memoria
 *
 */
public enum TbCategory {
	CATEGORY_I,
	CATEGORY_II,
	CATEGORY_III,
	CATEGORY_IV;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
