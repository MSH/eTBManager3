package org.msh.etbm.services.admin.sysconfig;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.db.entities.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 * Service to recover and change system configuration
 *
 * Created by rmemoria on 6/4/16.
 */
@Service
public class SysConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigService.class);

    @Autowired
    DozerBeanMapper mapper;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Update system configuration based on give data
     * @param data instance of {@link SysConfigFormData} containing the configuration data to be updated
     */
    public void updateConfig(SysConfigFormData data) {

    }

    /**
     * Read the configuration of the system
     * @return instance of {@link SysConfigFormData}
     */
    public SysConfigFormData readConfig() {
        SystemConfig conf;
        try {
            conf = entityManager.find(SystemConfig.class, SystemConfig.PRIMARY_KEY);
        } catch (EntityNotFoundException e) {
            LOGGER.warn("System configuration not found");
            conf = new SystemConfig();
        }

        SysConfigFormData data = mapper.map(conf, SysConfigFormData.class);
        return data;
    }

}
