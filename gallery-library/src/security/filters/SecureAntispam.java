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

package security.filters;

import common.web.filters.Antispam;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import security.beans.User;

/**
 * this filter is checking an antispam code for user to get protected resource
 * but if user is logined and is in an appropriate role the code is not checked ...
 * @author demchuck.dima@gmail.com
 */
public class SecureAntispam extends Antispam{
    protected static final String DELIMITER = ",";
    /** name of parameters with valid roles */
    protected static final String ROLES_PARAM = "roles";
    /** valid roles to access without antispam code */
    private String[] roles = null;

    @Override
    public boolean canAccess(HttpServletRequest request) {
        if (super.canAccess(request)){
            return true;
        } else {
            User user = security.Utils.getCurrentUser(request.getSession(false));
            //    System.out.println(roles.length);
            //cheking if current user is in an appropriate role
            if (user!=null)
                for (String role:roles){
                    if (security.Utils.isUserInRole(user, role)){
                        return true;
                    }
                }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        super.init(filterConfig);
        if (filterConfig!=null){
            setRoles(filterConfig.getInitParameter(ROLES_PARAM));
        }
    }

    /**
     * parses given String
     * @param s
     */
    public void setRoles(String s){
        if (s!=null&&!s.isEmpty()){
            roles = s.split(DELIMITER);
        } else {
            roles = new String[0];
        }
    }

}
