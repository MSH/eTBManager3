package org.msh.etbm.services.sys;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.db.dto.SystemConfigDTO;
import org.msh.etbm.db.entities.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * Created by rmemoria on 5/10/15.
 */
@Service
public class ConfigurationService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DozerBeanMapper mapper;

    private SystemConfigDTO systemConfig;

    /**
     * Return configuration about the system
     * @return information about system configuration
     */
    @Bean
    @Transactional
    public SystemConfigDTO systemConfig() {
        if (systemConfig != null) {
            return systemConfig;
        }

        SystemConfig cfg = getSystemConfig();

        systemConfig =  mapper.map(cfg, SystemConfigDTO.class);

        return systemConfig;
    }

    /**
     * Return system configuration
     * @return
     */
    private SystemConfig getSystemConfig() {
        SystemConfig cfg = entityManager.find(SystemConfig.class, 1);

        if (cfg == null) {
            cfg = new SystemConfig();
            cfg.setId(1);
            cfg.setUlaActive(true);
            cfg.setAllowRegPage(false);

            entityManager.persist(cfg);
            entityManager.flush();
        }

        return cfg;
    }

    /**
     * Save changes to the system configuration
     */
    @Transactional
    public void updateConfiguration() {
        if (systemConfig == null) {
            return;
        }

        SystemConfig cfg = getSystemConfig();

        mapper.map(systemConfig, cfg);

        entityManager.persist(cfg);
        entityManager.flush();
    }
}
