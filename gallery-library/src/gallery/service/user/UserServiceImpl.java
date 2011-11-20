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

package gallery.service.user;


import common.services.generic.GenericServiceImpl;
import java.util.List;
import security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements IUserService<User, Long>{
	public static final String[] ORDER_BY = null;
	public static final String[] ORDER_HOW = null;
	public static final String[] ALL_SHORT_CMS =  new String[]{"id","id_pages","login","name","last_accessed"};

	@Override
	public List<User> getShortOrderedByPropertyValueCms(String propertyName, Object propertyValue) {
		if (propertyValue==null){
			return dao.getByPropertyValuePortionOrdered(ALL_SHORT_CMS, ALL_SHORT_CMS, null, null, -1, -1, ORDER_BY, ORDER_HOW);
		}else{
			return dao.getByPropertyValuePortionOrdered(ALL_SHORT_CMS, ALL_SHORT_CMS, propertyName, propertyValue, -1, -1, ORDER_BY, ORDER_HOW);
		}
	}

	public static final String[] SECURITY_WHERE =  new String[]{"login","password"};
	@Override
	public User getUser(String login, String password) {
		List<User> r =
				dao.getByPropertiesValuePortionOrdered(null, null, SECURITY_WHERE, new Object[]{login, password}, 0, 1, null, null);
		if (r!=null&&!r.isEmpty()){
			return r.get(0);
		}else{
			return null;
		}
	}

	protected static final String[] SECURITY_UPDATE = new String[]{"last_accessed"};
	@Override
	public void userEntered(User user) {
		if (user!=null){
			dao.updatePropertiesById(SECURITY_UPDATE, new Object[]{new java.util.Date()}, user.getId());
		}
	}

	@Override
	public User getUser(Long id) {return getById(id);}
}
