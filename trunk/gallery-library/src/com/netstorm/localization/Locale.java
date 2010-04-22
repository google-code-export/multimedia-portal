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

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Locale {
	private Long id;
	private String name;
	private String path;
	private Boolean active;
	private Long sort;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public Boolean getActive() {return active;}
	public void setActive(Boolean active) {this.active = active;}

	public Long getSort() {return sort;}
	public void setSort(Long sort) {this.sort = sort;}

	public String getPath() {return path;}
	public void setPath(String path) {this.path = path;}
}
