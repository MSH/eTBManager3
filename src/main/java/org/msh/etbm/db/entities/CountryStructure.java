package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WSObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name="countrystructure")
public class CountryStructure extends WSObject implements Serializable {
	private static final long serialVersionUID = -9182467866935116572L;

	@Embedded
	@PropertyLog(messageKey="form.name")
	private String name;
	
	@Column(name="STRUCTURE_LEVEL")
	@PropertyLog(messageKey="global.level")
	private int level;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

}
