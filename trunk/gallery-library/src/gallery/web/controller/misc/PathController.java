/*
 *  Copyright 2010 demchuck.dima@gmail.com.
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

package gallery.web.controller.misc;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Controller(value="pathController")
public class PathController {
	protected Logger logger = Logger.getLogger(getClass());

	@RequestMapping(value="/{test}")
	@ResponseBody
	public String helloWorld(@PathVariable("test") String[] test){
		logger.info("hello - "+test);
		String rez = "";
		for (int i=0;i<test.length;i++){
			rez+=test[i];
		}
		return "hello:"+rez;
	}

	@RequestMapping
	@ResponseBody
	public String hello(HttpServletRequest request){
		logger.info("encoding="+request.getCharacterEncoding());
		
		logger.info("info="+request.getPathInfo());
		logger.info("uri="+request.getRequestURI());
		logger.info("hello");

		return "hello world<br>"+"info="+request.getPathInfo()+"<br>uri="+request.getRequestURI();
	}
}
