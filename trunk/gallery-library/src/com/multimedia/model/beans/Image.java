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

import common.beans.AFileItem;
import javax.validation.constraints.Min;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Image extends AFileItem{
	@Min(0)
	private Long id;
	@Min(0)
	private Long id_item;

	@Min(0)
	private Long width;
	@Min(0)
	private Long height;

	private CommonItem commonItem;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public Long getId_item() {return id_item;}
	public void setId_item(Long id_item) {this.id_item = id_item;}

	public Long getWidth() {return width;}
	public void setWidth(Long width) {this.width = width;}

	public Long getHeight() {return height;}
	public void setHeight(Long height) {this.height = height;}

	public CommonItem getCommonItem() {return commonItem;}
	public void setCommonItem(CommonItem commonItem) {this.commonItem = commonItem;}

}
