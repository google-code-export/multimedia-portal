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

package common.services.generic;

import common.dao.IGenericDAO;
import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @param <T> 
 * @author demchuck.dima@gmail.com
 */
public class SingletonInstanceServiceImpl<T, ID extends Serializable> implements ISingletonInstanceService<T>{
	protected Logger logger = Logger.getLogger(this.getClass());
    protected IGenericDAO<T, ID> dao;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(dao, "dao", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	public void setDao(IGenericDAO<T, ID> dao) {this.dao = dao;}

	@Override
	public T getInstance() {
		List<T> rez = dao.getPortion(0, 1);

		if (rez.size()>0)
			return rez.get(0);
		else
			return null;
	}

	@Override
	public void saveInstance(T entity) {dao.makePersistent(entity);}

}