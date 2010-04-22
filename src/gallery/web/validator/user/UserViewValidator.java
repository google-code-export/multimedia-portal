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
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import security.beans.Role;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class UserViewValidator extends AUserValidator{

	public BindingResult bindAndValidate(User command,HttpServletRequest request, User old_command){
		BindingResult err = bindAndValidate(command, request);
		if (!err.hasErrors()){
			if ((command.getPassword()==null&&command.getPassword_repeat()!=null)
					||(command.getPassword()!=null&&!command.getPassword().equals(command.getPassword_repeat())))
			{
				err.rejectValue("password_repeat", "password_repeat.different");
			}
			if (old_command==null){
				//creating new user (insert)
				if (userService.getRowCount("login", command.getLogin())>0){
					err.rejectValue("login", "exists.login");
				}
				if (userService.getRowCount("email", command.getEmail())>0){
					err.rejectValue("email", "exists.email");
				}
				if (!err.hasErrors()){
					Set<Role> new_roles = new HashSet<Role>(1);
					new_roles.add(new Role("user",command));
					command.setRoles(new_roles);
				}
			}else{
				//user edits his private data (update)
				command.setId(old_command.getId());
				if (!command.getPassword_old().equals(old_command.getPassword())){
					err.rejectValue("password_old", "password_old.different");
				}
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
				if (!err.hasErrors()){
					command.setRoles(old_command.getRoles());
				}
			}
		}
		return err;
	}

}
