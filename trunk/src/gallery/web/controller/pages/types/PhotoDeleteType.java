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

import common.beans.KeepParameters;
import common.services.IDeleteService;
import common.utils.RequestUtils;
import common.web.controller.CommonActions;
import gallery.service.photo.IPhotoService;
import gallery.web.validator.photo.PhotoUpdateBindValidator;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PhotoDeleteType extends ASingleContentType{
	protected final Logger logger = Logger.getLogger(getClass());
    /** string constant that represents type for this page */
    public static final String TYPE="system_delete_photo";
	/** rus type */
	public static final String TYPE_RU="---Удаление фото---";

	public static final String KEEP_PARAMETERS_ATTR = "query_string";
	/** service for validating */
	private IPhotoService photo_service;
	/** service for deleting */
	private IDeleteService delete_service;
	/** for keeping parameters */
	private KeepParameters keepParameters;

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(photo_service, "photo_service", sb);
		common.utils.MiscUtils.checkNotNull(delete_service, "delete_service", sb);
		common.utils.MiscUtils.checkNotNull(keepParameters, "keepParameters", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public String getType() {return TYPE;}
	@Override
	public String getTypeRu() {return TYPE_RU;}

	@Override
	public UrlBean execute(HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws Exception
	{
		Long id = RequestUtils.getLongParam(request, "id");
		if (PhotoUpdateBindValidator.validate(id, null, request, photo_service)){
			CommonActions.doDelete(delete_service, request);
		}else{
			common.CommonAttributes.addErrorMessage("operation_fail", request);
		}

		UrlBean url = new UrlBean();
		url.setContent(contentUrl);
		request.setAttribute(KEEP_PARAMETERS_ATTR, keepParameters.getKeepParameters(request));

        return url;
	}

	public void setPhoto_service(IPhotoService photo_service) {this.photo_service = photo_service;}
	public void setDelete_service(IDeleteService delete_service) {this.delete_service = delete_service;}
	public void setKeepParameters(KeepParameters keepParameters) {this.keepParameters = keepParameters;}

}
