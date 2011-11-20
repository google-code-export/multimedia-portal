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

package gallery.service.newPassword;

import common.email.IMailService;
import common.types.typesCorrect.classes.CodeCorrecter;
import common.types.typesCorrect.classes.PasswordCorrecter;
import gallery.model.beans.NewPassword;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class NewPasswordViewImpl implements INewPasswordViewService{

	protected final Logger logger = Logger.getLogger(getClass());
	//number of digits in code for restoration
	public static final int CODE_LENGTH = 15;
	//number of digits in code for restoration
	public static final int PASSWORD_LENGTH = 6;
	/** template for email */
    protected static final String NOTIFICATION_TEMPLATE="whenUserRecoverPass.html";

	protected IMailService mail_service;
	protected INewPasswordService new_password_service;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(mail_service, "mail_service", sb);
		common.utils.MiscUtils.checkNotNull(new_password_service, "new_password_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public NewPassword getInsertBean() {return new NewPassword();}

	/**
	 * does nofing; in this imlementation all is made in sendInsertNotificationUser method
	 * @param obj
	 * @return false
	 */
	@Override
	public boolean insert(NewPassword obj) {return false;}

	@Override
	public boolean sendInsertNotificationUser(NewPassword obj, String server, String siteName, List<String> id_groups) {
		String new_password = CodeCorrecter.generate(PASSWORD_LENGTH);
		obj.setNew_pass(PasswordCorrecter.correctStatic(new_password));
		obj.setCode(CodeCorrecter.generate(CODE_LENGTH));
		obj.setCreationTime(System.currentTimeMillis());

		new_password_service.save(obj);

		String[] params = new String[]{"&url&","&user.login&","&user.password&","&user.name&","&this.link&","&this.name&","&advertisement&"};
		String[] values = new String[params.length];
		values[0] = server+obj.getUrl();
		values[1] = obj.getUser().getLogin();
		values[2] = new_password;
		values[3] = obj.getUser().getName();
		values[4] = server;
		values[5] = siteName;
		values[6] = "reklama";

		String sender = "\""+siteName+"\" <"+mail_service.getAutoanswerEmail()+">";

		try {
			mail_service.postMail("Восстановить пароль", obj.getUser().getEmail(), sender, params, values, NOTIFICATION_TEMPLATE);
		} catch (FileNotFoundException ex) {
			logger.error(ex);
			return false;
		} catch (IOException ex) {
			logger.error(ex);
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> initInsert() {return null;}

	public void setNew_passwordService(INewPasswordService value){this.new_password_service = value;}
	public void setMailService(IMailService value){this.mail_service = value;}

	@Override
	public boolean applyNewPassword(String code) {
		List<NewPassword> passwords = new_password_service.getByPropertyValueOrdered(null, "code", code, null, null);
		if (passwords==null||passwords.isEmpty()){
			return false;
		}else{
			NewPassword np = passwords.get(0);
			User u = np.getUser();
			//transaction will be commited automatically ... so we do not need to commit it
			u.setPassword(np.getNew_pass());
			//deleting  all new_passwords for this user
			new_password_service.deleteByPropertyValue("user", u);
			return true;
		}
	}

}
