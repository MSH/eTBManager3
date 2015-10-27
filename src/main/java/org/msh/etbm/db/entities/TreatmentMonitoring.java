/**
 * 
 */
package org.msh.etbm.db.entities;

import org.apache.commons.beanutils.PropertyUtils;
import org.msh.etbm.db.CaseData;
import org.msh.etbm.db.enums.TreatmentDayOption;

import javax.persistence.*;

/**
 * Store information about medicine in-take along the treatment
 * 
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="treatmentmonitoring")
public class TreatmentMonitoring extends CaseData {
	
	@Column(name="MONTH_TREAT")
	private int month;
	
	@Column(name="YEAR_TREAT")
	private int year;
	
	private TreatmentDayOption day1 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day2 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day3 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day4 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day5 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day6 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day7 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day8 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day9 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day10 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day11 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day12 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day13 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day14 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day15 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day16 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day17 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day18 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day19 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day20 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day21 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day22 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day23 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day24 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day25 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day26 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day27 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day28 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day29 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day30 = TreatmentDayOption.NOT_TAKEN;
	private TreatmentDayOption day31 = TreatmentDayOption.NOT_TAKEN;
	

	/**
	 * Return the number of dispensing days for the given month
	 * @return number of dispensing days
	 */
	public int getNumDispensingDays() {
		int res = 0;
		for (int i = 1; i <= 31; i++) {
			TreatmentDayOption opt = getDay(i);
			if ((opt == TreatmentDayOption.DOTS) || (opt == TreatmentDayOption.SELF_ADMIN))
				res++;
		}
		return res;
	}
	
	/**
	 * Return information about the treatment for a specific day
	 * @param day day of the month
	 * @return instance of {@link TreatmentDayOption}, or null if nothing is found
	 */
	public TreatmentDayOption getDay(int day) {
		try {
			return (TreatmentDayOption) PropertyUtils.getProperty(this, "day" + Integer.toString(day));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Update the status of a given day of treatment
	 * @param day day of the month (between 1 and 31)
	 * @param value instance of {@link TreatmentDayOption}
	 */
	public void setDay(int day, TreatmentDayOption value) {
		try {
			PropertyUtils.setProperty(this, "day" + Integer.toString(day), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the day1
	 */
	public TreatmentDayOption isDay1() {
		return day1;
	}
	/**
	 * @param day1 the day1 to set
	 */
	public void setDay1(TreatmentDayOption day1) {
		this.day1 = day1;
	}
	/**
	 * @return the day2
	 */
	public TreatmentDayOption isDay2() {
		return day2;
	}
	/**
	 * @param day2 the day2 to set
	 */
	public void setDay2(TreatmentDayOption day2) {
		this.day2 = day2;
	}
	/**
	 * @return the day3
	 */
	public TreatmentDayOption isDay3() {
		return day3;
	}
	/**
	 * @param day3 the day3 to set
	 */
	public void setDay3(TreatmentDayOption day3) {
		this.day3 = day3;
	}
	/**
	 * @return the day4
	 */
	public TreatmentDayOption isDay4() {
		return day4;
	}
	/**
	 * @param day4 the day4 to set
	 */
	public void setDay4(TreatmentDayOption day4) {
		this.day4 = day4;
	}
	/**
	 * @return the day5
	 */
	public TreatmentDayOption isDay5() {
		return day5;
	}
	/**
	 * @param day5 the day5 to set
	 */
	public void setDay5(TreatmentDayOption day5) {
		this.day5 = day5;
	}
	/**
	 * @return the day6
	 */
	public TreatmentDayOption isDay6() {
		return day6;
	}
	/**
	 * @param day6 the day6 to set
	 */
	public void setDay6(TreatmentDayOption day6) {
		this.day6 = day6;
	}
	/**
	 * @return the day7
	 */
	public TreatmentDayOption isDay7() {
		return day7;
	}
	/**
	 * @param day7 the day7 to set
	 */
	public void setDay7(TreatmentDayOption day7) {
		this.day7 = day7;
	}
	/**
	 * @return the day8
	 */
	public TreatmentDayOption isDay8() {
		return day8;
	}
	/**
	 * @param day8 the day8 to set
	 */
	public void setDay8(TreatmentDayOption day8) {
		this.day8 = day8;
	}
	/**
	 * @return the day9
	 */
	public TreatmentDayOption isDay9() {
		return day9;
	}
	/**
	 * @param day9 the day9 to set
	 */
	public void setDay9(TreatmentDayOption day9) {
		this.day9 = day9;
	}
	/**
	 * @return the day10
	 */
	public TreatmentDayOption isDay10() {
		return day10;
	}
	/**
	 * @param day10 the day10 to set
	 */
	public void setDay10(TreatmentDayOption day10) {
		this.day10 = day10;
	}
	/**
	 * @return the day11
	 */
	public TreatmentDayOption isDay11() {
		return day11;
	}
	/**
	 * @param day11 the day11 to set
	 */
	public void setDay11(TreatmentDayOption day11) {
		this.day11 = day11;
	}
	/**
	 * @return the day12
	 */
	public TreatmentDayOption isDay12() {
		return day12;
	}
	/**
	 * @param day12 the day12 to set
	 */
	public void setDay12(TreatmentDayOption day12) {
		this.day12 = day12;
	}
	/**
	 * @return the day13
	 */
	public TreatmentDayOption isDay13() {
		return day13;
	}
	/**
	 * @param day13 the day13 to set
	 */
	public void setDay13(TreatmentDayOption day13) {
		this.day13 = day13;
	}
	/**
	 * @return the day14
	 */
	public TreatmentDayOption isDay14() {
		return day14;
	}
	/**
	 * @param day14 the day14 to set
	 */
	public void setDay14(TreatmentDayOption day14) {
		this.day14 = day14;
	}
	/**
	 * @return the day15
	 */
	public TreatmentDayOption isDay15() {
		return day15;
	}
	/**
	 * @param day15 the day15 to set
	 */
	public void setDay15(TreatmentDayOption day15) {
		this.day15 = day15;
	}
	/**
	 * @return the day16
	 */
	public TreatmentDayOption isDay16() {
		return day16;
	}
	/**
	 * @param day16 the day16 to set
	 */
	public void setDay16(TreatmentDayOption day16) {
		this.day16 = day16;
	}
	/**
	 * @return the day17
	 */
	public TreatmentDayOption isDay17() {
		return day17;
	}
	/**
	 * @param day17 the day17 to set
	 */
	public void setDay17(TreatmentDayOption day17) {
		this.day17 = day17;
	}
	/**
	 * @return the day18
	 */
	public TreatmentDayOption isDay18() {
		return day18;
	}
	/**
	 * @param day18 the day18 to set
	 */
	public void setDay18(TreatmentDayOption day18) {
		this.day18 = day18;
	}
	/**
	 * @return the day19
	 */
	public TreatmentDayOption isDay19() {
		return day19;
	}
	/**
	 * @param day19 the day19 to set
	 */
	public void setDay19(TreatmentDayOption day19) {
		this.day19 = day19;
	}
	/**
	 * @return the day20
	 */
	public TreatmentDayOption isDay20() {
		return day20;
	}
	/**
	 * @param day20 the day20 to set
	 */
	public void setDay20(TreatmentDayOption day20) {
		this.day20 = day20;
	}
	/**
	 * @return the day21
	 */
	public TreatmentDayOption isDay21() {
		return day21;
	}
	/**
	 * @param day21 the day21 to set
	 */
	public void setDay21(TreatmentDayOption day21) {
		this.day21 = day21;
	}
	/**
	 * @return the day22
	 */
	public TreatmentDayOption isDay22() {
		return day22;
	}
	/**
	 * @param day22 the day22 to set
	 */
	public void setDay22(TreatmentDayOption day22) {
		this.day22 = day22;
	}
	/**
	 * @return the day23
	 */
	public TreatmentDayOption isDay23() {
		return day23;
	}
	/**
	 * @param day23 the day23 to set
	 */
	public void setDay23(TreatmentDayOption day23) {
		this.day23 = day23;
	}
	/**
	 * @return the day24
	 */
	public TreatmentDayOption isDay24() {
		return day24;
	}
	/**
	 * @param day24 the day24 to set
	 */
	public void setDay24(TreatmentDayOption day24) {
		this.day24 = day24;
	}
	/**
	 * @return the day25
	 */
	public TreatmentDayOption isDay25() {
		return day25;
	}
	/**
	 * @param day25 the day25 to set
	 */
	public void setDay25(TreatmentDayOption day25) {
		this.day25 = day25;
	}
	/**
	 * @return the day26
	 */
	public TreatmentDayOption isDay26() {
		return day26;
	}
	/**
	 * @param day26 the day26 to set
	 */
	public void setDay26(TreatmentDayOption day26) {
		this.day26 = day26;
	}
	/**
	 * @return the day27
	 */
	public TreatmentDayOption isDay27() {
		return day27;
	}
	/**
	 * @param day27 the day27 to set
	 */
	public void setDay27(TreatmentDayOption day27) {
		this.day27 = day27;
	}
	/**
	 * @return the day28
	 */
	public TreatmentDayOption isDay28() {
		return day28;
	}
	/**
	 * @param day28 the day28 to set
	 */
	public void setDay28(TreatmentDayOption day28) {
		this.day28 = day28;
	}
	/**
	 * @return the day29
	 */
	public TreatmentDayOption isDay29() {
		return day29;
	}
	/**
	 * @param day29 the day29 to set
	 */
	public void setDay29(TreatmentDayOption day29) {
		this.day29 = day29;
	}
	/**
	 * @return the day30
	 */
	public TreatmentDayOption isDay30() {
		return day30;
	}
	/**
	 * @param day30 the day30 to set
	 */
	public void setDay30(TreatmentDayOption day30) {
		this.day30 = day30;
	}
	/**
	 * @return the day31
	 */
	public TreatmentDayOption isDay31() {
		return day31;
	}
	/**
	 * @param day31 the day31 to set
	 */
	public void setDay31(TreatmentDayOption day31) {
		this.day31 = day31;
	}
	/**
	 * @return the day1
	 */
	public TreatmentDayOption getDay1() {
		return day1;
	}
	/**
	 * @return the day2
	 */
	public TreatmentDayOption getDay2() {
		return day2;
	}
	/**
	 * @return the day3
	 */
	public TreatmentDayOption getDay3() {
		return day3;
	}
	/**
	 * @return the day4
	 */
	public TreatmentDayOption getDay4() {
		return day4;
	}
	/**
	 * @return the day5
	 */
	public TreatmentDayOption getDay5() {
		return day5;
	}
	/**
	 * @return the day6
	 */
	public TreatmentDayOption getDay6() {
		return day6;
	}
	/**
	 * @return the day7
	 */
	public TreatmentDayOption getDay7() {
		return day7;
	}
	/**
	 * @return the day8
	 */
	public TreatmentDayOption getDay8() {
		return day8;
	}
	/**
	 * @return the day9
	 */
	public TreatmentDayOption getDay9() {
		return day9;
	}
	/**
	 * @return the day10
	 */
	public TreatmentDayOption getDay10() {
		return day10;
	}
	/**
	 * @return the day11
	 */
	public TreatmentDayOption getDay11() {
		return day11;
	}
	/**
	 * @return the day12
	 */
	public TreatmentDayOption getDay12() {
		return day12;
	}
	/**
	 * @return the day13
	 */
	public TreatmentDayOption getDay13() {
		return day13;
	}
	/**
	 * @return the day14
	 */
	public TreatmentDayOption getDay14() {
		return day14;
	}
	/**
	 * @return the day15
	 */
	public TreatmentDayOption getDay15() {
		return day15;
	}
	/**
	 * @return the day16
	 */
	public TreatmentDayOption getDay16() {
		return day16;
	}
	/**
	 * @return the day17
	 */
	public TreatmentDayOption getDay17() {
		return day17;
	}
	/**
	 * @return the day18
	 */
	public TreatmentDayOption getDay18() {
		return day18;
	}
	/**
	 * @return the day19
	 */
	public TreatmentDayOption getDay19() {
		return day19;
	}
	/**
	 * @return the day20
	 */
	public TreatmentDayOption getDay20() {
		return day20;
	}
	/**
	 * @return the day21
	 */
	public TreatmentDayOption getDay21() {
		return day21;
	}
	/**
	 * @return the day22
	 */
	public TreatmentDayOption getDay22() {
		return day22;
	}
	/**
	 * @return the day23
	 */
	public TreatmentDayOption getDay23() {
		return day23;
	}
	/**
	 * @return the day24
	 */
	public TreatmentDayOption getDay24() {
		return day24;
	}
	/**
	 * @return the day25
	 */
	public TreatmentDayOption getDay25() {
		return day25;
	}
	/**
	 * @return the day26
	 */
	public TreatmentDayOption getDay26() {
		return day26;
	}
	/**
	 * @return the day27
	 */
	public TreatmentDayOption getDay27() {
		return day27;
	}
	/**
	 * @return the day28
	 */
	public TreatmentDayOption getDay28() {
		return day28;
	}
	/**
	 * @return the day29
	 */
	public TreatmentDayOption getDay29() {
		return day29;
	}
	/**
	 * @return the day30
	 */
	public TreatmentDayOption getDay30() {
		return day30;
	}
	/**
	 * @return the day31
	 */
	public TreatmentDayOption getDay31() {
		return day31;
	}

}
