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

package gallery.model.command;

import common.beans.IFilterBean;
import common.services.IFilterService;
import gallery.model.beans.PhotoComment;
import java.util.List;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class FilterPhotoCommentCms implements IFilterBean<PhotoComment>{
	private Long id_photo;
	private Long id_users;

	private List<PhotoComment> rez;

	public static final String[] WHERE_VALUES = new String[]{"id_photo","id_users"};

	@Override
	public List<PhotoComment> getItems(IFilterService<PhotoComment> service) {
		if (rez==null){
			if (id_photo==null&&id_users==null){
				rez = service.getFilteredByPropertyValue(null, null);
			}else{
				if (id_photo!=null&&id_users!=null){
					rez = service.getFilteredByPropertiesValue(WHERE_VALUES, new Long[]{id_photo, id_users});
				} else if (id_photo==null){
					rez = service.getFilteredByPropertyValue("id_users", id_users);
				} else {
					rez = service.getFilteredByPropertyValue("id_photo", id_photo);
				}
			}
		}
		return rez;
	}

	public Long getId_photo_nav() {return id_photo;}
	public void setId_photo_nav(Long value) {this.id_photo = value;}

	public Long getId_users_nav() {return id_users;}
	public void setId_users_nav(Long value) {this.id_users = value;}

}
