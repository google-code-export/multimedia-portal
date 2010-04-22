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
import org.hibernate.ScrollableResults;

public class GenericServiceImpl<T, ID extends Serializable> implements IGenericService<T, ID> {
	protected Logger logger = Logger.getLogger(this.getClass());

   protected IGenericDAO<T, ID> dao;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(dao, "dao", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

    public IGenericDAO<T, ID> getDao() {return dao;}
	public void setDao(IGenericDAO<T, ID> dao) {this.dao = dao;}

	@Override
	public void save(T entity) {dao.makePersistent(entity);}

	@Override
	public void merge(T entity) {dao.merge(entity);}

	@Override
    public int deleteById(ID id) {return dao.deleteById(id);}

	@Override
    public T getById(ID id) {return dao.getById(id);}

	@Override
	public int updateObjectArrayShortById(String[] propertyNames, ID[] idValues, Object[]... propertyValues) {
		return dao.updateObjectArrayShortById(propertyNames, idValues, propertyValues);
	}

	@Override
	public int updateObjectArrayShortByProperty(String[] propertyNames, Object[] propertyValues, String property, Object[] values) {
		return dao.updateObjectArrayShortByProperty(propertyNames, propertyValues, property, values);
	}

	@Override
	public Long getRowCount(String propertyName, Object propertyValue) {
		return dao.getRowCount(propertyName, propertyValue);
	}

	@Override
	public Long getRowCount(String propertyName, List<Object> propertyValue) {
		return dao.getRowCount(propertyName, propertyValue);
	}

	@Override
	public Long getRowCount(String[] propertyName, Object[] propertyValue) {
		return dao.getRowCount(propertyName, propertyValue);
	}

	@Override
	public Object getSinglePropertyU(String prop, String propName, Object propValue) {
		return dao.getSinglePropertyU(prop,propName, propValue);
	}

	@Override
	public Object getSinglePropertyU(String prop, String propName, Object propValue, int number) {
		return dao.getSinglePropertyU(prop,propName, propValue, number);
	}

    @Override
	public Object getSinglePropertyU(String property, String propName, Object propValue, int number, String[] orderBy, String[] OrderHow){
        return dao.getSinglePropertyU(property, propName, propValue, number, orderBy, OrderHow);
    }

	@Override
	public Object getSinglePropertyU(String prop, String[] propNames, Object[] propValues, int number) {
		return dao.getSinglePropertyU(prop, propNames, propValues, number, null, null);
	}

	@Override
	public Object getSinglePropertyU(String prop) {
		return dao.getSinglePropertyU(prop);
	}

    @Override
    public List<Object> getSingleProperty(String property, String[] propName, Object[] propValue, int number, int count, String[] orderBy, String[] OrderHow){
        return dao.getSingleProperty(property, propName, propValue, number, count, orderBy, OrderHow);
    }

	@Override
	public List<T> getAllShortOrdered(String[] propertyNames, String[] orderBy, String[] orderHow) {
		return dao.getAllShortOrdered(propertyNames, orderBy, orderHow);
	}

	@Override
	public List<T> getShortByPropertyValueOrdered(String[] propNames, String propertyName, Object propertyValue, String[] orderBy, String[] orderHow) {
		return dao.getByPropertyValuePortionOrdered(propNames, propNames, propertyName, propertyValue, 0, -1, orderBy, orderHow);
	}

	@Override
	public List<T> getShortByPropertyValueOrdered(String[] properties, String[] pseudonymes, String propertyName, Object propertyValue, String[] orderBy, String[] orderHow) {
		return dao.getByPropertyValuePortionOrdered(properties, pseudonymes, propertyName, propertyValue, 0, -1, orderBy, orderHow);
	}

	@Override
	public List<T> getShortByPropertiesValueOrdered(String[] propNames, String[] propertyName, Object[] propertyValue, String[] orderBy, String[] orderHow) {
		return dao.getByPropertiesValuePortionOrdered(propNames, propNames, propertyName, propertyValue, 0, -1, orderBy, orderHow);
	}

	@Override
	public List<T> getShortByPropertiesValuesOrdered(String[] propNames, String[] propertyName, Object[][] propertyValue, String[] orderBy, String[] orderHow) {
		return dao.getByPropertiesValuesPortionOrdered(propNames, propNames, propertyName, propertyValue, 0, -1, orderBy, orderHow);
	}

	@Override
	public int deleteByPropertyValue(String propertyName, Object propertyValue) {
		return dao.deleteByPropertyValue(propertyName, propertyValue);
	}

	@Override
	public int deleteById(ID[] id) {return dao.deleteById(id);}

    @Override
    public void restartTransaction() {dao.restartTransaction();}

    @Override
    public List<T> getShortByPropertyValueRelationOrdered(String[] properties, String[] pseudonymes,
            String where_prop, Object where_val, String where_rel, String[] orderBy, String[] orderHow)
    {
        return dao.getByPropertyValuePortionOrdered(properties, pseudonymes, where_prop, where_val, where_rel, 0, 0, orderBy, orderHow);
    }

    @Override
    public void deattach(T entity) {dao.deattach(entity);}

	@Override
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases, String[] propertiesWhere, String[][] relations, Object[][] propertyValues, int firstResult, int resultCount, String[] orderBy, String[] orderHow) {
		return dao.getByPropertiesValuePortionOrdered(propertyNames, propertyAliases, propertiesWhere, relations, propertyValues, firstResult, resultCount, orderBy, orderHow);
	}

	@Override
	public Object getSinglePropertyU(String property, String[] propName, String[][] relations, Object[][] propValue, int number, String[] orderBy, String[] orderHow) {
		return dao.getSinglePropertyU(property, propName, relations, propValue, number, orderBy, orderHow);
	}

	@Override
	public ScrollableResults getScrollableResults(String property, String whereName, Object whereValue, String[] orderBy, String[] orderHow){
		return dao.getScrollableResults(property, whereName, whereValue, orderBy, orderHow);
	}

	@Override
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases, String[] propertiesWhere, String[] relations, Object[] propertyValues, int firstResult, int resultCount, String[] orderBy, String[] orderHow) {
		return dao.getByPropertiesValuePortionOrdered(propertyNames, propertyAliases, propertiesWhere, propertyValues, relations, firstResult, resultCount, orderBy, orderHow);
	}

}
