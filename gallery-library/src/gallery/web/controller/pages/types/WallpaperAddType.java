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
import common.bind.ABindValidator;
import gallery.model.beans.Wallpaper;
import gallery.service.pages.IPagesService;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperAddType extends ASingleContentType{
	protected final Logger logger = Logger.getLogger(getClass());
    /** string constant that represents type for this page */
    public static final String TYPE="system_add_wallpaper";
	/** rus type */
	public static final String TYPE_RU="---Добавление фото---";

	@Override
	public String getType() {return TYPE;}
	@Override
	public String getTypeRu() {return TYPE_RU;}

	public static final String CATEGORIES_ATTR = "categories_wallpaper";
	/** validator */
	ABindValidator validator;
	/** service for working with pages */
	protected IPagesService pagesService;
	/** service for working with wallpaper */
	protected IWallpaperService wallpaperService;

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(validator, "validator", sb);
		common.utils.MiscUtils.checkNotNull(pagesService, "pagesService", sb);
		common.utils.MiscUtils.checkNotNull(wallpaperService, "wallpaperService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public UrlBean execute(HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws Exception
	{
		request.setAttribute(CATEGORIES_ATTR, pagesService.getAllCombobox(Boolean.TRUE, Boolean.TRUE, WallpaperGalleryType.TYPE));

		String action = request.getParameter(gallery.web.controller.Config.ACTION_PARAM_NAME);
		/** bind command */
		Wallpaper command = new Wallpaper();
		if ("addWallpaper".equals(action)){
			BindingResult res = validator.bindAndValidate(command, request);
			if (res.hasErrors()||!wallpaperService.getImage(command)){
				//m.putAll(res.getModel());
				request.setAttribute(res.MODEL_KEY_PREFIX+"command", res);
				request.setAttribute("command", command);
				common.CommonAttributes.addErrorMessage("form_errors", request);
			}else{
				//command.setUser(security.Utils.getCurrentUser(request));
				wallpaperService.save(command);
				request.setAttribute("command", new Wallpaper());
				common.CommonAttributes.addHelpMessage("operation_succeed", request);
			}
		}else{
			request.setAttribute("command", command);
		}

		UrlBean url = new UrlBean();
		url.setContent(contentUrl);

        return url;
	}

	public void setPagesService(IPagesService service) {this.pagesService = service;}
	public void setWallpaperService(IWallpaperService value) {this.wallpaperService = value;}
	public void setBindValidator(ABindValidator validator){this.validator = validator;}

}
