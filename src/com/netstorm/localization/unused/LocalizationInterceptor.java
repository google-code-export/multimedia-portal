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

package com.netstorm.localization.unused;

import java.io.Serializable;
import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class LocalizationInterceptor extends EmptyInterceptor{
	protected Logger logger = Logger.getLogger(getClass());

	private String locale;

	private Session sess;

	public LocalizationInterceptor(String locale){
		this.locale = locale;
	}

	public void init (Session sess){this.sess = sess;}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity instanceof Localizable){
			Localizable loc = ((Localizable)entity);
			loc.setLang(locale);
			Query query = sess.createQuery("from "+loc.getLocalizationEntityName()+" where lang = :lang and id_rel = :id_rel");
			query.setParameter("lang", locale);
			query.setParameter("id_rel", id);

			loc.setLocalizedContent(query.uniqueResult());
		}
		return false;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity instanceof Localizable){
			Localizable loc = ((Localizable)entity);
			sess.save(loc.getLocalizationEntityName(), loc.getLocalizedContent());
		}
		return false;
	}

	
}