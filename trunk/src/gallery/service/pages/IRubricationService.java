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

package gallery.service.pages;

import common.services.generic.IGenericCacheService;
import gallery.model.beans.Pages;
import java.util.List;

/**
 * service for rubrication caching
 * caches only Pages object for each rubric
 * @author demchuck.dima@gmail.com
 */
public interface IRubricationService extends IGenericCacheService<List<Pages>>{
	/**
	 * get only current branch of rubrication.
	 * all other pages will not be returned.
	 * also sets 'selected' property of selected pages ...
	 * @param id_pages id of curr page
	 * @return list of pages
	 */
	public List<Pages> getCurrentBranch(Long id_pages);
}
