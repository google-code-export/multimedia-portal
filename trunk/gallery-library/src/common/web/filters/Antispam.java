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

package common.web.filters;

import common.CommonAttributes;
import common.web.servlets.RandomImageServ;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Antispam implements Filter {

    private static final boolean debug = false;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public static final String ANTISPAM_PARAM_NAME = "anti_spam_code";

    public static final String ATTR_VIEW_URL = "view_url";

	protected String view_url;

    public Antispam() {}

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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        //long time = System.nanoTime();
        if (canAccess((HttpServletRequest)request)){
            //parameter equals
            chain.doFilter(request, response);
            /*java.io.File f = new java.io.File(getFilterConfig().getServletContext().getRealPath("/test.jpg"));
            java.io.FileInputStream fis = new java.io.FileInputStream(f);
			OutputStream os = response.getOutputStream();

            java.nio.channels.WritableByteChannel wbc =  java.nio.channels.Channels.newChannel(os);
            fis.getChannel().transferTo(0, fis.available(), wbc);

            fis.close();
            os.flush();
            os.close();
            time = System.nanoTime() - time;
            System.out.println("time = "+time);*/
        } else {
            CommonAttributes.addErrorMessage("anti_spam_code.different", request);
            //parameter not equals
            request.getRequestDispatcher(view_url).forward(request, response);
        }
    }

    public boolean canAccess(HttpServletRequest request){
        Object o = request.getSession().getAttribute(RandomImageServ.CODE_ATTR_DEFAULT);
        String par = request.getParameter(ANTISPAM_PARAM_NAME);
        //System.out.println("super.canAccess = "+(o!=null&&(!"".equals(par))&&o.equals(par))+"; o="+o+"; par="+par);
        return (o!=null&&(!"".equals(par))&&o.equals(par));
    }
    
    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {return (this.filterConfig);}

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
		 this.filterConfig = filterConfig;
		 if (filterConfig==null){
			 view_url = null;
		 } else {
			 view_url = filterConfig.getInitParameter(ATTR_VIEW_URL);
			 if (view_url==null||view_url.equals("")) view_url = null;
            if (debug) {
                log("Antispam:Initializing filter");
            }
		 }
	 }

    /**
     * Destroy method for this filter 
     */
    @Override
    public void destroy() {}

    /**
     * Init method for this filter 
     */
    @Override
    public void init(FilterConfig filterConfig) {
		 setFilterConfig(filterConfig);
    }

    public void log(String msg) { filterConfig.getServletContext().log(msg);}

}
