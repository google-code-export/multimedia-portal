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

package common.beans;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class KeepParameters {
    protected String[] param_names;
    protected String[] param_pseudo;

	protected boolean first;

    /**
     * Creates a new instance of KeepParameters
	 * params_pseudo are same as param_names, first delim is &amp;
     * @param param_names names of parameters that should be kept in all urls
     */
    public KeepParameters(String[] param_names) {
		this(param_names, param_names, false);
	}
    
    /**
     * Creates a new instance of KeepParameters
     * @param param_names names of parameters that should be kept in all urls
	 * @param first if true first delimiter is ? else &amp;
     */
    public KeepParameters(String[] param_names, boolean first) {
		this(param_names, param_names, first);
	}

    /**
     * Creates a new instance of KeepParameters
	 * first delim is &amp;
     * @param param_names names of parameters that should be kept in all urls
     * @param param_pseudo names of parameters that should be kept set after request
     */
    public KeepParameters(String[] param_names, String[] param_pseudo) {
		this(param_names, param_pseudo, false);
    }

    /**
     * Creates a new instance of KeepParameters
     * @param param_names names of parameters that should be kept in all urls
     * @param param_pseudo names of parameters that should be kept set after request
	 * @param first if true first delimiter is ? else &amp;
     */
    public KeepParameters(String[] param_names, String[] param_pseudo, boolean first) {
        this.param_names=param_names;
		this.param_pseudo=param_pseudo;
		this.first = first;
    }
    
    /**
     * this function returns a string containing all parametrs that should be
     * added to url before sending redirect
     * it looks like:
     * pseud1=request.getParameter(parName1)&...&pseudN=request.getParameter(parNameN)
     * @param req actually the request we are handling
     * @return qctually a query string
     */
    public String getKeepParameters(HttpServletRequest req){
        StringBuilder rez=new StringBuilder();
		boolean f_t = first;
		String param;
        for (int i=0;i<param_names.length;i++){
            param=req.getParameter(param_names[i]);
			if (param!=null&&!"".equals(param)){
				rez.append(f_t?"?":"&amp;");
				rez.append(param_pseudo[i]);
				rez.append("=");
				rez.append(param);
				f_t = false;
			}
        }
        return rez.toString();
    }
    
}
