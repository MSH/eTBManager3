package org.msh.etbm.sys.daemon;


import org.springframework.boot.SpringApplication;
import org.springframework.util.ClassUtils;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import java.io.IOException;

/**
 * Basic daemon implementation for a Spring Boot app.
 *
 * @author Stephane Nicoll
 */
public class SpringBootService {

    public void start(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalStateException("Spring Boot application class must be provided.");
        }
        Class<?> springBootApp = ClassUtils.resolveClassName(args[0],
                SpringBootService.class.getClassLoader());
        System.out.println("Starting Spring Boot application [" + springBootApp.getName() + "]");
        SpringApplication.run(springBootApp);
    }

    public void stop(String[] args) throws IOException {
        System.out.println("Stopping Spring Boot application...");
        int jmxPort = Integer.parseInt(args[0]);
        String jmxName = SpringApplicationAdminClient.DEFAULT_OBJECT_NAME;
        JMXConnector connector = SpringApplicationAdminClient.connect(jmxPort);
        try {
            MBeanServerConnection connection = connector.getMBeanServerConnection();
            try {
                new SpringApplicationAdminClient(connection, jmxName).stop();
            } catch (InstanceNotFoundException ex) {
                throw new IllegalStateException("Spring application lifecycle JMX bean not " +
                        "found, could not stop application gracefully", ex);
            }
        } finally {
            connector.close();
        }
    }

}