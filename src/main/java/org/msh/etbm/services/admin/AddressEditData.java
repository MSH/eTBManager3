package org.msh.etbm.services.admin;

import java.util.UUID;

/**
 * Address data to be edited in the client side
 * Created by rmemoria on 18/1/16.
 */
public class AddressEditData {
    private String address;
    private String complement;
    private String zipCode;
    private UUID adminUnitId;

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

    public UUID getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(UUID adminUnitId) {
        this.adminUnitId = adminUnitId;
    }
}
