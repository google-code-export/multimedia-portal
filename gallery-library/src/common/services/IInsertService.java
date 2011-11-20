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

import java.util.Map;

/**
 * @param <T> type of beans to use
 * @author demchuck.dima@gmail.com
 */
public interface IInsertService<T> {
	/**
	 * is used before an object will be binded and before first time
	 * @return new bean instance
	 */
	public T getInsertBean();

	/**
	 * inserts an appropriate row to database
	 * @param obj object to be saved
	 * @return true if inserted
	 */
	public boolean insert(T obj);

	/**
	 * here you can make all required attributes for insert (such as data for comboboxes ...)
	 * @return map where with required attributes
	 */
	public Map<String, Object> initInsert();
}