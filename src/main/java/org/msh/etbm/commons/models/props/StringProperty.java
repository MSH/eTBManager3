package org.msh.etbm.commons.models.props;

/**
 * Created by rmemoria on 20/10/15.
 */
public class StringProperty extends Property {

    /**
     * String validators
     **/
    private int maxLength;
    private int minLength;
    private String regExp;

    @Override
    public String getType() {
        return "string";
    }
}
