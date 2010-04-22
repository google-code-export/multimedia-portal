/*
 *  Copyright 2010 demchuck.dima@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.netstorm.cache.ehcache.service;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.blocking.BlockingCache;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class GenericCachingService {
	protected String region_key;

	protected Object element_key;

    /**
     * The cache holding the web pages. Ensure that all threads for a given cache name are using the same instance of this.
     */
    protected BlockingCache blockingCache;

	public GenericCachingService(String region_key, Object element_key){
		this.region_key = region_key;
		if (element_key==null){
			this.element_key = "single";
		}else{
			this.element_key = element_key;
		}
        synchronized (this.getClass()) {
			Ehcache cache = getCacheManager().getEhcache(region_key);
			if (cache==null){
				throw new NullPointerException("no cache region defined for: "+region_key);
			}
			if (!(cache instanceof BlockingCache)) {
				//decorate and substitute
				BlockingCache newBlockingCache = new BlockingCache(cache);
				getCacheManager().replaceCacheWithDecoratedCache(cache, newBlockingCache);
			}
			blockingCache = (BlockingCache) getCacheManager().getEhcache(region_key);
        }
	}

    /**
     * Gets the CacheManager for this CachingFilter. It is therefore up to subclasses what CacheManager to use.
     * <p/>
     * This method was introduced in ehcache 1.2.1. Older versions used a singleton CacheManager instance created with
     * the default factory method.
     *
     * @return the CacheManager to be used
     * @since 1.2.1
     */
    protected CacheManager getCacheManager() {return CacheManager.getInstance();}
}
