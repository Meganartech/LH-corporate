package com.knowledgeVista.User.SecurityConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("institutionBlocked");
    }
}



//@Autowired
//private CacheManager cacheManager;
//
//public void updateAdminStatus(String institutionName, boolean isActive) {
//    // ... update DB status
//    cacheManager.getCache("institutionBlocked").evict("institutionBlocked::" + institutionName);
//}

