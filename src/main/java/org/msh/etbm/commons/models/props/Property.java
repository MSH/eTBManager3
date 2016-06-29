package org.msh.etbm.commons.models.props;

/**
 * Created by rmemoria on 18/10/15.
 */
public abstract class Property {

    /**
     * Indicate if the field is required
     */
    private boolean required;

    /**
     * Indicate the caption to be displayed beside the field
     */
    private String caption;

    /**
     * Return the supported type for this property
     *
     * @return
     */
    public abstract String getType();


    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
