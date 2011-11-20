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

package com.multimedia.service.commonItem;

import com.multimedia.model.beans.CommonItem;
import common.dao.IGenericDAO;
import common.services.generic.GenericServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="commonItemService")
public class CommonItemServiceImpl extends GenericServiceImpl<CommonItem, Long>{

	@Override
	@Resource(name="commonItemDAO")
	public void setDao(IGenericDAO<CommonItem, Long> dao) {super.setDao(dao);}
}
