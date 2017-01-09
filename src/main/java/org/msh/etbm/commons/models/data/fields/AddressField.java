package org.msh.etbm.commons.models.data.fields;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.msh.etbm.commons.models.data.Field;

/**
 * Created by rmemoria on 26/8/16.
 */
@FieldType("address")
public class AddressField extends Field {

    private String fieldAddress;
    private String fieldComplement;
    private String fieldZipCode;
    private String fieldAdminUnit;

    /**
     * Optional. The required level for the administrative unit in the address
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer requiredLevel;


    public String getFieldAddress() {
        return fieldAddress;
    }

    public void setFieldAddress(String fieldAddress) {
        this.fieldAddress = fieldAddress;
    }

    public String getFieldComplement() {
        return fieldComplement;
    }

    public void setFieldComplement(String fieldComplement) {
        this.fieldComplement = fieldComplement;
    }

    public String getFieldZipCode() {
        return fieldZipCode;
    }

    public void setFieldZipCode(String fieldZipCode) {
        this.fieldZipCode = fieldZipCode;
    }

    public String getFieldAdminUnit() {
        return fieldAdminUnit;
    }

    public void setFieldAdminUnit(String fieldAdminUnit) {
        this.fieldAdminUnit = fieldAdminUnit;
    }

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(Integer requiredLevel) {
        this.requiredLevel = requiredLevel;
    }
}
