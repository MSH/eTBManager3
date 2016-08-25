package org.msh.etbm.commons.models.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents a property that can host either a value or a Java script function
 * Created by rmemoria on 1/7/16.
 */
public class JSFuncValue<K> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private K value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String function;

    public JSFuncValue(K value) {
        this.value = value;
    }

    /**
     * Default constructor
     */
    public JSFuncValue() {
        super();
    }

    /**
     * Check if value is available
     * @return true if value is available, or null, if value is null
     */
    @JsonIgnore
    public boolean isValuePresent() {
        return value != null;
    }

    @JsonIgnore
    public boolean isExpressionPresent() {
        return function != null;
    }

    public K getValue() {
        return value;
    }

    public void setValue(K value) {
        this.value = value;
        if (value != null) {
            function = null;
        }
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
        if (function != null) {
            value = null;
        }
    }

    public static <K> JSFuncValue<K> function(String expr) {
        JSFuncValue<K> p = new JSFuncValue<>();
        p.setFunction(expr);
        return p;
    }

    public static <K> JSFuncValue<K> of(K value) {
        return new JSFuncValue<>(value);
    }
}
