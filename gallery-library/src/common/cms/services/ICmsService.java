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

package common.cms.services;

import common.services.IDeleteService;
import common.services.IFilterService;
import common.services.IInsertService;
import common.services.IMultiupdateService;
import common.services.IUpdateService;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @param <T> type of beans to use
 * @author demchuck.dima@gmail.com
 */
public interface ICmsService<T, ID extends Serializable> extends IMultiupdateService<T, ID>, IInsertService<T>, IDeleteService<ID>, IUpdateService<T, ID>, IFilterService<T>{
	/**
	 * get all rows to show in cms
	 * @return list of objects
	 */
	public List<T> getAllShortCms();
}
