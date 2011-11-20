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

package com.multimedia.web.controller.advertisement;

import com.multimedia.model.beans.Advertisement;
import common.cms.ICmsConfig;
import common.cms.controller2.SimpleGenericCmsController;
import common.cms.services2.ICmsService;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Controller(value="advertisementCmsController")
public class CmsController extends SimpleGenericCmsController<Advertisement>{

	@Resource(name="advertisementConfig")
	public void setConfig(ICmsConfig config) {this.config = config;}

	@Resource(name="cmsAdvertisementService")
	public void setCmsService(ICmsService<Advertisement, Long> cmsService) {
		this.cmsService = cmsService;
	}
}
