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

package common.services;

import common.beans.IFilterBean;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <T> 
 * @author demchuck.dima@gmail.com
 */
public interface IFilterService<T> {
	/**
	 * prepare attributes for update
	 * @return map with attributes
	 */
	public Map<String, Object> initFilter();

	/**
	 * bean that will be used for binding values on it
	 *  and then saved to database
	 * @return an object for filtering
	 */
	public IFilterBean<T> getFilterBean();

	/**
	 * @param propertyName name of property to search by
	 * @param propertyValue value of property
	 * @return list that matches given criteria
	 */
    public List<T> getFilteredByPropertyValue(String propertyName, Object propertyValue);

	/**
	 * @param propertyName name of property to search by
	 * @param propertyValue value of property
	 * @return list that matches given criteria
	 */
    public List<T> getFilteredByPropertiesValue(String propertyName[], Object propertyValue[]);
}
