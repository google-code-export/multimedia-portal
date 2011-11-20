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

package common.cms.services;

import common.beans.IFilterBean;
import common.beans.IMultiupdateBean;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * this class only loggs every method invocation
 * and returns nulls
 * @author demchuck.dima@gmail.com
 */
public class EmptyGenericCmsService<T, ID extends Serializable> implements ICmsService<T, ID>{
	protected Logger logger = Logger.getLogger(getClass());

	@Override
	public List<T> getAllShortCms() {
		logger.info("getAllShortCms()");
		return null;
	}

	@Override
	public int updateObjectArrayShortById(String[] propertyNames, ID[] idValues, Object[]... propertyValues) {
		logger.info("updateObjectArrayShortById()");
		return 0;
	}

	@Override
	public int saveOrUpdateCollection(Collection c) {
		logger.info("saveOrUpdateCollection()");
		return 0;
	}

	@Override
	public IMultiupdateBean<T, ID> getMultiupdateBean(int size) {
		logger.info("getMultiupdateBean()");
		return null;
	}

	@Override
	public T getInsertBean() {
		logger.info("getInsertBean()");
		return null;
	}

	@Override
	public boolean insert(T obj) {
		logger.info("insert()");
		return false;
	}

	@Override
	public Map<String, Object> initInsert() {
		logger.info("initInsert()");
		return null;
	}

	@Override
	public int deleteById(ID id) {
		logger.info("deleteById()");
		return 0;
	}

	@Override
	public T getUpdateBean(ID id) {
		logger.info("getUpdateBean()");
		return null;
	}

	@Override
	public Map<String, Object> initUpdate() {
		logger.info("initUpdate()");
		return null;
	}

	@Override
	public int update(T command) {
		logger.info("update()");
		return 0;
	}

	@Override
	public Map<String, Object> initFilter() {
		logger.info("initFilter()");
		return null;
	}

	@Override
	public IFilterBean<T> getFilterBean() {
		logger.info("getFilterBean()");
		return null;
	}

	@Override
	public List<T> getFilteredByPropertyValue(String propertyName, Object propertyValue) {
		logger.info("getFilteredByPropertyValue()");
		return null;
	}

	@Override
	public List<T> getFilteredByPropertiesValue(String[] propertyName, Object[] propertyValue) {
		logger.info("getFilteredByPropertiesValue()");
		return null;
	}
	
}
