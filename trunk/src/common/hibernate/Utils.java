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

import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.SessionHolder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Utils {
	public static void bindSession(SessionFactory sessionFactory){
		Session sess = sessionFactory.openSession();
		sess.beginTransaction();

	/*
	This is how spring ties the hibernate session to the current transaction/thread it acctually binds a SessionHolder object containing the session. This bindResource method expects the spring proxy sessionFactory and not the Hibernate session factory in order to work correctly.
	*/
		//Bind the Session to the current thread/transaction
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(sess));
		//Activate transaction synchronization for the current thread.
		//TransactionSynchronizationManager.initSynchronization();
	}

	public static void unbindSession(SessionFactory sessionFactory){
			TransactionSynchronizationManager.unbindResource(sessionFactory);
	}

	private Utils() {
	}

}
