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

import common.cms.services.ICmsService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class SimpleCmsDelegate<T> extends ACmsDelegate<T>{
	/** service for working with data */
	protected ICmsService<T, Long> service;

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
	public ModelAndView doView(HttpServletRequest req, HttpServletResponse resp){
			//logger.fine("do=view");
			getCommonModel(req);
			req.setAttribute(config.getContentUrlAttribute(), config.getContentViewTemplate());
			req.setAttribute(config.getContentDataAttribute(), service.getAllShortCms());
			return new ModelAndView(config.getTemplateUrl());
    }

	public void setService(ICmsService<T, Long> service){
		this.service = service;
		super.deleteService = service;
		super.filterService = service;
		super.insertService = service;
		super.multiUpdateService = service;
		super.updateService = service;
	}
}