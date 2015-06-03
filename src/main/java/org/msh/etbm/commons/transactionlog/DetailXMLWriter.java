package org.msh.etbm.commons.transactionlog;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Date;

/**
 * Writer class to create a XML representation of the details of the transaction log
 * @author Ricardo Memoria
 *
 */
public class DetailXMLWriter {

	private static final String rootTag = "r";
	
	private Element root;
	
	private Element table;
	
	/**
	 * Add a message from the message file with its optional parameters
	 * @param msg
	 * @param params
	 */
	public void addMessage(String msg, Object... params) {
		Element el = setRootElement("msg");
		el.addAttribute("id", msg);

		for (Object p: params) {
			addParamTag(el, p);
		}
	}
	
	
	
	/**
	 * Add a simple string text to the details of the log
	 * @param txt
	 */
	public void addText(String txt) {
		Element el = setRootElement("txt");
		el.setText(txt);
	}

	/**
	 * Start construction of a value table
	 */
	public void addTable() {
		table = setRootElement("tbl");
	}
	
	public Element addTableRow(String key, Object... vs) {
		if (table == null)
			addTable();
		
		Element row = DocumentHelper.createElement("r");
		row.addAttribute("id", key);
		table.add(row);
		for (Object v: vs)
			addParamTag(row, v);
		return row;
	}

	
	/**
	 * Add a parameter to a tag with the content of the value converted
	 * @param parent
	 * @param value
	 * @return
	 */
	protected Element addParamTag(Element parent, Object value) {
		String type = null;
		String val = null;

		Element el = DocumentHelper.createElement("p");
		
		if (value == null) {
			type = "string";
			val = "";
		}
		else
		if (value instanceof String) {
			type = "string";
			val = (String)value;
		}
		else
		if (value instanceof Integer) {
			type = "int";
			val = StringConverter.intToString((Integer) value);
		}
		else
		if (value instanceof Long) {
			type = "long";
			val = StringConverter.longToString((Long)value);
		}
		else
		if (value instanceof Date) {
			type = "date";
			val = StringConverter.dateToString((Date)value);
		}
		else
		if (value instanceof Boolean) {
			type = "bool";
			val = StringConverter.boolToString((Boolean)value);
		}
		else
		if (value instanceof Enum) {
			type = "enum";
			val = StringConverter.enumToString((Enum)value);
		}
		else
		if (value instanceof Double) {
			type = "double";
			val = StringConverter.floatToString((Double)value);
		}
		else {
			type = "string";
			val = value.toString();
		}
		
		el.addAttribute("type", type);
		if (val != null)
			el.setText(val);
		
		parent.add(el);
		
		return el;
	}


	/**
	 * Add a new root tag to the list of main tags
	 * @param tagName
	 * @return
	 */
	protected Element setRootElement(String tagName) {
		Element elem = DocumentHelper.createElement(tagName);

		// check if there is already a root element
		if (root != null) {
			// if root is not the default root tag, so this is the 2nd element at the same level of the root
			// so create a formal root element and add both elements under that
			if (!rootTag.equals(root.getName())) {
				Element child = root;
				root = DocumentHelper.createElement(rootTag);
				root.add(child);
			}
			root.add(elem);
		}
		else root = elem;
		
		return elem;
	}
	
	
	public String asXML() {
		return (root != null? root.asXML(): null);
	}

	/**
	 * Clear the content of the detail writer and make it ready to be used
	 * again
	 */
	public void clear() {
		root = null;
		table = null;
	}
}
