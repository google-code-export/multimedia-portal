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

package gallery.service.photoComment;

import common.beans.IFilterBean;
import common.beans.IMultiupdateBean;
import common.cms.services.ICmsService;
import gallery.model.beans.PhotoComment;
import gallery.model.command.FilterPhotoCommentCms;
import java.util.List;
import java.util.Map;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PhotoCommentCmsService implements ICmsService<PhotoComment, Long>{
	protected IPhotoCommentService service;

	public static final String[] ORDER_BY = null;
	public static final String[] ORDER_HOW = null;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public List<PhotoComment> getAllShortCms() {
		return service.getAllShortOrdered(null, ORDER_BY, ORDER_HOW);
	}

	@Override
	public int updateObjectArrayShortById(String[] propertyNames, Long[] idValues, Object[]... propertyValues) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public IMultiupdateBean getMultiupdateBean(int size) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PhotoComment getInsertBean() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean insert(PhotoComment obj) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Map initInsert() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int deleteById(Long id) {return service.deleteById(id);}

	@Override
	public PhotoComment getUpdateBean(Long id) {return service.getById(id);}

	@Override
	public Map initUpdate() {return null;}

	@Override
	public int update(PhotoComment command) {
		service.save(command);
		return 1;
	}

	@Override
	public Map initFilter() {return null;}

	@Override
	public IFilterBean getFilterBean() {return new FilterPhotoCommentCms();}

	@Override
	public List<PhotoComment> getFilteredByPropertyValue(String propertyName, Object propertyValue) {
		return service.getShortByPropertyValueOrdered(null, propertyName, propertyValue, ORDER_BY, ORDER_BY);
	}

	@Override
	public List<PhotoComment> getFilteredByPropertiesValue(String[] propertyName, Object[] propertyValue) {
		return service.getShortByPropertiesValueOrdered(null, propertyName, propertyValue, ORDER_BY, ORDER_BY);
	}

	public void setPhotoComment_service(IPhotoCommentService value){this.service = value;}

}
