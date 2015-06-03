package org.msh.etbm.entities;

import org.msh.etbm.commons.date.Period;

import java.util.Calendar;
import java.util.Date;


/**
 * @author Ricardo
 *
 * Handles the weekly frequency of dispensing of medicines
 */
public class WeeklyFrequency {

	private int value;


	public WeeklyFrequency(int value) {
		super();
		this.value = value;
	}

	public WeeklyFrequency() {
		super();
	}
	
	/**
	 * Set the week day dispensing information
	 * @param day - value between 1 and 7
	 * @param val
	 */
	public void setDay(int day, boolean val) {
		int n = (1 << (day - 1));

		if (val) {
			value |= n;			
		}
		else {
			if ((value & n) != 0) {
				value ^= n;
			}
		}
	}
	
	/**
	 * Returns if the day is set in the current date
	 * @param dt
	 * @return true if the week day is set in the current day, otherwise returns false
	 */
	public boolean isDateSet(Date dt) {
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		int day = c.get(Calendar.DAY_OF_WEEK);
		return getDay(day);
	}
	
	/**
	 * Calculate the number of days selected in the frequency between two dates
	 * @param p the period to evaluate
	 * @return
	 */
	public int calcNumDays(Period p) {
		if (p.getIniDate().equals(p.getEndDate())) {
			Calendar c = Calendar.getInstance();
			c.setTime(p.getIniDate());
			int wd = c.get(Calendar.DAY_OF_WEEK);
			return (getDay(wd)? 1: 0);
		}
		
		// get number of days between dates (+ 1 <= include the last day)
		int nd = p.getDays() + 1;
		
		int res;
		// number of days is bigger than a week
		if (nd > 7) {
			// calculate number of weeks
			int n = nd / 7;
			// calculate the number of days for the weeks
			res = n * getDaysInAWeek();
			
			if (nd % 7 == 0) 
				return res;
		}
		else res = 0;
		
		Calendar c = Calendar.getInstance();
		c.setTime(p.getIniDate());
		int wd1 = c.get(Calendar.DAY_OF_WEEK);
		
		c.setTime(p.getEndDate());
		int wd2 = c.get(Calendar.DAY_OF_WEEK);
		
		// include days of the last week
		while (wd1 != wd2) {
			if (getDay(wd1))
				res++;
			wd1++;
			if (wd1 > 7)
				wd1 = 1;
		}
		
		// adjust to include the last day in the check
		if (getDay(wd1))
			res++;
		
		return res;
	}
	
	/**
	 * Get the week day dispensing information
	 * @param day - value between 1 and 7
	 * @return true - the week day is set to have dispensing
	 */
	public boolean getDay(int day) {
		return ((value >> (day-1)) & 1) != 0;
	}

	/**
	 * Return the representation of the weekly scheme in a format n/7
	 * @return
	 */
	public String getDisplayName() {
		int num = getDaysInAWeek();
		
		if (num == 0)
			 return "";
		else return Integer.toString(num) + "/7";
	}
	
	/**
	 * Return the number of days of dispensing in a week 
	 * @return
	 */
	public int getDaysInAWeek() {
		if (value == 0)
			return 0;
		
		int mask = 1;
		int num = 0;
		for (int i = 1; i <= 7; i++) {
			if ((value & mask) != 0)
				num++;
			mask *= 2;
		}
		return num;
	}
	
	public Boolean[] getWeekMask() {
		Boolean[] mask = new Boolean[7];
		for (int i = 1; i <= 7; i++) {
			mask[i - 1] = getDay(i); 
		}
		
		return mask;
	}
	
	public void setWeekMask(Boolean[] vals) {
		int i = 1;
		for (Boolean b: vals) {
			if (b != null)
				setDay(i, b);
			i++;
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int val) {
		value = val;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeeklyFrequency other = (WeeklyFrequency) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
