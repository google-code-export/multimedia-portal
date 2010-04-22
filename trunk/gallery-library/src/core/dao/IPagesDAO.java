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

import common.dao.IGenericDAO;
//import gallery.model.beans.Pages;
//import core.model.beans.Pages;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @param <T> 
 * @author demchuck.dima@gmail.com
 */
public interface IPagesDAO<T extends core.model.beans.Pages, ID extends Serializable> extends IGenericDAO<T, ID>{
	/**
     * the result is sorted by sort and name columns works as follows
     *1  selects all pages where id_pages=null
     *2  sets their layer=0
     *3  adds first record to rezult (as r0)
     *4  selects all pages where id_pages=r0.id
     *5  sets their layer=r0.layer+1
     *   go 3
	 * @param orderBy properties to order
	 * @param orderHow type of sorting ASC or DESC
     * @param id id of page that will not be selected with its children
     * @return all children of page with id_pages
	 */
	public List<T> getPagesForRelocateOrdered(Long id, String[] orderBy, String[] orderHow);

	/**
     * the result is sorted by sort and name columns works as follows
     *1  selects all pages where id_pages=null
     *2  sets their layer=0
     *3  adds first record to rezult (as r0)
     *4  selects all pages where id_pages=r0.id
     *5  sets their layer=r0.layer+1
     *   go 3
	 * @param properties that will be selected (id is required property!!!)
     * @param propPseudonyms pseudonyms of properties to be used in select
	 * @param orderBy properties to order
	 * @param propertyNames is applyied as where condition on all children
	 * @param propertyValues is applyied as where condition on all children
	 * @param orderHow type of sorting ASC or DESC
     * @param first_id id to start selecting from
     * @return all children of page with id_pages
	 */
	public List<T> getPagesChildrenRecurciveOrderedWhere(String[] properties, String[] propPseudonyms, String[] propertyNames, Object[][] propertyValues,
			String[] orderBy, String[] orderHow, Long first_id);

	/**
     * the result is sorted by sort and name columns works as follows
     *1  selects all pages where id_pages=id
     *2  adds first record to rezult (as r0)
     *3  selects all pages where id_pages=r0.id
     *   go 2
	 * @param id id of page from where to start selecting
	 * @param orderBy
	 * @param orderHow 
	 * @return all children of page with id_pages
	 */
	public List<Long> getAllActiveChildrenId(Long id, String[] orderBy, String[] orderHow);

    /**
     * checks if id is parent of newIdPages
     * @param id primary id of row to be changed
     * @param newIdPages the id_pages to be set
     * @return true if no recursion
     */
    public boolean checkRecursion(Long id,Long newIdPages);

	/**
	 *
	 * @param propertyNames names of properties to retrieve
	 * @param id id of starting entity
	 * @param propertyAliases aliases of bean
	 * @return parents from current to last parent
	 */
	public List<T> getAllParentsRecursive(Long id, String[] propertyNames, String[] propertyAliases);
}