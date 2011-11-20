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

package com.multimedia.core.pages.types;

import gallery.web.controller.pages.types.UrlBean;

/**
 * this class is main module that is rendered in the center of template
 * @author demchuck.dima@gmail.com
 */
public interface IPagesType {
    /**
     * get name for this type
     * @return type name
     */
    public String getType();
    /**
     * get rus name
     * @return type name
     */
    public String getTypeRu();
    
    /**
     * get data model of this page
     * @param request request object for getting required parameters and setting attributes for a view
     * @param response response object 
	 * @return bean with urls that can be used by view to show differend parts (center, navigation)
	 * @throws Exception 
     */
    public UrlBean execute(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws Exception;
}
