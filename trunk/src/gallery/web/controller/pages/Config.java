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

package gallery.web.controller.pages;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Config extends common.web.ControllerConfig implements common.web.IControllerConfig, core.IPagesConfig{
	public static final String CURRENT_PAGE_ATTRIBUTE = "page";
	public static final String PAGE_KEEP_PARAMETERS = "page_params";

	public static final String[] NAVIGATION_PSEUDONYMES = new String[]{"id","id_pages","name","type"};

	private common.web.IControllerConfig conf;
	private String site_config_attr = "site_config";

    private String id_pagesParamName = "id_pages_nav";

	public void setConf(common.web.IControllerConfig conf) {this.conf = conf;}

	@Override
	public String getId_pagesParamName() {return id_pagesParamName;}
	public void setId_pagesParamName(String value) {this.id_pagesParamName = value;}

	public String getSiteConfigAttribute() {return this.site_config_attr;}
	public void setSiteConfigAttribute(String value) {this.site_config_attr = value;}

}
