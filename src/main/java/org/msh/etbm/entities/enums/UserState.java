/*
 * UserState.java
 *
 * Created on 29 de Janeiro de 2007, 15:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.msh.etbm.entities.enums;




/**
 * Possible status for an user
 * @author Ricardo
 */

public enum UserState {
	ACTIVE,
	BLOCKED,
	PASSWD_EXPIRED;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
