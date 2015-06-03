package org.msh.etbm.commons.transactionlog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringConverter {

	public static String intToString(Integer val) {
		return (val == null? "": val.toString());
	}
	
	public static String floatToString(Double val) {
		if (val == null)
			return "";
		DecimalFormat df = new DecimalFormat("0.###");
		String s = df.format(val);
		char c = df.getDecimalFormatSymbols().getDecimalSeparator();
		s = s.replace(c, '.');
		return s;
	}
	
	
	public static String longToString(Long val) {
		return (val == null? "": val.toString());
	}
	
	/**
	 * Convert a date to a string
	 * @param dt
	 * @return
	 */
	public static String dateToString(Date dt) {
		if (dt == null)
			return "";
		Calendar c = Calendar.getInstance();
		c.setTime(dt);

		SimpleDateFormat sp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS");
		
		return sp.format(dt);
	}


	/**
	 * Convert a boolean to a string
	 * @param b
	 * @return
	 */
	public static String boolToString(Boolean b) {
		return (b==null? "": (b.equals(Boolean.TRUE)?"1":"0"));
	}
	
	
	/**
	 * Convert a enumeration to a string
	 * @param val
	 * @return
	 */
	public static String enumToString(Enum val) {
		if (val == null)
			return "";
		
		return val.getClass().getName() + "." + val.toString(); 
	}
	
	/**
	 * Convert a string in the format <Enum class>.<EnumValue> to an enumeration
	 * @param val
	 * @return
	 */
	public static Enum stringToEnum(String val) {
		if ((val == null) || (val.isEmpty()))
			return null;
		
		int pos = val.lastIndexOf('.');
		
		String cl = val.substring(0, pos);
		String enumname = val.substring(pos + 1);
		
		Enum value = null;
		
		try {
			Class<?> clazz = Class.forName(cl);
			if (clazz.isEnum()) {
				value = Enum.valueOf((Class<? extends Enum>) clazz, enumname);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return value;
	}

	
	/**
	 * Convert a string to a boolean
	 * @param val
	 * @return
	 */
	public static Boolean stringToBool(String val) {
		if ((val == null) || (val.isEmpty()))
			return null;
		
		if ("0".equals(val))
			return Boolean.TRUE;
		
		if ("1".equals(val))
			return Boolean.FALSE;
		
		return null;
	}
	
	
	/**
	 * Convert a string to a date value
	 * @param s
	 */
	public static Date stringToDate(String s) {
		if ((s == null) || (s.isEmpty()))
			return null;

		String[] n = s.split("\\.");
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Integer.parseInt(n[0]));
		c.set(Calendar.MONTH, Integer.parseInt(n[1])-1);
		c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(n[2]));
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(n[3]));
		c.set(Calendar.MINUTE, Integer.parseInt(n[4]));
		c.set(Calendar.SECOND, Integer.parseInt(n[5]));
		c.set(Calendar.MILLISECOND, Integer.parseInt(n[6]));
		
		return c.getTime();
	}
	
	
	/**
	 * Convert a string to a long value
	 * @param s
	 * @return
	 */
	public static Long stringToLong(String s) {
		if ((s == null) || (s.isEmpty()))
			return null;

		return Long.parseLong(s);
	}
	
	
	public static Integer stringToInt(String s) {
		if ((s == null) || (s.isEmpty()))
			return null;

		return Integer.parseInt(s);
	}
	
	
	public static Double stringToDouble(String s) {
		if ((s == null) || (s.isEmpty()))
			return null;

		DecimalFormat df = new DecimalFormat();
		char c = df.getDecimalFormatSymbols().getDecimalSeparator();
		s = s.replace('.', c);
		try {
			return df.parse(s).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
