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

package common.cms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * like simple but provides filtering functionality instead of show all cms
 * @author demchuck.dima@gmail.com
 */
public class FilteredCmsDelegate<T> extends SimpleCmsDelegate<T>{
	/**
	 * must show items associated with this table
	 * that matches given criteria
	 * @param req
	 * @param resp
	 * @return
	 */
	@Override
	public ModelAndView doView(HttpServletRequest req, HttpServletResponse resp){
		return doFilteredView(req, resp);
    }

}
