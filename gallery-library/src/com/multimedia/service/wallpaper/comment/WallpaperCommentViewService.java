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

package com.multimedia.service.wallpaper.comment;

import common.services.IInsertService;
import common.services.generic.IGenericService;
import gallery.model.beans.WallpaperComment;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="wallpaperCommentViewServices")
public class WallpaperCommentViewService implements IInsertService<WallpaperComment>{
	protected IGenericService<WallpaperComment, Long> service;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public WallpaperComment getInsertBean() {
		return new WallpaperComment();
	}

	@Override
	public boolean insert(WallpaperComment obj) {
		service.save(obj);
		return true;
	}

	@Override
	public Map<String, Object> initInsert() {return null;}

	//--------------------------------------------------------- initialization --------------------------------------
	@Resource(name="wallpaperCommentService")
	public void setWallpaperCommentService(IGenericService<WallpaperComment, Long> value){this.service = value;}

}
