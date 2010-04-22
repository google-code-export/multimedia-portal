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

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.springframework.web.multipart.MultipartFile;
import security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Photo {
	private Long id;
	private Long id_pages;
	private Long id_users;

	private Pages pages;
	private User user;

	private String name;
	private String description;
	private String title;
	private String tags;
	protected Integer width;
	protected Integer height;

	private Boolean active;
	private Boolean optimized;

	private MultipartFile content;
	private File content_file;

	private Double rating;

	private Date date_upload;
	private String type;

	private List photoRating = new Vector(0);
    private List<PhotoComment> comments = new Vector(0);
	protected List<Resolution> resolutions = new Vector(0);

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

	public Boolean getActive() {return active;}
	public void setActive(Boolean value) {this.active = value;}

	public Boolean getOptimized() {return optimized;}
	public void setOptimized(Boolean optimized) {this.optimized = optimized;}

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

	public List getPhotoRating() {return photoRating;}
	public void setPhotoRating(List value) {this.photoRating = value;}

	public User getUser() {return user;}
	public void setUser(User value) {this.user = value;}

	public List<PhotoComment> getComments() {return this.comments;}
	public void setComments(List<PhotoComment> value) {this.comments = value;}

	public File getContent_file() {return content_file;}
	public void setContent_file(File value) {this.content_file = value;}

	/**
	 * this method copies only id, id_pages, id_users, name, date_upload
	 * @param p
	 * @return
	 */
	public static Map toMap(Photo p){
		HashMap hm = new HashMap();
		hm.put("id", p.getId());
		hm.put("id_pages", p.getId_pages());
		hm.put("id_users", p.getId_users());
		hm.put("name", p.getName());
		hm.put("date_upload", p.getDate_upload());
		return hm;
	}

	public Integer getWidth() {return width;}
	public void setWidth(Integer width) {this.width = width;}

	public Integer getHeight() {return height;}
	public void setHeight(Integer height) {this.height = height;}

	public List<Resolution> getResolutions() {return resolutions;}
	public void setResolutions(List<Resolution> resolutions) {this.resolutions = resolutions;}
}
