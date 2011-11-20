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

import common.beans.IFilterBean;
import common.beans.IMultiupdateBean;
import common.services.generic.IGenericService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public abstract class AGenericCmsService<T, ID extends Serializable> implements ICmsService<T, ID>{
	protected IGenericService<T, ID> service;

	/*public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}*/

	@Required
	public void setService(IGenericService<T, ID> value){this.service = value;}

	@Override
	public int updateObjectArrayShortById(String[] propertyNames, ID[] idValues, Object[]... propertyValues) {
		return service.updateObjectArrayShortById(propertyNames, idValues, propertyValues);
	}

	@Override
	public boolean insert(T obj) {
		service.save(obj);
		return true;
	}

	@Override
	public Map<String, Object> initInsert() {return null;}

	@Override
	public int deleteById(ID id) {return service.deleteById(id);}

	@Override
	public T getUpdateBean(ID id) {return service.getById(id);}

	@Override
	public Map<String, Object> initUpdate() {return null;}

	@Override
	public int update(T command) {
		service.save(command);
		return 1;
	}

	@Override
	public Map<String, Object> initFilter() {return null;}

	@Override
	public IFilterBean<T> getFilterBean() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public IMultiupdateBean<T, ID> getMultiupdateBean(int size) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<T> getFilteredByPropertyValue(String propertyName, Object propertyValue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<T> getFilteredByPropertiesValue(String[] propertyName, Object[] propertyValue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int saveOrUpdateCollection(Collection c) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
