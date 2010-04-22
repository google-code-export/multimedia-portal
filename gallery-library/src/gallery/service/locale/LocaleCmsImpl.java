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

package gallery.service.locale;

import com.netstorm.localization.ILocaleService;
import common.beans.IFilterBean;
import common.beans.IMultiupdateBean;
import common.cms.services.ICmsService;
import com.netstorm.localization.Locale;
import gallery.model.command.MultiLocaleCms;
import java.util.List;
import java.util.Map;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class LocaleCmsImpl implements ICmsService<Locale, Long>{
	protected ILocaleService service;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public List<Locale> getAllShortCms() {
		return service.getAllShortOrdered(null, Config.ORDER_BY, Config.ORDER_HOW);
	}

	@Override
	public int updateObjectArrayShortById(String[] propertyNames, Long[] idValues, Object[]... propertyValues) {
		return service.updateObjectArrayShortById(propertyNames, idValues, propertyValues);
	}

	@Override
	public IMultiupdateBean getMultiupdateBean(int size) {
		return new MultiLocaleCms(size);
	}

	@Override
	public Locale getInsertBean() {
		Locale c = new Locale();
		Long sort = (Long) service.getSinglePropertyU("max(sort)");
		if (sort == null) sort = new Long(0);
		else sort++;
		c.setSort(sort);
		c.setActive(Boolean.TRUE);
		return c;
	}

	@Override
	public boolean insert(Locale obj) {
		service.save(obj);
		return true;
	}

	@Override
	public Map initInsert() {return null;}

	@Override
	public int deleteById(Long id) {return service.deleteById(id);}

	@Override
	public Locale getUpdateBean(Long id) {return service.getById(id);}

	@Override
	public Map initUpdate() {return null;}

	@Override
	public int update(Locale command) {
		service.save(command);
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
	public List<Locale> getFilteredByPropertyValue(String propertyName, Object propertyValue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Locale> getFilteredByPropertiesValue(String[] propertyName, Object[] propertyValue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}


	public void setService(ILocaleService value){this.service = value;}
}
