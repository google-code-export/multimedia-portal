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

package com.multimedia.service.advertisement;

import common.dao.IGenericDAO;
import common.services.generic.GenericServiceImpl;
import com.multimedia.model.beans.Advertisement;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="advertisementService")
public class AdvertisementServiceImpl extends GenericServiceImpl<Advertisement, Long> implements IAdvertisementService{
	protected List<String> positions;

	@Override
	public void init() {
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(positions, "positions", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public List<String> getPositions() {return positions;}

//--------------------------------- initialization ----------------------------------------------------

	@Resource(name="advertisementPositions")
	public void setPositions(List<String> value){this.positions = value;}

	@Override
	@Resource(name="advertisementDAO")
	public void setDao(IGenericDAO<Advertisement, Long> dao) {super.setDao(dao);}
}
