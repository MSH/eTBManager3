package org.msh.etbm.db;

/**
 * An interface a class or enumeration must implement in order to return the corresponding key in the list of messages 
 * to give it the property message in the current language 
 * @author Ricardo Memoria
 *
 */
public interface MessageKey {

	String getMessageKey();
}
