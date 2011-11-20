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

package core.model.beans;

import java.util.List;

public class Pages implements java.io.Serializable, java.lang.Cloneable {

     private Long id;
     private Long id_pages;
     private Pages pages;

     private String name;
     private String info_top;
     private String info_bottom;
     private String title;
     private String description;
     private String keywords;

     private Long sort;
	 private Long layer;

     private String type;

     private Boolean active;
     private Boolean last;

     private List pageses;
     //for view only
     /** if this page is selected (comes with parameters) */
     private Boolean selected;

    public Pages() {
    }

    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}

    public Pages getPages() {return this.pages;}
    public void setPages(Pages pages) {this.pages = pages;}

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public String getInfo_top() {return this.info_top;}
    public void setInfo_top(String info_top) {this.info_top = info_top;}

    public String getInfo_bottom() {return this.info_bottom;}
    public void setInfo_bottom(String info_bottom) {this.info_bottom = info_bottom;}

    public String getTitle() {return this.title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;}

    public String getKeywords() {return this.keywords;}
    public void setKeywords(String keywords) {this.keywords = keywords;}

    public Long getSort() {return this.sort;}
    public void setSort(Long sort) {this.sort = sort;}

    public Boolean getActive() {return this.active;}
    public void setActive(Boolean active) {this.active = active;}

    public String getType() {return this.type;}
    public void setType(String type) {this.type = type;}

    public List getPageses() {return this.pageses;}
    public void setPageses(List pageses) {this.pageses = pageses;}

	public Long getId_pages(){return id_pages;}
	public void setId_pages(Long id_pages) {this.id_pages = id_pages;}

	/**
	 * its number in hierarchy
	 * @return
	 */
	public Long getLayer() {return layer;}

	/**
	 * its number in hierarchy
	 * @param layer
	 */
	public void setLayer(Long layer) {this.layer = layer;}

	/**
	 * true if has no subpages
	 * @return
	 */
	public Boolean getLast() {return last;}

	/**
	 * true if has no subpages
	 * @param value
	 */
	public void setLast(Boolean value) {this.last = value;}

    public Boolean getSelected() {return selected;}
    public void setSelected(Boolean selected) {this.selected = selected;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:");sb.append(id);sb.append("; ");
        sb.append("id_pages:");sb.append(id_pages);sb.append("; ");
        sb.append("name:");sb.append(name);sb.append("; ");
        sb.append("top:");sb.append(info_top);sb.append("; ");
        sb.append("bottom:");sb.append(info_bottom);sb.append("; ");
        sb.append("title:");sb.append(title);sb.append("; ");
        sb.append("descr:");sb.append(description);sb.append("; ");
        sb.append("keywords:");sb.append(keywords);sb.append("; ");
        sb.append("sort:");sb.append(sort);sb.append("; ");
        sb.append("l:");sb.append(layer);sb.append("; ");
        sb.append("t:");sb.append(type);sb.append("; ");
        sb.append("a:");sb.append(active);sb.append("; ");
        sb.append("l:");sb.append(last);sb.append("; ");
        sb.append("s:");sb.append(selected);sb.append("; ");
        return sb.toString();
    }

	@Override
	public Object clone(){
		try {
			Pages p = (Pages) super.clone();
			//p.setPages(null);
			//p.setPageses(null);
			//System.out.println("cloned"+p);
			return p;
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}
	}
}