package org.msh.etbm.entities;

import javax.persistence.*;

/**
 * Specify a system parameter, used in any generic configuration of the system or for a specific workspace
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="systemparam")
public class SystemParam {

	@Id
	@Column(name="param_key")
	private String key;
	
	@Column(length=100, name="param_value")
	private String value;

	@ManyToOne
	@JoinColumn(name="WORKSPACE_ID")
	private Workspace workspace;


	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

	/**
	 * @param workspace the workspace to set
	 */
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}
}
