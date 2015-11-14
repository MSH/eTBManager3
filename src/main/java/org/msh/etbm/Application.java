package org.msh.etbm;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.cache.CacheBuilder;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.entities.impl.DozerEntityConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * eTBM bootstrap and entry point class
 *
 * Created by rmemoria on 7/5/15.
 */
@SpringBootApplication
@PropertySource("file:./etbmanager.properties")
@EnableJpaRepositories(value="org.msh.etbm.db.repositories")
@EnableCaching
public class Application {

    /**
     * Key used for caching sessions
     */
    public static final String CACHE_SESSION_ID = "session";
    public static final Integer CACHE_SESSION_TIMEOUT_MIN = 5;

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
        lst.add("dozer/source.mapper.xml");
        lst.add("dozer/product.mapper.xml");
        lst.add("dozer/substance.mapper.xml");
        lst.add("dozer/workspace.mapper.xml");
        m.setMappingFiles(lst);

        Map<String, CustomConverter> convs = new HashMap<>();
        convs.put("entity-id", entityConverter);

        m.setCustomConvertersWithId(convs);

        return m;
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.modulesToInstall(Jdk8Module.class);
        return builder;
    }


    /**
     * Configure the cache manager
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        GuavaCache sessionCache = new GuavaCache(CACHE_SESSION_ID, CacheBuilder.newBuilder()
                .expireAfterAccess(CACHE_SESSION_TIMEOUT_MIN, TimeUnit.MINUTES)
                .build());
        cacheManager.setCaches(Arrays.asList(sessionCache));
        return cacheManager;
    }
}
