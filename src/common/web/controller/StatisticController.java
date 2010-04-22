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

package common.web.controller;

import common.cms.ICmsConfig;
import common.interceptors.MyInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class StatisticController implements Controller{
	private ICmsConfig conf;
	private MyInterceptor service;

	protected String content_url;
	protected String navigation_url;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(conf, "conf", sb);
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		common.utils.MiscUtils.checkNotNull(content_url, "content_url", sb);
		common.utils.MiscUtils.checkNotNull(navigation_url, "navigation_url", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		request.setAttribute(conf.getContentUrlAttribute(), content_url);
		request.setAttribute(conf.getNavigationUrlAttribute(), navigation_url);
		request.setAttribute(conf.getContentDataAttribute(), service.getStatistics());

		request.setAttribute("title", "Статистика");
		request.setAttribute("top_header", "Статистика");

		return new ModelAndView(conf.getTemplateUrl());
	}

	public void setConfig(ICmsConfig conf) {this.conf = conf;}
	public void setService(MyInterceptor service) {this.service = service;}
	public void setContent_url(String value){this.content_url = value;}
	public void setNavigation_url(String value){this.navigation_url = value;}

}