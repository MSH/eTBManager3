package org.msh.etbm.commons.indicators.filters;

/**
 * Represents an option of a filter
 * @author Ricardo Memoria
 *
 */
public class FilterOption {

    private Object value;
    private String label;

    public FilterOption() {
        super();
    }

    public FilterOption(Object value, String label) {
        super();
        this.value = value;
        this.label = label;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
