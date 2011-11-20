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

package gallery.web.controller.resolution.cms;

import common.cms.ICmsConfig;
import common.web.controller.CommonActions;
import gallery.model.beans.Resolution;
import gallery.service.resolution.IResolutionService;
import gallery.web.controller.resolution.Config;
import gallery.web.controller.resolution.Validation;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * @author demchuck.dima@gmail.com
 */
public class ResolutionCmsDelegate {
	protected final Logger logger = Logger.getLogger(getClass());
	/** config class is used to store some constants */
	protected ICmsConfig config;
	/** service for working with data */
	protected IResolutionService service;
	/** validator */
	private Validation validator;

	protected String show_url;
	protected String insert_url;
	protected String navigation_url;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(config, "config", sb);
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		common.utils.MiscUtils.checkNotNull(validator, "validator", sb);
		common.utils.MiscUtils.checkNotNull(show_url, "show_url", sb);
		common.utils.MiscUtils.checkNotNull(insert_url, "insert_url", sb);
		common.utils.MiscUtils.checkNotNull(insert_url, "insert_url", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}


	public ModelAndView doView(HttpServletRequest req, HttpServletResponse resp){
		//logger.fine("do=view");
		Map<String, Object> m = getCommonModel(req);
		m.put(config.getContentUrlAttribute(), show_url);
		m.put("resolutions", service.getOrdered(null, Config.ORDER_BY, Config.ORDER_HOW));

        return new ModelAndView(config.getTemplateUrl(), m);
    }

	public static final String[] REQUIRED_FIELDS = new String[]{"width","height"};
	public ModelAndView doInsert(HttpServletRequest req, HttpServletResponse resp/*, Pages command*/){
		Map<String, Object> m = getCommonModel(req);
		m.put(config.getContentUrlAttribute(), insert_url);
		m.put("editForm_topHeader", "Добавление");

		String action = req.getParameter(gallery.web.controller.Config.ACTION_PARAM_NAME);
		Resolution command = new Resolution();

		if (action!=null&&action.equals("insert")){
			/** bind command */
			ServletRequestDataBinder binder = new ServletRequestDataBinder(command);
			binder.setRequiredFields(REQUIRED_FIELDS);
			binder.bind(req);
			BindingResult res = binder.getBindingResult();
			validator.validateCMS(command, res);
			if (res.hasErrors()){
				m.put(res.MODEL_KEY_PREFIX+"command", res);
				m.put("command", command);
				common.CommonAttributes.addErrorMessage("form_errors", req);
			}else{
				service.save(command);
				m.put("command", command);
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
			}
		}else{
			m.put("command", command);
		}

        return new ModelAndView(config.getTemplateUrl(), m);
    }

	public ModelAndView doMultiUpdate(HttpServletRequest req, HttpServletResponse resp){
		//logger.fine("do=multi update");
		String action = req.getParameter("action");
		if (action != null && action.equals("multiUpdate")) {
			ResolutionMulti command = new ResolutionMulti();
			BindingResult res = null;

			ServletRequestDataBinder binder = new ServletRequestDataBinder(command);
			binder.bind(req);
			res = binder.getBindingResult();
			if (res.hasErrors()) {
				common.CommonAttributes.addErrorMessage("form_errors", req);
			//logger.fine("hasErrors");
			} else {
				int rez = service.updateObjectArrayShortById(new String[]{"width", "height"}, command.getId(), command.getWidth(), command.getHeight());
				if (rez > 0) {
					common.CommonAttributes.addHelpMessage("operation_succeed", req);
				//logger.fine("not hasErrors");
				} else {
					common.CommonAttributes.addErrorMessage("operation_fail", req);
				//logger.fine("hasErrors");
				}
			}
		}
		//after updating values shoving all pages(doView)
        return doView(req, resp);
    }


	public ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp){
		//logger.fine("do=delete");

		CommonActions.doDelete(service, req);

        return doView(req, resp);
    }

	public Map<String, Object> getCommonModel(HttpServletRequest req){
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("title","Разрешения");
		m.put("top_header","Разрешения");

		m.put(config.getNavigationUrlAttribute(), navigation_url);
		return m;
	}

	public void setConfig(ICmsConfig config){this.config = config;}

	public void setService(IResolutionService service) {
		this.service = service;
		this.validator = new Validation(service);
	}

	public void setShowUrl(String value) {this.show_url = value;}
	public void setInsertUrl(String value) {this.insert_url = value;}
	public void setNavigationUrl(String value) {this.navigation_url = value;}

}
class ResolutionMulti{
	private Long[] id;
	private Integer[] width;
	private Integer[] height;

	public Long[] getId() {
		return id;
	}

	public void setId(Long[] id) {
		this.id = id;
	}

	public Integer[] getWidth() {
		return width;
	}

	public void setWidth(Integer[] width) {
		this.width = width;
	}

	public Integer[] getHeight() {
		return height;
	}

	public void setHeight(Integer[] height) {
		this.height = height;
	}
}
