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

import common.bind.ABindValidator;
import common.services.notificationUser.IInsertServiceNotificationUser;
import common.utils.RequestUtils;
import common.web.ControllerConfig;
import common.web.IControllerConfig;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

/**
 * makes an action and send a notification if succeed
 * @author demchuck.dima@gmail.com
 */
public class CommonActionsEmail {

	/**
	 * executes an update, serves as a template method that incapsulates common logic for insert action
	 * @param service
	 * @param config
	 * @param val for validation
	 * @param req resulting model (i.e. errors, command objects ...) will be placed here
	 */
	public static void doInsert(IInsertServiceNotificationUser service, IControllerConfig config, ABindValidator val, HttpServletRequest req){
		String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);
		Object command = service.getInsertBean();
		RequestUtils.copyRequestAttributesFromMap(req, service.initInsert());

		if ("insert".equals(action)){
			/** bind command */
			BindingResult res = val.bindAndValidate(command, req);
			if (res.hasErrors()){
				//m.putAll(res.getModel());
				req.setAttribute(res.MODEL_KEY_PREFIX+config.getContentDataAttribute(), res);
				req.setAttribute(config.getContentDataAttribute(), command);
				common.CommonAttributes.addErrorMessage("form_errors", req);
				//return false;
			}else{
				service.insert(command);
				String server = RequestUtils.getFullServerPathHttp(req);
				//TODO set groups for getting advertisment from DB
				service.sendInsertNotificationUser(command, server, "site name", null);
				req.setAttribute(config.getContentDataAttribute(), service.getInsertBean());
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
				//return true;
			}
		}else{
			req.setAttribute(config.getContentDataAttribute(), command);
		}
	}

	private CommonActionsEmail() {}

}
