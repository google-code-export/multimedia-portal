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

package gallery.service.counter;

import common.beans.IFilterBean;
import common.beans.IMultiupdateBean;
import common.cms.services.ICmsService;
import gallery.model.beans.Counter;
import gallery.model.command.MultiCounterCms;
import common.services.generic.IGenericService;
import java.util.List;
import java.util.Map;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class CounterCmsImpl implements ICmsService<Counter, Long>{
	protected IGenericService<Counter, Long> counter_service;

	public static final String[] ORDER_BY = new String[]{"sort"};
	public static final String[] ORDER_HOW = new String[]{"ASC"};

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(counter_service, "counter_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	public static final String[] CMS_SHORT_PSEUDONYMES = new String[]{"id","name","sort"};

	@Override
	public List<Counter> getAllShortCms() {
		return counter_service.getAllShortOrdered(CMS_SHORT_PSEUDONYMES, ORDER_BY, ORDER_HOW);
	}

	@Override
	public int updateObjectArrayShortById(String[] propertyNames, Long[] idValues, Object[]... propertyValues) {
		return counter_service.updateObjectArrayShortById(propertyNames, idValues, propertyValues);
	}

	@Override
	public IMultiupdateBean getMultiupdateBean(int size) {
		return new MultiCounterCms(size);
	}

	@Override
	public Counter getInsertBean() {
		Counter c = new Counter();
		Long sort = (Long) counter_service.getSinglePropertyU("max(sort)");
		if (sort == null) sort = new Long(0);
		else sort++;
		c.setSort(sort);
		return c;
	}

	@Override
	public boolean insert(Counter obj) {
		counter_service.save(obj);
		return true;
	}

	@Override
	public Map initInsert() {return null;}

	@Override
	public int deleteById(Long id) {return counter_service.deleteById(id);}

	@Override
	public Counter getUpdateBean(Long id) {return counter_service.getById(id);}

	@Override
	public Map initUpdate() {return null;}

	@Override
	public int update(Counter command) {
		counter_service.save(command);
		return 1;
	}


	@Override
	public Map initFilter() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public IFilterBean getFilterBean() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Counter> getFilteredByPropertyValue(String propertyName, Object propertyValue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Counter> getFilteredByPropertiesValue(String[] propertyName, Object[] propertyValue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}


	public void setCounterService(IGenericService<Counter, Long> value){this.counter_service = value;}
}
