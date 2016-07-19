package org.msh.etbm.services.admin;


import org.msh.etbm.services.admin.admunits.data.AdminUnitData;

/**
 * Created by rmemoria on 1/11/15.
 */
public class AddressData {
    private String address;
    private String complement;
    private String zipCode;

    private AdminUnitData adminUnit;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public AdminUnitData getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdminUnitData adminUnit) {
        this.adminUnit = adminUnit;
    }
}
