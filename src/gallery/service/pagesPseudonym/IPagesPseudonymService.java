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

package gallery.service.pagesPseudonym;

import gallery.model.beans.PagesPseudonym;
import common.services.generic.IGenericService;
import java.util.List;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IPagesPseudonymService extends IGenericService<PagesPseudonym, Long>{
    /**
     * order and properties are given in services
     * @param property
     * @param value
     * @return
     */
    public List<PagesPseudonym> getShortByPropertyValueOrdered(String property, Object value);

    /**
     * order and properties are given in services
     * @param property
     * @param value
     * @return
     */
    public List<PagesPseudonym> getShortByPropertiesValueOrdered(String[] property, Object[] value);

	/**
	 * 
	 * @param prop
	 * @param propNames
	 * @param propValues
	 * @param number
	 * @return
	 */
	public Object getSinglePropertyUOrdered(String prop, String[] propNames, Object[] propValues, int number);
}
