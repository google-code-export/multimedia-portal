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

package core.cms.services2;

import gallery.model.beans.Pages;
import gallery.service.pages.IPagesService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="pagesCmsNavigationService")
public class PagesCmsNavigationServiceImpl implements IPagesCmsNavigationService{
	protected IPagesService pagesService;

	public void init() {
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(pagesService, "pagesService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public List<Pages> getNavigationData(Long id_pages) {
		return pagesService.getAllPagesParents(id_pages, gallery.web.controller.pages.Config.NAVIGATION_PSEUDONYMES);
	}

	@Resource(name="pagesServices")
	public void setPagesService(IPagesService pagesService){this.pagesService = pagesService;}

}
