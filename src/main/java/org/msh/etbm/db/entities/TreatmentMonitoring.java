package org.msh.etbm.db.entities;

import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.CaseEntity;
import org.msh.etbm.db.enums.TreatmentDayStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Store information about medicine in-take along the treatment
 *
 * @author Ricardo Memoria
 */
@Entity
@Table(name = "treatmentmonitoring")
public class TreatmentMonitoring extends CaseEntity {

    @Column(name = "MONTH_TREAT")
    private int month;

    @Column(name = "YEAR_TREAT")
    private int year;

    private TreatmentDayStatus day1 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day2 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day3 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day4 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day5 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day6 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day7 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day8 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day9 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day10 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day11 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day12 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day13 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day14 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day15 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day16 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day17 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day18 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day19 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day20 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day21 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day22 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day23 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day24 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day25 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day26 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day27 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day28 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day29 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day30 = TreatmentDayStatus.NOT_TAKEN;
    private TreatmentDayStatus day31 = TreatmentDayStatus.NOT_TAKEN;


    /**
     * Return the number of dispensing days for the given month
     *
     * @return number of dispensing days
     */
    public int getNumDispensingDays() {
        int res = 0;
        for (int i = 1; i <= 31; i++) {
            TreatmentDayStatus opt = getDay(i);
            if ((opt == TreatmentDayStatus.DOTS) || (opt == TreatmentDayStatus.SELF_ADMIN)) {
                res++;
            }
        }
        return res;
    }

    /**
     * Return information about the treatment for a specific day
     *
     * @param day day of the month
     * @return instance of {@link TreatmentDayStatus}, or null if nothing is found
     */
    public TreatmentDayStatus getDay(int day) {
        return (TreatmentDayStatus) ObjectUtils.getProperty(this, "day" + Integer.toString(day));
    }

    /**
     * Update the status of a given day of treatment
     *
     * @param day   day of the month (between 1 and 31)
     * @param value instance of {@link TreatmentDayStatus}
     */
    public void setDay(int day, TreatmentDayStatus value) {
        ObjectUtils.setProperty(this, "day" + Integer.toString(day), value);
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
    public TreatmentDayStatus isDay1() {
        return day1;
    }

    /**
     * @param day1 the day1 to set
     */
    public void setDay1(TreatmentDayStatus day1) {
        this.day1 = day1;
    }

    /**
     * @return the day2
     */
    public TreatmentDayStatus isDay2() {
        return day2;
    }

    /**
     * @param day2 the day2 to set
     */
    public void setDay2(TreatmentDayStatus day2) {
        this.day2 = day2;
    }

    /**
     * @return the day3
     */
    public TreatmentDayStatus isDay3() {
        return day3;
    }

    /**
     * @param day3 the day3 to set
     */
    public void setDay3(TreatmentDayStatus day3) {
        this.day3 = day3;
    }

    /**
     * @return the day4
     */
    public TreatmentDayStatus isDay4() {
        return day4;
    }

    /**
     * @param day4 the day4 to set
     */
    public void setDay4(TreatmentDayStatus day4) {
        this.day4 = day4;
    }

    /**
     * @return the day5
     */
    public TreatmentDayStatus isDay5() {
        return day5;
    }

    /**
     * @param day5 the day5 to set
     */
    public void setDay5(TreatmentDayStatus day5) {
        this.day5 = day5;
    }

    /**
     * @return the day6
     */
    public TreatmentDayStatus isDay6() {
        return day6;
    }

    /**
     * @param day6 the day6 to set
     */
    public void setDay6(TreatmentDayStatus day6) {
        this.day6 = day6;
    }

    /**
     * @return the day7
     */
    public TreatmentDayStatus isDay7() {
        return day7;
    }

    /**
     * @param day7 the day7 to set
     */
    public void setDay7(TreatmentDayStatus day7) {
        this.day7 = day7;
    }

    /**
     * @return the day8
     */
    public TreatmentDayStatus isDay8() {
        return day8;
    }

    /**
     * @param day8 the day8 to set
     */
    public void setDay8(TreatmentDayStatus day8) {
        this.day8 = day8;
    }

    /**
     * @return the day9
     */
    public TreatmentDayStatus isDay9() {
        return day9;
    }

    /**
     * @param day9 the day9 to set
     */
    public void setDay9(TreatmentDayStatus day9) {
        this.day9 = day9;
    }

    /**
     * @return the day10
     */
    public TreatmentDayStatus isDay10() {
        return day10;
    }

    /**
     * @param day10 the day10 to set
     */
    public void setDay10(TreatmentDayStatus day10) {
        this.day10 = day10;
    }

    /**
     * @return the day11
     */
    public TreatmentDayStatus isDay11() {
        return day11;
    }

    /**
     * @param day11 the day11 to set
     */
    public void setDay11(TreatmentDayStatus day11) {
        this.day11 = day11;
    }

    /**
     * @return the day12
     */
    public TreatmentDayStatus isDay12() {
        return day12;
    }

    /**
     * @param day12 the day12 to set
     */
    public void setDay12(TreatmentDayStatus day12) {
        this.day12 = day12;
    }

    /**
     * @return the day13
     */
    public TreatmentDayStatus isDay13() {
        return day13;
    }

    /**
     * @param day13 the day13 to set
     */
    public void setDay13(TreatmentDayStatus day13) {
        this.day13 = day13;
    }

    /**
     * @return the day14
     */
    public TreatmentDayStatus isDay14() {
        return day14;
    }

    /**
     * @param day14 the day14 to set
     */
    public void setDay14(TreatmentDayStatus day14) {
        this.day14 = day14;
    }

    /**
     * @return the day15
     */
    public TreatmentDayStatus isDay15() {
        return day15;
    }

    /**
     * @param day15 the day15 to set
     */
    public void setDay15(TreatmentDayStatus day15) {
        this.day15 = day15;
    }

    /**
     * @return the day16
     */
    public TreatmentDayStatus isDay16() {
        return day16;
    }

    /**
     * @param day16 the day16 to set
     */
    public void setDay16(TreatmentDayStatus day16) {
        this.day16 = day16;
    }

    /**
     * @return the day17
     */
    public TreatmentDayStatus isDay17() {
        return day17;
    }

    /**
     * @param day17 the day17 to set
     */
    public void setDay17(TreatmentDayStatus day17) {
        this.day17 = day17;
    }

    /**
     * @return the day18
     */
    public TreatmentDayStatus isDay18() {
        return day18;
    }

    /**
     * @param day18 the day18 to set
     */
    public void setDay18(TreatmentDayStatus day18) {
        this.day18 = day18;
    }

    /**
     * @return the day19
     */
    public TreatmentDayStatus isDay19() {
        return day19;
    }

    /**
     * @param day19 the day19 to set
     */
    public void setDay19(TreatmentDayStatus day19) {
        this.day19 = day19;
    }

    /**
     * @return the day20
     */
    public TreatmentDayStatus isDay20() {
        return day20;
    }

    /**
     * @param day20 the day20 to set
     */
    public void setDay20(TreatmentDayStatus day20) {
        this.day20 = day20;
    }

    /**
     * @return the day21
     */
    public TreatmentDayStatus isDay21() {
        return day21;
    }

    /**
     * @param day21 the day21 to set
     */
    public void setDay21(TreatmentDayStatus day21) {
        this.day21 = day21;
    }

    /**
     * @return the day22
     */
    public TreatmentDayStatus isDay22() {
        return day22;
    }

    /**
     * @param day22 the day22 to set
     */
    public void setDay22(TreatmentDayStatus day22) {
        this.day22 = day22;
    }

    /**
     * @return the day23
     */
    public TreatmentDayStatus isDay23() {
        return day23;
    }

    /**
     * @param day23 the day23 to set
     */
    public void setDay23(TreatmentDayStatus day23) {
        this.day23 = day23;
    }

    /**
     * @return the day24
     */
    public TreatmentDayStatus isDay24() {
        return day24;
    }

    /**
     * @param day24 the day24 to set
     */
    public void setDay24(TreatmentDayStatus day24) {
        this.day24 = day24;
    }

    /**
     * @return the day25
     */
    public TreatmentDayStatus isDay25() {
        return day25;
    }

    /**
     * @param day25 the day25 to set
     */
    public void setDay25(TreatmentDayStatus day25) {
        this.day25 = day25;
    }

    /**
     * @return the day26
     */
    public TreatmentDayStatus isDay26() {
        return day26;
    }

    /**
     * @param day26 the day26 to set
     */
    public void setDay26(TreatmentDayStatus day26) {
        this.day26 = day26;
    }

    /**
     * @return the day27
     */
    public TreatmentDayStatus isDay27() {
        return day27;
    }

    /**
     * @param day27 the day27 to set
     */
    public void setDay27(TreatmentDayStatus day27) {
        this.day27 = day27;
    }

    /**
     * @return the day28
     */
    public TreatmentDayStatus isDay28() {
        return day28;
    }

    /**
     * @param day28 the day28 to set
     */
    public void setDay28(TreatmentDayStatus day28) {
        this.day28 = day28;
    }

    /**
     * @return the day29
     */
    public TreatmentDayStatus isDay29() {
        return day29;
    }

    /**
     * @param day29 the day29 to set
     */
    public void setDay29(TreatmentDayStatus day29) {
        this.day29 = day29;
    }

    /**
     * @return the day30
     */
    public TreatmentDayStatus isDay30() {
        return day30;
    }

    /**
     * @param day30 the day30 to set
     */
    public void setDay30(TreatmentDayStatus day30) {
        this.day30 = day30;
    }

    /**
     * @return the day31
     */
    public TreatmentDayStatus isDay31() {
        return day31;
    }

    /**
     * @param day31 the day31 to set
     */
    public void setDay31(TreatmentDayStatus day31) {
        this.day31 = day31;
    }

    /**
     * @return the day1
     */
    public TreatmentDayStatus getDay1() {
        return day1;
    }

    /**
     * @return the day2
     */
    public TreatmentDayStatus getDay2() {
        return day2;
    }

    /**
     * @return the day3
     */
    public TreatmentDayStatus getDay3() {
        return day3;
    }

    /**
     * @return the day4
     */
    public TreatmentDayStatus getDay4() {
        return day4;
    }

    /**
     * @return the day5
     */
    public TreatmentDayStatus getDay5() {
        return day5;
    }

    /**
     * @return the day6
     */
    public TreatmentDayStatus getDay6() {
        return day6;
    }

    /**
     * @return the day7
     */
    public TreatmentDayStatus getDay7() {
        return day7;
    }

    /**
     * @return the day8
     */
    public TreatmentDayStatus getDay8() {
        return day8;
    }

    /**
     * @return the day9
     */
    public TreatmentDayStatus getDay9() {
        return day9;
    }

    /**
     * @return the day10
     */
    public TreatmentDayStatus getDay10() {
        return day10;
    }

    /**
     * @return the day11
     */
    public TreatmentDayStatus getDay11() {
        return day11;
    }

    /**
     * @return the day12
     */
    public TreatmentDayStatus getDay12() {
        return day12;
    }

    /**
     * @return the day13
     */
    public TreatmentDayStatus getDay13() {
        return day13;
    }

    /**
     * @return the day14
     */
    public TreatmentDayStatus getDay14() {
        return day14;
    }

    /**
     * @return the day15
     */
    public TreatmentDayStatus getDay15() {
        return day15;
    }

    /**
     * @return the day16
     */
    public TreatmentDayStatus getDay16() {
        return day16;
    }

    /**
     * @return the day17
     */
    public TreatmentDayStatus getDay17() {
        return day17;
    }

    /**
     * @return the day18
     */
    public TreatmentDayStatus getDay18() {
        return day18;
    }

    /**
     * @return the day19
     */
    public TreatmentDayStatus getDay19() {
        return day19;
    }

    /**
     * @return the day20
     */
    public TreatmentDayStatus getDay20() {
        return day20;
    }

    /**
     * @return the day21
     */
    public TreatmentDayStatus getDay21() {
        return day21;
    }

    /**
     * @return the day22
     */
    public TreatmentDayStatus getDay22() {
        return day22;
    }

    /**
     * @return the day23
     */
    public TreatmentDayStatus getDay23() {
        return day23;
    }

    /**
     * @return the day24
     */
    public TreatmentDayStatus getDay24() {
        return day24;
    }

    /**
     * @return the day25
     */
    public TreatmentDayStatus getDay25() {
        return day25;
    }

    /**
     * @return the day26
     */
    public TreatmentDayStatus getDay26() {
        return day26;
    }

    /**
     * @return the day27
     */
    public TreatmentDayStatus getDay27() {
        return day27;
    }

    /**
     * @return the day28
     */
    public TreatmentDayStatus getDay28() {
        return day28;
    }

    /**
     * @return the day29
     */
    public TreatmentDayStatus getDay29() {
        return day29;
    }

    /**
     * @return the day30
     */
    public TreatmentDayStatus getDay30() {
        return day30;
    }

    /**
     * @return the day31
     */
    public TreatmentDayStatus getDay31() {
        return day31;
    }

}
