package org.msh.etbm.services.admin.admunits.parents;

import org.dozer.CustomConverter;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.services.usersession.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by rmemoria on 17/12/15.
 */
@Component
public class DozerAdminUnitSeriesConverter implements CustomConverter {

    @Autowired
    UserRequest userRequest;

    @Autowired
    ParentAdmUnitsService parentAdmUnitsService;

    @Override
    public Object convert(Object dest, Object source, Class<?> destClass, Class<?> sourceClass) {
        if (source == null) {
            return null;
        }

        if (source instanceof AdministrativeUnit) {
            return convertToSeries((AdministrativeUnit) source);
        }

        return null;
    }


    /**
     * Convert an administrative unit in a series of parent administrative units
     * @param adm the administrative unit to be converted
     * @return the administrative unit series
     */
    public AdminUnitSeries convertToSeries(AdministrativeUnit adm) {
        // check if admin unit is in the request data
        // the request data is a place where components may include data to be used later by other components
        List<AdminUnitSeries> seriesList = (List<AdminUnitSeries>)userRequest.get(UserRequest.KEY_ADMUNITSERIES_LIST);

        // series list exists ?
        if (seriesList != null) {
            // check in the series list
            for (AdminUnitSeries au: seriesList) {
                SynchronizableItem item = au.getAdminUnit();
                if (item != null && adm.getId().equals(item.getId())) {
                    return au;
                }
            }
        }

        return parentAdmUnitsService.getAdminUnitSeries(adm);
    }
}
