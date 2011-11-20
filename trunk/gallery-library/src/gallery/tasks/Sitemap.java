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

package gallery.tasks;

import gallery.service.sitemap.ISitemapService;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Sitemap extends TimerTask{
	protected final Logger log = Logger.getLogger(getClass());

    protected ISitemapService sitemap_service;
	protected SessionFactory sessionFactory;
    public void setSitemapService(ISitemapService value){this.sitemap_service = value;}
    public void setSessionFactory(SessionFactory value){this.sessionFactory = value;}

	public Sitemap(){
		//System.out.println("created");
	}

	@Override
	public void run() {
		/*Session sess = sessionFactory.openSession();
		sess.beginTransaction();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(sess));*/

		//Exception ex = null;
		try {
			sitemap_service.createSitemap();
		} catch (Exception e){
			log.error("after the request", e);
		//	ex = e;
		}

		/*try {
			sess = sessionFactory.getCurrentSession();
			if (ex==null){
				sess.getTransaction().commit();
			}else{
				sess.getTransaction().rollback();
			}
			sess.close();
		} catch (Exception e){
			log.error("after the request", e);
		}finally{
			try{
				if(sess.isOpen()){sess.close();}
			}catch(Exception e){}
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			//TransactionSynchronizationManager.clearSynchronization();
			//log.debug("resource is unbunded");
		}*/
	}
}
