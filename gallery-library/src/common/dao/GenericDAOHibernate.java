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

import common.hibernate.HQLPartGenerator;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;

/**
 *
 * @param <T>
 * @author demchuck.dima@gmail.com
 */
public class GenericDAOHibernate<T, ID extends Serializable> implements IGenericDAO<T, ID> {

	protected final Logger logger = Logger.getLogger(getClass());

	protected SessionFactory sessionFactory;

	protected Class<T> persistentClass;

	protected String entityName;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(sessionFactory, "sessionFactory", sb);
		common.utils.MiscUtils.checkNotNull(persistentClass, "persistentClass", sb);
		common.utils.MiscUtils.checkNotNull(entityName, "entityName", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	public GenericDAOHibernate(java.lang.String entityName) {
		this.entityName = entityName;
	}


	public GenericDAOHibernate(java.lang.String entityName, java.lang.String persistentClass) {
		this.entityName = entityName;
		try {
			this.persistentClass = (Class<T>) Class.forName(persistentClass);
		} catch (ClassNotFoundException ex) {
			logger.error("can't find a persistentClass "+persistentClass, ex);
		}
	}

	protected Class<T> getPersistentClass() {return persistentClass;}

	public String getEntityName() {return entityName;}

	public SessionFactory getSessionFactory() {return sessionFactory;}

	public void setSessionFactory(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}

	/**
	 * creates new hibernate criteria, ready for usage in other methods
	 *
	 * @return newly created criteria
	 */
	protected Criteria createCriteria() {
		return getSessionFactory().getCurrentSession().createCriteria(entityName);
	}

	@Override
	public int deleteById(ID id){
		StringBuilder hql = new StringBuilder("DELETE ");
		hql.append(entityName);
		hql.append(" WHERE id = :id");
		//logger.debug("Entity " + entityName + " has been just deleted");
		try{
			return getSessionFactory().getCurrentSession().createQuery(hql.toString()).setParameter("id", id).executeUpdate();
		} catch (org.hibernate.exception.ConstraintViolationException e){
			return -1;
		}
	}

	@Override
	public List<T> getByPropertyValue(String propertyName, Object propertyValue) {
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		baseHQL.append(" FROM ");
		baseHQL.append(this.entityName);
		HQLPartGenerator.getWhereColumnValue(propertyName, propertyValue, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());
		if (propertyName!=null&&propertyValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propertyValue);
		return q.list();
	}

	@Override
	public List<T> getByPropertyValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String propertyWhere, Object propertyValue,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		common.hibernate.HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" FROM ");
		baseHQL.append(this.entityName);
		HQLPartGenerator.getWhereColumnValue(propertyWhere, propertyValue, baseHQL);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyWhere!=null&&propertyValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propertyValue);
		if (propertyNames!=null&&propertyAliases!=null&&propertyNames.length>0&&propertyNames.length==propertyAliases.length){
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		}
		return q.list();
	}

	@Override
	public List<T> getByPropertyValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String propertyWhere, Object propertyValue, String relation,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		common.hibernate.HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" FROM ");
		baseHQL.append(this.entityName);
		HQLPartGenerator.getWhereColumnValueRelation(propertyWhere, propertyValue, relation, baseHQL);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyWhere!=null&&propertyValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propertyValue);
		if (propertyNames!=null&&propertyAliases!=null&&propertyNames.length>0&&propertyNames.length==propertyAliases.length){
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		}
		return q.list();
	}

	@Override
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, Object[] propertyValues,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		common.hibernate.HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" FROM ");
		baseHQL.append(this.entityName);
		common.hibernate.HQLPartGenerator.getWhereManyColumns(propertiesWhere, propertyValues, baseHQL);
		common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (propertiesWhere!=null&&propertyValues!=null)
			for (int i=0;i<propertiesWhere.length;i++){
				if (propertyValues[i]!=null)
					q = q.setParameter("prop"+String.valueOf(i), propertyValues[i]);
			}
		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyNames!=null&&propertyAliases!=null&&propertyAliases.length==propertyNames.length)
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Override
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, Object[] propertyValues, String[] relations,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		common.hibernate.HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" FROM ");
		baseHQL.append(this.entityName);
		common.hibernate.HQLPartGenerator.getWhereManyColumnsRelations(propertiesWhere, propertyValues, relations, baseHQL);
		common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (propertiesWhere!=null&&propertyValues!=null)
			for (int i=0;i<propertiesWhere.length;i++){
				if (propertyValues[i]!=null)
					//logger.info("prop"+String.valueOf(i)+"="+propertyValues[i]);
					q = q.setParameter("prop"+String.valueOf(i), propertyValues[i]);
			}
		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyNames!=null&&propertyAliases!=null&&propertyAliases.length==propertyNames.length)
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Override
	public List<T> getAllShortOrdered(String[] properties, String[] orderBy, String[] orderHow) {
		List<T> l = null;
		//selecting using queries with select new ...
		/*{
			long l1 = System.currentTimeMillis();
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT new gallery.model.beans.Pages(");
			sb.append(properties[0]);
			for (int i=1;i<properties.length;i++){
				sb.append(", ");
				sb.append(properties[i]);
			}
			sb.append(") FROM PagesFull");

			l = getSession().createQuery(sb.toString()).list();
			long l2 = System.currentTimeMillis() - l1;
			System.out.println("3-rd = "+l2);
		}*/
		//selecting using queries with alieses and result transform
		{
			//long l1 = System.currentTimeMillis();
			StringBuilder sb = new StringBuilder();
			HQLPartGenerator.getValuesListWithAliases(properties,properties,sb);
			sb.append(" FROM ");
			sb.append(entityName);
			HQLPartGenerator.getOrderBy(orderBy, orderHow, sb);
			//sb.append("SELECT photo.id as id, photo.name as name, pages.name as pages from Photo as photo");
			//sb.append(" inner join photo.pages as pages");

			Session s = getSessionFactory().getCurrentSession();
			Query q = s.createQuery(sb.toString());
			if (properties!=null)
				q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
				//q = q.setResultTransformer(new MyAliasToBeamTransformer(persistentClass));

			l = q.list();
			//long l2 = System.currentTimeMillis() - l1;
			//System.out.println("1-st = "+l2);
		}
		// selecting using criteria
		/*{
			long l1 = System.currentTimeMillis();
			Criteria crit = getSessionFactory().getCurrentSession().createCriteria(entityName);
			if (properties!=null){
				ProjectionList list = Projections.projectionList();
				for (String p:properties){
					list = list.add(Projections.property(p), p);
				}
				crit = crit.setProjection(list).setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
				//crit = crit.setProjection(list).setResultTransformer(new MyAliasToBeamTransformer(persistentClass));
			}
			l = crit.list();
			long l2 = System.currentTimeMillis() - l1;
			System.out.println("2-nd = "+l2);
		}*/
		
		return l;
	}

	@Override
	public int updateCollectionShortById(Collection<T> entities, String[] propertyNames) {
		throw new UnsupportedOperationException("Not supported yet.");
		/*if (entities==null||propertyNames==null||entities.size()==0||propertyNames.length==0)
			return 0;
		Method[] m = new Method[propertyNames.length];
		{
			//searching for getter methods
			for (int i=0;i<propertyNames.length;i++){
				try {
					m[i] = persistentClass.getMethod(common.utils.StringUtils.getterNameForProperty(propertyNames[i]));
				} catch (NoSuchMethodException ex) {
					log.log(Level.SEVERE, null, ex);
					return -1;
				} catch (SecurityException ex) {
					log.log(Level.SEVERE, null, ex);
					return -1;
				}
			}
		}
		//actually creating hql
		StringBuilder hql = new StringBuilder("UPDATE ");
		hql.append(entityName);
		hql.append(" SET ");
		HQLPartGenerator.getValuesListForUpdate(propertyNames, hql);
		//appending values
		Query q = this.getSessionFactory().getCurrentSession().createQuery(entityName);
		Iterator<T> i = entities.iterator();
		while (i.hasNext()){
			try {
				for (int j=0;j<propertyNames.length;j++){
					q = q.setParameter(propertyNames[j], m[j].invoke(i.next()));
				}
			} catch (IllegalAccessException ex) {
				Logger.getLogger(GenericDAOHibernate.class.getName()).log(Level.SEVERE, null, ex);
				return -1;
			} catch (IllegalArgumentException ex) {
				Logger.getLogger(GenericDAOHibernate.class.getName()).log(Level.SEVERE, null, ex);
				return -1;
			} catch (InvocationTargetException ex) {
				Logger.getLogger(GenericDAOHibernate.class.getName()).log(Level.SEVERE, null, ex);
				return -1;
			}
		}
		return q.executeUpdate();-*/
	}

	@Override
	public int updateObjectArrayShortById(String[] propertyNames, ID[] idValues, Object[]... propertyValues) {
		if (propertyNames==null||idValues==null||propertyValues==null||
				propertyNames.length==0||idValues.length==0||propertyValues.length!=propertyNames.length){
			return -1;
		}
		
		int rez = 0;
		//actually creating hql
		StringBuilder hql = new StringBuilder("UPDATE ");
		hql.append(entityName);
		HQLPartGenerator.getValuesListForUpdate(propertyNames, hql);
		hql.append(" WHERE id = :id");
		Query q = null;
		Session sess = this.getSessionFactory().getCurrentSession();
		for (int i=0;i<idValues.length;i++){
			if (idValues[i]!=null){
				q = sess.createQuery(hql.toString());
				//appending values
				q = q.setParameter("id", idValues[i]);
				//log.fine("id="+idValues[i]);
				for (int j=0;j<propertyNames.length;j++){
					q = q.setParameter(propertyNames[j], propertyValues[j][i]);
					//log.fine(propertyNames[j]+"="+propertyValues[j][i]);
				}
				rez+= q.executeUpdate();
			}
		}
		return rez;
	}

	@Override
	public int updateObjectArrayShortByProperty(String[] propertyNames, Object[] propertyValues, String property, Object[] values) {
		if (propertyNames==null||values==null||propertyValues==null||
				propertyNames.length==0||values.length==0||propertyValues.length!=propertyNames.length){
			return -1;
		}

		int rez = 0;
		//actually creating hql
		StringBuilder hql = new StringBuilder("UPDATE ");
		hql.append(entityName);
		HQLPartGenerator.getValuesListForUpdate(propertyNames, hql);
		hql.append(" WHERE ");
		hql.append(property);
		hql.append(" in (:property)");

		Session sess = this.getSessionFactory().getCurrentSession();

		Query q = sess.createQuery(hql.toString()).setParameterList("property", values);
		for (int i=0;i<propertyNames.length;i++){
			q = q.setParameter(propertyNames[i], propertyValues[i]);
		}

		rez+= q.executeUpdate();

		return rez;
	}

	@Override
	public int updatePropertiesById(String[] propertyNames, Object[] propertyValues, ID id) {
		if (propertyNames==null||id==null||propertyValues==null||
				propertyNames.length==0||propertyValues.length!=propertyNames.length){
			return -1;
			//throw new NullPointerException("updateObjectArrayShortById: One of arguments is null of has 0 length or propertyNames length not eq to propertyValues length");
		}

		//actually creating hql----------------------------------------
		StringBuilder hql = new StringBuilder("UPDATE ");
		hql.append(entityName);
		HQLPartGenerator.getValuesListForUpdateNumbers(propertyNames, hql);
		hql.append(" WHERE id = :id");
		//-------------------------------------------------------------
		Session sess = this.getSessionFactory().getCurrentSession();

		Query q = sess.createQuery(hql.toString()).setParameter("id", id);
		//appending values
		for (int j=0;j<propertyNames.length;j++){
			q = q.setParameter(j, propertyValues[j]);
		}
		return q.executeUpdate();
	}

	@Override
	public List<T> getByPropertiesValuesPortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, Object[][] propertyValues, int firstResult, int resultCount,
			String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		common.hibernate.HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" FROM ");
		baseHQL.append(this.entityName);
		common.hibernate.HQLPartGenerator.getWhereManyColumnsManyValues(propertiesWhere, propertyValues, baseHQL);
		common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (propertiesWhere!=null)
			q = HQLPartGenerator.appendWherePropertiesValues(propertyValues, q);
		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyNames!=null&&propertyAliases!=null&&propertyAliases.length==propertyNames.length)
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Override
	public Long getRowCount(String propertyName, Object propertyValue) {
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValue(propertyName, propertyValue, hql);

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		if (propertyName!=null&&propertyValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propertyValue);
		return (Long)q.uniqueResult();
	}

	@Override
	public Long getRowCount(String propertyName, List<Object> propertyValue) {
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValues(propertyName, propertyValue, hql);

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		q = HQLPartGenerator.appendWherePropertiesValue(propertyValue, q);

		return (Long)q.uniqueResult();
	}

	@Override
	public Long getRowCount(String[] propertyName, Object[] propertyValue) {
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM ");
		hql.append(entityName);
		HQLPartGenerator.getWhereManyColumns(propertyName, propertyValue, hql);

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		q = HQLPartGenerator.appendWherePropertiesValue(propertyValue, q);
	
		return (Long)q.uniqueResult();
	}

	@Override
	public Long getRowCount(String[] propertyName, Object[][] propertyValue) {
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM ");
		hql.append(entityName);
		HQLPartGenerator.getWhereManyColumnsManyValues(propertyName, propertyValue, hql);

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		q = HQLPartGenerator.appendWherePropertiesValues(propertyValue, q);

		return (Long)q.uniqueResult();
	}

	@Override
	public Object getSinglePropertyU(String property, String propName, Object propValue){
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(property);
		hql.append(" FROM ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValue(propName, propValue, hql);

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		if (propName!=null&&propValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propValue);

		return q.uniqueResult();
	}

	@Override
	public Object getSinglePropertyU(String property, String propName, Object propValue, int number){
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(property);
		hql.append(" FROM ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValue(propName, propValue, hql);

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		q.setFirstResult(number);
		q.setMaxResults(1);
		if (propName!=null&&propValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propValue);

		return q.uniqueResult();
	}

	@Override
	public Object getSinglePropertyU(String property, String propName, Object propValue, int number, String[] orderBy, String[] orderHow){
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(property);
		hql.append(" FROM ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValue(propName, propValue, hql);
        HQLPartGenerator.getOrderBy(orderBy, orderHow, hql);

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		q.setFirstResult(number);
		q.setMaxResults(1);
		if (propName!=null&&propValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propValue);

		return q.uniqueResult();
	}

	@Override
	public Object getSinglePropertyU(String property, String[] propName, Object[] propValue, int number, String[] orderBy, String[] orderHow){
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(property);
		hql.append(" FROM ");
		hql.append(entityName);
		HQLPartGenerator.getWhereManyColumns(propName, propValue, hql);
        HQLPartGenerator.getOrderBy(orderBy, orderHow, hql);

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		q.setFirstResult(number);
		q.setMaxResults(1);
		q = HQLPartGenerator.appendWherePropertiesValue(propValue, q);

		return q.uniqueResult();
	}

	@Override
	public Object getSinglePropertyU(String property){
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(property);
		hql.append(" FROM ");
		hql.append(entityName);
		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());

		return q.uniqueResult();
	}

	@Override
	public List getSinglePropertyOrderRand(String property, String whereName, Object whereValue, int first, int max){
		StringBuilder hql = new StringBuilder("select ");
		hql.append(property);
		hql.append(" from ");
		hql.append(entityName);
		if (whereName!=null){
			hql.append(" where ");
			hql.append(whereName);
			if (whereValue==null){
				hql.append(" is null ");
			}else{
				hql.append(" = :prop ");
			}
		}
		hql.append(" order by rand()");
		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		if (max>0) q = q.setMaxResults(max);
		if (first>0) q = q.setFirstResult(first);
		if (whereName!=null&&whereValue!=null) q = q.setParameter("prop", whereValue);
		return q.list();
	}

    @Override
    public List getSingleProperty(String property, String[] whereName, Object[] whereValue, int first, int max, String[] orderBy, String[] orderHow){
		StringBuilder hql = new StringBuilder("select ");
		hql.append(property);
		hql.append(" from ");
		hql.append(entityName);
        HQLPartGenerator.getWhereManyColumns(whereName, whereValue, hql);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, hql);

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		if (max>0) q = q.setMaxResults(max);
		if (first>0) q = q.setFirstResult(first);
		q = HQLPartGenerator.appendWherePropertiesValue(whereValue, q);
		return q.list();
    }

	@Override
	public void deattach(T obj){sessionFactory.getCurrentSession().evict(obj);}

	@Override
	public T getById(ID id) {return (T) sessionFactory.getCurrentSession().get(entityName, id);}

	@Override
	public void makePersistent(T entity) {sessionFactory.getCurrentSession().saveOrUpdate(entityName, entity);}

	@Override
	public void merge(T entity) {sessionFactory.getCurrentSession().merge(entityName, entity);}

	@Override
	public int deleteByPropertyValue(String propertyName, Object propertyValue) {
		if (propertyName==null) return -1;
		StringBuilder hql = new StringBuilder("DELETE ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValue(propertyName, propertyValue, hql);

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		return q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propertyValue).executeUpdate();
	}

	@Override
	public int deleteById(ID[] id) {
		StringBuilder hql = new StringBuilder("DELETE ");
		hql.append(entityName);
		hql.append(" WHERE id in (:idList)");

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		return q.setParameterList("idList", id).executeUpdate();
	}

	@Override
	public List<T> getByPropertyValues(String[] properties, String propertyName, Object[] propertyValues) {
		StringBuilder hql = new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(properties, properties, hql);
		hql.append(" from ");
		hql.append(entityName);
		hql.append(" where ");
		hql.append(propertyName);
		hql.append(" in (:idList)");

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		q = q.setParameterList("idList", propertyValues);
		if (properties!=null) q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Override
	public List<T> getByPropertyValues(String[] properties, String propertyName, List<Object> propertyValues) {
		if (propertyName==null||propertyValues==null||propertyValues.size()==0)
			return null;
		
		StringBuilder hql = new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(properties, properties, hql);
		hql.append(" from ");
		hql.append(entityName);
		hql.append(" where ");
		hql.append(propertyName);
		hql.append(" in (:idList)");

		Query q = sessionFactory.getCurrentSession().createQuery(hql.toString());
		q = q.setParameterList("idList", propertyValues);
		if (properties!=null) q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Override
	public Object getRowNumber(Object[] values, String[] orderBy, String[] orderHow,
			String[] propertyName, Object[] propertyValue)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		baseHQL.append("SELECT COUNT(*)+1 FROM ");
		baseHQL.append(this.entityName);
		if (orderBy!=null&&values!=null&&orderHow!=null){
			if (propertyName!=null&&propertyValue!=null){
				HQLPartGenerator.getWhereManyColumns(propertyName, propertyValue, baseHQL);
				baseHQL.append(" and (");
			} else {
				baseHQL.append(" WHERE (");
			}
			for (int i=0;i<orderBy.length;i++){
				for (int j=0;j<i;j++){
					baseHQL.append(orderBy[j]);
					if (values[j]==null){
						baseHQL.append(" IS NULL");
					}else{
						baseHQL.append(" = :p");
						baseHQL.append(j);
					}
				}
				baseHQL.append(orderBy[i]);
				if ("desc".equalsIgnoreCase(orderHow[i])){
					if (values[i]==null){
						baseHQL.append(" > NULL");
					}else{
						baseHQL.append(" > :p");
						baseHQL.append(i);
					}
				} else {
					if (values[i]==null){
						baseHQL.append(" < NULL");
					}else{
						baseHQL.append(" < :p");
						baseHQL.append(i);
					}
				}
			}
			baseHQL.append(")");
		}
		common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = sessionFactory.getCurrentSession().createQuery(baseHQL.toString());

		if (values!=null&&orderBy!=null)
			for (int i=0;i<values.length;i++)
				if (values[i]!=null)
					q = q.setParameter("p"+i, values[i]);
		q = HQLPartGenerator.appendWherePropertiesValue(propertyValue, q);

		return q.uniqueResult();
	}

    @Override
    public void restartTransaction(){
        sessionFactory.getCurrentSession().getTransaction().commit();
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().beginTransaction();
    }

	@Override
	public ScrollableResults getScrollableResults(String property, String whereName, Object whereValue, String[] orderBy, String[] orderHow){
		StringBuilder baseHQL=new StringBuilder();
		baseHQL.append("select ");
		baseHQL.append(property);
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		if (whereName!=null){
			baseHQL.append(" where ");
			baseHQL.append(whereName);
			if (whereValue==null){
				baseHQL.append(" is null");
			}else{
				baseHQL.append("= :param");
			}
		}
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		Query q = sessionFactory.getCurrentSession().createQuery(baseHQL.toString());
		if (whereName!=null&&whereValue!=null) q = q.setParameter("param", whereValue);
		return q.scroll();
	}

	@Override
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases, String[] propertiesWhere, String[][] relations, Object[][] propertyValues, int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		common.hibernate.HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		common.hibernate.HQLPartGenerator.getWhereManyColumnsManyValuesRelations(propertiesWhere, relations, propertyValues, baseHQL);
		common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		common.hibernate.HQLPartGenerator.appendtWhereManyColumnsManyValuesRelations(propertyValues, q);
		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyNames!=null&&propertyAliases!=null&&propertyAliases.length==propertyNames.length)
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Override
	public Object getSinglePropertyU(String property, String[] propName, String[][] relations, Object[][] propValue, int number, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		baseHQL.append("select ");
		baseHQL.append(property);
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		common.hibernate.HQLPartGenerator.getWhereManyColumnsManyValuesRelations(propName, relations, propValue, baseHQL);
		common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		common.hibernate.HQLPartGenerator.appendtWhereManyColumnsManyValuesRelations(propValue, q);
		q.setFirstResult(number);
		q.setMaxResults(1);
		return q.uniqueResult();
	}

	@Override
	public void enableFilter(String name, String[] param_names, Object[] param_values){
		Filter f = this.getSessionFactory().getCurrentSession().enableFilter(name);
		if (param_names==null||param_values==null)
			return;
		for (int i=0;i<param_names.length;i++){
			f = f.setParameter(param_names[i], param_values[i]);
		}
	}

	@Override
	public void disableFilter(String name){
		this.getSessionFactory().getCurrentSession().disableFilter(name);
	}

}
