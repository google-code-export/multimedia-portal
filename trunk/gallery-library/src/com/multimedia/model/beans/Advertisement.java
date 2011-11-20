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

package com.multimedia.model.beans;

import java.util.List;
import javax.validation.constraints.Min;
import test.annotations.HtmlSpecialChars;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Advertisement {
	@Min(0)
	private Long id;

	private String text;
	@HtmlSpecialChars
	private String name;
	@HtmlSpecialChars
	private String position;

	private Long sort;
	private Boolean active;

	private List<AdvertisementPages> advertisementPages;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getText() {return text;}
	public void setText(String text) {this.text = text;}

	public String getPosition() {return position;}
	public void setPosition(String position) {this.position = position;}

	public Long getSort() {return sort;}
	public void setSort(Long sort) {this.sort = sort;}

	public Boolean getActive() {return active;}
	public void setActive(Boolean active) {this.active = active;}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public List<AdvertisementPages> getAdvertisementPages() {return advertisementPages;}
	public void setAdvertisementPages(List<AdvertisementPages> advertisementPages) {this.advertisementPages = advertisementPages;}
}
