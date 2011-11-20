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

package com.multimedia.model.beans;

import gallery.model.beans.Pages;
import javax.validation.constraints.Min;
import test.annotations.HtmlSpecialChars;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Item {
	@Min(0)
	private Long id;
	@Min(0)
	private Long views;
	@Min(0)
	private Long id_user;
	@Min(0)
	private Long id_pages;

	private Boolean active;

	@HtmlSpecialChars
	private String title;
	@HtmlSpecialChars
	private String description;
	@HtmlSpecialChars
	private String tags;

	private Pages pages;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public Long getViews() {return views;}
	public void setViews(Long views) {this.views = views;}

	public Long getId_user() {return id_user;}
	public void setId_user(Long id_user) {this.id_user = id_user;}

	public Boolean getActive() {return active;}
	public void setActive(Boolean active) {this.active = active;}

	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}

	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}

	public String getTags() {return tags;}
	public void setTags(String tags) {this.tags = tags;}

	public Long getId_pages() {return id_pages;}
	public void setId_pages(Long id_pages) {this.id_pages = id_pages;}

	public Pages getPages() {return pages;}
	public void setPages(Pages pages) {this.pages = pages;}
}
