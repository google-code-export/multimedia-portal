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

import gallery.web.controller.pages.submodules.ASubmodule;
import java.util.Map;

/**
 * this bean contains all properties with urls that can be used by the template
 * @author demchuck.dima@gmail.com
 */
public class UrlBean {
    private String navigation;
    private String content;
    private String optimization;
    private String page_top;
    private String page_bottom;
	private Map<String, ASubmodule> submodules;

    /** @return url for navigation */
    public String getNavigation() {return navigation;}
    /** @param navigation url with navigation */
    public void setNavigation(String navigation) {this.navigation = navigation;}
    /** @return url for center(main content) */
    public String getContent() {return content;}
    /** @param center url with center(main content) */
    public void setContent(String value) {this.content = value;}
	/** @return types of sybmodules that are required by this type */
	public Map<String, ASubmodule> getSubmodules(){return submodules;}
	/** @param value types of sybmodules that are required by this type */
	public void setSubmodules(Map<String, ASubmodule> value){this.submodules = value;}
	/** @return url with optimization */
    public String getOptimization() {return optimization;}
	/** @param optimization url with optimization */
	public void setOptimization(String optimization) {this.optimization = optimization;}
    /** @return url for page_top(before main content) */
    public String getPage_top() {return page_top;}
    /** @param center url with page_top(before main content) */
    public void setPage_top(String value) {this.page_top = value;}
    /** @return url for page_bottom(after main content) */
    public String getPage_bottom() {return page_bottom;}
    /** @param center url with page_bottom(after main content) */
    public void setPage_bottom(String value) {this.page_bottom = value;}
    
}
