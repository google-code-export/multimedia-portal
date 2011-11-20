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

package gallery.service.wallpaper;

import com.multimedia.service.wallpaper.IWallpaperService;
import common.email.IMailService;
import common.services.IDeleteService;
import common.services.IUpdateService;
import gallery.model.beans.Wallpaper;
import gallery.service.pages.IPagesService;
import gallery.service.resolution.IResolutionService;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperServiceViewImpl implements IUpdateService<Wallpaper, Long>, IDeleteService<Long>{
	protected IWallpaperService wallpaper_service;
	protected IResolutionService resolution_service;
	protected IPagesService pages_service;
	protected IMailService mail_service;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(wallpaper_service, "wallpaper_service", sb);
		common.utils.MiscUtils.checkNotNull(resolution_service, "resolution_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		common.utils.MiscUtils.checkNotNull(mail_service, "mail_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public Wallpaper getUpdateBean(Long id) {return wallpaper_service.getById(id);}

	@Override
	public Map<String, Object> initUpdate() {
		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("categories_wallpaper_select", pages_service.getAllCombobox(Boolean.TRUE, Boolean.TRUE, gallery.web.controller.pages.types.WallpaperGalleryType.TYPE));
		return m;
	}

	@Override
	public int update(Wallpaper command) {
		if (command.getContent()==null){
			//we just need to update rows in database
			wallpaper_service.save(command);
		}else{
			//1-st delete old wallpapers
			if (wallpaper_service.deleteFiles(command)&&wallpaper_service.getImage(command)){
				//2-nd update rows in database, create new, and count new resolution
				wallpaper_service.save(command);
			}else{
				return -1;
			}
		}
		return 1;
	}

	@Override
	public int deleteById(Long id) {return wallpaper_service.deleteById(id);}

	public void setWallpaper_service(IWallpaperService value){this.wallpaper_service = value;}
	public void setResolution_service(IResolutionService resolution_service){this.resolution_service = resolution_service;}
	public void setPages_service(IPagesService pages_service){this.pages_service = pages_service;}
	public void setMail_service(IMailService value){this.mail_service = value;}
}
