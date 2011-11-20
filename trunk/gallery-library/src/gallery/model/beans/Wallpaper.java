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

import com.multimedia.model.beans.PagesFolder;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Min;
import org.springframework.web.multipart.MultipartFile;
import security.beans.User;
import test.annotations.HtmlSpecialChars;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Wallpaper {
	@Min(0)
	private Long id;
	@Min(0)
	private Long id_pages;
	@Min(0)
	private Long id_users;

	private Pages pages;
	private User user;
	private PagesFolder pagesFolder;//TODO: mb make a folder in each wallpaper

	private String name;
	@HtmlSpecialChars
	private String description;
	@HtmlSpecialChars
	private String title;
	@HtmlSpecialChars
	private String tags;

	protected Integer width;
	protected Integer height;

	protected Long views;

	private Boolean active;
	private Boolean optimized;
	private Boolean optimized_manual;

	private MultipartFile content;
	private File content_file;

	private Double rating;

	private Date date_upload;
	private String type;

	protected List wallpaperRating;
    protected List<WallpaperComment> comments;
	protected List<Resolution> resolutions;
	protected String[] tagsList;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public Long getId_pages() {return id_pages;}
	public void setId_pages(Long value) {this.id_pages = value;}

	public Long getId_users() {return id_users;}
	public void setId_users(Long value) {this.id_users = value;}

	public String getName() {return name;}
	public void setName(String value) {this.name = value;}

	public String getDescription() {return description;}
	public void setDescription(String value) {this.description = value;}

	public String getTitle() {return title;}
	public void setTitle(String value) {this.title = value;}

	public String getTags() {return tags;}
	public void setTags(String value) {this.tags = value;}

	public String[] getTagsList() {return tagsList==null?(tags==null?null:tags.split(", ")):tagsList;}
	//public void setTagsList(String value) {this.tags = value;}

	public Boolean getActive() {return active;}
	public void setActive(Boolean value) {this.active = value;}

	public Boolean getOptimized() {return optimized;}
	public void setOptimized(Boolean optimized) {this.optimized = optimized;}

	public Boolean getOptimized_manual() {return optimized_manual;}
	public void setOptimized_manual(Boolean optimized_manual) {this.optimized_manual = optimized_manual;}

	public Date getDate_upload() {return date_upload;}
	public void setDate_upload(Date value) {this.date_upload = value;}

	public String getType() {return type;}
	public void setType(String value) {this.type = value;}

	public Double getRating() {
		if (rating==null)
			return 0.0;
		else
			return rating;
	}
	public void setRating(Double value) {this.rating = value;}

	public MultipartFile getContent() {return content;}
	public void setContent(MultipartFile value) {this.content = value;}

	public Pages getPages() {return pages;}
	public void setPages(Pages value) {this.pages = value;}

	public PagesFolder getPagesFolder() {return pagesFolder;}
	public void setPagesFolder(PagesFolder value) {this.pagesFolder = value;}

	public List getWallpaperRating() {return wallpaperRating;}
	public void setWallpaperRating(List value) {this.wallpaperRating = value;}

	public User getUser() {return user;}
	public void setUser(User value) {this.user = value;}

	public List<WallpaperComment> getComments() {return this.comments;}
	public void setComments(List<WallpaperComment> value) {this.comments = value;}

	public File getContent_file() {return content_file;}
	public void setContent_file(File value) {this.content_file = value;}

	public Integer getWidth() {return width;}
	public void setWidth(Integer width) {this.width = width;}

	public Integer getHeight() {return height;}
	public void setHeight(Integer height) {this.height = height;}

	public Long getViews() {return views;}
	public void setViews(Long value) {this.views = value;}

	public List<Resolution> getResolutions() {return resolutions;}
	public void setResolutions(List<Resolution> resolutions) {this.resolutions = resolutions;}

	/**
	 * this method copies only id, id_pages, id_users, name, date_upload
	 * @param p
	 * @return
	 */
	public static Map<String, Object> toMap(Wallpaper p){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("id", p.getId());
		hm.put("id_pages", p.getId_pages());
		hm.put("id_users", p.getId_users());
		hm.put("name", p.getName());
		hm.put("date_upload", p.getDate_upload());
		return hm;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Wallpaper other = (Wallpaper) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		if (this.id_pages != other.id_pages && (this.id_pages == null || !this.id_pages.equals(other.id_pages))) {
			return false;
		}
		if (this.id_users != other.id_users && (this.id_users == null || !this.id_users.equals(other.id_users))) {
			return false;
		}
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
			return false;
		}
		if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
			return false;
		}
		if ((this.tags == null) ? (other.tags != null) : !this.tags.equals(other.tags)) {
			return false;
		}
		if (this.width != other.width && (this.width == null || !this.width.equals(other.width))) {
			return false;
		}
		if (this.height != other.height && (this.height == null || !this.height.equals(other.height))) {
			return false;
		}
		if (this.active != other.active && (this.active == null || !this.active.equals(other.active))) {
			return false;
		}
		if (this.optimized != other.optimized && (this.optimized == null || !this.optimized.equals(other.optimized))) {
			return false;
		}
		if (this.rating != other.rating && (this.rating == null || !this.rating.equals(other.rating))) {
			return false;
		}
		if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
			return false;
		}
		return true;
	}

}
