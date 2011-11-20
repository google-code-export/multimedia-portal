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

package com.multimedia.web.controller.commonItem;

import com.multimedia.model.beans.CommonItem;
import common.cms.ICmsConfig;
import common.cms.services2.ICmsService;
import core.cms.controller2.AGenericPagesCmsController;
import java.util.List;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Controller(value="commonItemCmsController")
public class CmsController extends AGenericPagesCmsController<CommonItem>{
	protected final List<String> filterProperties = java.util.Arrays.asList("id_pages_nav");
	protected final List<String> filterAliases = java.util.Arrays.asList("this.id_pages");

	//-------------------------------------- overriden methods from parent ------------------------

	@Resource(name="commonItemConfig")
	public void setConfig(ICmsConfig config) {this.config = config;}

	@Resource(name="cmsCommonItemService")
	public void setCmsService(ICmsService<CommonItem, Long> cmsService) {
		this.cmsService = cmsService;
	}

	//-------------------------------------- initialization ---------------------------------------

	@Override
	public List<String> getFilterProperties() {return filterProperties;}
	@Override
	public List<String> getFilterAliases() {return filterAliases;}

}
