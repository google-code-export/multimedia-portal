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

package gallery.model.beans;

import security.beans.User;

public class NewPassword  implements java.io.Serializable {

    private Long id;
    //private Long id_users;
    private String code;
    private String new_pass;
    private long creationTime;

	private User user;

// not for database
	private String email;
	private String id_pages_nav;

	public NewPassword() {}

	public Long getId() {return this.id;}
	public void setId(Long id) {this.id = id;}

	/*public Long getId_users() {return this.id_users;}
	public void setId_users(Long id_users) {this.id_users = id_users;}*/

	public String getCode() {return this.code;}
	public void setCode(String code) {this.code = code;}

	public String getNew_pass() {return this.new_pass;}
	public void setNew_pass(String new_pass) {this.new_pass = new_pass;}

	public long getCreationTime() {return this.creationTime;}
	public void setCreationTime(long creationTime) {this.creationTime = creationTime;}

	public User getUser() {return user;}
	public void setUser(User user) {this.user = user;}

	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}

	public void setId_pages_nav(String value) {this.id_pages_nav = value;}
	/**
	 *
	 * @return context relative url for recovering password
	 */
	public String getUrl(){
		return "/index.htm?id_pages_nav="+id_pages_nav+"&amp;"+gallery.service.newPassword.Config.CODE_PARAM_NAME+"="+code;
	}

}