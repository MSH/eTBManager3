package org.msh.etbm.sys.daemon;


/**
 * Start a Spring Boot application as a service.
 *
 * @author Stephane Nicoll
 */
public class StartSpringBootService {

    /**
     * Private constructor to avoid instantiation of this class by mistake
     */
    private StartSpringBootService() {

    }

    public static void main(String[] args) throws Exception {
        new SpringBootService().start(args);
    }

}
