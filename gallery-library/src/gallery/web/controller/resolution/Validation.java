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

package gallery.web.controller.resolution;

import gallery.model.beans.Resolution;
import gallery.service.resolution.IResolutionService;
import org.springframework.validation.Errors;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Validation {
	private IResolutionService service;

	protected static final String[] PROPERTY_NAMES = new String[]{"width","height"};

	public void validateCMS(Object target, Errors errors) {
		Resolution p = (Resolution) target;
		if (p.getHeight()!=null&&p.getHeight().intValue()<1){
			errors.rejectValue("height", "typeMismatch.sort");
		}
		if (p.getWidth()!=null&&p.getWidth().intValue()<1){
			errors.rejectValue("width", "typeMismatch.sort");
		}
		//checks if such resolution is allready in database
		if (!errors.hasErrors()){
			Long c = service.getRowCount(PROPERTY_NAMES, new Object[]{p.getWidth(),p.getHeight()});
			if (c.longValue()>0){
				errors.reject("resolution.duplicate");
			}
		}
	}

	/**
	 * @param service the service to set
	 */
	public Validation(IResolutionService service) {
		this.service = service;
	}
}
