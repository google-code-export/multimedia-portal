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

package com.multimedia.service.commonItem;

import com.multimedia.model.beans.CommonItem;
import common.services.generic.IGenericService;
import core.cms.services2.AGenericPagesCmsService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="cmsCommonItemService")
public class CmsCommonItemServiceImpl extends AGenericPagesCmsService<CommonItem, Long>{
	protected final List<String> CMS_SHORT_PROPS = java.util.Arrays.asList("id", "active", "views", "title", "substring(description, 1, 20)", "substring(tags, 1, 20)", "pages");
	protected final List<String> CMS_SHORT_ALIAS = java.util.Arrays.asList("id", "active", "views", "title", "description", "tags", "pages");

	/**
	 * name of attribute with pageses for selecting as current item pages
	 */
	public static final String CATEGORIES_ATTRIBUTE = "categories";

	@Override
	public CommonItem getInsertBean(CommonItem obj) {
		if (obj==null)
			obj = new CommonItem();
		obj.setActive(Boolean.TRUE);
		obj.setViews(Long.valueOf(0));
		return obj;
	}

	@Override
	public void initInsert(Map<String, Object> model) {
		model.put("categories", pagesService.getAllCombobox(null, Boolean.TRUE, com.multimedia.core.pages.types.CommonItemType.TYPE));
	}

	@Override
	public void initUpdate(Map<String, Object> model) {
		model.put("categories", pagesService.getAllCombobox(null, Boolean.TRUE, com.multimedia.core.pages.types.CommonItemType.TYPE));
	}

	@Override
	public int saveOrUpdateCollection(Collection<CommonItem> c) {return service.updateCollection(c, "active");}

	@Override
	public List<String> getListPropertyNames() {return CMS_SHORT_PROPS;}

	@Override
	public List<String> getListPropertyAliases() {return CMS_SHORT_ALIAS;}

	
	//---------------------------  services -----------------------
	@Resource(name="commonItemService")
	public void setService(IGenericService<CommonItem, Long> service){this.service = service;}

}
