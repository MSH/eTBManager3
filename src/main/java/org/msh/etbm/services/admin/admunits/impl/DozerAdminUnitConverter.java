package org.msh.etbm.services.admin.admunits.impl;

import org.dozer.CustomConverter;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by rmemoria on 17/12/15.
 */
@Component
public class DozerAdminUnitConverter implements CustomConverter {

    @Autowired
    UserRequestService userRequestService;

    @Override
    public Object convert(Object dest, Object source, Class<?> destClass, Class<?> sourceClass) {
        if (source == null) {
            return null;
        }

        if (source instanceof AdministrativeUnit) {
            return convertToDTO((AdministrativeUnit) source);
        }

        return null;
    }


    /**
     * Convert an administrative unit in a series of parent administrative units
     *
     * @param adm the administrative unit to be converted
     * @return the administrative unit series
     */
    public AdminUnitData convertToDTO(AdministrativeUnit adm) {
        AdminUnitData data = new AdminUnitData();

        data.setId(adm.getId());
        data.setName(adm.getName());
        data.setUnitsCount(adm.getUnitsCount());

        // set information about parent units
        if (adm.getPid0() != null) {
            data.setP0(new SynchronizableItem(adm.getPid0(), adm.getPname0()));
        }

        if (adm.getPid1() != null) {
            data.setP1(new SynchronizableItem(adm.getPid1(), adm.getPname1()));
        }

        if (adm.getPid2() != null) {
            data.setP2(new SynchronizableItem(adm.getPid2(), adm.getPname2()));
        }

        if (adm.getPid3() != null) {
            data.setP3(new SynchronizableItem(adm.getPid3(), adm.getPname3()));
        }

        // set information about country structure
        CountryStructure cs = adm.getCountryStructure();
        data.setCountryStructure(new SynchronizableItem(cs.getId(), cs.getName()));

        return data;
    }
}
