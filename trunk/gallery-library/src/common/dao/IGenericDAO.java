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

package common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.hibernate.ScrollableResults;

/**
 *
 * @param <T> 
 * @author demchuck.dima@gmail.com
 */
public interface IGenericDAO<T, ID extends Serializable> {

	/**
	 * @param entity -
	 *            the transient instance to save or update (to be associated
	 *            with the Hibernate Session)
	 */
	public void makePersistent(T entity);

	/**
	 * @param entity - use if you have allready loaded an instance with same identifier
	 */
	public void merge(T entity);

	/**
	 * for persistent DAO implementations if you must to make any changes not backed to database
	 * @param obj
	 */
	public void deattach(T obj);


	/**
	 * Remove an instance from the datastore.
	 *
	 * @param id - id of entity to be deleted
	 * @return quantity of entities deleted
	 */
	public int deleteById(ID id);

	/**
	 * deletes an instances from database with given ids
	 * @param id ids of instances to delete
	 * @return quantity of rows deleted
	 */
	public int deleteById(ID[] id);

	/**
	 * deletes an instance from database by given criteria
	 * @param propertyName name of field
	 * @param propertyValue value of field
	 * @return quantity of rows deleted
	 */
	public int deleteByPropertyValue(String propertyName, Object propertyValue);

	/**
	 * updated some properties of entities
	 * WARNING entities must have an id not null
	 * @param entities entities to be updated
	 * @param propertyNames property names to be updated
	 * @return quantity of updated objects
	 */
	public int updateCollectionShortById(Collection<T> entities, String[] propertyNames);

	/**
	 * updated some properties of entities
	 * WARNING entities must have an id not null
	 * @param propertyValues values of properties to be updated
	 * @param idValues id of entities to be updated
	 * @param propertyNames property names to be updated
	 * @return quantity of updated objects
	 */
	public int updateObjectArrayShortById(String[] propertyNames,ID[] idValues,Object[]... propertyValues);

	/**
	 * updates propertyNames to propertyValues in all entities with property = Values
	 * @param propertyNames names of properties to update
	 * @param propertyValues new values
	 * @param property name
	 * @param values values
	 * @return
	 */
    public int updateObjectArrayShortByProperty(String[] propertyNames, Object[] propertyValues, String property, Object[] values);

	/**
	 *
	 * @param propertyNames names of properties to update
	 * @param propertyValues values to be set
	 * @param id id of entity to update
	 * @return updated entities count (must be 1)
	 */
	public int updatePropertiesById(String[] propertyNames, Object[] propertyValues, ID id);

	/**
	 * @param id
	 * @return the persistent instance of the entity T with the given
	 *         identifier, or null if there is no such persistent instance.
	 */
	public T getById(ID id);

	/**
	 * @param propertyName
	 *            property name to be queried
	 * @param propertyValue
	 *            value by which records will be filtered
	 * @return all entities that have the same value in given field
	 */
	public List<T> getByPropertyValue(String propertyName, Object propertyValue);

	/**
	 * @param properties names of properties to be selected
	 * @param propertyName property name to be queried
	 * @param propertyValue value by which records will be filtered
	 * @return all entities that have the same value in given field
	 */
	public List<T> getByPropertyValues(String[] properties, String propertyName, Object[] propertyValue);

	/**
	 *
	 * @param properties
	 * @param propertyName
	 * @param propertyValues
	 * @return
	 */
	public List<T> getByPropertyValues(String[] properties, String propertyName, List<Object> propertyValues);

	/**
	 * @param properties properties of a bean that will be selected
	 * @param orderBy an array of fields for order clause
	 * @param orderHow type of sorting ASC, DESC
	 * @return a distinct list of instances
	 */
	public List<T> getAllShortOrdered(String[] properties, String[] orderBy, String[] orderHow);

	/**
	 * @param propertyNames property names to retrieve
	 * @param propertyAliases aliases of bean
	 * @param propertyWhere property for criteria by which records will be filtered
	 * @param propertyValue property value by which records will be filtered
	 * @param firstResult first result to retrieve from db
	 * @param resultCount result count
	 * @param orderBy an array of fields for order clause
	 * @param orderHow type of sorting ASC, DESC
	 * @return all entities that have the same value in given field
	 */
	public List<T> getByPropertyValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String propertyWhere, Object propertyValue,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow);
	/**
	 * @param propertyNames property names to retrieve
	 * @param propertyAliases aliases of bean
	 * @param propertyWhere properies for criteria by which records will be filtered
	 * @param propertyValue property values by which records will be filtered
	 * @param relation relations beetween fields (<,>,=) if null then set to =
	 * @param firstResult first result to retrieve from db
	 * @param resultCount result count
	 * @param orderBy an array of fields for order clause
	 * @param orderHow type of sorting ASC, DESC
	 * @return all entities that have the same value in given field
	 */
	public List<T> getByPropertyValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String propertyWhere, Object propertyValue, String relation,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow);

	/**
	 * @param propertyNames property names to retrieve
	 * @param propertyAliases aliases of bean
	 * @param propertiesWhere properies for criteria by which records will be filtered
	 * @param propertyValues property values by which records will be filtered
	 * @param firstResult first result to retrieve from db
	 * @param resultCount result count
	 * @param orderBy an array of fields for order clause
	 * @param orderHow type of sorting ASC, DESC
	 * @return all entities that have the same value in given field
	 */
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, Object[] propertyValues,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow);
	/**
	 * @param propertyNames property names to retrieve
	 * @param propertyAliases aliases of bean
	 * @param propertiesWhere properies for criteria by which records will be filtered
	 * @param propertyValues property values by which records will be filtered
	 * @param relations relations beetween fields (<,>,=) if null then set to =
	 * @param firstResult first result to retrieve from db
	 * @param resultCount result count
	 * @param orderBy an array of fields for order clause
	 * @param orderHow type of sorting ASC, DESC
	 * @return all entities that have the same value in given field
	 */
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, Object[] propertyValues, String[] relations,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow);
	/**
	 * @param propertyNames property names to retrieve
	 * @param propertyAliases aliases of bean
	 * @param propertiesWhere properies for criteria by which records will be filtered
	 * @param propertyValues property values by which records will be filtered
	 * @param relations relations beetween fields (<,>,=, LIKE) if null then set to =
	 * @param firstResult first result to retrieve from db
	 * @param resultCount result count
	 * @param orderBy an array of fields for order clause
	 * @param orderHow type of sorting ASC, DESC
	 * @return all entities that have the same value in given field
	 */
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, String[][] relations, Object[][] propertyValues,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow);

	/**
	 * @param propertyNames property names to retrieve
	 * @param propertyAliases aliases of bean
	 * @param propertiesWhere properies for criteria by which records will be filtered
	 * @param propertyValues property values by which records will be filtered a sub array defines values delimited by or condition
	 * @param firstResult first result to retrieve from db
	 * @param resultCount result count
	 * @param orderBy an array of fields for order clause
	 * @param orderHow type of sorting ASC, DESC
	 * @return all entities that have the same value in given field
	 */
	public List<T> getByPropertiesValuesPortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, Object[][] propertyValues,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow);

	/**
	 * eq criterion
	 * @param propertyName name of property
	 * @param propertyValue value of property
	 * @return row count of T by propery value
	 */
	public Long getRowCount(String propertyName, Object propertyValue);

	/**
	 * eq criterion
	 * @param propertyName names of property
	 * @param propertyValue values of properties
	 * @return row count of T by propery value
	 */
	public Long getRowCount(String[] propertyName, Object propertyValue[]);

	public Long getRowCount(String propertyName, List<Object> propertyValue);

	/**
	 * eq criterion
	 * @param propertyName names of property
	 * @param propertyValue values of properties
	 * @return row count of T by propery value
	 */
	public Long getRowCount(String[] propertyName, Object propertyValue[][]);

	/**
	 * select a property (aggregative functions must be used to select a single result)
	 * ex: max(sort)...
	 * @param property name of property (may contain aggregative functions)
	 * @return single result
	 */
	public Object getSinglePropertyU(String property);

	/**
	 * select a property (aggregative functions must be used to select a single result)
	 * get a single property that matches criteria;
	 * ex: max(sort)...
	 * @param property name of property (may contain aggregative functions)
	 * @param propName where property name
	 * @param propValue where property value
	 * @return single result
	 */
	public Object getSinglePropertyU(String property, String propName, Object propValue);

	/**
	 * select a property (aggregative functions must be used to select a single result)
	 * get a single property that matches criteria;
	 * ex: max(sort)...
	 * @param property name of property (may contain aggregative functions)
	 * @param propName where property name
	 * @param propValue where property value
	 * @param number number of selected item(if more then one items returned)
	 * @return single result
	 */
	public Object getSinglePropertyU(String property, String propName, Object propValue, int number);

	/**
	 * select a property (aggregative functions must be used to select a single result)
	 * get a single property that matches criteria;
	 * ex: max(sort)...
	 * @param property name of property (may contain aggregative functions)
	 * @param propName where property name
	 * @param propValue where property value
	 * @param number number of selected item(if more then one items returned)
	 * @return single result
	 */
	public Object getSinglePropertyU(String property, String propName, Object propValue, int number, String[] orderBy, String[] OrderHow);

	/**
	 * select a property (aggregative functions must be used to select a single result)
	 * get a single property that matches criteria;
	 * ex: max(sort)...
	 * @param property name of property (may contain aggregative functions)
	 * @param propName where properties names
	 * @param propValue where properties values
	 * @return single result
	 */
	public Object getSinglePropertyU(String property, String[] propName, Object[] propValue, int number, String[] orderBy, String[] orderHow);

	/**
	 * select a property (aggregative functions must be used to select a single result)
	 * get a single property that matches criteria;
	 * ex: max(sort)...
	 * @param property name of property (may contain aggregative functions)
	 * @param propName where properties names
	 * @param relations rerlation between name and value
	 * @param propValue where properties values
	 * @return single result
	 */
	public Object getSinglePropertyU(String property, String[] propName, String[][] relations, Object[][] propValue, int number, String[] orderBy, String[] orderHow);

	/**
	 * get list of single properties
	 * @param property property name to get
	 * @param whereName where critria prop name
	 * @param whereValue where critria prop value
	 * @param first first result
	 * @param max max results
	 * @return list of values
	 */
	public List getSingleProperty(String property, String[] whereName, Object[] whereValue, int first, int max, String[] orderBy, String[] orderHow);

    public List getSinglePropertyOrderRand(String property, String whereName, Object whereValue, int first, int max);

    /**
     * commits current transaction and starts new
     * @return
     */
    public void restartTransaction();

	/**
	 * get a number of row in a query with given order by clause
	 * !!! first select the row from database(by id for example)
	 *
	 * TODO: remake :)
	 * @param values value of property for current entity
	 * @param orderBy an array of fields for order clause
	 * @param orderHow type of sorting ASC, DESC
	 * @param propertyName name of property for where condition
	 * @param propertyValue value of property for where condition
	 * @return single result
	 */
	public Object getRowNumber(Object[] values, String[] orderBy, String[] orderHow, String[] propertyName, Object[] propertyValue);

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

	/**
	 * enables a filter defined in xml
	 * @param name
	 * @param param_names
	 * @param param_values
	 */
	public void enableFilter(String name, String[] param_names, Object[] param_values);

	/**
	 * disables a filter defined in xml
	 * @param name
	 */
	public void disableFilter(String name);
}
