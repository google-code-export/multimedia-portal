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

package common.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class RequestUtils {
	/**
	 *
	 * @param req
	 * @param name name of request parameter
	 * @return long or null, if parameter not exists or cannot be converted
	 */
	public static Long getLongParam(HttpServletRequest req,String name){
		try{
			long rez = Long.valueOf(req.getParameter(name), 10);
			return new Long(rez);
		}catch(Exception e){
			return null;
		}
	}
	/**
	 *
	 * @param req
	 * @param name name of request parameter
	 * @return boolean or null, if parameter not exists or cannot be converted
	 */
	public static Boolean getBoolParam(HttpServletRequest req,String name){
		try{
			Boolean rez = Boolean.valueOf(req.getParameter(name));
			return rez;
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * returns array of parameters as Long[]
	 * @param req request where to get value of param
	 * @param name name of request parameter
	 * @return long or null, if parameter not exists or cannot be converted
	 */
	public static Long[] getLongParameters(HttpServletRequest req,String name){
		try{
			String[] temp = req.getParameterValues(name);
			Long[] rez = new Long[temp.length];
			for (int i=0;i<temp.length;i++){
				rez[i] = Long.valueOf(temp[i]);
			}
			return rez;
		}catch(Exception e){
			return null;
		}
	}
	/**
	 *
	 * @param req
	 * @param name name of request parameter
	 * @param defaultVal the value to be returned if any exception occurs
	 * @return long or default, if parameter not exists or cannot be converted
	 */
	public static long getLongParam(HttpServletRequest req, String name, long defaultVal){
		try{
			return Long.valueOf(req.getParameter(name), 10);
		}catch(Exception e){
			return defaultVal;
		}
	}
	/**
	 *
	 * @param req
	 * @param name name of request parameter
	 * @return long or null, if parameter not exists or cannot be converted
	 */
	public static Integer getIntegerParam(HttpServletRequest req, String name){
		try{
			int rez = Integer.valueOf(req.getParameter(name), 10);
			return new Integer(rez);
		}catch(Exception e){
			return null;
		}
	}
	/**
	 *
	 * @param req
	 * @param m where to take attributes
	 */
	public static void copyRequestAttributesFromMap(HttpServletRequest req, Map m){
		if (m!=null&&!m.isEmpty()){
			Iterator<Entry<String,Object>> i = m.entrySet().iterator();
			while (i.hasNext()){
				Entry<String,Object> e = i.next();
				req.setAttribute(e.getKey(), e.getValue());
			}
		}
	}

	/**
	 * path is actually http://serverName:serverPort contextPath
	 * @param request http request
	 * @return path to server via http
	 */
	public static String getFullServerPathHttp(HttpServletRequest request){
		return "http://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath();
	}

	private RequestUtils() {}
}
