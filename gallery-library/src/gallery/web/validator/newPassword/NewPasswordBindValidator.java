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

package gallery.web.validator.newPassword;

import common.bind.CommonBindValidator;
import gallery.model.beans.NewPassword;
import gallery.service.user.IUserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.Errors;
import security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class NewPasswordBindValidator extends CommonBindValidator{
	protected IUserService<User, Long> user_service;

	public void init(){
		StringBuilder sb = new StringBuilder();

		common.utils.MiscUtils.checkNotNull(user_service, "user_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	protected void validate(Object command, Errors err, HttpServletRequest request) {
		super.validate(command, err, request);
		if (!err.hasErrors()){
			NewPassword new_password = (NewPassword)command;
			List<User> users = user_service.getByPropertyValueOrdered(null, "email", new_password.getEmail(), null, null);
			if (users.isEmpty()){
				err.rejectValue("email", "not_exists.email");
			}else{
				new_password.setUser(users.get(0));
			}
		}
	}

	public void setUser_service(IUserService<User, Long> value){this.user_service = value;}

}
