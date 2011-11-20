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

import com.multimedia.model.beans.Locale;
import common.services.IServiceBean;
import common.services.generic.IGenericService;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class LocalesServiceBean implements IServiceBean{
	public static final String WHERE_PROPERTIES[] = new String[]{"active"};

	private IGenericService<Locale, Long> service;
	private String sub_path;
	private java.util.Locale default_locale;
	private java.util.Locale locale;

	private boolean only_active;

	/**
	 * return locales
	 * @param service
	 * @param active if true return only active locales, else all
	 */
	public LocalesServiceBean(IGenericService<Locale, Long> service, java.util.Locale default_locale, java.util.Locale locale) {
		this(service, default_locale, locale, "/", true);
	}

	/**
	 * return locales
	 * @param service
	 * @param active if true return only active locales, else all
	 */
	public LocalesServiceBean(IGenericService<Locale, Long> service, java.util.Locale default_locale, java.util.Locale locale, String sub_path) {
		this(service, default_locale, locale, sub_path, true);
	}

	/**
	 * return locales
	 * @param service
	 * @param active if true return only active locales, else all
	 */
	public LocalesServiceBean(IGenericService<Locale, Long> service, java.util.Locale default_locale, java.util.Locale locale, String sub_path, boolean only_active) {
		this.service = service;
		this.only_active = only_active;
		this.sub_path = (sub_path==null||sub_path.equals("/"))?"/index.htm":sub_path;
		this.default_locale = default_locale;
		this.locale = locale;
	}

	@Override
	public Object getData() {
		List<String> locales;
		if (only_active){
			locales = (List<String>)service.getSingleProperty("name", WHERE_PROPERTIES, new Object[]{Boolean.TRUE}, 0, 0, com.multimedia.config.LocaleConfig.ORDER_BY, com.multimedia.config.LocaleConfig.ORDER_HOW);
		} else {
			locales = (List<String>)service.getSingleProperty("name", null, null, 0, 0, com.multimedia.config.LocaleConfig.ORDER_BY, com.multimedia.config.LocaleConfig.ORDER_HOW);
		}

		if (locales==null){
			return null;
		}else{
			List<Locale> rez = new Vector<Locale>(locales.size()+1);
			Locale tmp;
			for (int i=0;i<locales.size();i++){
				tmp = new Locale();
				tmp.setName(locales.get(i));
				if (tmp.getName().equals(default_locale.getLanguage())){
					tmp.setPath(sub_path);
				} else {
					tmp.setPath("/"+tmp.getName()+sub_path);
				}
				rez.add(tmp);
			}
			return rez;
		}
	}

	public String getKey(){return sub_path;}
	public java.util.Locale getLocale(){return this.locale;};

}
