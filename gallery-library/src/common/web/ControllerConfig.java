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

package common.web;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class ControllerConfig implements IControllerConfig{
	protected String templateUrl;
	/** param whitch stores action i.e. if it equals to an appropriate value execute an action(update,insert) */
	public static final String ACTION_PARAM_NAME = "action";
	/** attribute with data for navigation (array or vector for example) */
	protected String navigationDataAttribute = "pages_navigation";
	/** attribute where to set url with content */
	protected String contentDataAttribute = "content_data";
	/** attribute where to set url with content */
	protected String urlAttribute = "url";

	/**
	 * copy values from given config
	 * @param conf
	 */
	public void setConfig(IControllerConfig conf){templateUrl = conf.getTemplateUrl();}

	@Override
	public String getNavigationDataAttribute() {return navigationDataAttribute;}

	@Override
	public String getTemplateUrl() {return templateUrl;}
	public void setTemplateUrl(String templateUrl) {this.templateUrl = templateUrl;}

	@Override
	public String getUrlAttribute() {return this.urlAttribute;}
	public void setUrlAttribute(String value) {this.urlAttribute = value;}

	@Override
	public String getContentDataAttribute() {return contentDataAttribute;}
	public void setContentDataAttribute(String value) {this.contentDataAttribute = value;}

}
