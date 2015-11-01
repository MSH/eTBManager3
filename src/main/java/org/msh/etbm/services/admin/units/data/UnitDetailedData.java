package org.msh.etbm.services.admin.units.data;

import org.msh.etbm.services.admin.AddressData;

import java.util.Date;

/**
 * Created by rmemoria on 1/11/15.
 */
public class UnitDetailedData extends UnitItemData {
    private String customId;
    private boolean active;
    private String shipContactName;
    private String shipContactPhone;
    private AddressData address;
    private AddressData shipAddress;
    private UnitItemData supplier;
    private UnitItemData authorizer;
    private boolean receiveFromManufacturer;

    /** laboratory part */
    private Boolean performCulture;
    private Boolean performMicroscopy;
    private Boolean performDst;
    private Boolean performXpert;
    
    /** TB unit part */
    private Boolean tbUnit;
    private Boolean mdrUnit;
    private Boolean ntmUnit;
    private Boolean notificationUnit;
    private Boolean patientDispensing;
    private Integer numDaysOrder;
    
}
