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

package gallery.web.validator.photoComment;

import gallery.model.beans.PhotoComment;
import gallery.service.photo.IPhotoService;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PhotoCommentValidator extends common.bind.CommonBindValidator{
	protected IPhotoService photoService;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(photoService, "photoService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public BindingResult bindAndValidate(Object command, HttpServletRequest request) {
		BindingResult res = super.bindAndValidate(command, request);
		if (!res.hasErrors()){
			PhotoComment p = (PhotoComment) command;
			//checking if there is such photo
			if (photoService.getRowCount("id", p.getId_photo())==1){
				p.setCreationTime(new Date());
				//setting user
				p.setUser(security.Utils.getCurrentUser(request));
			}else{
				common.CommonAttributes.addErrorMessage("not_exists.photo", request);
				res.reject("not_exists.photo");
			}
		}
		return res;
	}

	public void setPhotoService(IPhotoService value) {this.photoService = value;}

}
