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

package common.bind;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public abstract class ABindValidator{

	public BindingResult bindAndValidate(Object command, HttpServletRequest request){
		ServletRequestDataBinder binder = new ServletRequestDataBinder(command);

		initBinder(binder);
		binder.bind(request);
		BindingResult res = binder.getBindingResult();
		validate(command, res, request);
		return res;
	}

	/**
	 * validation of command and request may be placed here
	 * @param command
	 * @param err
	 * @param request
	 */
	protected abstract void validate(Object command, Errors err, HttpServletRequest request);

	/**
	 * implement all actions you want to do before binding
	 * i.e. set binder's required, avaible fields ...
	 * @param binder
	 */
	protected abstract void initBinder(ServletRequestDataBinder binder);

}
