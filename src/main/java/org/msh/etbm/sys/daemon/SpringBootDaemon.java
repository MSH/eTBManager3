package org.msh.etbm.sys.daemon;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ClassUtils;

import java.util.Arrays;

/**
 * Basic {@link Daemon} implementation for a Spring Boot app. Only for demonstration
 * purposes as Spring Boot 1.3 has a much better support for this.
 *
 * @author Stephane Nicoll
 */
public class SpringBootDaemon implements Daemon {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringBootDaemon.class);

    private Class<?> springBootApp;

    private ConfigurableApplicationContext content;

    @Override
    public void init(DaemonContext context) throws Exception {
        LOGGER.info("Daemon initialized with arguments [" +
                Arrays.toString(context.getArguments()) + "]");
        this.springBootApp = ClassUtils.resolveClassName(context.getArguments()[0],
                SpringBootDaemon.class.getClassLoader());
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("Starting Spring Boot application [" + this.springBootApp.getName() + "]");
        this.content = SpringApplication.run(springBootApp);
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Stopping Spring Boot application [" + this.springBootApp.getName() + "]");
        this.content.close();
    }

    @Override
    public void destroy() {

    }

}