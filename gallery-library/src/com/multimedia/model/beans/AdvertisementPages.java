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

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class AdvertisementPages {
	@Min(0)
	private Long id;
	@Min(0)
	private Long id_pages;
	@Min(0)
	private Long id_advertisement;

	private Boolean allow;
	private Boolean useInChildren;
	private Pages pages;
	//private Advertisement advertisement;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public Long getId_pages() {return id_pages;}
	public void setId_pages(Long id_pages) {this.id_pages = id_pages;}

	public Long getId_advertisement() {return id_advertisement;}
	public void setId_advertisement(Long id_advertisement) {this.id_advertisement = id_advertisement;}

	public Boolean getAllow() {return allow;}
	public void setAllow(Boolean allow) {this.allow = allow;}

	public Boolean getUseInChildren() {return useInChildren;}
	public void setUseInChildren(Boolean useInChildren) {this.useInChildren = useInChildren;}

	public Pages getPages() {return pages;}
	public void setPages(Pages pages) {this.pages = pages;}

	//public Advertisement getAdvertisement() {return advertisement;}
	//public void setAdvertisement(Advertisement advertisement) {this.advertisement = advertisement;}

}
