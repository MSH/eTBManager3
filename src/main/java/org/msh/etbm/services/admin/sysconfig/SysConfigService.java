package org.msh.etbm.services.admin.sysconfig;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.db.entities.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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

    @Autowired
    Validator validator;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Update system configuration based on give data
     * @param data instance of {@link SysConfigFormData} containing the configuration data to be updated
     */
    public void updateConfig(SysConfigFormData data) {
        SystemConfig conf = loadConfig();
        mapper.map(data, conf);
        BindingResult bindingResult = new BeanPropertyBindingResult(conf, SystemConfig.class.getSimpleName());

        validator.validate(data, bindingResult);

    }

    /**
     * Read the configuration of the system
     * @return instance of {@link SysConfigFormData}
     */
    public SysConfigFormData readConfig() {
        SystemConfig conf = loadConfig();

        SysConfigFormData data = mapper.map(conf, SysConfigFormData.class);
        return data;
    }


    /**
     * Load the system configuration
     * @return
     */
    private SystemConfig loadConfig() {
        try {
            return entityManager.find(SystemConfig.class, SystemConfig.PRIMARY_KEY);
        } catch (EntityNotFoundException e) {
            LOGGER.warn("System configuration not found");
            return new SystemConfig();
        }
    }

}
