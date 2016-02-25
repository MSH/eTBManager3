package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.enums.MedicineLine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "substance")
public class Substance extends WorkspaceEntity {

	@NotNull
	@PropertyLog(messageKey = "form.name")
	private String name;
	
	@PropertyLog(messageKey = "form.shortName")
    @NotNull
	private String shortName;

    @NotNull
	private MedicineLine line;
	
	private boolean prevTreatmentForm = true;
	
	private boolean dstResultForm = true;

    private boolean active = true;

	@PropertyLog(messageKey = "form.displayorder")
	private Integer displayOrder;

	@Column(length = 50)
	@PropertyLog(messageKey = "form.customId")
	private String customId;

	public void setLine(MedicineLine line) {
		this.line = line;
	}

	public MedicineLine getLine() {
		return line;
	}

	/**
	 * @return the prevTreatmentForm
	 */
	public boolean isPrevTreatmentForm() {
		return prevTreatmentForm;
	}

	/**
	 * @param prevTreatmentForm the prevTreatmentForm to set
	 */
	public void setPrevTreatmentForm(boolean prevTreatmentForm) {
		this.prevTreatmentForm = prevTreatmentForm;
	}

	/**
	 * @return the dstResultForm
	 */
	public boolean isDstResultForm() {
		return dstResultForm;
	}

	/**
	 * @param dstResultForm the dstResultForm to set
	 */
	public void setDstResultForm(boolean dstResultForm) {
		this.dstResultForm = dstResultForm;
	}

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public String getDisplayString() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
