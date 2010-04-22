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

package common.hibernate;


import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *This HandlerInterceptor implements the single session per request pattern when added as an interceptor to a handler mapping.
 * It expects to be used in a JTA environment and used with a JTASessionContext CurrentSessionContext.
 * To use it you must declare it has an interceptor for a handler mapping.
<bean id="sessionPerRequestInterceptor" class="packageName.HibernateSessionPerRequestHandlerInterceptor">
   <property name="sessionFactory" ref="mySessionFactory"/>
</bean> <bean id="urlMapping" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
   <property name="interceptors">
      <list><ref bean="sessionPerRequestInterceptor"/></list>
   </property>
   <property name="mappings">
      <props>
         <prop key="blah/blah">maintainCalendarFormController</prop>
      </props>
   </property> </bean>
* @author demchuck.dima@gmail.com
* @see HandlerInterceptor
*/
public class HibernateSessionPerRequestHandlerInterceptor
		implements HandlerInterceptor
{
	protected final Logger log = Logger.getLogger(getClass());
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {this.sessionFactory=sessionFactory;}

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(sessionFactory, "sessionFactory", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception
	{}

	/**
	 *This guy is supposed to intercept a request that needs to be wrapped in a transaction. It will create a new Hibernate
	 *session and begin a transaction on that session. It will then bind that session to the current thread using springs
	 *TransactionSynchronizationManager class. This will cause the calls to the getCurrentSession() method of the SessionFactory proxy provided by spring as well has the calls to springs SessionFactoryUtils.getSession() method to return the session attached to the current transaction/thread.
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 * @see HandlerInterceptor.preHandle(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse, java.lang.Object)
	 * @see TransactionSynchronizationManager
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception
	{
		log.debug("Opening session and beginning transaction.");
		//com.netstorm.localization.LocalizationInterceptor interceptor = new com.netstorm.localization.LocalizationInterceptor("ru");
		//Session sess = sessionFactory.openSession(interceptor);
		//interceptor.init(sess);

		Session sess = sessionFactory.openSession();
		sess.beginTransaction();

	/*
	This is how spring ties the hibernate session to the current transaction/thread it acctually binds a SessionHolder object containing the session. This bindResource method expects the spring proxy sessionFactory and not the Hibernate session factory in order to work correctly.
	*/
		//Bind the Session to the current thread/transaction
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(sess));
		//Activate transaction synchronization for the current thread.
		//TransactionSynchronizationManager.initSynchronization();
		return true;
	}

	/**
	This guy will check to see if an exception was thrown while processing and if so will roll back the current transaction. It will obtain the current session attached to the Thread and close it.
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @throws Exception
	 * @see HandlerInterceptor.afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	* @see TransactionSynchronizationManager
	*/
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception
	{

	/*
	  Since the sessionFactory here is acctually the Spring proxy around the
	  Hibernate Sesison Factory this getCurrentSession() method will use the TransactionSynchronizationManager
	  to retreive the Session bound to the current thread/transaction. Its basically the same has calling
	  TransactionSynchronizationManager.getResource(sessionFactory) but its easier to read this way.
	 */
		Session sess = sessionFactory.getCurrentSession();
		try{
			if(ex==null){
				log.debug("Committing the database transaction");
				sess.getTransaction().commit();
			}else{
				//An exception was thrown during the processing of the request.
				log.debug("Rolling back the database transaction");
				sess.getTransaction().rollback();
			}
			if(sess.isOpen()) sess.close();
		}catch(Exception e){
			log.error("after the request", e);
		}finally{
			try{
				if(sess.isOpen()){sess.close();}
			}catch(Exception e){/*do nothing*/}
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			//TransactionSynchronizationManager.clearSynchronization();
			log.debug("resource is unbunded");
		}
	}
}
