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

package core;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PagesCmsConfig extends common.cms.CmsConfig implements common.cms.ICmsConfig, core.IPagesConfig{

    private String id_pagesParamName = "id_pages_nav";
	private String site_config_attr = "site_config";

	protected common.web.IControllerConfig conf;
   
	public void setConf(common.web.IControllerConfig conf) {this.conf = conf;}

	@Override
	public String getNavigationDataAttribute() {return conf.getNavigationDataAttribute();}

	@Override
	public String getTemplateUrl() {return conf.getTemplateUrl();}

	@Override
	public String getUrlAttribute() {return conf.getUrlAttribute();}

	@Override
	public String getContentDataAttribute() {return conf.getContentDataAttribute();}

	public String getSiteConfigAttribute() {return this.site_config_attr;}
	public void setSiteConfigAttribute(String value) {this.site_config_attr = value;}

    @Override
    public String getId_pagesParamName() {return id_pagesParamName;}
    public void setId_pagesParamName(String value) {this.id_pagesParamName = value;}

}
