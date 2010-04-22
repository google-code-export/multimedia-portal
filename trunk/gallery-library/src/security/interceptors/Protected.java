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

package security.interceptors;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import security.Utils;
import security.beans.Role;
import security.beans.User;
import security.services.ISecurityService;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Protected implements HandlerInterceptor{
	protected Logger logger = Logger.getLogger(this.getClass());
    /**
     * user rolesthat will be allowed to see protected content
     *  if no roles all authorized users can see
     */
    protected Role[] validRoles;

	protected ISecurityService service;

	public void init(){
		StringBuilder sb = new StringBuilder();
		if (validRoles==null){
			logger.warn("no roles specified. Any user can access.");
		}
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception
	{
		//logger.debug("Protected.preHandle()");

        User user=Utils.getCurrentUser(request);
        if (user==null){
            response.sendError(response.SC_NOT_FOUND);
			return false;
        }else{
			if (canAccess(user)){
				return true;
			}else{
				response.sendError(response.SC_NOT_FOUND);
				return false;
			}
        }
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception
	{}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception
	{}

    /**
     * determines if user has an appropriate role to see protected content
     * @param user user
     * @return true if can access
     */
    protected boolean canAccess(User user){
        //searching for an appropriate role
		if (validRoles==null){
			return true;
		}else{
			Set<Role> roles=user.getRoles();
			for (int i=0;i<validRoles.length;i++){
				if (roles.contains(validRoles[i])){
					return true;
				}
			}
		}
        return false;
    }

	/**
	 *
	 * converting strings to array of Role beans
	 * @param validRoles
	 */
	public void setValidRoles(String[] validRoles) {
		if (validRoles==null){
			this.validRoles = null;
		}else{
			this.validRoles = new Role[validRoles.length];
			for (int i=0;i<validRoles.length;i++){
				this.validRoles[i] = new Role(validRoles[i], null);
			}
		}
	}
	public void setService(ISecurityService service) {this.service = service;}

}
