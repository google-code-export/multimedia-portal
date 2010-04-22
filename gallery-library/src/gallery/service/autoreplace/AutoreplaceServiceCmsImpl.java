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

package gallery.service.autoreplace;

import gallery.model.command.MultiAutoreplaceCms;
import common.beans.IFilterBean;
import common.beans.IMultiupdateBean;
import common.cms.services.ICmsService;
import gallery.model.beans.Autoreplace;
import gallery.model.beans.AutoreplaceL;
import java.util.List;
import java.util.Map;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class AutoreplaceServiceCmsImpl implements ICmsService<AutoreplaceL, Long>{
	protected IAutoreplaceService service;

	public static final String[] ORDER_BY = new String[]{"parent.sort"};
	public static final String[] ORDER_HOW = new String[]{"asc"};

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public List<AutoreplaceL> getAllShortCms() {return service.getAllShortOrdered(null, ORDER_BY, ORDER_HOW);}

	@Override
	public IMultiupdateBean getMultiupdateBean(int size) {return new MultiAutoreplaceCms(size);}

	@Override
	public int deleteById(Long id) {return service.deleteById(id);}

	@Override
	public AutoreplaceL getInsertBean() {
		//first selecting max sort
		AutoreplaceL a = new AutoreplaceL();
		Autoreplace parent = new Autoreplace();
		Long sort = (Long) service.getSinglePropertyU("max(parent.sort)");
		if (sort == null) sort = new Long(0);
		else sort++;
		parent.setSort(sort);
		a.setParent(parent);
		return a;
	}

	@Override
	public int updateObjectArrayShortById(String[] propertyNames, Long[] idValues, Object[]... propertyValues) {
		return service.updateObjectArrayShortById(propertyNames, idValues, propertyValues);
	}

	@Override
	public boolean insert(AutoreplaceL obj) {
		service.save(obj);
		return true;
	}

	public void setService(IAutoreplaceService service){this.service = service;}

	@Override
	public Map initInsert() {return null;}

	@Override
	public AutoreplaceL getUpdateBean(Long id) {return service.getById(id);}

	@Override
	public Map initUpdate() {return null;}

	@Override
	public int update(AutoreplaceL command) {
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
	public List<AutoreplaceL> getFilteredByPropertyValue(String propertyName, Object propertyValue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<AutoreplaceL> getFilteredByPropertiesValue(String[] propertyName, Object[] propertyValue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
