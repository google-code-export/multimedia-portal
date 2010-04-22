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

package common.web.controller;

import common.web.IServletContextGetter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MultiactionController2 extends MultiActionController implements IServletContextGetter{

	private String errorMethod = "doError";

	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		return invokeNamedMethod(errorMethod, request, response);
	}

	public String getErrorMethod() {return errorMethod;}
	public void setErrorMethod(String errorMethod) {this.errorMethod = errorMethod;}

	@Override
	public ServletContext getContext() {return super.getServletContext();}

}
