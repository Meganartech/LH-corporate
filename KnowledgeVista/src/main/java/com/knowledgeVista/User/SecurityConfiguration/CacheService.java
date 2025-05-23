package com.knowledgeVista.User.SecurityConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
	 @Autowired
	    private CacheManager cacheManager;

	    public void updateAdminStatus(String institutionName) {
	        // update DB status...
	        cacheManager.getCache("institutionBlocked").evict("institutionBlocked::" + institutionName);
	    }

}
