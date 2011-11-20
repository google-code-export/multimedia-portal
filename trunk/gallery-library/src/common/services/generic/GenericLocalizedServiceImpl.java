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

import com.netstorm.localization.LocalizedBean;
import common.dao.IGenericDAO;
import java.io.Serializable;

//TODO: mb some more methods from GenericServiceImpl might be overriden
/**
 * @param <C_ID> id type of common localization object
 * @param <C_T> type of common localization object
 * @author demchuck.dima@gmail.com
 */
public class GenericLocalizedServiceImpl<T extends LocalizedBean<C_T>, ID extends Serializable, C_T, C_ID extends Serializable> extends GenericServiceImpl<T, ID> implements IGenericLocalizedService<T, ID>{
	protected IGenericDAO<C_T, C_ID> commonLocaleDAO;

	@Override
	public int deleteById(ID id) {
		//1-st get parent id
		C_ID parent_id = (C_ID)dao.getSinglePropertyU("localeParent.id","id",id);
		if (dao.getRowCount("localeParent.id", parent_id)>1){//2-nd get all children
			dao.deleteById(id);
			//3-rd if > 1 delete only current
			return 1;
		} else {
			//TODO: think about what todo if on-delete="cascade" is not set
			commonLocaleDAO.deleteById(parent_id);
			return 2;
		}
		//return deleteLocalized(dao.getById(id));
		//return super.deleteById(id);
	}

	public void setCommonLocaleDAO(IGenericDAO<C_T, C_ID> commonLocaleDAO){this.commonLocaleDAO = commonLocaleDAO;}
}
