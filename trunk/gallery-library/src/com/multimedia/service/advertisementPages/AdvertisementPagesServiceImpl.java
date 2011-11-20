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

package com.multimedia.service.advertisementPages;

import com.multimedia.model.beans.AdvertisementPages;
import common.dao.IGenericDAO;
import common.services.generic.GenericServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="advertisementPagesService")
public class AdvertisementPagesServiceImpl extends GenericServiceImpl<AdvertisementPages, Long> implements IAdvertisementPagesService{

	@Override
	public void init() {
		super.init();
	}

//--------------------------------- initialization ----------------------------------------------------

	@Override
	@Resource(name="advertisementPagesDAO")
	public void setDao(IGenericDAO<AdvertisementPages, Long> dao) {super.setDao(dao);}
}
