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

import common.beans.IFilterBean;
import common.bind.ABindValidator;
import common.services.IDeleteService;
import common.services.IFilterService;
import common.services.IInsertService;
import common.services.IUpdateService;
import common.utils.RequestUtils;
import common.web.ControllerConfig;
import common.web.IControllerConfig;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class CommonActions {

	/**
	 * executes bind of parameters to command and selecting results that matches criteria
	 * @param service
	 * @param config
	 * @param val for validation
	 * @param req resulting model (i.e. errors, command objects ...) will be placed here
	 */
	public static <T> void doFilteredSelect(IFilterService<T> service, IControllerConfig config, ABindValidator val, HttpServletRequest req){
		RequestUtils.copyRequestAttributesFromMap(req, service.initFilter());
		IFilterBean<T> command = service.getFilterBean();

		BindingResult res = val.bindAndValidate(command, req);

		if (res.hasErrors()) {
			common.CommonAttributes.addErrorMessage("form_errors", req);
			//req.setAttribute(res.MODEL_KEY_PREFIX+config.getContentDataAttribute(), res);
			//req.setAttribute(config.getContentDataAttribute(), command);
		} else {
			//String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);
			//if (action != null && action.equals("filter")){
			//	req.setAttribute(config.getContentDataAttribute(), command);
			//} else {
			req.setAttribute(config.getContentDataAttribute(), command.getItems(service));
			//}
		}
	}

	/**
	 * executes an update, serves as a template method that incapsulates common logic for insert action
	 * @param service
	 * @param config
	 * @param val for validation
	 * @param req resulting model (i.e. errors, command objects ...) will be placed here
	 */
	public static <T> boolean doInsert(IInsertService<T> service, IControllerConfig config, ABindValidator val, HttpServletRequest req){
		String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);
		T command = service.getInsertBean();
		RequestUtils.copyRequestAttributesFromMap(req, service.initInsert());

		if ("insert".equals(action)){
			/** bind command */
			BindingResult res = val.bindAndValidate(command, req);
			if (res.hasErrors()||!service.insert(command)){
				//m.putAll(res.getModel());
				req.setAttribute(res.MODEL_KEY_PREFIX+config.getContentDataAttribute(), res);
				req.setAttribute(config.getContentDataAttribute(), command);
				common.CommonAttributes.addErrorMessage("form_errors", req);
				return false;
			}else{
				//req.setAttribute(config.getContentDataAttribute(), service.getInsertBean());
				req.setAttribute(config.getContentDataAttribute(), command);
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
			}
		}else{
			//val.bindAndValidate(command, req);
			req.setAttribute(config.getContentDataAttribute(), command);
		}
		return true;
	}

	/**
	 * executes an update, serves as a template method that incapsulates common logic for update action
	 * @param service
	 * @param config
	 * @param val
	 * @param req
	 * @return false if id cannot be converted to long or where is no command with an appropriate id
	 */
	public static <T> boolean doUpdate(IUpdateService<T, Long> service, IControllerConfig config, ABindValidator val, HttpServletRequest req){
		Long id = RequestUtils.getLongParam(req, "id");
		T command = null;
		if (id!=null){
			command = service.getUpdateBean(id);
		}
		if (id==null||command==null){
			common.CommonAttributes.addErrorMessage("form_errors", req);
			return false;
		}

		String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);
		RequestUtils.copyRequestAttributesFromMap(req, service.initUpdate());

		if ("update".equals(action)){
			/** bind command */
			BindingResult res = val.bindAndValidate(command, req);
			if (res.hasErrors()){
				//m.putAll(res.getModel());
				req.setAttribute(res.MODEL_KEY_PREFIX+config.getContentDataAttribute(), res);
				req.setAttribute(config.getContentDataAttribute(), command);
				common.CommonAttributes.addErrorMessage("form_errors", req);
			}else{
				service.update(command);
				req.setAttribute(config.getContentDataAttribute(), command);
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
			}
		}else{
			req.setAttribute(config.getContentDataAttribute(), command);
		}
		return true;
	}

	/**
	 * executes an update, serves as a template method that incapsulates common logic for delete action
	 * @param service
	 * @param req
	 * @return false if id cannot be converted to long
	 */
	public static boolean doDelete(IDeleteService<Long> service, HttpServletRequest req){
		Long id = RequestUtils.getLongParam(req, "id");
		if (id==null){
			common.CommonAttributes.addErrorMessage("form_errors", req);
			return false;
		}

		String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);

		if ("delete".equals(action)) {
			if (service.deleteById(id)>0){
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
				//logger.fine("not hasErrors");
			}else{
				common.CommonAttributes.addErrorMessage("operation_fail", req);
				//logger.fine("hasErrors");
			}
		}
		return true;
	}

	private CommonActions() {}

}
