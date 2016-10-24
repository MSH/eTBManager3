package org.msh.etbm.commons.indicators.variables;

import org.msh.etbm.commons.Item;

/**
 * Options that a variable exposes
 *
 * Created by rmemoria on 4/10/16.
 */
public class VariableOptions {

    public static final Item CASE_COUNTS = new Item("cases", "Cases");

    /**
     * If the variable must display information in a two-level structure, so this method must return true
     * @return true if variable is grouped
     */
    private boolean grouped;

    /**
     * Return true if the total of the values can be achieved by calculating
     * the sum of the values. If false, the total will not be calculated
     */
    private boolean totalEnabled = true;

    /**
     * Return the number of iterations that this variable will be called, i.e,
     * the number of times the report will query the database using different
     * conditions based on its iteration.
     */
    private int iterationCount;

    /**
     * Return the unit of measure returned by the variable. Example, patients, cases, exams, etc.
     * Object can be anything that represents this unit. It's basically used to compare
     * if a variable is compatible with another variable
     */
    private Item<String> countingUnit;

    /**
     * Default constructor
     */
    public VariableOptions() {
    }

    public VariableOptions(boolean grouped, boolean totalEnabled, int iterationCount, Item<String> countingUnit) {
        this.grouped = grouped;
        this.totalEnabled = totalEnabled;
        this.iterationCount = iterationCount;
        this.countingUnit = countingUnit;
    }


    public boolean isGrouped() {
        return grouped;
    }

    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    public boolean isTotalEnabled() {
        return totalEnabled;
    }

    public void setTotalEnabled(boolean totalEnabled) {
        this.totalEnabled = totalEnabled;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public Item<String> getCountingUnit() {
        return countingUnit;
    }

    public void setCountingUnit(Item<String> countingUnit) {
        this.countingUnit = countingUnit;
    }
}
