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

package com.multimedia.service.advertisementPages;

import com.multimedia.model.beans.Advertisement;
import common.services.generic.IGenericService;
import com.multimedia.model.beans.AdvertisementPages;
import com.multimedia.service.advertisement.IAdvertisementService;
import common.cms.services2.AGenericCmsService;
import gallery.model.active.PagesCombobox;
import gallery.service.pages.IPagesService;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="cmsAdvertisementPagesService")
public class CmsAdvertisementPagesServiceImpl extends AGenericCmsService<AdvertisementPages, Long>{
	protected IGenericService<Advertisement, Long> advertisement_service;
	protected IPagesService pages_service;

	public final String[] ORDER_BY = new String[]{"id_pages", "id_advertisement"};
	public final String[] ORDER_HOW = new String[]{"asc", "asc"};

	public final String[] ADV_PROPS = new String[]{"id", "name"};
	public final String[] PROPS_ORDER_BY = new String[]{"name"};
	public final String[] PROPS_ORDER_HOW = new String[]{"asc"};

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(advertisement_service, "advertisement_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public AdvertisementPages getInsertBean(AdvertisementPages a) {
		if (a==null)
			a = new AdvertisementPages();
		a.setUseInChildren(Boolean.TRUE);
		a.setAllow(Boolean.TRUE);
		return a;
	}

	@Override
	public void initInsert(Map<String, Object> model) {
		model.put("advertisements", advertisement_service.getOrdered(ADV_PROPS, PROPS_ORDER_BY, PROPS_ORDER_HOW));
	}

	@Override
	public void initUpdate(Map<String, Object> model) {
		model.put("advertisements", advertisement_service.getOrdered(ADV_PROPS, PROPS_ORDER_BY, PROPS_ORDER_HOW));
	}

	@Override
	public void initFilter(Map<String, Object> model) {
		model.put("advertisements", advertisement_service.getOrdered(ADV_PROPS, PROPS_ORDER_BY, PROPS_ORDER_HOW));
		model.put("categories_wallpaper_select", new PagesCombobox(null, pages_service));
	}

	@Override
	public String[] getListOrderBy() {return ORDER_BY;}

	@Override
	public String[] getListOrderHow() {return ORDER_HOW;}

//------------------------------------ initialization ----------------------------------
	@Resource(name="advertisementPagesService")
	public void setService(IGenericService<AdvertisementPages, Long> value) {
		this.service = value;
	}
	@Resource(name="advertisementService")
	public void setAdvertisementService(IAdvertisementService value) {
		this.advertisement_service = value;
	}
	@Resource(name="pagesServices")
	public void setPagesService(IPagesService value) {
		this.pages_service = value;
	}

}