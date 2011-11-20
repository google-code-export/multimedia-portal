/*
 *  Copyright 2010 demchuck.dima@gmail.com.
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

package core.cms.services2;

import gallery.model.beans.Pages;
import java.util.List;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IPagesCmsNavigationService{
	/**
	 * returns data for navigation
	 * i.e list of pages that are parents of given page
	 * in an appropriate order
	 * @param id_pages 
	 * @return
	 */
	public List<Pages> getNavigationData(Long id_pages);
}
