package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="countrystructure")
public class CountryStructure extends WorkspaceEntity {

    @Column(length = 100)
    @PropertyLog(messageKey = "form.name")
    @NotNull
	private String name;
	
	@Column(name="STRUCTURE_LEVEL")
    @Max(5)
    @Min(1)
    @PropertyLog(messageKey = "form.level", operations = {Operation.ALL})
    @NotNull
	private Integer level;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @param level the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

    @Override
    public String toString() {
        return super.toString() +
                "name='" + name + '\'' +
                ", level=" + level;
    }

    @Override
    public String getDisplayString() {
        return name;
    }
}
