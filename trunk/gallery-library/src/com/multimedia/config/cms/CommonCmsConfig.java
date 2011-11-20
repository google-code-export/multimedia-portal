/*
 *  Copyright 2010 demchuck.dima@gmail.com.
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

package com.multimedia.config.cms;

import common.cms.ICmsConfig;

/**
 * Presents an abstract superclass for all cms configurations.
 * Defines attributes that may be common across all cms configurations.
 * Attributes may be overriden in children.
 * @author demchuck.dima@gmail.com
 */
public abstract class CommonCmsConfig implements ICmsConfig{
    protected final String contentUrlAttribute = "content_url";
    protected final String navigationUrlAttribute = "navigation_url";

	protected final String templateUrl = "/WEB-INF/jsp/cms/main.jsp";

	protected final String contentDataAttribute = "content_data";
	protected final String navigationDataAttribute = "pages_navigation";

	protected final String urlAttribute = "url";

	@Override
	public String getContentUrlAttribute() {return contentUrlAttribute;}

	@Override
	public String getNavigationUrlAttribute() {return navigationUrlAttribute;}

	@Override
	public String getTemplateUrl() {return templateUrl;}

	@Override
	public String getContentDataAttribute() {return contentDataAttribute;}

	@Override
	public String getNavigationDataAttribute() {return navigationDataAttribute;}

	@Override
	public String getUrlAttribute() {return urlAttribute;}

}
