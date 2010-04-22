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

package gallery.service.photo;

import common.email.IMailService;
import common.services.IDeleteService;
import common.services.IUpdateService;
import gallery.model.beans.Photo;
import gallery.service.pages.IPagesService;
import gallery.service.resolution.IResolutionService;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PhotoServiceViewImpl implements IUpdateService<Photo, Long>, IDeleteService<Long>{
	protected IPhotoService photo_service;
	protected IResolutionService resolution_service;
	protected IPagesService pages_service;
	protected IMailService mail_service;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(photo_service, "photo_service", sb);
		common.utils.MiscUtils.checkNotNull(resolution_service, "resolution_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		common.utils.MiscUtils.checkNotNull(mail_service, "mail_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public Photo getUpdateBean(Long id) {return photo_service.getById(id);}

	@Override
	public Map initUpdate() {
		HashMap m = new HashMap();
		m.put("categories_photo_select", pages_service.getAllCombobox(Boolean.TRUE, Boolean.TRUE, gallery.web.controller.pages.types.WallpaperGalleryType.TYPE));
		return m;
	}

	@Override
	public int update(Photo command) {
		if (command.getContent()==null){
			//we just need to update rows in database
			photo_service.save(command);
		}else{
			//1-st delete old photos
			if (photo_service.deleteFiles(command)&&photo_service.getPhoto(command)){
				//2-nd update rows in database, create new, and count new resolution
				photo_service.save(command);
			}else{
				return -1;
			}
		}
		return 1;
	}

	@Override
	public int deleteById(Long id) {return photo_service.deleteById(id);}

	public void setPhoto_service(IPhotoService value){this.photo_service = value;}
	public void setResolution_service(IResolutionService resolution_service){this.resolution_service = resolution_service;}
	public void setPages_service(IPagesService pages_service){this.pages_service = pages_service;}
	public void setMail_service(IMailService value){this.mail_service = value;}
}
