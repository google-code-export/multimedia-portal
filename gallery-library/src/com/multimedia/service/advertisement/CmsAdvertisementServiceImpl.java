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

package com.multimedia.service.advertisement;

import common.services.generic.IGenericService;
import com.multimedia.model.beans.Advertisement;
import common.cms.services2.AGenericCmsService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="cmsAdvertisementService")
public class CmsAdvertisementServiceImpl extends AGenericCmsService<Advertisement, Long>{
	protected IAdvertisementService advertisement_service;

	public final String[] ORDER_BY = new String[]{"sort"};
	public final String[] ORDER_HOW = new String[]{"asc"};
	public final List<String> CMS_SHORT_ALIAS = java.util.Arrays.asList("id","name","sort","active","position");

	@Override
	public int saveOrUpdateCollection(Collection<Advertisement> c) {
		return service.updateCollection(c, "sort", "active", "position");
	}

	@Override
	public Advertisement getInsertBean(Advertisement a) {
		if (a==null)
			a = new Advertisement();
		Long sort = (Long) service.getSinglePropertyU("max(sort)");
		if (sort == null) sort = Long.valueOf(0);
		else sort++;
		a.setSort(sort);
		a.setActive(Boolean.TRUE);
		return a;
	}

	@Override
	public void initUpdate(Map<String, Object> model) {
		model.put("positions", this.advertisement_service.getPositions());
	}

	@Override
	public void initInsert(Map<String, Object> model) {
		model.put("positions", this.advertisement_service.getPositions());
	}

	@Override
	public void initView(Map<String, Object> model) {
		model.put("positions", this.advertisement_service.getPositions());
	}

	@Override
	public String[] getListOrderBy() {return ORDER_BY;}

	@Override
	public String[] getListOrderHow() {return ORDER_HOW;}

	@Override
	public List<String> getListPropertyNames() {return CMS_SHORT_ALIAS;}

	@Override
	public List<String> getListPropertyAliases() {return CMS_SHORT_ALIAS;}

//------------------------------------ initialization ----------------------------------
	@Resource(name="advertisementService")
	public void setService(IGenericService<Advertisement, Long> value) {
		if (value instanceof IAdvertisementService){
			this.advertisement_service = (IAdvertisementService)value;
		}
		this.service = advertisement_service;
	}

}