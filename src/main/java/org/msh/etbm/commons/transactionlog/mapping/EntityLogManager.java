package org.msh.etbm.commons.transactionlog.mapping;

import org.hibernate.Hibernate;
import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.PropertyValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Singleton class responsible for managing the property mapping of entities for logging operations, i.e, 
 * for each entity, it stores the list of properties that must be logged during a transaction
 * @author Ricardo Memoria
 *
 */
//@Name("entityLogManager")
//@Scope(ScopeType.APPLICATION)
//@AutoCreate
//@BypassInterceptors
public class EntityLogManager {

	private List<EntityLogMapping> entities = new ArrayList<EntityLogMapping>();
	
	/**
	 * Return the instance of the {@link EntityLogManager} class. This is a singleton instance
	 * @return
	 */
/*
	public static EntityLogManager instance() {
		return (EntityLogManager)Component.getInstance("entityLogManager");
	}
*/

	/**
	 * Return property mapping of the given object to be logged during a transaction 
	 * @param obj
	 * @return
	 */
	public EntityLogMapping get(Object obj) {
//		entities.clear(); // <- JUST FOR TESTING
		// return the true class, and not the hibernate proxy
		Class entityClass = Hibernate.getClass(obj);

		EntityLogMapping ent = find(entityClass);
		
		if (ent == null) {
			// this call is synchronized to avoid double 
			// registration of other threads calling at the same time 
			synchronized (this) {
				ent = createEntityMapping(entityClass);
			}
		}
		return ent;
	}

	
	/**
	 * Create a new mapping for the given entity class 
	 * @param clazz
	 * @return
	 */
	protected EntityLogMapping createEntityMapping(Class clazz) {
		// double checking inside the method to avoid double call at the same time of the same class
		EntityLogMapping ent = find(clazz);
		if (ent != null)
			return ent;

		ent = new EntityLogMapping(clazz);
		updateMapping(ent);
		entities.add(ent);

		return ent;
	}
	


	/**
	 * Find an entity mapping of a given entity class
	 * @param clazz Entity class to search for
	 * @return instance of {@link EntityLogMapping}, or null if it was not found
	 */
	public EntityLogMapping find(Class clazz) {
		for (EntityLogMapping ent: entities)
			if (ent.getEntityClass().equals(clazz))
				return ent;
		
		return null;
	}


	/**
	 * Update the property mapping of the entity class
	 */
	private void updateMapping(EntityLogMapping ent) {
		Class clazz = ent.getEntityClass();

		while (clazz != Object.class) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field f: fields) {
				String s = f.getName();
				char ch = Character.toUpperCase(s.charAt(0));
				
				String getname;
				if ((f.getType() == Boolean.class) || (f.getType() == boolean.class))
					 getname = "is" + ch + s.substring(1);
				else getname = "get" + ch + s.substring(1);
				boolean isProperty = true;
				
				try {
					Class[] params = null;
					clazz.getMethod(getname, params);	} 
				catch (Exception e) {
					isProperty = false;
				}

				if (isProperty) {
					analyzeProperty(ent, clazz, f);
				}
			}
			clazz = clazz.getSuperclass();
		}
	}


	/**
	 * Check if property must be logged, if so, the {@link PropertyMapping} instance of the property
	 * is created
	 * @param entMap
	 * @param clazz Class where property is declared
	 * @param field name of the property
	 */
	protected void analyzeProperty(EntityLogMapping entMap, Class clazz, Field field) {
		PropertyLog propertyLog = null;
		try {
			propertyLog = field.getAnnotation(PropertyLog.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// check if property must be logged
		if ((propertyLog != null) && (propertyLog.ignore()))
			return;
		
		// exceptions
		if (propertyLog == null) {
			if (("id".equals(field.getName())) && (propertyLog == null))
				return;

			Class type = field.getType();
			if (classImplementsInterface(type, Collection.class))
				return;
		}
		
		PropertyMapping pm = new PropertyMapping(entMap, field.getName());
		entMap.getProperties().add(pm);

		if (propertyLog != null) {
			if (propertyLog.logEntityFields()) {
				try {
					Class propType = field.getType();
					EntityLogMapping map = createEntityMapping(propType);
					pm.setEntityProperty(map);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			pm.setMessageKey(propertyLog.messageKey());
			pm.setOperations(propertyLog.operations());
		}
		
		if ((pm.getMessageKey() == null) || (pm.getMessageKey().isEmpty())) {
			String key;
			if (PropertyValue.isPrimitiveType(field.getType()))
				 key = field.getDeclaringClass().getSimpleName() + "." + pm.getName();
			else key = field.getType().getSimpleName();
			pm.setMessageKey(key);
		}

		// no operation was declared
		if (pm.getOperations() == null) {
			Operation oper[] = new Operation[0];
			pm.setOperations(oper);
		}
	}

	/**
	 * Check if class implements a particular interface
	 * @param clazz
	 * @return
	 */
	private boolean classImplementsInterface(Class clazz, Class interf) {
		if (clazz.isInterface())
			return isInterfaceImplemented(clazz, interf);

		if (clazz.isPrimitive())
			return false;

		Class[] ints = clazz.getInterfaces();
		
		if (ints != null) {
			for (Class intf: clazz.getInterfaces()) {
				if (isInterfaceImplemented(intf, interf))
					return true;
			}
		}
		
		Class parent = clazz.getSuperclass();
		if (parent == Object.class)
			return false;
		
		return classImplementsInterface(parent, interf);
	}
	
	/**
	 * Check if a given interface extends another interface
	 * @param clazz
	 * @param interf
	 * @return
	 */
	private boolean isInterfaceImplemented(Class clazz, Class interf) {
		if (clazz == interf)
			return true;
		
		Class[] lst = clazz.getInterfaces();
		for (Class aux: lst)
			if (isInterfaceImplemented(aux, interf))
				return true;
		
		return false;
	}
}
