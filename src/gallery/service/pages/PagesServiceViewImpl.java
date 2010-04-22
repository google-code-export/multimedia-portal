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

package gallery.service.pages;

import gallery.model.beans.Counter;
import common.services.generic.IGenericService;
import gallery.model.beans.Pages;
import java.util.List;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PagesServiceViewImpl implements IPagesServiceView{
	/** service for counters */
	protected IGenericService<Counter, Long> counter_service;
	protected IPagesService pages_service;

	public static final String[] COUNTERS_ORDER_BY = new String[]{"sort"};
	public static final String[] COUNTERS_ORDER_HOW = new String[]{"ASC"};

	protected static final String[] MAIN_PSEUDONYMES = new String[]{"id_pages","active"};
	protected static final Object[] MAIN_VALUES = new Object[]{null,Boolean.TRUE};

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(counter_service, "counter_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public List<Counter> getCounters() {
		return counter_service.getAllShortOrdered(null, COUNTERS_ORDER_BY, COUNTERS_ORDER_HOW);
	}

    @Override
    public Pages getMainPage(){
        List<Pages> temp = pages_service.getShortByPropertiesValueOrdered(null, MAIN_PSEUDONYMES, MAIN_VALUES, null, null);
        if (temp.isEmpty()){
            return null;
        }else{
            Pages p = temp.get(0);
            pages_service.deattach(p);
            return p;
        }
    }

	public void setCounterService(IGenericService<Counter, Long> value){this.counter_service = value;}
	public void setPages_service(IPagesService value){this.pages_service = value;}
}
