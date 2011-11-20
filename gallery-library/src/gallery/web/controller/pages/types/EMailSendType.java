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
import common.email.IMailService;
import gallery.model.command.SendEmail;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class EMailSendType extends ASingleContentType{
    /** string constant that represents type for this page */
    public static final String TYPE="system_send_email";
	/** rus type */
	public static final String TYPE_RU="---Обратная связь---";

	@Override
	public String getType() {return TYPE;}

	@Override
	public String getTypeRu() {return TYPE_RU;}

	private IMailService mail_service;
	private ABindValidator validator;
	private String email_adresses;

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(mail_service, "mail_service", sb);
		common.utils.MiscUtils.checkNotNull(validator, "validator", sb);
		common.utils.MiscUtils.checkNotNull(email_adresses, "email_adresses", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public UrlBean execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		SendEmail command = new SendEmail();
		BindingResult res =  validator.bindAndValidate(command, request);
		if (res.hasErrors()){
			request.setAttribute(res.MODEL_KEY_PREFIX+config.getContentDataAttribute(), res);
			common.CommonAttributes.addErrorMessage("form_errors", request);
		}else{
			command.setEmail_to(email_adresses);
			mail_service.postMail(gallery.web.Config.SITE_NAME+": "+command.getEmail_from(), command);
			common.CommonAttributes.addHelpMessage("operation_succeed", request);
		}
		request.setAttribute(config.getContentDataAttribute(), command);

		UrlBean url = new UrlBean();
		url.setContent(contentUrl);
		return url;
	}

	public void setMail_service(IMailService mail_service) {this.mail_service = mail_service;}
	public void setValidator(ABindValidator validator) {this.validator = validator;}
	public void setEmail_adresses(String email_adresses) {this.email_adresses = email_adresses;}

}
