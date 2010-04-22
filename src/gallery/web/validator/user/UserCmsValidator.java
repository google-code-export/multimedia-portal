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

package gallery.web.validator.user;

import security.beans.User;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class UserCmsValidator extends AUserValidator{

	public BindingResult bindAndValidate(User command,HttpServletRequest request, User old_command){
		BindingResult err = bindAndValidate(command, request);
		if (!err.hasErrors()){
			if ((command.getPassword()==null&&command.getPassword_repeat()!=null)
					||(command.getPassword()!=null&&!command.getPassword().equals(command.getPassword_repeat())))
			{
				err.rejectValue("password_repeat", "password_repeat.different");
			}
			if (old_command==null){
				if (userService.getRowCount("login", command.getLogin())>0){
					err.rejectValue("login", "exists.login");
				}
				if (userService.getRowCount("email", command.getEmail())>0){
					err.rejectValue("email", "exists.email");
				}
			}else{
				if (command.getPassword()==null||command.getPassword().equals("")){
					command.setPassword(old_command.getPassword());
				}
				if ((!command.getLogin().equals(old_command.getLogin()))&&
						(userService.getRowCount("login", command.getLogin())>0))
				{
					err.rejectValue("login", "exists.login");
				}
				if ((!command.getEmail().equals(old_command.getEmail()))&&
						(userService.getRowCount("email", command.getEmail())>0))
				{
					err.rejectValue("email", "exists.email");
				}
				command.setNewRoles(old_command.getRoles());
			}
		}
		return err;
	}

}
