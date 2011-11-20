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
import gallery.model.beans.Wallpaper;
import java.util.List;

/**
 *
 * @author demchuck.dima@gmail.com
 */
//TODO: delete but copy smth to new cms service
public class FilterWallpaperCms implements IFilterBean<Wallpaper>{
	private Long id_pages;
	private Long id_users;

	private List<Wallpaper> rez;

	public static final String[] WHERE_VALUES = new String[]{"id_pages","id_users"};

	@Override
	public List<Wallpaper> getItems(IFilterService<Wallpaper> service) {
		if (rez==null){
			if (id_pages==null&&id_users==null){
				rez = service.getFilteredByPropertyValue(null, null);
			}else{
				if (id_pages!=null&&id_users!=null){
					rez = service.getFilteredByPropertiesValue(WHERE_VALUES, new Long[]{id_pages, id_users});
				} else if (id_pages==null){
					rez = service.getFilteredByPropertyValue(WHERE_VALUES[1], id_users);
				} else {
					rez = service.getFilteredByPropertyValue(WHERE_VALUES[0], id_pages);
				}
			}
		}
		return rez;
	}

	public Long getId_pages_nav() {return id_pages;}
	public void setId_pages_nav(Long value) {this.id_pages = value;}

	public Long getId_users_nav() {return id_users;}
	public void setId_users_nav(Long value) {this.id_users = value;}

}
