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
import core.service.IRubricImageService;
import gallery.service.pages.IRubricationService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class CacheControlCms implements Controller{
    public static final String DO_PARAM = "do";
    public static final String PARAM_RUBRICATOR_CLEAR = "clear_rubricator";
    public static final String PARAM_RUBRICATOR_REFRESH = "refresh_rubricator";
    public static final String PARAM_RUBRICATOR_IMAGE_CLEAR = "clear_rub_img";
    public static final String PARAM_RUBRICATOR_IMAGE_REFRESH = "refresh_rub_img";
    public static final String PARAM_REGION_CLEAR =   "region_clear";

    public static final String PARAM_REGION_NAME = "name";

    private IRubricationService rubrication_service;
    private IRubricImageService rubric_image_service;

	protected ICmsConfig config;
	protected String content_url;
	protected String navigation_url;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(rubrication_service, "rubrication_service", sb);
		common.utils.MiscUtils.checkNotNull(rubric_image_service, "rubric_image_service", sb);
		common.utils.MiscUtils.checkNotNull(config, "config", sb);
		common.utils.MiscUtils.checkNotNull(content_url, "content_url", sb);
		common.utils.MiscUtils.checkNotNull(navigation_url, "navigation_url", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		//TODO: remake
		String do_param = request.getParameter(DO_PARAM);
		if (PARAM_RUBRICATOR_CLEAR.equals(do_param)){
			rubrication_service.clearCache();
			common.CommonAttributes.addHelpMessage("operation_succeed", request);
		}else if (PARAM_RUBRICATOR_REFRESH.equals(do_param)){
			rubrication_service.refreshCache();
			common.CommonAttributes.addHelpMessage("operation_succeed", request);
		}else if (PARAM_RUBRICATOR_IMAGE_CLEAR.equals(do_param)){
			if (rubric_image_service.clearImages(null)){
				common.CommonAttributes.addHelpMessage("operation_succeed", request);
			} else {
				common.CommonAttributes.addErrorMessage("operation_fail", request);
			}
		} else if (PARAM_RUBRICATOR_IMAGE_REFRESH.equals(do_param)){
			if (rubric_image_service.refreshImages(null)){
				common.CommonAttributes.addHelpMessage("operation_succeed", request);
			} else {
				common.CommonAttributes.addErrorMessage("operation_fail", request);
			}
		} if (PARAM_REGION_CLEAR.equals(do_param)){
			String name = request.getParameter(PARAM_REGION_NAME);
			if (name!=null){
				Ehcache cache = getCacheManager().getEhcache(name);
				if (cache==null){
					common.CommonAttributes.addErrorMessage("operation_fail", request);
				} else {
					cache.removeAll();
					common.CommonAttributes.addHelpMessage("operation_succeed", request);
				}
			}
		}
        request.setAttribute("content_url",content_url);
        request.setAttribute("navigation_url",navigation_url);

        request.setAttribute("title","Управление кешем");
        request.setAttribute("top_header","Управление кешем");

        return new ModelAndView(config.getTemplateUrl());
    }

	public CacheManager getCacheManager(){
		return CacheManager.getInstance();
	}

	public void setRubrication_service(IRubricationService value) {this.rubrication_service = value;}
	public void setRubric_image_service(IRubricImageService value) {this.rubric_image_service = value;}

	public void setConfig(ICmsConfig value){this.config = value;}
	public void setContentUrl(String value){this.content_url = value;}
	public void setNavigationUrl(String value){this.navigation_url = value;}

}
