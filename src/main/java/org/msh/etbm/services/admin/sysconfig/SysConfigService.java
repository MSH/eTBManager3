package org.msh.etbm.services.admin.sysconfig;

import org.msh.etbm.commons.entities.dao.EntityDAO;
import org.msh.etbm.commons.entities.dao.EntityDAOFactory;
import org.msh.etbm.db.entities.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to recover and change system configuration
 * <p>
 * Created by rmemoria on 6/4/16.
 */
@Service
public class SysConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigService.class);

    @Autowired
    EntityDAOFactory entityDAOFactory;


    /**
     * Load the system configuration
     *
     * @return instance of {@link SysConfigFormData} containing system configuration
     */
    @Transactional
    public SysConfigFormData loadConfig() {
        EntityDAO<SystemConfig> dao = entityDAOFactory.newDAO(SystemConfig.class);

        dao.setIdIfExists(SystemConfig.PRIMARY_KEY);

        return dao.mapFromEntity(SysConfigFormData.class);
    }

    /**
     * Update system configuration based on give data
     *
     * @param data instance of {@link SysConfigFormData} containing the configuration data to be updated
     */
    @Transactional
    public void updateConfig(SysConfigFormData data) {
        EntityDAO<SystemConfig> dao = entityDAOFactory.newDAO(SystemConfig.class);

        // load config if exists
        if (!dao.setIdIfExists(1)) {
            // if config doesn't exist, set the primary key for the new config
            dao.getEntity().setId(SystemConfig.PRIMARY_KEY);
        }

        dao.mapToEntity(data);

        SystemConfig config = dao.getEntity();
        if (!config.isAllowRegPage()) {
            config.setWorkspace(null);
            config.setUnit(null);
            config.setUserProfile(null);
        }

        dao.save();
    }


}
