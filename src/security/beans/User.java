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

package security.beans;

import gallery.model.beans.Pages;
import gallery.model.beans.Photo;
import gallery.model.beans.PhotoComment;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class User{

     private Long id;
     private Long id_pages;

     private String name;
     private String email;
     private String login;
     private String password;

	 private Date last_accessed;

	 private Pages pages;
     private Set<Role> roles = new HashSet<Role>(0);
     private Set new_passwords = new HashSet(0);
	 private Set<Photo> photos = new HashSet<Photo>(0);
     private List<PhotoComment> comments = new Vector(0);

	 //not for db
	 /** for password confirmation */
	 private String password_repeat;
	 /** old password */
	 private String password_old;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public Long getId_pages() {return id_pages;}
	public void setId_pages(Long id_pages) {this.id_pages = id_pages;}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}

	public String getLogin() {return login;}
	public void setLogin(String login) {this.login = login;}

	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}

	public Pages getPages() {return pages;}
	public void setPages(Pages pages) {this.pages = pages;}

	public Set getNew_passwords() {return new_passwords;}
	public void setNew_passwords(Set new_passwords) {this.new_passwords = new_passwords;}

	public Set getPhotos() {return photos;}
	public void setPhotos(Set<Photo> photos) {this.photos = photos;}

	public String getPassword_repeat() {return password_repeat;}
	public void setPassword_repeat(String password_repeat) {this.password_repeat = password_repeat;}

	public String getPassword_old() {return password_old;}
	public void setPassword_old(String password_old) {this.password_old = password_old;}

	public List<PhotoComment> getComments() {return this.comments;}
	public void setComments(List<PhotoComment> value) {this.comments = value;}

	//public String getAnti_spam_code() {return anti_spam_code;}
	//public void setAnti_spam_code(String anti_spam_code) {this.anti_spam_code = anti_spam_code;}

	public Set<Role> getRoles() {return roles;}
	public void setRoles(Set<Role> roles) {this.roles = roles;}
	public Set<String> getRolesStr() {
		if (roles==null){
			return null;
		}else{
			Iterator<Role> items = roles.iterator();
			Set<String> roles_new = new HashSet();
			while (items.hasNext()){
				Role r = items.next();
				roles_new.add(r.getRole());
			}
			return roles_new;
		}
	}
	public void setRolesStr(Set<String> roles) {
		//transforming to set of Role objects
		if (roles!=null){
			HashSet<Role> new_items = new HashSet<Role>();
			Iterator<String> items = roles.iterator();
			while (items.hasNext()){
				Role r = new Role();
				r.setRole(items.next());
				r.setUser(this);
				new_items.add(r);
			}

			if (this.roles==null){
				this.roles = new_items;
			}else{
				this.roles.retainAll(new_items);
				this.roles.addAll(new_items);
			}
		}
	}

	/**
	 * replace all values that are in both sets by values in second set
	 * @param new_items
	 */
	public void setNewRoles(Set<Role> new_items){
		if (this.roles!=null){
			new_items.retainAll(roles);
			new_items.addAll(roles);
			roles = new_items;
		}
	}

	/**
	 * copies only simple types i.e. strings, long ...
	 * @return
	 */
	@Override
	public Object clone(){
		User o = new User();
		o.email = this.email;
		o.id = this.id;
		o.id_pages = this.id_pages;
		o.login = this.login;
		o.name = this.name;
		//o.new_passwords = this.new_passwords;
		//o.pages = this.pages;
		o.password = this.password;
		o.password_repeat = this.password_repeat;
		//o.roles = this.roles;
		return o;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.login != null ? this.login.hashCode() : 0);
		hash = 97 * hash + (this.password != null ? this.password.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		if ((this.login == null) ? (other.login != null) : !this.login.equals(other.login)) {
			return false;
		}
		if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
			return false;
		}
		return true;
	}

	public Date getLast_accessed() {return last_accessed;}
	public void setLast_accessed(Date last_accessed) {this.last_accessed = last_accessed;}
}
