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

package common.cms;

import common.web.ControllerConfig;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class CmsConfig extends ControllerConfig implements ICmsConfig{
	private String navigationTemplate;
	private String contentViewTemplate;
	//private String contentFilterTemplate;
	private String contentInsertTemplate;
	private String contentUpdateTemplate;
	private String contentMultiupdateTemplate;
	private String nameRu;
    private String contentUrlAttribute = "content_url";
    private String navigationUrlAttribute = "navigation_url";


	@Override
	public String getContentViewTemplate() {return this.contentViewTemplate;}
	public void setContentViewTemplate(String value){this.contentViewTemplate = value;}

	@Override
	public String getNavigationTemplate() {return navigationTemplate;}
	public void setNavigationTemplate(String navigationTemplate) {this.navigationTemplate = navigationTemplate;}

	@Override
	public String getNameRu() {return nameRu;}
	public void setNameRu(String nameRu) {this.nameRu = nameRu;}

	@Override
	public String getContentInsertTemplate() {return contentInsertTemplate;}
	public void setContentInsertTemplate(String contentInsertTemplate) {this.contentInsertTemplate = contentInsertTemplate;}

	@Override
	public String getContentUpdateTemplate() {return contentUpdateTemplate;}
	public void setContentUpdateTemplate(String contentUpdateTemplate) {this.contentUpdateTemplate = contentUpdateTemplate;}

	//@Override
	//public String getContentFilterTemplate() {return contentFilterTemplate;}
	//public void setContentFilterTemplate(String contentFilterTemplate) {this.contentFilterTemplate = contentFilterTemplate;}

	@Override
	public String getContentMultiupdateTemplate() {return contentMultiupdateTemplate;}
	public void setContentMultiupdateTemplate(String contentMultiupdateTemplate) {this.contentMultiupdateTemplate = contentMultiupdateTemplate;}

    @Override
    public String getContentUrlAttribute() {return contentUrlAttribute;}
    public void setContentUrlAttribute(String value) {this.contentUrlAttribute = value;}

    @Override
    public String getNavigationUrlAttribute() {return navigationUrlAttribute;}
    public void setNavigationUrlAttribute(String value) {this.navigationUrlAttribute = value;}

}
