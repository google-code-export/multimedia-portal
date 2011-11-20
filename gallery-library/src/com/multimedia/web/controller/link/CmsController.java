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

package com.multimedia.web.controller.link;

import com.multimedia.model.beans.Link;
import common.cms.ICmsConfig;
import common.cms.services2.ICmsService;
import core.cms.controller2.AGenericPagesCmsController;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Controller(value="linkCmsController")
public class CmsController extends AGenericPagesCmsController<Link>{
	protected final List<String> filterProperties = java.util.Arrays.asList("id_item_nav");
	protected final List<String> filterAliases = java.util.Arrays.asList("this.id_item");

	@ModelAttribute
    public void populateLinkParams(@RequestParam(value="id_item_nav", required=false) Long id_item_nav, Map<String, Object> model) {
		model.put("id_item_nav", id_item_nav);
    }

	//-------------------------------------- overriden methods from parent ------------------------

	@Override
	public List<String> getFilterProperties() {return filterProperties;}
	@Override
	public List<String> getFilterAliases() {return filterAliases;}

	//-------------------------------------- initialization ---------------------------------------

	@Resource(name="linkConfig")
	public void setConfig(ICmsConfig config) {this.config = config;}

	@Resource(name="cmsLinkService")
	public void setCmsService(ICmsService<Link, Long> cmsService) {
		this.cmsService = cmsService;
	}

}
