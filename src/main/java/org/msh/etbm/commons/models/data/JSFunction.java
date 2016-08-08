package org.msh.etbm.commons.models.data;

/**
 * Simple class that wraps a string value to indicate that it is in fact the body of a java script function
 *
 * Created by rmemoria on 5/8/16.
 */
public class JSFunction {

    /**
     * The expression that will be returned inside the java script function
     */
    private String expression;

    public JSFunction(String expression) {
        this.expression = expression;
    }

    public JSFunction() {
        super();
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
