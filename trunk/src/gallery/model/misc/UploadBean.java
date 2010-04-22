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

package gallery.model.misc;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class UploadBean {
	private Long id;
	private String folder_name;
	private String page_name;
	private Integer item_count;

	public String getFolder_name() {return folder_name;}
	public void setFolder_name(String folder_name) {this.folder_name = folder_name;}

	public String getPage_name() {return page_name;}
	public void setPage_name(String page_name) {this.page_name = page_name;}

	public Integer getItem_count() {return item_count;}
	public void setItem_count(Integer item_count) {this.item_count = item_count;}

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

}
