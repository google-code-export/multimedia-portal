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

package com.multimedia.core.pages.types;

import gallery.web.controller.pages.types.UrlBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class CommonItemType extends APagesType{
	Logger logger = Logger.getLogger(this.getClass());
    /** string constant that represents type for this page */
    public static final String TYPE="general_common_item";
	/** rus type */
	public static final String TYPE_RU="Простой объект(с файлами)";

	protected String contentUrl;

	@Override
	public String getType() {return TYPE;}

	@Override
	public String getTypeRu() {return TYPE_RU;}

	@Override
	public UrlBean execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		UrlBean url = new UrlBean();
		url.setContent(contentUrl);
		return url;
	}

	public void setContentUrl(String contentUrl){this.contentUrl = contentUrl;}

}
