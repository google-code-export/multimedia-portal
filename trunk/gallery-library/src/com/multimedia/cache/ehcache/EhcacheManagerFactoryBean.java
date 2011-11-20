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

package com.multimedia.cache.ehcache;

import java.io.IOException;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Status;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class EhcacheManagerFactoryBean implements InitializingBean, DisposableBean {

	protected final Logger logger = Logger.getLogger(getClass());

	private Resource configLocation;

	private boolean shared = false;

	private CacheManager cacheManager;


	/**
	 * Set the location of the EHCache config file. A typical value is "/WEB-INF/ehcache.xml".
	 * <p>Default is "ehcache.xml" in the root of the class path, or if not found,
	 * "ehcache-failsafe.xml" in the EHCache jar (default EHCache initialization).
	 */
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	/**
	 * Set whether the EHCache CacheManager should be shared (as a singleton at the VM level)
	 * or independent (typically local within the application). Default is "false", creating
	 * an independent instance.
	 * <p>Note that independent CacheManager instances are only available on EHCache 1.2 and
	 * higher. Switch this flag to "true" if you intend to run against an EHCache 1.1 jar.
	 */
	public void setShared(boolean shared) {this.shared = shared;}

	@Override
	public void afterPropertiesSet() throws IOException, CacheException {
		//logger.info("shared = " + this.shared);
		if (this.shared) {
			// Shared CacheManager singleton at the VM level.
			if (this.configLocation != null) {
				logger.info("Initializing EHCache CacheManager from '"+this.configLocation.getFile().getPath()+"'");
				this.cacheManager = CacheManager.create(this.configLocation.getFile().getPath());
			} else {
				logger.info("Initializing EHCache CacheManager from default ehcache configuration");
				this.cacheManager = CacheManager.create();
			}
		}
		else {
			// Independent CacheManager instance (the default).
			if (this.configLocation != null) {
				logger.info("Initializing EHCache CacheManager from '"+this.configLocation.getFile().getPath()+"'");
				this.cacheManager = new CacheManager(this.configLocation.getFile().getPath());
			} else {
				logger.info("Initializing EHCache CacheManager from default ehcache configuration");
				this.cacheManager = new CacheManager();
			}
		}
	}

	@Override
	public void destroy() {
		if (this.shared){
			if (CacheManager.getInstance()!=null){
				logger.info("Shutting down shared EHCache CacheManager:"+CacheManager.getInstance());
				CacheManager.getInstance().shutdown();
			}
		}
		if (this.cacheManager!=null&&Status.STATUS_ALIVE.equals(this.cacheManager.getStatus())){
			logger.info("Shutting down EHCache CacheManager:"+this.cacheManager);
			this.cacheManager.shutdown();
			this.cacheManager = null;
		}
	}

	public CacheManager getObject() throws Exception {return this.cacheManager;}
}
