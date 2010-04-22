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

import common.bind.ABindValidator;
import common.beans.IMultiupdateBean;
import common.cms.ICmsConfig;
import common.cms.services.ICmsService;
import common.web.ControllerConfig;
import common.web.controller.CommonActions;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class SimpleCmsDelegate<T, ID extends Serializable> {
	protected final Logger logger = Logger.getLogger(getClass());
	/** config class is used to store some constants */
	protected ICmsConfig config;
	/** service for working with data */
	protected ICmsService<T, ID> service;
	//validators
	protected ABindValidator insertValidator;
	protected ABindValidator updateValidator;
	protected ABindValidator multiValidator;
	protected ABindValidator filterValidator;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(config, "config", sb);
		common.utils.MiscUtils.checkNotNull(service, "service", sb);

		if (insertValidator==null){logger.warn("insertValidator is not specified; ");}
		if (updateValidator==null){logger.warn("updateValidator is not specified; ");}
		if (multiValidator==null){logger.warn("multiValidator is not specified; ");}
		if (filterValidator==null){logger.warn("filterValidator is not specified; ");}
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
	public ModelAndView doView(HttpServletRequest req, HttpServletResponse resp){
			//logger.fine("do=view");
			getCommonModel(req);
			req.setAttribute(config.getContentUrlAttribute(), config.getContentViewTemplate());
			req.setAttribute(config.getContentDataAttribute(), service.getAllShortCms());
			return new ModelAndView(config.getTemplateUrl());
    }

	public static final String MARKER_PARAM = "marker";
	public static final String _MARKER_PARAM = "_marker";
	/**
	 * updates rows in database
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doMultiUpdate(HttpServletRequest req, HttpServletResponse resp){
		String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);
		String[] _marker = req.getParameterValues(_MARKER_PARAM);

		if ("multiUpdate".equals(action) && _marker!=null && _marker.length>0){
			getCommonModel(req);
			req.setAttribute(config.getContentUrlAttribute(), config.getContentMultiupdateTemplate());
			IMultiupdateBean command = service.getMultiupdateBean(_marker.length);

			BindingResult res = multiValidator.bindAndValidate(command, req);

			if (res.hasErrors()) {
				common.CommonAttributes.addErrorMessage("form_errors", req);
				req.setAttribute(res.MODEL_KEY_PREFIX+config.getContentDataAttribute(), res);
			} else {
				if (command.save(service) > 0) {
					common.CommonAttributes.addHelpMessage("operation_succeed", req);
				} else {
					common.CommonAttributes.addErrorMessage("operation_fail", req);
				}
			}
			if (command.isModel()){
				req.setAttribute(config.getContentDataAttribute(), command);
				return new ModelAndView(config.getTemplateUrl());
			} else {
				return doView(req, resp);
			}
		}else{
			//set that operation fail and make doView operation
			common.CommonAttributes.addErrorMessage("operation_fail", req);
			return doView(req, resp);
		}
		//after updating values shoving all pages(doView)
    }

	/**
	 * inserts a row into db ...
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doInsert(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		req.setAttribute("editForm_topHeader", "Добавление");
		req.setAttribute(config.getContentUrlAttribute(),config.getContentInsertTemplate());

		CommonActions.doInsert(service, config, insertValidator, req);

		return new ModelAndView(config.getTemplateUrl());
    }

	public ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp){
		//logger.fine("do=delete");

		CommonActions.doDelete(service, req);

        return doView(req, resp);
    }

	public ModelAndView doUpdate(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		req.setAttribute("editForm_topHeader", "Редактирование");

		if (CommonActions.doUpdate(service, config, updateValidator, req)){
			req.setAttribute(config.getContentUrlAttribute(),config.getContentUpdateTemplate());
			return new ModelAndView(config.getTemplateUrl());
		}else{
			return doView(req, resp);
		}
    }

	/**
	 * this method is called in all delegate action methods
	 * @param req
	 */
	public void getCommonModel(HttpServletRequest req){
		req.setAttribute(config.getNavigationUrlAttribute(), config.getNavigationTemplate());
		req.setAttribute("title", config.getNameRu());
		req.setAttribute("top_header", config.getNameRu());
	}

	/**
	 * must show items associated with this table
	 * that matches given criteria
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doFilteredView(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		req.setAttribute(config.getContentUrlAttribute(), config.getContentViewTemplate());

		CommonActions.doFilteredSelect(service, config, filterValidator, req);

		return new ModelAndView(config.getTemplateUrl());
    }

	public void setService(ICmsService<T, ID> service){this.service = service;}
	public void setConfig(ICmsConfig config){this.config = config;}
	public void setInsertBindValidator(ABindValidator validator){this.insertValidator = validator;}
	public void setUpdateBindValidator(ABindValidator validator){this.updateValidator = validator;}
	public void setMultiUpdateBindValidator(ABindValidator validator){this.multiValidator = validator;}
	public void setFilterBindValidator(ABindValidator validator){this.filterValidator = validator;}
}