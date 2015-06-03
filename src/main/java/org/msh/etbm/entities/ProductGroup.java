package org.msh.etbm.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="productgroup")
public class ProductGroup extends WSObject implements Serializable {
	private static final long serialVersionUID = 6289600516436983258L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

	@Column(length=30, name="GROUP_CODE")
	private String code;
	
	@Column(length=100, name="GROUP_NAME")
	private String name;
	
	@Column(name="GROUP_LEVEL")
	private int level;


	public void updateLevel() {
		level = 0;
		if (code == null)
			return;
		final char sep = '.';
		
		int pos = code.indexOf(sep);
		while (pos >= 0) {
			level++;
			pos = code.indexOf(sep, pos + 1);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
