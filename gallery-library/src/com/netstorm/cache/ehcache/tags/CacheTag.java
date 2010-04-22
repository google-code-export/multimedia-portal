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
 
package com.netstorm.cache.ehcache.tags;

/**
 *
 * @author demchuck.dima@gmail.com
 */

public class CacheTag extends ACacheTag {
	/** 
	 * if no keygenerator is specified for this tag
	 * it will have only one cache for all query parameters
	 *
	 */
	public static final String NOKEYGEN_VALUE = "single";
	protected String cache_key;

	@Override
	protected Object generateKey(){return cache_key==null?NOKEYGEN_VALUE:cache_key;}

	/**
	 * the key of cache entry
	 * @param key
	 */
	public void setCacheKey(String key) {this.cache_key = key;}
    
}
