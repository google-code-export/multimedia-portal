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

package gallery.web.controller.pages.types;

import com.multimedia.core.pages.types.APagesType;

/**
 * this class provides a variable for setting content url
 * @author demchuck.dima@gmail.com
 */
public abstract class ASingleContentType extends APagesType{
	protected String contentUrl;

	@Override
	public void init() {
		super.init();
		if (contentUrl==null){
			throw new NullPointerException("content url is not specified.");
		}
	}

	public void setContentUrl(String value){this.contentUrl = value;}
}
