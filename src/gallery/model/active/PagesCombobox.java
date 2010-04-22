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

package gallery.model.active;

import common.services.IServiceBean;
import gallery.model.beans.Pages;
import gallery.service.pages.IPagesService;
import java.util.List;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PagesCombobox implements IServiceBean{
	public static final String DEFAULT_PROPERTIES[] = new String[]{"id","name","active","last"};
	public static final char DEFAULT_DELIMITER = '>';
	protected IPagesService pages_services;
	protected String[] properties;
	protected String[] pseudonyms;
	protected String[] whereProps;
	protected Object[][] whereVals;
	protected Long first_id;

	/**
	 * creates a service that is used to generate pages combobox
	 * @param first_id
	 * @param pages_services
	 */
	public PagesCombobox(Long first_id, IPagesService pages_services){
		this.first_id = first_id;
		this.pages_services = pages_services;
		this.properties = DEFAULT_PROPERTIES;
		this.pseudonyms = DEFAULT_PROPERTIES;
	}

	@Override
	public Object getData(){
		List<Pages> list = pages_services.getPagesChildrenRecurciveOrderedWhere(properties, pseudonyms, whereProps, whereVals, first_id);
		//fixing pages names with '-' for each layer
		long layer;
		StringBuilder sb;
		for (Pages p:list){
			if ((layer = p.getLayer())>0){
				sb = new StringBuilder();
				for (int i=0;i<layer;i++){
					sb.append(DEFAULT_DELIMITER);
				}
				p.setName(sb.append(p.getName()).toString());
			}
		}
		return list;
	}

	public void setPages_services(IPagesService pages_services) {this.pages_services = pages_services;}
	public void setProperties(String[] properties) {this.properties = properties;}
	public void setWhereProps(String[] whereProps) {this.whereProps = whereProps;}
	public void setPseudonyms(String[] pseudonyms) {this.pseudonyms = pseudonyms;}
	public void setWhereVals(Object[][] whereVals) {this.whereVals = whereVals;}
	public void setFirst_id(Long first_id) {this.first_id = first_id;}
	
}
