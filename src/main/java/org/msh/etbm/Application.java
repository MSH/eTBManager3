package org.msh.etbm;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.entities.DozerEntityConverter;
import org.msh.etbm.commons.entities.DozerEnumConverter;
import org.msh.etbm.services.admin.admunits.impl.DozerAdminUnitConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * eTBM bootstrap and entry point class
 * <p>
 * Created by rmemoria on 7/5/15.
 */
@SpringBootApplication
@PropertySource("file:${app-property-file:./etbmanager.properties}")
@EnableJpaRepositories(value = "org.msh.etbm.db.repositories")
@EnableCaching
public class Application {

    /**
     * Application entry-point
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Configure Dozer lib to be used in the application
     *
     * @return instance of DozerBeanMapper
     */
    @Bean
    public DozerBeanMapper mapper(DozerEntityConverter entityConverter, DozerAdminUnitConverter admconv, DozerEnumConverter enumConverter) {
        DozerBeanMapper m = new DozerBeanMapper();

        List<CustomConverter> customConverters = new ArrayList<>();
        customConverters.add(admconv);
        customConverters.add(enumConverter);
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
        lst.add("dozer/followup.mapper.xml");
        lst.add("dozer/case.mapper.xml");
        m.setMappingFiles(lst);

        Map<String, CustomConverter> convs = new HashMap<>();
        convs.put("entity-id", entityConverter);
        convs.put("adminunit", admconv);
        convs.put("enum-item", enumConverter);

        m.setCustomConvertersWithId(convs);

        return m;
    }
}
