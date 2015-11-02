package org.msh.etbm.services.admin;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Request with information about the address, to be mapped back to {@link org.msh.etbm.db.Address}
 *
 * Created by rmemoria on 28/10/15.
 */
public class AddressRequest {

    private String address;
    private String complement;
    private String zipCode;
    @NotNull
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
