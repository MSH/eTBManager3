package org.msh.etbm.commons.models.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents a property that can host either a value or a Java script expression
 * Created by rmemoria on 1/7/16.
 */
public class JSExpressionProperty<K> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private K value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String expression;

    public JSExpressionProperty(K value) {
        this.value = value;
    }

    /**
     * Default constructor
     */
    public JSExpressionProperty() {
        super();
    }

    /**
     * Check if value is available
     * @return true if value is available, or null, if value is null
     */
    @JsonIgnore
    public boolean isValueAvailable() {
        return value != null;
    }

    public K getValue() {
        return value;
    }

    public void setValue(K value) {
        this.value = value;
        if (value != null) {
            expression = null;
        }
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
        if (expression != null) {
            value = null;
        }
    }

}
