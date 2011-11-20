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
import java.util.HashMap;
import java.util.Map;
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

    protected IRubricationService rubrication_service;
    protected IRubricImageService rubric_image_service;

	protected CacheManager cacheManager;

	protected ICmsConfig config;
	protected String content_url;
	protected String navigation_url;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(rubrication_service, "rubrication_service", sb);
		common.utils.MiscUtils.checkNotNull(rubric_image_service, "rubric_image_service", sb);
		common.utils.MiscUtils.checkNotNull(cacheManager, "cacheManager", sb);
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
			String[] names = request.getParameterValues(PARAM_REGION_NAME);
			Map<String, Boolean> result = new HashMap<String, Boolean>();
			for (String name:names){
				if (name!=null){
					Ehcache cache = cacheManager.getEhcache(name);
					if (cache==null){
						result.put(name, Boolean.FALSE);
						common.CommonAttributes.addErrorMessage("operation_fail", request);
					} else {
						result.put(name, Boolean.TRUE);
						cache.removeAll();
						common.CommonAttributes.addHelpMessage("operation_succeed", request);
					}
				}
			}
			request.setAttribute(config.getContentDataAttribute() ,result);
		}
        request.setAttribute(config.getContentUrlAttribute() ,content_url);
        request.setAttribute(config.getNavigationUrlAttribute() ,navigation_url);

        request.setAttribute("title","Управление кешем");
        request.setAttribute("top_header","Управление кешем");

        return new ModelAndView(config.getTemplateUrl());
    }

	public void setRubrication_service(IRubricationService value) {this.rubrication_service = value;}
	public void setRubric_image_service(IRubricImageService value) {this.rubric_image_service = value;}

	public void setCacheManager(CacheManager value){this.cacheManager = value;}

	public void setConfig(ICmsConfig value){this.config = value;}
	public void setContentUrl(String value){this.content_url = value;}
	public void setNavigationUrl(String value){this.navigation_url = value;}

}
