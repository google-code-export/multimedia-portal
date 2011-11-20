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

package com.multimedia.service.locale;

import com.multimedia.model.beans.Locale;
import common.cms.services2.AGenericCmsService;
import common.services.generic.IGenericService;
import java.util.Collection;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="cmsLocaleService")
public class CmsLocaleServiceImpl extends AGenericCmsService<Locale, Long>{

//----------------------------- methods ---------------------------------------------------

	@Override
	public Locale getInsertBean(Locale obj) {
		if (obj==null)
			obj = new Locale();
		Long sort = (Long) service.getSinglePropertyU("max(sort)");
		if (sort == null) sort = Long.valueOf(0);
		else sort++;
		obj.setSort(sort);
		obj.setActive(Boolean.TRUE);
		return obj;
	}

	@Override
	public int saveOrUpdateCollection(Collection<Locale> c) {return service.updateCollection(c, "active", "sort");}

	@Override
	public String[] getListOrderBy() {return com.multimedia.config.LocaleConfig.ORDER_BY;}

	@Override
	public String[] getListOrderHow() {return com.multimedia.config.LocaleConfig.ORDER_HOW;}

//---------------------------  services ------------------------------------------------
	@Resource(name="localeService")
	public void setService(IGenericService<Locale, Long> service){this.service = service;}
}
