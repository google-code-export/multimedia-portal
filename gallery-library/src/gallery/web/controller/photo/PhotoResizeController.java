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

import common.utils.RequestUtils;
import gallery.model.beans.Resolution;
import gallery.service.photo.IPhotoService;
import gallery.service.resolution.IResolutionService;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PhotoResizeController extends AbstractController{
	private IResolutionService<Resolution, Long> resolutionService;
	private IPhotoService photoService;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(resolutionService, "resolutionService", sb);
		common.utils.MiscUtils.checkNotNull(photoService, "photoService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id_photo = RequestUtils.getLongParam(request, "id_photo");
		Long id_resolution = RequestUtils.getLongParam(request, "id_resolution");
		if (id_photo!=null&&id_resolution!=null){
			Resolution r = resolutionService.getById(id_resolution);
			try {
				OutputStream os = response.getOutputStream();
				//if (!photoService.getResizedPhotoStream(id_photo, r.getWidth(), os)){
				if (!photoService.getResizedPhotoStream(id_photo, r.getWidth(), r.getHeight(), os)){
					//error handling
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
				os.flush();
				os.close();
			} catch (IOException ex) {
				logger.error("while resizing image ", ex);
			}
		}
		return null;
	}

	public void setResolutionService(IResolutionService<Resolution, Long> resolutionService) {this.resolutionService = resolutionService;}
	public void setPhotoService(IPhotoService photoService) {this.photoService = photoService;}

}
