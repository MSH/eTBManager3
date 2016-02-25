package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "administrativeunit")
public class AdministrativeUnit extends WorkspaceEntity {

	@PropertyLog(messageKey = "form.name", operations = {Operation.NEW, Operation.DELETE})
    private String name;

	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	@PropertyLog(operations = {Operation.NEW, Operation.DELETE})
	private AdministrativeUnit parent;
	
	@OneToMany(mappedBy = "parent",fetch = FetchType.LAZY)
	@OrderBy("NAME")
	private List<AdministrativeUnit> units = new ArrayList<>();

	@Column(length = 50)
	@PropertyLog(messageKey = "form.customId")
	private String customId;
	
	// properties to help dealing with trees
	private int unitsCount;
	
	@Column(length = 15, nullable = false)
	@PropertyLog(ignore = true)
	private String code;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRYSTRUCTURE_ID")
	@PropertyLog(operations = {Operation.ALL})
	private CountryStructure countryStructure;
	

	/**
	 * Return the parent list including the own object
	 * @return List of {@link AdministrativeUnit} instance
	 */
	public List<AdministrativeUnit> getParents() {
		return getParentsTreeList(true);
	}


	/**
	 * Return the display name of the administrative unit concatenated with its parent units
	 * @return
	 */
	public String getFullDisplayName() {
		String s = getName();

		for (AdministrativeUnit adm: getParentsTreeList(false)) {
			s += ", " + adm.getName();
		}
		
		return s;
	}
	
	/**
	 * Return the parent units display name with the name of this instance only on the end.
	 * @return
	 */
	public String getFullDisplayName2() {
		String s = null;

		for (AdministrativeUnit adm: getParentsTreeList(true)) {
			if (s == null) {
                s = adm.getName();
            } else {
                s += ", " + adm.getName();
            }
		}
		
		return s;
	}
	
	/**
	 * Return a list with parents administrative unit, where the first is the upper level administrative unit and
	 * the last the lowest level
	 * @return {@link List} of {@link AdministrativeUnit} instances
	 */
	public List<AdministrativeUnit> getParentsTreeList(boolean includeThis) {
		ArrayList<AdministrativeUnit> lst = new ArrayList<>();

		AdministrativeUnit aux;
		if (includeThis) {
            aux = this;
        } else {
            aux = getParent();
        }

		while (aux != null) {
			lst.add(0, aux);
			aux = aux.getParent();
		}
		return lst;
	}


	/**
	 * Static method that return the parent code of a given code
	 * @param code
	 * @return
	 */
	public static String getParentCode(String code) {
		if ((code == null) || (code.length() <= 3)) {
            return null;
        }

		if (code.length() <= 6) {
            return code.substring(0, 3);
        }

		if (code.length() <= 9) {
            return code.substring(0, 6);
        }

        return code.length() <= 12 ? code.substring(0, 9) : code.substring(0, 12);
	}
	
	/**
	 * Check if an administrative unit code (passed as the code parameter) is a child of the current administrative unit
	 * @param code of the unit
	 * @return true if code is of a child unit, otherwise return false
	 */
	public boolean isSameOrChildCode(String code) {
		return isSameOrChildCode(this.code, code);
	}
	
	
	/**
	 * Static method to check if a code is equals of a child of the code given by the parentCode param
	 * @param parentCode
	 * @param code
	 * @return
	 */
	public static boolean isSameOrChildCode(String parentCode, String code) {
		int len = parentCode.length();
		if (len > code.length()) {
            return false;
        }

		return parentCode.equals(code.substring(0, parentCode.length()));
	}


	/**
	 * Return the parent administrative unit based on its level. If level is the same of this unit, it returns itself.
	 * If level is bigger than the level of this unit, it returns null
	 * @param level
	 * @return {@link AdministrativeUnit} instance, which is itself or a parent admin unit
	 */
	public AdministrativeUnit getAdminUnitByLevel(int level) {
		if (level == countryStructure.getLevel()) {
            return this;
        }

		List<AdministrativeUnit> lst = getParents();
		for (AdministrativeUnit adm: lst) {
			if (adm.getLevel() == level) {
                return adm;
            }
		}
		return null;
	}

	
	/**
	 * Return parent administrative unit of level 1
	 * @return {@link AdministrativeUnit} instance
	 */
	public AdministrativeUnit getParentLevel1() {
		return getAdminUnitByLevel(1);
	}

	
	/**
	 * Return parent administrative unit of level 2
	 * @return {@link AdministrativeUnit} instance
	 */
	public AdministrativeUnit getParentLevel2() {
		return getAdminUnitByLevel(2);
	}

	
	/**
	 * Return parent administrative unit of level 3
	 * @return {@link AdministrativeUnit} instance
	 */
	public AdministrativeUnit getParentLevel3() {
		return getAdminUnitByLevel(3);
	}

	
	/**
	 * Return parent administrative unit of level 4
	 * @return {@link AdministrativeUnit} instance
	 */
	public AdministrativeUnit getParentLevel4() {
		return getAdminUnitByLevel(4);
	}


	/**
	 * Return parent administrative unit of level 5
	 * @return {@link AdministrativeUnit} instance
	 */
	public AdministrativeUnit getParentLevel5() {
		return getAdminUnitByLevel(5);
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

	/**
	 * @return the parent
	 */
	public AdministrativeUnit getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(AdministrativeUnit parent) {
		this.parent = parent;
	}

	/**
	 * @return the units
	 */
	public List<AdministrativeUnit> getUnits() {
		return units;
	}

	/**
	 * @param units the units to set
	 */
	public void setUnits(List<AdministrativeUnit> units) {
		this.units = units;
	}

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
	@Override
	public String toString() {
		return getFullDisplayName();
	}

	/**
	 * @return the unitsCount
	 */
	public int getUnitsCount() {
		return unitsCount;
	}

	/**
	 * @param unitsCount the unitsCount to set
	 */
	public void setUnitsCount(int unitsCount) {
		this.unitsCount = unitsCount;
	}

	/**
	 * @return the countryStructure
	 */
	public CountryStructure getCountryStructure() {
		return countryStructure;
	}

	/**
	 * @param countryStructure the countryStructure to set
	 */
	public void setCountryStructure(CountryStructure countryStructure) {
		this.countryStructure = countryStructure;
	}


	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	public int getLevel() {
		String s = getCode();
		if (s == null) {
            return 0;
        }

		return s.length() / 3;
	}

    @Override
    public String getDisplayString() {
        return name;
    }
}
