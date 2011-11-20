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

package common.web.filters;

import common.beans.StatisticsBean;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class StatisticFilter implements Filter {
	protected final Logger logger = Logger.getLogger(getClass());

	public static final String STAT_NAME = "STAT_NAME_FILTER";

	protected Boolean logTime = Boolean.TRUE;

	protected StatisticsBean statsBean;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
		throws IOException, ServletException
	{
		//getting time
		if (logTime){
			request.setAttribute("timeStart", Long.valueOf(getTime()));
		}
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
		throws IOException, ServletException
	{
		if (logTime){
			Long time_start = (Long)request.getAttribute("timeStart");
			if (time_start!=null){
				long time = getTime() - time_start;
				statsBean.increaseStat(time);
				if (logger.isDebugEnabled()){
					logger.debug("processed time="+time);
				}
			}
		}

    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
	@Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
		throws IOException, ServletException
	{

		doBeforeProcessing(request, response);

		Throwable problem = null;
		try {
			chain.doFilter(request, response);
		} catch(Throwable t) {
			// If an exception is thrown somewhere down the filter chain,
			// we still want to execute our after processing, and then
			// rethrow the problem after that.
			problem = t;
		}

		doAfterProcessing(request, response);

		// If there was a problem, we want to rethrow it if it is
		// a known type, otherwise log it.
		if (problem != null) {
			if (problem instanceof ServletException) throw (ServletException)problem;
			if (problem instanceof IOException) throw (IOException)problem;
			if (problem instanceof RuntimeException) throw (RuntimeException)problem;
			logger.error("even xz what other error can occur here");
		}
    }
    
    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
	return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
	this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter 
     */
	@Override
    public void destroy() {
		statsBean = null;
    }

    /**
     * Init method for this filter 
     */
	@Override
    public void init(FilterConfig filterConfig) {
		logger.info("statistic filter initialzied");
		this.filterConfig = filterConfig;
		statsBean = new StatisticsBean(getTime());
		if (filterConfig!=null){
			filterConfig.getServletContext().setAttribute(STAT_NAME, statsBean);
		}
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
		if (filterConfig == null) return ("NewFilter()");
		StringBuilder sbr = new StringBuilder("NewFilter(");
		sbr.append(filterConfig);
		sbr.append(")");
		return (sbr.toString());
    }

	public static StatisticsBean getStatistics(ServletContext sc){
		return (StatisticsBean)sc.getAttribute(STAT_NAME);
	}

	public static long getTime(){return System.nanoTime();}
}
