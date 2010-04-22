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

package gallery.service.user;

import security.services.ISecurityService;
import common.services.generic.IGenericService;
import java.io.Serializable;
import java.util.List;
import security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IUserService<T extends User, ID extends Serializable> extends IGenericService<T, ID>, ISecurityService{

	/**
	 * returns not all columns
	 * @param propertyName property to search by
	 * @param propertyValue value of property
	 * @return users for current page
	 */
	public List<T> getShortOrderedByPropertyValueCms(String propertyName, Object propertyValue);

	@Override
	public T getById(ID id);
}
