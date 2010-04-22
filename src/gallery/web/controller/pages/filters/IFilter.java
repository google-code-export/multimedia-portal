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

package gallery.web.controller.pages.filters;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IFilter {
	/**
	 * enable filters to be applyied for all data retrieving.
	 * typicaly enabled before content processing
	 */
	public void enableFilters();

	/**
	 * disable filters to be applyied for all data retrieving
	 * typicaly disabled after content processing
	 */
	public void disableFilters();

	/**
	 * get query param to be aplyed to all urls whitch are affected by this filter
	 * @return query param (&param_name=param_value) or null (if parameter is empty)
	 */
	public String getQueryParam();

	/**
	 * get name of an attribute where this filter will be stored
	 * @return
	 */
	public String getFilterName();

}
