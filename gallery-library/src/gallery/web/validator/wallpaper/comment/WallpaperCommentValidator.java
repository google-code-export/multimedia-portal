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

package gallery.web.validator.wallpaper.comment;

import com.multimedia.service.wallpaper.IWallpaperService;
import gallery.model.beans.WallpaperComment;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperCommentValidator extends common.bind.CommonBindValidator{
	protected IWallpaperService wallpaperService;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(wallpaperService, "wallpaperService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	//TODO: use it some where
	@Override
	public BindingResult bindAndValidate(Object command, HttpServletRequest request) {
		BindingResult res = super.bindAndValidate(command, request);
		if (!res.hasErrors()){
			WallpaperComment p = (WallpaperComment) command;
			//checking if there is such wallpaper
			if (wallpaperService.getRowCount("id", p.getId_photo())==1){
				p.setCreationTime(new Timestamp(System.currentTimeMillis()));
				//setting user
				p.setUser(security.Utils.getCurrentUser(request));
			}else{
				common.CommonAttributes.addErrorMessage("not_exists.wallpaper", request);
				res.reject("not_exists.wallpaper");
			}
		}
		return res;
	}

	public void setWallpaperService(IWallpaperService value) {this.wallpaperService = value;}

}
