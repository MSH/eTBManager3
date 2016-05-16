package org.msh.etbm.sys.daemon;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
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

    private Class<?> springBootApp;

    private ConfigurableApplicationContext content;

    public void init(DaemonContext context) throws Exception {
        System.out.println("Daemon initialized with arguments [" +
                Arrays.toString(context.getArguments()) + "]");
        this.springBootApp = ClassUtils.resolveClassName(context.getArguments()[0],
                SpringBootDaemon.class.getClassLoader());
    }

    public void start() throws Exception {
        System.out.println("Starting Spring Boot application [" + this.springBootApp.getName() + "]");
        this.content = SpringApplication.run(springBootApp);
    }

    public void stop() throws Exception {
        System.out.println("Stopping Spring Boot application [" + this.springBootApp.getName() + "]");
        this.content.close();
    }

    public void destroy() {

    }

}