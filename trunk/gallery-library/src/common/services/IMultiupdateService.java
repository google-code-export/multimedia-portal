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

import common.beans.IMultiupdateBean;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IMultiupdateService<T, ID extends Serializable> {
	/**
	 * updated some properties of entities
	 * WARNING entities must have an id not null
	 * @param propertyValues values of properties to be updated
	 * @param idValues id of entities to be updated
	 * @param propertyNames property names to be updated
	 * @return quantity of updated objects
	 */
	public int updateObjectArrayShortById(String[] propertyNames,ID[] idValues,Object[]... propertyValues);

	/**
	 * this method saes or updates collection of entities
	 * @param c collection
	 * @return quantity of updated records
	 */
	public int saveOrUpdateCollection(Collection<T> c);

	/**
	 * bean that will be used for binding values on it
	 *  and then saved to database
	 * @param size if there is a need of precreate some arrays, collections ...
	 * @return an object for multi update
	 */
	public IMultiupdateBean<T, ID> getMultiupdateBean(int size);
}
