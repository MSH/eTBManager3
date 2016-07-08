package org.msh.etbm.commons.models.data.fields;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by rmemoria on 1/7/16.
 */
@FieldType("string")
public class StringField extends SingleField {

    /**
     * The maximum length of the string value
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer max;

    /**
     * The minimum length of the string value
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer min;

    /**
     * The character case in use
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CharCase charCase;

    private boolean trim = true;


    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public CharCase getCharCase() {
        return charCase;
    }

    public void setCharCase(CharCase charCase) {
        this.charCase = charCase;
    }

    public boolean isTrim() {
        return trim;
    }

    public void setTrim(boolean trim) {
        this.trim = trim;
    }
}
