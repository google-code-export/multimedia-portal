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

package gallery.web.controller.photo;

import common.CommonAttributes;
import common.bind.ABindValidator;
import common.bind.CommonBindValidator;
import common.cms.ICmsConfig;
import gallery.service.photo.IPhotoServiceCms;
import gallery.web.controller.Config;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PhotoDimmensionsControllerCms implements Controller{
	private IPhotoServiceCms photo_service;
	/** config class is used to store some constants */
	protected ICmsConfig config;

	protected String beforeUrl;
	protected String navigationUrl;

	protected ABindValidator validator;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(photo_service, "photo_service", sb);
		common.utils.MiscUtils.checkNotNull(config, "config", sb);
		common.utils.MiscUtils.checkNotNull(beforeUrl, "beforeUrl", sb);
		common.utils.MiscUtils.checkNotNull(navigationUrl, "navigationUrl", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}else{
			validator = new CommonBindValidator();
			//logger.debug("initialized successfully. ");
		}
	}

	public static final String RESIZE_VALUE = "resize";

	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse arg1)
			throws Exception
	{
		String action_param = req.getParameter(Config.ACTION_PARAM_NAME);
		//String do_param = req.getParameter("do");

		req.setAttribute("title","Локальная система управления --> Resize");
		req.setAttribute(config.getNavigationUrlAttribute(),navigationUrl);
		boolean handled = false;

		long l = System.currentTimeMillis();
		if (RESIZE_VALUE.equals(action_param)){
			req.setAttribute("top_header","Resize");
			req.setAttribute(config.getContentUrlAttribute(), beforeUrl);
			String append_all = req.getParameter("append_all");
			photo_service.reResizePhotos(Boolean.valueOf(append_all));
			handled = true;
			CommonAttributes.addHelpMessage("operation_succeed", req);
		}
		if (!handled){
			req.setAttribute("top_header","Подготовка Resize");
			req.setAttribute(config.getContentUrlAttribute(), beforeUrl);
		}
		l = System.currentTimeMillis() - l;

		//DateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss.S");
		req.setAttribute("time", l);

		return new ModelAndView(config.getTemplateUrl());
	}

	public void setPhoto_service(IPhotoServiceCms photo_service) {this.photo_service = photo_service;}
	public void setConfig(ICmsConfig config){this.config = config;}

	public void setBeforeUrl(String beforeUrl) {this.beforeUrl = beforeUrl;}
	public void setNavigationUrl(String navigationUrl) {this.navigationUrl = navigationUrl;}
}
