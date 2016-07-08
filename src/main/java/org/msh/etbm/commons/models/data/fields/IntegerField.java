package org.msh.etbm.commons.models.data.fields;

/**
 * Created by rmemoria on 1/7/16.
 */
@FieldType("int")
public class IntegerField extends SingleField {

    private Integer max;

    private Integer min;

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
}
