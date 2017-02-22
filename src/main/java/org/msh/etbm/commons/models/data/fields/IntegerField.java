package org.msh.etbm.commons.models.data.fields;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Support for integer fields in the model
 *
 * Created by rmemoria on 1/7/16.
 */
@FieldType("int")
public class IntegerField extends SingleField {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer max;

    @JsonInclude(JsonInclude.Include.NON_NULL)
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
