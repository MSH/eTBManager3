package org.msh.etbm.entities.enums;

public enum CultureResult {
	NEGATIVE ('-'),
	POSITIVE ('+'),
	PLUS	 ('+'),
	PLUS2	 ('+'),
	PLUS3	 ('+'),
	PLUS4	 ('+'),
	CONTAMINATED('0'),
	NOTDONE  ('0'),
	NTM('0'),
	POSITIVE_UNKNOWN_SCALE('0'), 
	NO_GROWTH('0'),
	OTHER    ('0'),
	PENDING  ('0');
	
	private char result;
	
	private CultureResult(char result) {
		this.result = result;
	}
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
	
	public boolean isPositive() {
		return (result == '+');
	}
	
	public boolean isNegative() {
		return (result == '-');
	}
	
	/**
	 * Return the list of values that are positive
	 * @return
	 */
	static public CultureResult[] getPositiveResults() {
		int num = 0;
		for (CultureResult res: values()) {
			if (res.isPositive())
				num++;
		}
		
		CultureResult[] lst = new CultureResult[num];
		int i = 0;
		for (CultureResult res: values()) {
			if (res.isPositive()) {
				lst[i] = res;
				i++;
			}
		}
		return lst;
	}

	
	/**
	 * Return the list of values that are negative
	 * @return
	 */
	static public CultureResult[] getNegativeResults() {
		int num = 0;
		for (CultureResult res: values()) {
			if (res.isNegative())
				num++;
		}
		
		CultureResult[] lst = new CultureResult[num];
		int i = 0;
		for (CultureResult res: values()) {
			if (res.isNegative()) {
				lst[i] = res;
				i++;
			}
		}
		return lst;
	}
}
