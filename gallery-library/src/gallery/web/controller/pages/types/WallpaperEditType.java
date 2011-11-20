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

import common.bind.ABindValidator;
import common.services.IUpdateService;
import common.web.controller.CommonActions;
import gallery.model.beans.Wallpaper;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperEditType extends ASingleContentType{
    /** string constant that represents type for this page */
    public static final String TYPE="system_edit_wallpaper";
	/** rus type */
	public static final String TYPE_RU="---Редактирование фото---";

	@Override
	public String getType() {return TYPE;}
	@Override
	public String getTypeRu() {return TYPE_RU;}

	private ABindValidator updateValidator;
	private IUpdateService<Wallpaper, Long> service;

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(updateValidator, "updateValidator", sb);
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public UrlBean execute(HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws Exception
	{
		//req.setAttribute("editForm_topHeader", "Редактирование");

		UrlBean url = new UrlBean();
		if (CommonActions.<Wallpaper>doUpdate(service, config, updateValidator, request)){
			url.setContent(contentUrl);
		} else {
			request.setAttribute(config.getContentDataAttribute(), new Wallpaper());
		}

        return url;
	}

	public void setUpdateValidator(ABindValidator value) {this.updateValidator = value;}
	public void setService(IUpdateService<Wallpaper, Long> value) {this.service = value;}

}
