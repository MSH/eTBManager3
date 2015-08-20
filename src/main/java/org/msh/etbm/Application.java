package org.msh.etbm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * eTBM bootstrap and entry point class
 *
 * Created by rmemoria on 7/5/15.
 */
@SpringBootApplication
public class Application {

    /**
     * Application entry-point
     * @param args
     */
    public static void main(String[] args) {
        // evaluate command line arguments
//        String[] newargs = CommandLineArgs.evaluate(args);

        String[] test = {"--spring.datasource.url=jdbc:hsqldb:file:etbmanager;default_schema=true"};
        // run app
        SpringApplication.run(Application.class, test);
    }
}
