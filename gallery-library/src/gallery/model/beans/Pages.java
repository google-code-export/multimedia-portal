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

package gallery.model.beans;


import gallery.web.controller.pages.types.APagesType;
import java.util.List;
import java.util.Vector;

public class Pages extends core.model.beans.Pages {

    private List<PagesPseudonym> pseudonyms = new Vector(0);
    private List<Photo> photos = new Vector(0);
    private Integer pseudonyms_count;
    private Integer photo_count;
    private Boolean optimized;

    public Pages() {}

    public List<Photo> getPhotos() {return this.photos;}
    public void setPhotos(List<Photo> photos) {this.photos = photos;}

	public List<PagesPseudonym> getPseudonyms() {return pseudonyms;}
	public void setPseudonyms(List<PagesPseudonym> pseudonyms) {this.pseudonyms = pseudonyms;}

    public Integer getPseudonymsCount() {return pseudonyms_count;}
    public void setPseudonymsCount(Integer pseudonyms_count) {this.pseudonyms_count = pseudonyms_count;}

    public Integer getPhotoCount() {return photo_count;}
    public void setPhotoCount(Integer photo_count) {this.photo_count = photo_count;}

    public Boolean getOptimized() {return optimized;}
    public void setOptimized(Boolean optimized) {this.optimized = optimized;}

	private String module_name = null;
	public String getModuleName(){return module_name==null?(module_name=APagesType.modules.get(getType())):module_name;}
	
}