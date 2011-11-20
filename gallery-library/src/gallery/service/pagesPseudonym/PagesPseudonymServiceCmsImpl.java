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

package gallery.service.pagesPseudonym;

import common.beans.IFilterBean;
import common.beans.IMultiupdateBean;
import common.cms.services.ICmsService;
import gallery.model.beans.PagesPseudonym;
import gallery.model.command.FilterPagesPseudonymCms;
import gallery.model.command.MultiPagesPseudonymCms;
import gallery.service.pages.IPagesService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PagesPseudonymServiceCmsImpl implements  ICmsService<PagesPseudonym, Long>{
	private IPagesPseudonymService service;
	protected IPagesService pages_service;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public List<PagesPseudonym> getAllShortCms() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int updateObjectArrayShortById(String[] propertyNames, Long[] idValues, Object[]... propertyValues) {
		return service.updateObjectArrayShortById(propertyNames, idValues, propertyValues);
	}

	@Override
	public IMultiupdateBean<PagesPseudonym, Long> getMultiupdateBean(int size) {return new MultiPagesPseudonymCms(size);}

	@Override
	public PagesPseudonym getInsertBean() {
		PagesPseudonym item = new PagesPseudonym();
		Long sort = (Long) service.getSinglePropertyU("max(sort)");
		if (sort == null) sort = Long.valueOf(0);
		else sort++;
		item.setSort(sort);
        item.setUseInItems(Boolean.TRUE);
        item.setUseInPages(Boolean.TRUE);
		return item;
    }

	@Override
	public boolean insert(PagesPseudonym obj) {
		service.save(obj);
		return true;
	}

	@Override
	public Map<String, Object> initInsert() {
		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("categories", pages_service.getAllCombobox(null, null, null));
		return m;
	}

	@Override
	public int deleteById(Long id) {
		return service.deleteById(id);
	}

	@Override
	public PagesPseudonym getUpdateBean(Long id) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Map<String, Object> initUpdate() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int update(PagesPseudonym command) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Map<String, Object> initFilter() {return null;}

	@Override
	public IFilterBean<PagesPseudonym> getFilterBean() {return new FilterPagesPseudonymCms();}

	@Override
	public List<PagesPseudonym> getFilteredByPropertyValue(String propertyName, Object propertyValue) {
		return service.getShortByPropertyValueOrdered(propertyName, propertyValue);
	}

	@Override
	public List<PagesPseudonym> getFilteredByPropertiesValue(String[] propertyName, Object[] propertyValue) {
		return service.getShortByPropertiesValueOrdered(propertyName, propertyValue);
	}

	public void setService(IPagesPseudonymService service) {this.service = service;}
	public void setPages_service(IPagesService pages_service){this.pages_service = pages_service;}

	@Override
	public int saveOrUpdateCollection(Collection c) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
