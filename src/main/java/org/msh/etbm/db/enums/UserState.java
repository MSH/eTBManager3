/*
 * UserState.java
 *
 * Created on 29 de Janeiro de 2007, 15:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.msh.etbm.db.enums;




/**
 * Possible status for an user
 * @author Ricardo
 */

public enum UserState {
    /**
     * User is ready to enter in the system
     */
	ACTIVE,
    /**
     * User is blocked and cannot enter in the system
     */
	BLOCKED,
    /**
     * User must change password next time he logs into the system
     */
	PASSWD_EXPIRED,
    /**
     * New user that must validate the e-mail address by confirming the link received from the system
     */
    VALIDATE_EMAIL;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
