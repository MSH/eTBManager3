/*
 * UserRole.java
 *
 * Created on 31 de Janeiro de 2007, 15:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.msh.etbm.db.entities;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Store information about a user role
 * @author Ricardo Memoria
 */

//@Entity
//@Table(name = "userrole")
public class UserRole {

	@Id
    private Integer id;

    @Column(length = 80)
    @NotNull
    private String name;

    @Column(length = 50)
    private String code;
    
    /**
     * Indicate if this role contains operations of read-write, like, for instance, insert-update-delete commands
     */
    private boolean changeable;

    
    /**
     * Return the parent code of the user role
     * @return
     */
    public String getParentCode() {
    	int level = getLevel();
    	switch (level) {
    	case 2: return code.substring(0, 2).concat("0000");
    	case 3: return code.substring(0, 4).concat("00");
    	default: return null;
    	}
    }


    /**
     * Check if user role is a child of the given role
     * @param role
     * @return
     */
    public boolean isChildOf(UserRole role) {
    	int parentLevel = role.getLevel();
    	if ((parentLevel >= getLevel()) || (parentLevel == 0))
    		return false;
    	
    	String parentCode = role.getCode().substring(0, parentLevel * 2);
    	return code.startsWith(parentCode);
    }

   
    /**
     * Return the level of the user role
     * @return
     */
    public int getLevel() {
		if ((code == null) || (code.isEmpty()))
			 return 0;
		
		if (code.endsWith("0000"))
			 return 1;
		else
		if (code.endsWith("00"))
			 return 2;
		else
		if (code.length() == 6)
			return 3;
		else return 0;
	}


    /**
     * Return the text to be displayed to the user
     * @return
     */
/*
    public String getDisplayName() {
    	String msg;
    	
    	if ((messageKey != null) && (!messageKey.isEmpty()))
    		 msg = messageKey;
    	else msg = "userrole." + name;

    	return Messages.instance().get(msg);
    }
*/


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int compareTo(UserRole userRole) {
		return code.compareTo(userRole.getCode());
	}

	/**
	 * @return the changeable
	 */
	public boolean isChangeable() {
		return changeable;
	}

	/**
	 * @param changeable the changeable to set
	 */
	public void setChangeable(boolean changeable) {
		this.changeable = changeable;
	}

}
