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

import java.io.Serializable;
import java.util.List;
import org.hibernate.ScrollableResults;


/**
 * @author demchuck.dima@gmail.com
 * @param <T>
 * 
 *  Should be modified for project-specific common functions
 */
public interface IGenericService<T, ID extends Serializable>{

    public void save(T entity);

    public void merge(T entity);

    public void deattach(T entity);

    public int deleteById(ID id);

	/**
	 * deletes chosen items
	 * @param id identifier of items
	 * @return affected items
	 */
    public int deleteById(ID[] id);

    public int deleteByPropertyValue(String propertyName, Object propertyValue);

    public int updateObjectArrayShortById(String[] propertyNames, ID[] idValues, Object[]... propertyValues);

	/**
	 * updates propertyNames to propertyValues in all entities with property = Values
	 * @param propertyNames names of properties to update
	 * @param propertyValues new values
	 * @param property name
	 * @param values values
	 * @return
	 */
    public int updateObjectArrayShortByProperty(String[] propertyNames, Object[] propertyValues, String property, Object[] values);

    public T getById(ID id);

	public List<T> getAllShortOrdered(String[] propertyNames, String[] orderBy, String[] orderHow);

    public List<T> getShortByPropertyValueOrdered(String[] propNames, String propertyName, Object propertyValue, String[] orderBy, String[] orderHow);
	public List<T> getShortByPropertyValueOrdered(String[] properties, String[] pseudonymes, String propertyName, Object propertyValue, String[] orderBy, String[] orderHow);

    public List<T> getShortByPropertiesValueOrdered(String[] propNames, String[] propertyName, Object[] propertyValue, String[] orderBy, String[] orderHow);

    public List<T> getShortByPropertiesValuesOrdered(String[] propNames, String[] propertyName, Object[][] propertyValue, String[] orderBy, String[] orderHow);

    public List<T> getShortByPropertyValueRelationOrdered(String[] properties, String[] pseudonymes,
            String where_prop, Object where_val, String where_rel, String[] orderBy, String[] orderHow);

	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, String[] relations, Object[] propertyValues,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow);

	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, String[][] relations, Object[][] propertyValues,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow);

	/**
	 * get a single property that matches criteria;
	 * ex: max(sort)...
	 * @param prop single property to retrieve
	 * @param propNames where property name
	 * @param propValues where property value
	 * @return property retrieved or null
	 */
	//public Object getSinglePropertyU(String prop, String propNames[], Object propValues[]);

    public Long getRowCount(String propertyName, Object propertyValue);
    public Long getRowCount(String[] propertyName, Object[] propertyValue);
	public Long getRowCount(String propertyName, List<Object> propertyValue);

	public Object getSinglePropertyU(String prop);
	public Object getSinglePropertyU(String prop, String propName, Object propValue);
	public Object getSinglePropertyU(String prop, String propName, Object propValue, int number);
    public Object getSinglePropertyU(String prop, String[] propName, Object[] propValue, int number);
	public Object getSinglePropertyU(String property, String propName, Object propValue, int number, String[] orderBy, String[] OrderHow);
	public Object getSinglePropertyU(String property, String[] propName, String[][] relations, Object[][] propValue, int number, String[] orderBy, String[] orderHow);

    public List<Object> getSingleProperty(String property, String[] propName, Object[] propValue, int number, int count, String[] orderBy, String[] OrderHow);

    /**
     * applyes changes made to database.
     * (if you use hiberanate for example they will be applied at the end of handling request automatically)
     * It is usefull if you have a lot of standalone changes made to database
     */
    public void restartTransaction();

	/**
	 * you mast close ScrollableResults after finished using
	 * @param property property you wat to select
	 * @param whereName a where property
	 * @param whereValue value for where
	 * @param orderBy
	 * @param orderHow
	 * @return
	 */
	public ScrollableResults getScrollableResults(String property, String whereName, Object whereValue, String[] orderBy, String[] orderHow);

}
