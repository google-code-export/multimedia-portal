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

package gallery.web.controller.cms;

import common.cms.controller.FilteredCmsDelegate;
import gallery.service.pages.IPagesService;
import gallery.web.controller.pages.Config;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class CommonPagesCmsDelegate<T> extends FilteredCmsDelegate<T>{
	protected IPagesService pages_service;
	protected String id_pagesParamName;

	@Override
	public Map<String, Object> getCommonModel(HttpServletRequest req) {
		Map map = super.getCommonModel(req);

		Long id_pages_nav = common.utils.RequestUtils.getLongParam(req, id_pagesParamName);
		map.put(config.getNavigationDataAttribute(), pages_service.getAllPagesParents(id_pages_nav, Config.NAVIGATION_PSEUDONYMES));
		return map;
	}

	public void setPages_service(IPagesService service){this.pages_service = service;}
	public void setId_pagesParamName(String value){this.id_pagesParamName = value;}
}
