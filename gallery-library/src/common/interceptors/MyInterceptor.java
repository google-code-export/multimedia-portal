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

package common.interceptors;

import common.cms.ICmsConfig;
import gallery.model.beans.Pages;
import com.multimedia.core.pages.types.IPagesType;
import gallery.web.controller.pages.types.UrlBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MyInterceptor implements HandlerInterceptor{
	protected final Logger log = Logger.getLogger(getClass());

	protected String encoding = "UTF-8";
	protected IPagesType type;
	protected ICmsConfig config;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception
	{
		request.setCharacterEncoding(encoding);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception
	{
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception
	{
		if (ex!=null){
			log.error(":(",ex);
			//trying to show error page
			Pages errorPage = (Pages)request.getAttribute("error_page");
			if (errorPage!=null){
				request.setAttribute(gallery.web.controller.pages.Config.CURRENT_PAGE_ATTRIBUTE, errorPage);
				UrlBean url = type.execute(request, response);
				request.setAttribute(config.getUrlAttribute(), url);
				request.getRequestDispatcher(config.getTemplateUrl()).forward(request, response);
			}
		}
	}

	public void setErrorType(IPagesType type){this.type = type;}
	public void setCharacterEncoding(String s){this.encoding = s;}
	public void setConfig(ICmsConfig c){this.config = c;}

}
