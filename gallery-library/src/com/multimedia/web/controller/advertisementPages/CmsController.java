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

package com.multimedia.web.controller.advertisementPages;

import com.multimedia.model.beans.AdvertisementPages;
import common.cms.ICmsConfig;
import common.cms.services2.ICmsService;
import core.cms.controller2.AGenericPagesCmsController;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Controller(value="advertisementPagesCmsController")
public class CmsController extends AGenericPagesCmsController<AdvertisementPages>{
	protected final List<String> filterProperties = java.util.Arrays.asList("id_advertisement_nav", "id_pages_nav");
	protected final List<String> filterAliases = java.util.Arrays.asList("this.id_advertisement", "this.id_pages");

	@ModelAttribute
    public void populateAdvertisementPagesParams(@RequestParam(value="id_advertisement_nav", required=false) Long id_advertisement_nav, Map<String, Object> model) {
		model.put("id_advertisement_nav", id_advertisement_nav);
    }

	//-------------------------------------- overriden methods from parent ------------------------

	@Override
	public List<String> getFilterProperties() {return filterProperties;}
	@Override
	public List<String> getFilterAliases() {return filterAliases;}

	//-------------------------------------- initialization ---------------------------------------

	@Resource(name="advertisementPagesConfig")
	public void setConfig(ICmsConfig config) {this.config = config;}

	@Resource(name="cmsAdvertisementPagesService")
	public void setCmsService(ICmsService<AdvertisementPages, Long> cmsService) {
		this.cmsService = cmsService;
	}
}
