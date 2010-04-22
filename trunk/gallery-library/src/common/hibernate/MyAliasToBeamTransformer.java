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

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MyAliasToBeamTransformer implements ResultTransformer {

	private final Class resultClass;

	public MyAliasToBeamTransformer(Class resultClass) {
		if ( resultClass == null ) {
			throw new IllegalArgumentException( "resultClass cannot be null" );
		}
		this.resultClass = resultClass;
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		BeanWrapperImpl wrapper = new BeanWrapperImpl(resultClass);

		try {
			for (int i=0; i<tuple.length; i++){
				wrapper.setPropertyValue(aliases[i], tuple[i]);
			}
		} catch ( BeansException e ) {
			throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName(),e);
		}

		return wrapper.getRootInstance();
	}

	@Override
	public List transformList(List collection) {return collection;}
}
