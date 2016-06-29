package org.msh.etbm;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Configuration of the cache mechanism in the application. Executed during application bootstrap
 * <p>
 * Created by rmemoria on 14/11/15.
 */
@Component
@Configuration
public class CacheConfiguration {

    /**
     * Key used for caching sessions
     */
    public static final String CACHE_SESSION_ID = "session";
    public static final Integer CACHE_SESSION_TIMEOUT_MIN = 5;


    /**
     * Configure the cache manager
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        // configuration of the session ID
        GuavaCache sessionCache = new GuavaCache(CACHE_SESSION_ID, CacheBuilder.newBuilder()
                .expireAfterAccess(CACHE_SESSION_TIMEOUT_MIN, TimeUnit.MINUTES)
                .build());
        cacheManager.setCaches(Arrays.asList(sessionCache));
        return cacheManager;
    }
}
