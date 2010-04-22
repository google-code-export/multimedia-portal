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

package gallery.web.controller.photo;

import common.bind.ABindValidator;
import common.bind.CommonBindValidator;
import common.cms.ICmsConfig;
import gallery.model.beans.Photo;
import gallery.model.command.PhotoBackupCms;
import gallery.service.photo.IPhotoService;
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
public class PhotoBackupControllerCms implements Controller{
	private IPhotoService photo_service;
	/** config class is used to store some constants */
	protected ICmsConfig config;

	protected ABindValidator validator;

	protected String beforeUrl;
	protected String afterUrl;
	protected String navigationUrl;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(photo_service, "photo_service", sb);
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

		PhotoBackupCms command = new PhotoBackupCms();
		BindingResult res = validator.bindAndValidate(command, req);
		boolean handled = false;

		long l = System.currentTimeMillis();
		if (!res.hasErrors()){
			if (BACKUP_VALUE.equals(do_param)){
				req.setAttribute("top_header","Backup");
				req.setAttribute(config.getContentUrlAttribute(), afterUrl);
				List<Photo> photos;
				//if (command.getOnly_files()){
				photos = photo_service.getAllShortOrdered(PSEUDONYMES, null, null);
				/*} else {
					photos = photo_service.getAllShortOrdered(null, null, null);
				}*/
				req.setAttribute(config.getContentDataAttribute(), photos);

				photo_service.backupPhotos(photos, command.getAppend_all(), Boolean.TRUE);
				handled = true;
			}else if (RESTORE_VALUE.equals(do_param)){
				req.setAttribute("top_header","Restore");
				req.setAttribute(config.getContentUrlAttribute(), afterUrl);
				List<Photo> photos;
				//if (command.getOnly_files()){
                photos = photo_service.getAllShortOrdered(PSEUDONYMES, null, null);
				/**} else {
					photos = photo_service.getAllShortOrdered(null, null, null);
				}*/
				req.setAttribute(config.getContentDataAttribute(), photos);

				photo_service.restorePhotos(photos, command.getAppend_all(), Boolean.TRUE);
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

	public void setPhoto_service(IPhotoService photo_service) {this.photo_service = photo_service;}
	public void setConfig(ICmsConfig config){this.config = config;}

	public void setBeforeUrl(String beforeUrl) {this.beforeUrl = beforeUrl;}
	public void setAfterUrl(String afterUrl) {this.afterUrl = afterUrl;}
	public void setNavigationUrl(String navigationUrl) {this.navigationUrl = navigationUrl;}
}
