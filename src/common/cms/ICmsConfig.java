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

package common.cms;

import common.web.IControllerConfig;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface ICmsConfig extends IControllerConfig{
	/** @return name of jsp for rendering content after doView */
	public String getContentViewTemplate();
	/** @return name of jsp for rendering content after doMultiupdate */
	public String getContentMultiupdateTemplate();
	/** @return name of jsp for rendering content after doInsert */
	//public String getContentFilterTemplate();
	/** @return name of jsp for rendering content after doInsert */
	public String getContentInsertTemplate();
	/** @return name of jsp for rendering content after doUpdate */
	public String getContentUpdateTemplate();
	/** @return name of jsp for rendering navigation */
	public String getNavigationTemplate();
	/** @return rus name of this module (for some headers) */
	public String getNameRu();
    public String getContentUrlAttribute();
    public String getNavigationUrlAttribute();
}
