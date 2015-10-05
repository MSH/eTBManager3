package org.msh.etbm;

import org.dozer.DozerBeanMapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;


/**
 * eTBM bootstrap and entry point class
 *
 * Created by rmemoria on 7/5/15.
 */
@SpringBootApplication
@PropertySource("file:./etbmanager.properties")
public class Application {

    /**
     * Application entry-point
     * @param args
     */
    public static void main(String[] args) {
        // run app
        SpringApplication.run(Application.class, args);
    }

    /**
     * Configure Dozer lib to be used in the application
     * @return instance of DozerBeanMapper
     */
    @Bean
    public DozerBeanMapper mapper() {
        DozerBeanMapper m = new DozerBeanMapper();

        List<String> lst = new ArrayList<>();
        lst.add("dozer/global.mapper.xml");
        m.setMappingFiles(lst);

        return m;
    }

}
