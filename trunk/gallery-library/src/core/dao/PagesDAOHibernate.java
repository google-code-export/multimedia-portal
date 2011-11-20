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

package core.dao;

import common.dao.GenericDAOHibernate;
import common.hibernate.HQLPartGenerator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @param <T>
 * @author demchuck.dima@gmail.com
 */
public class PagesDAOHibernate<T extends core.model.beans.Pages, ID extends Serializable> extends GenericDAOHibernate<T, ID> implements IPagesDAO<T, ID>{
	public PagesDAOHibernate(java.lang.String entityName, java.lang.String className){
		super(entityName);
		Class c;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException ex) {
			throw new NullPointerException("persistent class is not specified; ");
		}
		if (c.isAssignableFrom(core.model.beans.Pages.class)){
			throw new NullPointerException("persistent class is not valid; ");
		}
		persistentClass = c;
	}

	/**
	 * pseudonyms of columns that will be selected by getPagesForRelocate(Long) method
	 *  must have at least "id" and "id_pages" columns
	 */
	public static final String[] PAGES_FOR_RELOCATE_PSEUDONYMS = new String[]{"id","id_pages","name"};
	@Override
	@Transactional(readOnly = true)
	public List<T> getPagesForRelocateOrdered(Long id, String[] orderBy, String[] orderHow) {
        if (id==null){
            return new ArrayList<T>();
        }else{
            //creating table for results
            List<T> rez=new ArrayList<T>();
			AliasToBeanResultTransformer pagesTransformer = new AliasToBeanResultTransformer(persistentClass);
            //forming HQL-----------------------------------------------------------
            StringBuilder baseHQL=new StringBuilder();
            common.hibernate.HQLPartGenerator.getValuesListWithAliases(PAGES_FOR_RELOCATE_PSEUDONYMS,PAGES_FOR_RELOCATE_PSEUDONYMS, baseHQL);
            baseHQL.append(" FROM ");
			baseHQL.append(this.entityName);
            //----------------------------------------------------------------------
			Session sess = this.getSessionFactory().getCurrentSession();
			List<T> temp;
			{
				//selecting base entities
				StringBuilder hql = new StringBuilder(baseHQL);
				hql.append(" WHERE id_pages is null");
				common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, hql);
			
				temp=sess.createQuery(hql.toString()).setResultTransformer(pagesTransformer).list();
			}

			//deleting instance with given id and setting layer
			for (int i=0;i<temp.size();i++){
				if (temp.get(i).getId().equals(id)){
					temp.remove(i);
					i--;
				}else{
					temp.get(i).setLayer(Long.valueOf(0));
				}
			}

			//completing hql forming
			baseHQL.append(" WHERE id_pages = :id_pages");
			common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
			String hql = baseHQL.toString();
			baseHQL = null;

			while (temp.size()>0){
				//getting first element
				T currRow=temp.remove(0);

				List<T> children=sess.createQuery(hql).setParameter("id_pages", currRow.getId()).
					setResultTransformer(pagesTransformer).list();

				//deleting instance with given id and setting layer
				for (int i=0;i<children.size();i++){
					if (children.get(i).getId().equals(id)){
						children.remove(i);
						i--;
					}else{
						children.get(i).setLayer(currRow.getLayer()+1);
					}
				}
				temp.addAll(0,children);
				rez.add(currRow);
			}
            return rez;
        }
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getPagesChildrenRecurciveOrderedWhere(String[] properties, String[] propPseudonyms, String[] propertyNames, Object[][] propertyValues,
			String[] orderBy, String[] orderHow, Long first_id)
	{
		//creating table for results
		List<T> rez=new ArrayList<T>();
		AliasToBeanResultTransformer pagesTransformer = new AliasToBeanResultTransformer(persistentClass);
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		common.hibernate.HQLPartGenerator.getValuesListWithAliases(properties, propPseudonyms, baseHQL);
		baseHQL.append(" FROM ");
		baseHQL.append(this.entityName);
		common.hibernate.HQLPartGenerator.getWhereManyColumnsManyValues(propertyNames, propertyValues, baseHQL);
		//----------------------------------------------------------------------
		Session sess = this.getSessionFactory().getCurrentSession();
		List<T> temp;
		{
			//selecting base entities
			StringBuilder hql = new StringBuilder(baseHQL);
            if (propertyNames==null)
                hql.append(" WHERE");
            else
                hql.append(" AND");
            if (first_id==null){
                hql.append(" id_pages is null");
            } else {
                hql.append(" id = ");
                hql.append(first_id);
            }
			common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, hql);

			Query q = sess.createQuery(hql.toString());
			common.hibernate.HQLPartGenerator.appendWherePropertiesValues(propertyValues, q);

			temp = q.setResultTransformer(pagesTransformer).list();
		}

		//deleting instance with given id and setting layer
		for (int i=0;i<temp.size();i++){
			temp.get(i).setLayer(Long.valueOf(0));
		}

		//completing hql forming
        if (propertyNames==null)
            baseHQL.append(" WHERE");
        else
            baseHQL.append(" AND");
		baseHQL.append(" id_pages = :id_pages");
		common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		String hql = baseHQL.toString();
		baseHQL = null;

		while (temp.size()>0){
			//getting first element
			T currRow=temp.remove(0);

			Query q = sess.createQuery(hql.toString());
			common.hibernate.HQLPartGenerator.appendWherePropertiesValues(propertyValues, q);
			List<T> children=q.setParameter("id_pages", currRow.getId()).
				setResultTransformer(pagesTransformer).list();

			//setting layer
			for (int i=0;i<children.size();i++){
				children.get(i).setLayer(currRow.getLayer()+1);
			}
			temp.addAll(0,children);
			rez.add(currRow);
		}
		return rez;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkRecursion(Long id, Long newIdPages) {
        if (id==null){
            return false;
        }
        if (newIdPages==null){
            return true;
        }
        //forming hql----------------------------------------------------
        StringBuilder baseHQL=new StringBuilder("SELECT pages.id FROM ");
		baseHQL.append(this.entityName);
        baseHQL.append(" WHERE id = :id");
		String hql = baseHQL.toString();
		baseHQL = null;
		//---------------------------------------------------------------

		Session sess = this.getSessionFactory().getCurrentSession();
        
        Object cur_id=newIdPages;
        while (cur_id!=null){
            if (id.equals(cur_id)){
                return false;
            }
			cur_id = sess.createQuery(hql).setParameter("id", cur_id).uniqueResult();
        }
        return true;
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getAllParentsRecursive(Long id, String[] propertyNames, String[] propertyAliases) {
        if (id==null||propertyAliases==null||propertyNames==null||propertyNames.length!=propertyAliases.length)
			return null;
        //forming hql----------------------------------------------------
        StringBuilder baseHQL=new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(propertyNames, propertyAliases, baseHQL);
		baseHQL.append(" FROM ");
		baseHQL.append(this.entityName);
        baseHQL.append(" WHERE ");
		baseHQL.append("id = :id");
		String hql = baseHQL.toString();
		baseHQL = null;
		//---------------------------------------------------------------
		List<T> rez = new ArrayList<T>();
		Session sess = this.getSessionFactory().getCurrentSession();
		AliasToBeanResultTransformer trans = new AliasToBeanResultTransformer(persistentClass);

        boolean completed=false;
        Long cur_id=id;
		T p = null;
		//TODO: check recursion
        while (!completed){
			p = (T) sess.createQuery(hql).setParameter("id", cur_id).setResultTransformer(trans).uniqueResult();
			if (p==null) return rez;
			rez.add(p);
			cur_id = p.getId_pages();
            if (cur_id==null||id.equals(cur_id)) completed=true;
        }
        return rez;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> getAllActiveChildrenId(Long id, String[] orderBy, String[] orderHow) {
		List<Long> rez = new ArrayList<Long>();
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder("SELECT ");
		baseHQL.append("id");
		baseHQL.append(" FROM ");
		baseHQL.append(this.entityName);
		//----------------------------------------------------------------------
		Session sess = this.getSessionFactory().getCurrentSession();
		List<Long> temp;
		if (id==null){
			//selecting base entities
			StringBuilder hql = new StringBuilder(baseHQL);
			hql.append(" WHERE id_pages is null AND active = 1");
			common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, hql);
			temp=sess.createQuery(hql.toString()).list();
		}else{
			temp=new ArrayList<Long>(1);
			temp.add(id);
		}
		//----------------------------------------------------------------------
		//completing hql forming
		baseHQL.append(" WHERE id_pages = :id_pages AND active = 1");
		common.hibernate.HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		String hql = baseHQL.toString();
		baseHQL = null;

		while (temp.size()>0){
			//getting first element
			Long currRow=temp.remove(0);

			List<Long> children=sess.createQuery(hql).setLong("id_pages", currRow).list();

			temp.addAll(0,children);

			rez.add(currRow);
		}
		return rez;
	}
}