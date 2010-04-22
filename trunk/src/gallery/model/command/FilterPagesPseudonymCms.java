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
import gallery.model.beans.PagesPseudonym;
import java.util.List;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class FilterPagesPseudonymCms implements IFilterBean<PagesPseudonym>{
	private Long id_pages;
	private List<PagesPseudonym> rez;

	@Override
	public List<PagesPseudonym> getItems(IFilterService<PagesPseudonym> service) {
		if (rez==null){
			if (id_pages==null){
				rez = service.getFilteredByPropertyValue(null, null);
			}else{
				rez = service.getFilteredByPropertyValue("id_pages", id_pages);
			}
		}
		return rez;
	}

	public Long getId_pages_nav() {return id_pages;}
	public void setId_pages_nav(Long value) {this.id_pages = value;}

}
