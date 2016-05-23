package org.msh.etbm;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.entities.DozerEntityConverter;
import org.msh.etbm.services.admin.admunits.parents.DozerAdminUnitSeriesConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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
@EnableJpaRepositories(value = "org.msh.etbm.db.repositories")
@EnableCaching
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
    public DozerBeanMapper mapper(DozerEntityConverter entityConverter, DozerAdminUnitSeriesConverter admconv) {
        DozerBeanMapper m = new DozerBeanMapper();

        List<CustomConverter> customConverters = new ArrayList<>();
        customConverters.add(admconv);
        m.setCustomConverters(customConverters);

        List<String> lst = new ArrayList<>();
        lst.add("dozer/config.mapper.xml");
        lst.add("dozer/global.mapper.xml");
        lst.add("dozer/adminunit.mapper.xml");
        lst.add("dozer/unit.mapper.xml");
        lst.add("dozer/source.mapper.xml");
        lst.add("dozer/product.mapper.xml");
        lst.add("dozer/substance.mapper.xml");
        lst.add("dozer/workspace.mapper.xml");
        lst.add("dozer/userws.mapper.xml");
        lst.add("dozer/regimen.mapper.xml");
        lst.add("dozer/sysconfig.mapper.xml");
        m.setMappingFiles(lst);

        Map<String, CustomConverter> convs = new HashMap<>();
        convs.put("entity-id", entityConverter);
        convs.put("adminunit", admconv);

        m.setCustomConvertersWithId(convs);

        return m;
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.modulesToInstall(Jdk8Module.class);
        return builder;
    }
}
