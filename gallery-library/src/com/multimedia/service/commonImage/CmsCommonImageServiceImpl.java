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

package com.multimedia.service.commonImage;

import com.multimedia.model.beans.CommonImage;
import com.multimedia.model.beans.CommonItem;
import common.cms.services2.AGenericCmsService;
import common.services.generic.IGenericService;
import java.util.Collection;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="cmsCommonImageService")
public class CmsCommonImageServiceImpl extends AGenericCmsService<CommonImage, Long>{
	protected IGenericService<CommonItem, Long> commonItemService;

	@Override
	public void init() {
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(commonItemService, "commonItemService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}
//----------------------------------- service methods ----------------------------

	@Override
	public CommonImage getInsertBean(CommonImage obj) {
		if (obj==null)
			return new CommonImage();
		obj.setCommonItem(commonItemService.getById(obj.getId_item()));
		return obj;
	}

	@Override
	public int saveOrUpdateCollection(Collection<CommonImage> c) {throw new UnsupportedOperationException();}
//---------------------------  services -----------------------
	@Resource(name="commonItemService")
	public void setCommonItemService(IGenericService<CommonItem, Long> service){this.commonItemService = service;}
	@Resource(name="commonImageService")
	public void setService(IGenericService<CommonImage, Long> service){this.service = service;}
}
