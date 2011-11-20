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

import com.multimedia.core.pages.types.IPagesType;
import common.email.IMailService;
import common.utils.RequestUtils;
import gallery.service.user.IUserService;
import gallery.web.validator.user.UserViewValidator;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class UserType implements IPagesType{
	protected final Logger logger = Logger.getLogger(getClass());
    /** string constant that represents type for this page */
    public static final String TYPE="system_user";
	/** rus type */
	public static final String TYPE_RU="---Пользователи---";

	@Override
	public String getType() {return TYPE;}
	@Override
	public String getTypeRu() {return TYPE_RU;}

	protected IUserService<User, Long> userService;
	protected IMailService mailService;
	private UserViewValidator validatorInsert;
	private UserViewValidator validatorUpdate;

	protected String insertUrl;
	protected String updateUrl;

	public static final String USER_REGISTER_MAIL = "userInsert.html";
	public static final String USER_UPDATE_MAIL = "userUpdate.html";

	public UserType(){}

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(userService, "userService", sb);
		common.utils.MiscUtils.checkNotNull(mailService, "mailService", sb);
		common.utils.MiscUtils.checkNotNull(insertUrl, "insertUrl", sb);
		common.utils.MiscUtils.checkNotNull(updateUrl, "updateUrl", sb);
		if (common.utils.MiscUtils.checkNotNull(validatorInsert, "validatorInsert", sb)){
			validatorInsert.setUserService(userService);
		}
		if (common.utils.MiscUtils.checkNotNull(validatorUpdate, "validatorUpdate", sb)){
			validatorUpdate.setUserService(userService);
		}
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public UrlBean execute(HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws Exception
	{
		UrlBean url = new UrlBean();
		String action = request.getParameter(gallery.web.controller.Config.ACTION_PARAM_NAME);
		User command_db = security.Utils.getCurrentUser(request);

		if (command_db==null){
			url.setContent(insertUrl);
			request.setAttribute("editForm_topHeader", "Регистрация");

			if ("user".equals(action)){
				/** bind command */
				User command = new User();
				BindingResult res = validatorInsert.bindAndValidate(command, request, null);
				if (res.hasErrors()){
					//m.putAll(res.getModel());
					request.setAttribute(res.MODEL_KEY_PREFIX+"command", res);
					request.setAttribute("command", command);
					common.CommonAttributes.addErrorMessage("form_errors", request);
				}else{
					userService.save(command);
					String[] param_names =
							new String[]{"&user.name&","&user.login&","&user.password&"
									,"&advertisement&","&this.link&","&this.name&"};
					String[] param_values =
							new String[]{command.getName(),command.getLogin(),request.getParameter("password")
									,":)", RequestUtils.getFullServerPathHttp(request),"Gallery"};
					mailService.postMail("Регистрация", command.getEmail(), mailService.getAutoanswerEmail(),
							param_names, param_values,
							USER_REGISTER_MAIL);
					request.setAttribute("command", new User());
					common.CommonAttributes.addHelpMessage("operation_succeed", request);
				}
			}else{
				request.setAttribute("command", new User());
			}
		}else{
			url.setContent(updateUrl);
			request.setAttribute("editForm_topHeader", "Редактирование");

			User command;

			if (action!=null&&action.equals("user")){
				/** bind command */
				command = new User();
				BindingResult res = validatorUpdate.bindAndValidate(command, request, command_db);

				if (res.hasErrors()){
					request.setAttribute(res.MODEL_KEY_PREFIX+"command", res);
					common.CommonAttributes.addErrorMessage("form_errors", request);
					//logger.fine("hasErrors");
				}else{
					userService.merge(command);
					String[] param_names =
							new String[]{"&user.name&","&user.login&","&user.password&"
									,"&advertisement&","&this.link&","&this.name&"};
					String password = request.getParameter("password");
					if (password==null||password.equals(""))
						password = request.getParameter("password_old");
					String[] param_values =
							new String[]{command.getName(),command.getLogin(),password
									,":)", RequestUtils.getFullServerPathHttp(request),"Gallery"};
					mailService.postMail("Редактирование личных данных", command.getEmail(), mailService.getAutoanswerEmail(),
							param_names, param_values,
							USER_UPDATE_MAIL);
					common.CommonAttributes.addHelpMessage("operation_succeed", request);
					//logger.fine("not hasErrors");
				}
			}else{
				command = command_db;
			}

			request.setAttribute("command", command);
		}
		return url;
	}

	public void setUserService(IUserService<User, Long> service) {this.userService = service;}
    public void setInsertValidator(UserViewValidator val){this.validatorInsert = val;}
    public void setUpdateValidator(UserViewValidator val){this.validatorUpdate = val;}
	public void setMailService(IMailService value){this.mailService = value;}

	public void setInsertUrl(String insertUrl) {this.insertUrl = insertUrl;}
	public void setUpdateUrl(String updateUrl) {this.updateUrl = updateUrl;}

}
