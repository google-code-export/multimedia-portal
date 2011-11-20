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

import common.cms.ICmsConfig;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class StatisticController implements Controller{
	private ICmsConfig conf;

	protected String content_url;
	protected String navigation_url;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(conf, "conf", sb);
		common.utils.MiscUtils.checkNotNull(content_url, "content_url", sb);
		common.utils.MiscUtils.checkNotNull(navigation_url, "navigation_url", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		request.setAttribute(conf.getContentUrlAttribute(), content_url);
		request.setAttribute(conf.getNavigationUrlAttribute(), navigation_url);

		request.setAttribute("title", "Статистика");
		request.setAttribute("top_header", "Статистика");

		createModel(request);

		return new ModelAndView(conf.getTemplateUrl());
	}

	public void createModel(HttpServletRequest request){
        long[] result = new long[16];
        boolean ok = false;
        try {
            String methodName = "info";
            Class paramTypes[] = new Class[1];
            paramTypes[0] = result.getClass();
            Object paramValues[] = new Object[1];
            paramValues[0] = result;
            Method method = Class.forName("org.apache.tomcat.jni.OS")
                .getMethod(methodName, paramTypes);
            method.invoke(null, paramValues);
            ok = true;
        } catch (Throwable t) {
            // Ignore
        }

        if (ok) {
			Long time_kernel = Long.valueOf(result[11] / 1000);
			Long time_user = Long.valueOf(result[12] / 1000);
			Long time_work = Long.valueOf(System.currentTimeMillis() - result[10]/1000);
			Double percent_work = new Double((double)time_user*(double)100/(double)time_work);
			request.setAttribute("time_user", formatTime(time_user, ok));
			request.setAttribute("time_kernel", formatTime(time_kernel, ok));
			request.setAttribute("time_work", formatTime(time_work, ok));
			request.setAttribute("percent_work", percent_work);
        }
	}

    /**
     * Display the given time in ms, either as ms or s.
     *
     * @param seconds true to display seconds, false for milliseconds
     */
    public static String formatTime(Object obj, boolean seconds) {

        long time = -1L;

        if (obj instanceof Long) {
            time = ((Long) obj).longValue();
        } else if (obj instanceof Integer) {
            time = ((Integer) obj).intValue();
        }

        if (seconds) {
            return ((((float) time ) / 1000) + " s");
        } else {
            return (time + " ms");
        }
    }

	public void setConfig(ICmsConfig conf) {this.conf = conf;}
	public void setContent_url(String value){this.content_url = value;}
	public void setNavigation_url(String value){this.navigation_url = value;}

}