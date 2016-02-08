package org.msh.etbm.commons.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Scan for beans that implements the {@link FormRequestHandler} interface and auto register them
 *
 * Created by rmemoria on 5/2/16.
 */
@Configuration
public class FormConfiguration implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormConfiguration.class);

    @Autowired
    FormService formService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

        LOGGER.info("Scanning form type handlers");

        List<String> names = new ArrayList<>();

        for (String beanName: applicationContext.getBeanNamesForType(FormRequestHandler.class)) {
            FormRequestHandler handler = (FormRequestHandler)applicationContext.getBean(beanName);
            formService.registerRequestHandler(handler);

            names.add(handler.getFormCommandName());
        }

        String s = String.join(", ", names);
        LOGGER.info("Form request types found: " + s);
    }
}
