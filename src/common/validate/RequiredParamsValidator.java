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

package common.validate;


import common.beans.BeanManager;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

/**
 * validates all types for being null
 * also String for being an empty String
 * and Multipart file for being empty
 * @author demchuck.dima@gmail.com
 */
public class RequiredParamsValidator implements Validator{
    protected String[] required_properties=null;

	private BeanManager manager;

	/**
	 * @param properties names of properties
	 * @param manager to access bean properties
	 */
	public RequiredParamsValidator(BeanManager manager, String[] properties){
		this.required_properties = properties;
		this.manager = manager;
		this.manager.addProperties(this.required_properties);
	}

	@Override
	public boolean supports(Class arg0) {
		return manager.getKlass().isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object command, Errors err) {
		for (String prop:required_properties){
			Object o = manager.getProperty(command, prop);
			if (o==null||
					(o instanceof String && ((String)o).equals(""))||
					(o instanceof MultipartFile&&((MultipartFile)o).isEmpty())){
				err.rejectValue(prop, "required");
			}
		}
	}
    
}