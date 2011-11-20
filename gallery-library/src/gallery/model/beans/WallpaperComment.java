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

import java.util.Date;
import javax.validation.constraints.Min;
import security.beans.User;
import test.annotations.HtmlSpecialChars;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperComment {
	@Min(0)
	private Long id;
	@Min(0)
	private Long id_photo;
	private User user;

	@HtmlSpecialChars
	private String text;

	private Date creation_time;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public Long getId_photo() {return id_photo;}
	public void setId_photo(Long value) {this.id_photo = value;}

	public User getUser() {return user;}
	public void setUser(User user) {this.user = user;}

	public String getText() {return text;}
	public void setText(String text) {this.text = text;}

	public Date getCreationTime() {return creation_time;}
	public void setCreationTime(Date creation_time) {this.creation_time = creation_time;}
}
