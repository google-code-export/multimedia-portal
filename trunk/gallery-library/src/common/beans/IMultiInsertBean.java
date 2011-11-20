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

package common.beans;

import common.services.IInsertService;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IMultiInsertBean<T> {
	/**
	 * saves to database using an appropriate method :)
	 * actually you must just specify fields for update
	 * and alhorithm for value extraction
	 * @param service used for saving
	 * @return rows updated
	 */
	public int save(IInsertService<T> service);

	//TODO ... rework this 
	/**
	 * determine if this bean contains all required information to be set as a model
	 * @return false if another model should be created
	 */
	public boolean isModel();
}
