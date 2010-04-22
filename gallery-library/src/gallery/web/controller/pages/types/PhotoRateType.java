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

import gallery.model.beans.PhotoRating;
import gallery.service.photo.IPhotoService;
import gallery.service.photoRating.IPhotoRatingService;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PhotoRateType implements IPagesType{
	protected final Logger logger = Logger.getLogger(getClass());
    /** string constant that represents type for this page */
    public static final String TYPE="system_rate_photo";
	/** rus type */
	public static final String TYPE_RU="---Оценка фото---";

	@Override
	public String getType() {return TYPE;}
	@Override
	public String getTypeRu() {return TYPE_RU;}

	/** service for working with photo rating */
	protected IPhotoRatingService photoRatingService;

	protected IPhotoService photoService;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(photoRatingService, "photoRatingService", sb);
		common.utils.MiscUtils.checkNotNull(photoService, "photoService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	protected static final String[] RATE_PHOTO_WHERE = new String[]{"id_photo","ip"};
	public static final String[] REQUIRED_FIELDS = new String[]{"id_photo","rate"};
	@Override
	public UrlBean execute(HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws Exception
	{
		/** bind command */
		PhotoRating command = new PhotoRating();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(command);
		binder.setRequiredFields(REQUIRED_FIELDS);

		binder.bind(request);
		BindingResult res = binder.getBindingResult();

		int error = '1';
		if (res.hasErrors()){
			common.CommonAttributes.addErrorMessage("form_errors", request);
		}else{
			//correcting rating
			gallery.web.support.photoRating.Utils.correctRate(command);

			command.setIp(request.getRemoteAddr());
			if (photoService.getRowCount("id", command.getId_photo())==1){
				Object[] values = new Object[]{command.getId_photo(),command.getIp()};
				if (photoRatingService.getRowCount(RATE_PHOTO_WHERE, values)>0){
					common.CommonAttributes.addErrorMessage("duplicate_ip", request);
				}else{
					photoRatingService.save(command);
					common.CommonAttributes.addHelpMessage("operation_succeed", request);
					error = '0';
				}
			}else{
				common.CommonAttributes.addErrorMessage("not_exists.photo", request);
			}
		}
		OutputStream os = response.getOutputStream();
		os.write(error);
		os.flush();

        return null;
	}

	public void setPhotoRatingService(IPhotoRatingService service) {this.photoRatingService = service;}
	public void setPhotoService(IPhotoService service) {this.photoService = service;}
}
