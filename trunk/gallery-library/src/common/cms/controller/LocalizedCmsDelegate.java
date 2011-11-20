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

package common.cms.controller;

import com.netstorm.localization.LocaleDataCms;
import common.cms.services.ILocalizedCmsService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class LocalizedCmsDelegate<T> extends ACmsDelegate<T>{
	/** service for working with data */
	protected ILocalizedCmsService<T, Long> service;

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	/**
	 * must show items associated with this table
	 * @param req
	 * @param resp
	 * @return
	 */
	@Override
	public ModelAndView doView(HttpServletRequest request, HttpServletResponse resp){
			//logger.fine("do=view");
			getCommonModel(request);
			request.setAttribute(config.getContentUrlAttribute(), config.getContentViewTemplate());
			LocaleDataCms data = new LocaleDataCms();
			List<T> cur = service.getCurrentLocalization(RequestContextUtils.getLocale(request).getLanguage());
			data.setCurrent(cur);
			data.setOther(service.getOtherLocalization(RequestContextUtils.getLocale(request).getLanguage(), cur));
			request.setAttribute(config.getContentDataAttribute(), data);
			return new ModelAndView(config.getTemplateUrl());
    }

	public void setService(ILocalizedCmsService<T, Long> service){
		this.service = service;
		super.deleteService = service;
		super.filterService = service;
		super.insertService = service;
		super.multiUpdateService = service;
		super.updateService = service;
	}
}
