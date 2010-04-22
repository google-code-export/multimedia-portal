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

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import security.Config;
import security.beans.User;
import security.Utils;
import security.services.ISecurityService;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Authorization implements HandlerInterceptor{
	protected Logger logger = Logger.getLogger(this.getClass());
	/** service for working with user */
	protected ISecurityService securityService;
	//error messages
    public static final String LOGIN_ERROR="Authorization.LOGIN_ERROR";
    public static final String LOGIN_FAILED="Authorization.LOGIN_FAILED";
    public static final String LOGIN_DELETED="Authorization.LOGIN_DELETED";
	public static final String PASSWORD_CHANGED="Authorization.PASSWORD_CHANGED";
    /**
     * this constant is name of attribute that shows
     * if this request is allready processed by this filter
     * if this attribute equials to true then it is allready processed
     */
    public static final String ALREADY_PROCESSED="Authorization.ALREADY_PROCESSED";
    public static final String TRUE = "true";
    /** this value is time length of storing cookie for remember me */
    public static final int COOKIE_MAX_AGE=60*60*24*365;//one year
    /** if param with given name exists in request then logout current user and send redirect on start page */
    protected static final String logout_param="logout";
    /** parameter with user login */
    protected static final String login_param="user_login";
    /** parameter with user password */
    protected static final String password_param="user_password";
    /** if this parameter exists we need to remember user i.e. add cookies with his password and login to the responce */
    protected static final String remember_param="user_remember";
    /** login of anonym user */
    protected static final String ANONYM_USER_LOGIN="anonym";

	/**
	 * checks if all properties have been set
	 */
	public void init(){
		if (securityService!=null){
			logger.debug("initialized successfully");
		}else{
			throw new NullPointerException("no security service specified");
			//logger.fatal("no security service specified");
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.debug("Authorization.preHandle()");

        // if the request has already been processed by the filter, pass it through unchecked
        if (!TRUE.equals(request.getAttribute(ALREADY_PROCESSED))) {
			HttpSession session=request.getSession();
            // set an attribute on this request to indicate that it has already been processed
            request.setAttribute(ALREADY_PROCESSED, TRUE);
			//trying to get current user
			User userSession=Utils.getCurrentUser(session);
            //checking if user has already logined
            if (userSession==null){
                //if user is not logged in then checking database
                //  if user with same login and password exists then allowing him to login
                {
                    String login=null;
                    String password=null;
                    boolean usedCookie=false;

                    //getting user login and password from request parameters
                    login=request.getParameter(login_param);
                    password=request.getParameter(password_param);
                    if (password!=null&&!password.equals("")){
                        //getting and encoding password
                        password=org.apache.catalina.realm.RealmBase.Digest(password,"MD5","UTF-8");
                    }else{
                        //getting user login and password from request cookies
                        //in cookies password is stored encoded so we don't need to hash it
                        Cookie cookies[]=request.getCookies();
                        if (cookies!=null){
                            for (int i=0;i<cookies.length;i++){
                                if (cookies[i].getName().equals(login_param)){
                                    login=cookies[i].getValue();
                                }
                                if (cookies[i].getName().equals(password_param)){
                                    password=cookies[i].getValue();
                                }
                            }
                        }
                        usedCookie=true;
                    }

                    if (login==null||password==null){
                        //if user is not attempting to login then set him to anonymus user
                        //req.setAttribute(Config.USER_ATTRIBUTE,anonymUser);
                    }else{
						//if user is attempting to login trying to find it in database
						userSession=securityService.getUser(login,password);
						if (userSession==null){
                            //if user login and password do not match then set user to anonymus
                            //request.setAttribute(Config.USER_ATTRIBUTE,anonymUser);
                            //and setting the error if user logged in not from cookie
                            if (!usedCookie) request.setAttribute(LOGIN_ERROR,LOGIN_FAILED);
                        }else{
                            //TODO make smth
                            Set roles=userSession.getRoles();
                            if (roles!=null&&!roles.isEmpty()){}
                            //setting attribute to a session
                            session.setAttribute(Config.USER_ATTRIBUTE,userSession);
                            //setting attribute to request
                            request.setAttribute(Config.USER_ATTRIBUTE,userSession);
                            //if user authorized successufully and wants to be remembered then add his cookies to response
                            if (request.getParameter(remember_param)!=null){
								setUserCookie(userSession, response);
                            }
                        }
                    }
                }
                //letting chain to process next
            }else{
                //checking parameter if user wants to logout
                if (request.getParameter(logout_param)!=null){
					logout(response, request);
					return false;
                }else{
                    //get user info from database
                    User userDB=securityService.getById(userSession.getId());
					if (userDB==null){
						userChanged(request,LOGIN_DELETED);
						//if user login and password do not match then set user to anonymus
						//request.setAttribute(Config.USER_ATTRIBUTE,anonymUser);
					}else{
						//checking password from DB and from session if they are different logouting current user
						if (userSession.equals(userDB)){
                            //TODO make smth
                            Set roles=userDB.getRoles();
                            if (roles!=null&&!roles.isEmpty()){}
							//setting attribute to a session
							session.setAttribute(Config.USER_ATTRIBUTE,userDB);
							//setting attribute to request
							request.setAttribute(Config.USER_ATTRIBUTE,userDB);
							//renewing cookies
							//setUserCookie(userDB, response);
						}else{
							userChanged(request,PASSWORD_CHANGED);
							//TODO: mb delete
							//userDB=anonymUser.clone();
							//userDB.setId(userSession.getId());
							request.setAttribute(Config.USER_ATTRIBUTE,userDB);
						}
                    }
                    //letting chain to process next
                }
            }
        }
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
		throws Exception
	{}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
		throws Exception
	{
		User u = Utils.getCurrentUser(request);
		if (u!=null){
			u.setLast_accessed(new Date());
		}
		//TODO: mb force update
	}

	/**
	 * call this method then some information was changed(password) or its account was deleted
	 * @param req current http request
	 * @param msg message to show to user
	 */
	protected void userChanged(HttpServletRequest req,String msg){
		//and setting the error
		req.setAttribute(LOGIN_ERROR,msg);
		//and removing it from the session
		req.getSession().removeAttribute(security.Config.USER_ATTRIBUTE);
	}

	/**
	 * Call this method if you want to logout user.
	 * It invalidates session and deletes user login and password from cookies.
	 * @param response
	 * @param req
	 * @throws java.io.IOException
	 */
	protected void logout(HttpServletResponse response,HttpServletRequest req)
			throws IOException
	{
		req.getSession().invalidate();
		req.removeAttribute(Config.USER_ATTRIBUTE);
		//deleting password and login from cookies
		Cookie[] cookies=req.getCookies();
		if (cookies!=null){
			for (int i=0;i<cookies.length;i++){
				if (cookies[i].getName().equals(login_param)){
					cookies[i].setMaxAge(0);
					response.addCookie(cookies[i]);
				}else if (cookies[i].getName().equals(password_param)){
					cookies[i].setMaxAge(0);
					response.addCookie(cookies[i]);
				}
			}
		}
		response.sendRedirect(req.getContextPath()+"/");
	}

	/**
	 * add cookie with user password and login to responce
	 * @param user
	 * @param response
	 */
	protected void setUserCookie(User user, HttpServletResponse response){
			Cookie loginC = new Cookie(login_param,user.getLogin());
			Cookie passwordC = new Cookie(password_param,user.getPassword());
			loginC.setMaxAge(COOKIE_MAX_AGE);
			passwordC.setMaxAge(COOKIE_MAX_AGE);
			response.addCookie(loginC);
			response.addCookie(passwordC);
	}

	public void setSecurityService(ISecurityService securityService) {this.securityService = securityService;}

}
