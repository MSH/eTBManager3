package org.msh.etbm.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class LocalizedNameComp implements Serializable {
	private static final long serialVersionUID = -957982131483299044L;

	@Column(name="name1", length=100, nullable=true)
	private String name1;

	@Column(name="name2", length=100)
	private String name2;


	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	
	/**
	 * Set the names to upper case
	 */
	public void toUpper() {
		if (name1 != null)
			name1 = name1.toUpperCase();
		
		if (name2 != null)
			name2 = name2.toUpperCase();
	}
	
	@Override
	public String toString() {
		return getDefaultName();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name1 == null) ? 0 : name1.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalizedNameComp other = (LocalizedNameComp) obj;
		if (name1 == null) {
			if (other.name1 != null)
				return false;
		} else if (!name1.equals(other.name1))
			return false;
		return true;
	}


	public String getDefaultName() {
		return (isAlternateLanguage() ? (name2 != null && (!name2.isEmpty())? name2: name1) : name1);
	}
	
	public void setDefaultName(String name) {
		if (isAlternateLanguage())
			 name1 = name;
		else name2 = name;
	}
	
	/**
	 * Check if language selected by the user is the alternate language defined in the workspace
	 * @return
	 */
	public boolean isAlternateLanguage() {
		return false;
/*
		if (Contexts.getSessionContext() == null)
			return false;
		UserLogin userLogin = (UserLogin)Contexts.getSessionContext().get("userLogin");
		Workspace ws = (Workspace)Component.getInstance("defaultWorkspace");
		
		if ((userLogin == null) || (ws == null))
			return true;

		String lang = ws.getAlternateLocale();
		if ((lang != null) && (lang.equals(userLogin.getUser().getLanguage())))
			 return true;
		else return false;
*/
	}
}
