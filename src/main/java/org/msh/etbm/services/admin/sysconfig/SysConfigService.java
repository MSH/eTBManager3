package org.msh.etbm.services.admin.sysconfig;

import org.msh.etbm.commons.entities.EntityValidationException;
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
    public SysConfigFormData loadFormConfig() {
        EntityDAO<SystemConfig> dao = getEntityDAO();

        return dao.mapFromEntity(SysConfigFormData.class);
    }

    @Transactional
    public SysConfigData loadConfig() {
        EntityDAO<SystemConfig> dao = getEntityDAO();

        return dao.mapFromEntity(SysConfigData.class);
    }

    /**
     * Return the instance of {@link EntityDAO} managing the configuration
     * @return
     */
    private EntityDAO<SystemConfig> getEntityDAO() {
        EntityDAO<SystemConfig> dao = entityDAOFactory.newDAO(SystemConfig.class);

        dao.setIdIfExists(SystemConfig.PRIMARY_KEY);

        return dao;
    }

    /**
     * Update system configuration based on give data
     *
     * @param data instance of {@link SysConfigFormData} containing the configuration data to be updated
     */
    @Transactional
    public void updateConfig(SysConfigFormData data) {
        EntityDAO<SystemConfig> dao = getEntityDAO();

        // config exists ?
        if (dao.isNew()) {
            // if config doesn't exist, set the primary key for the new config
            dao.getEntity().setId(SystemConfig.PRIMARY_KEY);
        }

        dao.mapToEntity(data);

        validate(dao);

        SystemConfig config = dao.getEntity();
        if (!config.isAllowRegPage()) {
            config.setWorkspace(null);
            config.setUnit(null);
            config.setUserProfile(null);
        }

        dao.save();
    }


    /**
     * Validate the data
     * @param dao The instance of {@link EntityDAO} containing the data to be validated
     */
    private void validate(EntityDAO<SystemConfig> dao) {
        SystemConfig cfg = dao.getEntity();

        if (cfg.isAllowRegPage() &&
                (cfg.getWorkspace() == null || cfg.getUnit() == null || cfg.getUserProfile() == null)) {
            throw new EntityValidationException(cfg,
                    "allowRegPage",
                    "To allow self-registration, the workspace, unit and user profile must be selected",
                    null);
        }
    }
}
