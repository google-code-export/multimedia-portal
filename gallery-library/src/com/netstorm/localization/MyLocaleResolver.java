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

package com.netstorm.localization;

import com.multimedia.service.locale.ILocaleService;
import gallery.model.active.LocalesServiceBean;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.LocaleResolver;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MyLocaleResolver implements LocaleResolver{
	protected Logger logger = Logger.getLogger(getClass());

	private ILocaleService locale_service;
	private String all_locales_prefix;

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		//logger.info("resolving locale");
		LocalesServiceBean tmp = Utils.getLocalesBean(request);
		if (tmp!=null){return tmp.getLocale();}

		tmp = createLocaleBean(request);
		request.setAttribute(Utils.LOCALE_REQUEST_ATTRIBUTE_NAME, tmp);
		return tmp.getLocale();
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		if (locale==null){
			//determine cur locale
			request.setAttribute(Utils.LOCALE_REQUEST_ATTRIBUTE_NAME, createLocaleBean(request));
		}else{
			request.setAttribute(Utils.LOCALE_REQUEST_ATTRIBUTE_NAME, new LocalesServiceBean(locale_service, getDefaultLocale(), locale));
		}
	}

	public LocalesServiceBean createLocaleBean(HttpServletRequest request){
		String sub_path;
		String contextPath = request.getContextPath();
		String uri = request.getRequestURI();
		int index1 = 1+contextPath.length();
		int index2 = uri.indexOf("/", index1);

		if (index2>index1){
			String locale_name = uri.substring(index1, index2);
			Boolean active = (Boolean)locale_service.getSinglePropertyU("active", "name", locale_name);
			sub_path = uri.substring(index2);

			if (active!=null&&(active||sub_path.startsWith(all_locales_prefix))){
				return new LocalesServiceBean(locale_service, getDefaultLocale(), new java.util.Locale(locale_name), sub_path, !sub_path.startsWith(all_locales_prefix));
			}
		}

		Locale locale = getDefaultLocale();
		if (locale == null) {
			locale = Locale.getDefault();
		}
		sub_path = uri.substring(contextPath.length());
		return new LocalesServiceBean(locale_service, getDefaultLocale(), locale, sub_path, !sub_path.startsWith(all_locales_prefix));
	}

	public Locale getDefaultLocale(){return locale_service.getDefaultLocale();}

	public void setLocale_service(ILocaleService value) {this.locale_service = value;}
	public void setAll_locales_prefix(String value) {this.all_locales_prefix = value;}

}
