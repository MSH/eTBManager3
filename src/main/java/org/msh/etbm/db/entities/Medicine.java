package org.msh.etbm.db.entities;

import org.msh.etbm.db.enums.MedicineCategory;
import org.msh.etbm.db.enums.MedicineLine;
import org.msh.etbm.services.admin.products.ProductType;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("med")
public class Medicine extends Product {

    @NotNull
	private MedicineCategory category;

    @NotNull
	private MedicineLine line;

	@OneToMany(mappedBy="medicine", cascade={CascadeType.ALL})
	private List<MedicineComponent> components = new ArrayList<>();

	public String getTbInfoKey() {
		return line != null? line.getKey(): null;
	}


    @Override
    public ProductType getType() {
        return ProductType.MEDICINE;
    }

	public MedicineCategory getCategory() {
		return category;
	}

	public void setCategory(MedicineCategory category) {
		this.category = category;
	}


	public MedicineLine getLine() {
		return line;
	}

	public void setLine(MedicineLine line) {
		this.line = line;
	}

	public void setComponents(List<MedicineComponent> components) {
		this.components = components;
	}

	public List<MedicineComponent> getComponents() {
		return components;
	}

	/**
	 * Return medicine component, which contains stated substance
	 * @author A.M.
	 */
	public MedicineComponent getComponentBySubstance(Substance s) {
		MedicineComponent res = null;
		if (components!=null)
			for (MedicineComponent mc: components)
				if (mc.getSubstance().equals(s))
					res = mc;
		return res;
	}

}
