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

package gallery.model.command;

import common.beans.IMultiInsertBean;
import common.services.IInsertService;
import gallery.model.beans.Photo;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;
import security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MultiInsertPhotoCms implements IMultiInsertBean{
    private Long[] id;
    private Boolean[] active;
	private Long[] id_pages;
	private Long id_pages_one;
	private String[] description;
	private MultipartFile[] content;

	private User user;

	public MultiInsertPhotoCms(int size) {
		id = new Long[size];
		active = new Boolean[size];
		id_pages = new Long[size];
		description = new String[size];
		content = new MultipartFile[size];
		java.util.Arrays.fill(active, Boolean.FALSE);
	}

	public Long[] getId() {return id;}
	public void setId(Long[] id) {this.id = id;}

	public Boolean[] getActive() {return active;}
	public void setActive(Boolean[] active) {this.active = active;}

	public String[] getDescription() {return description;}
	public void setDescription(String[] description) {this.description = description;}

	public MultipartFile[] getContent() {return content;}
	public void setContent(MultipartFile[] content) {this.content = content;}

	public Long[] getId_pages() {return id_pages;}
	public void setId_pages(Long[] id_pages) {this.id_pages = id_pages;}

	public Long getId_pages_one() {return id_pages_one;}
	public void setId_pages_one(Long value) {this.id_pages_one = value;}

	public User getUser() {return user;}
	public void setUser(User user) {this.user = user;}

	@Override
	public int save(IInsertService service) {
		if (id!=null&&active!=null&&id_pages!=null&&description!=null&&content!=null){
			int count = 0;
			for (int i=0;i<id.length;i++){
				Photo p = new Photo();
				if (id[i]!=null&&content[i]!=null){
					p.setActive(active[i]);
					p.setContent(content[i]);
					p.setDate_upload(new Date());
					p.setDescription(description[i]);
					if (id_pages_one!=null){
						p.setId_pages(id_pages_one);
					}else{
						p.setId_pages(id_pages[i]);
					}
					p.setUser(user);
					service.insert(p);
					count++;
				}
			}
			return count;
		}else{
			return -1;
		}
	}

	public int getSize(){
		if (id!=null){
			return id.length-1;
		}else{
			return 0;
		}
	}

	@Override
	public boolean isModel() {return true;}
}
