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

package gallery.web.controller.pages.types;

import com.multimedia.service.wallpaper.IWallpaperService;
import gallery.web.controller.pages.submodules.EmptySubmodule;
import gallery.web.controller.pages.submodules.ASubmodule;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import security.Utils;
import security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperListType extends ASingleContentType{
	protected final Logger logger = Logger.getLogger(getClass());
    /** string constant that represents type for this page */
    public static final String TYPE="system_wallpaper_list";
	/** rus type */
	public static final String TYPE_RU="---Фотографии пользователя---";

	@Override
	public String getType() {return TYPE;}
	@Override
	public String getTypeRu() {return TYPE_RU;}

	/** service for working with data */
	protected IWallpaperService wallpaperService;
	/** attribute for storing categories vector for view */
	public static final String WALLPAPERS_ATTRIBUTE = "wallpapers";
	/** names of properties to select */
	public static final String[] SELECT_FIELDS = new String[]{"id","name","active","id_pages","width","height"};

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(wallpaperService, "wallpaperService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	public static final String[] ORDER_BY = new String[]{"date_upload"};
	public static final String[] ORDER_HOW = new String[]{"DESC"};
	@Override
	public UrlBean execute(HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws Exception
	{
		UrlBean url = new UrlBean();
		url.setContent(contentUrl);

		User user = Utils.getCurrentUser(request);
		if (user!=null&&Utils.isUserInRole(user, "user")){
			HashMap<String, ASubmodule> hs = new HashMap<String, ASubmodule>();
			hs.put(gallery.web.controller.pages.types.WallpaperDeleteType.TYPE, new EmptySubmodule());
			hs.put(gallery.web.controller.pages.types.WallpaperEditType.TYPE, new EmptySubmodule());
			hs.put(gallery.web.controller.pages.types.WallpaperAddType.TYPE, new EmptySubmodule());
			url.setSubmodules(hs);
			request.setAttribute(WALLPAPERS_ATTRIBUTE, wallpaperService.getByPropertyValueOrdered(SELECT_FIELDS,"id_users",user.getId(),ORDER_BY,ORDER_HOW));
		}else{
			response.sendError(response.SC_NOT_FOUND);
		}
		return url;
	}

	public void setWallpaperService(IWallpaperService value) {this.wallpaperService = value;}

}
