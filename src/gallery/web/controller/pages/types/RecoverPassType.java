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

package gallery.web.controller.pages.types;

import common.bind.ABindValidator;
import common.web.controller.CommonActionsEmail;
import gallery.service.newPassword.INewPasswordViewService;
import javax.servlet.http.HttpServletRequest;

/**
 * used for recovering password.
 * sends mail notification to user with confirmation url;
 * if url contains code parameter then makes user password active.
 * @author demchuck.dima@gmail.com
 */
public class RecoverPassType extends ASingleContentType{
    /** string constant that represents type for this page */
    public static final String TYPE="system_recover_pass";
	/** rus type */
	public static final String TYPE_RU="---Восстановление пароля---";

	@Override
	public String getType() {return TYPE;}
	@Override
	public String getTypeRu() {return TYPE_RU;}

	private INewPasswordViewService service;
	private ABindValidator validator;

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		common.utils.MiscUtils.checkNotNull(validator, "validator", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public UrlBean execute(HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws Exception
	{
		String code = request.getParameter(gallery.service.newPassword.Config.CODE_PARAM_NAME);
		if (code==null||code.equals("")){
			CommonActionsEmail.doInsert(service, config, validator, request);
		} else {
			request.setAttribute(config.getContentDataAttribute(), service.getInsertBean());
			if (service.applyNewPassword(code)){
				common.CommonAttributes.addHelpMessage("operation_succeed", request);
			}else{
				common.CommonAttributes.addErrorMessage("form_errors", request);
			}
		}

		UrlBean url = new UrlBean();
		url.setContent(contentUrl);
		return url;
	}

	public void setService(INewPasswordViewService value) {this.service = value;}
	public void setValidator(ABindValidator value) {this.validator = value;}

}
