package org.msh.etbm.commons.transactionlog.mapping;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityLogMapping {

	private Class entityClass;
	private List<PropertyMapping> properties = new ArrayList<PropertyMapping>();

	protected EntityLogMapping(Class entityClass) {
		this.entityClass = entityClass;
	}


	/**
	 * Create a list of all properties of the object and its respective values
	 * @param entity
	 * @return
	 */
/*
	public List<PropertyValue> describeEntity(Object entity, Operation oper) {
		if (oper == Operation.ALL)
			throw new IllegalArgumentException("operation must be specified. ALL is not allowed");

		Map<PropertyMapping, String> lst = getPropertyList();
		List<PropertyValue> values = new ArrayList<PropertyValue>();

		for (PropertyMapping prop: lst.keySet()) {
			try {
				String propname = lst.get(prop);
				if ((oper == Operation.EDIT) || (prop.isLoggedForOperation(oper))) {
					Object value = PropertyUtils.getProperty(entity, propname);
					values.add( new PropertyValue(entity, prop, propname, value) );
				}

			} catch (Exception e) {
				if (!e.getClass().getName().contains("NestedNullException")){ //AK 07/07/12 exclude annoyed error message about treatmentPeriod
					e.printStackTrace();
					new RuntimeException(e);
				}
			}
		}

		return values;
	}
*/


	/**
	 * Return list of properties to be logged (including nested properties of other objects)
	 * @return
	 */
	public Map<PropertyMapping, String> getPropertyList() {
		Map<PropertyMapping, String> map = new HashMap<PropertyMapping, String>();
		addPropertiesToList(this, null, map);

		return map;
	}


	/**
	 * Add name of properties to a list of strings. If properties must be nested, 
	 * @param map
	 * @param prefix
	 * @param map
	 */
	protected void addPropertiesToList(EntityLogMapping entityMap, String prefix, Map<PropertyMapping, String> map) {
		for (PropertyMapping pm: entityMap.getProperties()) {
			String s = pm.getName();
			if (prefix != null)
				s = prefix + s;

			if (pm.isPrimitiveValue())
				map.put(pm, s);
			else addPropertiesToList(pm.getEntityProperty(), s + ".", map);
		}
	}


	/**
	 * @return the entityClass
	 */
	public Class getEntityClass() {
		return entityClass;
	}
	/**
	 * @param entityClass the entityClass to set
	 */
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}
	/**
	 * @return the properties
	 */
	public List<PropertyMapping> getProperties() {
		return properties;
	}
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<PropertyMapping> properties) {
		this.properties = properties;
	}
}
