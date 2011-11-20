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

package gallery.web.controller.wallpaper;

import com.multimedia.service.wallpaper.IWallpaperService;
import common.bind.ABindValidator;
import common.bind.CommonBindValidator;
import common.cms.ICmsConfig;
import gallery.model.beans.Wallpaper;
import gallery.model.command.WallpaperBackupCms;
import gallery.web.controller.Config;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperBackupControllerCms implements Controller{
	private IWallpaperService wallpaper_service;
	/** config class is used to store some constants */
	protected ICmsConfig config;

	protected ABindValidator validator;

	protected String beforeUrl;
	protected String afterUrl;
	protected String navigationUrl;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(wallpaper_service, "wallpaper_service", sb);
		common.utils.MiscUtils.checkNotNull(config, "config", sb);
		common.utils.MiscUtils.checkNotNull(beforeUrl, "beforeUrl", sb);
		common.utils.MiscUtils.checkNotNull(afterUrl, "afterUrl", sb);
		common.utils.MiscUtils.checkNotNull(navigationUrl, "navigationUrl", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}else{
			validator = new CommonBindValidator();
			//logger.debug("initialized successfully. ");
		}
	}

	public static final String[] PSEUDONYMES = new String[]{"id","name"};

	public static final String RESTORE_VALUE = "restore";
	public static final String BACKUP_VALUE  = "backup";
	public static final String IMPORT_VALUE  = "import";
	public static final String EXPORT_VALUE  = "export";

	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse arg1)
			throws Exception
	{
		String action_param = req.getParameter(Config.ACTION_PARAM_NAME);
		String do_param = req.getParameter("do");

		req.setAttribute("title","Локальная система управления --> Backup / Restore");
		req.setAttribute(config.getNavigationUrlAttribute(), navigationUrl);

		WallpaperBackupCms command = new WallpaperBackupCms();
		BindingResult res = validator.bindAndValidate(command, req);
		boolean handled = false;

		long l = System.currentTimeMillis();
		if (!res.hasErrors()){
			if (BACKUP_VALUE.equals(do_param)){
				req.setAttribute("top_header","Backup");
				req.setAttribute(config.getContentUrlAttribute(), afterUrl);
				List<Wallpaper> wallpapers = wallpaper_service.getOrdered(PSEUDONYMES, null, null);
				req.setAttribute(config.getContentDataAttribute(), wallpapers);

				wallpaper_service.backupWallpapers(wallpapers, command.getAppend_all(), Boolean.TRUE);
				handled = true;
			}else if (RESTORE_VALUE.equals(do_param)){
				req.setAttribute("top_header","Restore");
				req.setAttribute(config.getContentUrlAttribute(), afterUrl);
				List<Wallpaper> wallpapers = wallpaper_service.getOrdered(PSEUDONYMES, null, null);
				req.setAttribute(config.getContentDataAttribute(), wallpapers);

				wallpaper_service.restoreWallpapers(wallpapers, command.getAppend_all(), Boolean.TRUE);
				handled = true;
			}
		}
		if (!handled){
			req.setAttribute("top_header","Подготовка Backup/Restore");
			req.setAttribute(config.getContentUrlAttribute(), beforeUrl);
		}
		l = System.currentTimeMillis() - l;

		//DateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss.S");
		req.setAttribute("time", l);

		return new ModelAndView(config.getTemplateUrl());
	}

	public void setWallpaper_service(IWallpaperService value) {this.wallpaper_service = value;}
	public void setConfig(ICmsConfig config){this.config = config;}

	public void setBeforeUrl(String beforeUrl) {this.beforeUrl = beforeUrl;}
	public void setAfterUrl(String afterUrl) {this.afterUrl = afterUrl;}
	public void setNavigationUrl(String navigationUrl) {this.navigationUrl = navigationUrl;}
}
