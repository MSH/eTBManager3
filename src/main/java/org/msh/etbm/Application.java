package org.msh.etbm;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.entities.impl.DozerEntityConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * eTBM bootstrap and entry point class
 *
 * Created by rmemoria on 7/5/15.
 */
@SpringBootApplication
@PropertySource("file:./etbmanager.properties")
@EnableJpaRepositories(value="org.msh.etbm.db.repositories")
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
    public DozerBeanMapper mapper(DozerEntityConverter entityConverter) {
        DozerBeanMapper m = new DozerBeanMapper();

        List<String> lst = new ArrayList<>();
        lst.add("dozer/config.mapper.xml");
        lst.add("dozer/global.mapper.xml");
        lst.add("dozer/adminunit.mapper.xml");
        lst.add("dozer/unit.mapper.xml");
        m.setMappingFiles(lst);

        Map<String, CustomConverter> convs = new HashMap<>();
        convs.put("entity-id", entityConverter);
        m.setCustomConvertersWithId(convs);

        return m;
    }

}
