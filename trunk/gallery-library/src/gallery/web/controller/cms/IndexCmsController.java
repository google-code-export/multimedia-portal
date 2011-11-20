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

import common.cms.ICmsConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class IndexCmsController extends org.springframework.web.servlet.mvc.AbstractController{

	protected String content_url;
	protected String navigation_url;
	protected ICmsConfig config;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(content_url, "content_url", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("content_url",content_url);
		request.setAttribute("navigation_url",navigation_url);
		request.setAttribute("title","Локальная система управления");
		request.setAttribute("top_header","Локальная система управления");
		return new ModelAndView(config.getTemplateUrl());
	}

	public void setContentUrl(String content_url) {this.content_url = content_url;}
	public void setNavigationUrl(String navigation_url) {this.navigation_url = navigation_url;}
	public void setConfig(ICmsConfig config) {this.config = config;}

}