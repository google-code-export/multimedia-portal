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

package gallery.service.pages;

import gallery.model.beans.Pages;
import common.services.generic.IGenericService;
import gallery.web.controller.pages.submodules.ASubmodule;
import java.util.List;
import java.util.Map;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IPagesService extends IGenericService<Pages, Long>{

	/**
	 * returns not all columns
	 * @return the pages object
	 */
	public List<Pages> getAllShortCms();

	/**
	 * returns not all columns
	 * @param propertyName property to search by
	 * @param propertyValue value of property
	 * @return the pages object
	 */
	public List<Pages> getShortOrderedByPropertyValueCms(String propertyName, Object propertyValue);

	/**
     * the result is sorted by sort and name columns works as follows
     *1  selects all pages where id_pages=null
     *2  sets their layer=0
     *3  adds first record to rezult (as r0)
     *4  selects all pages where id_pages=r0.id
     *5  sets their layer=r0.layer+1
     *   go 3
     * @param id id of page that will not be selected with its children
	 * @return all children of page with id_pages
	 */
	public List<Pages> getPagesForRelocate(Long id);

    /**
     * changes id_pages of record by id=id to newIdPages
     * @param id primary id of row to be changed
     * @param newIdPages the id_pages to be set
     * @return true if succeed
     */
    public boolean relocatePages(Long id,Long newIdPages);

	/**
	 * retrieves parents of current page for navigation
	 * starting from first parent(id_pages=null) to current page
	 * @param id id of starting page
	 * @param property_names names of properties to retrieve
	 * @return all parents including current page
	 */
	public List<Pages> getAllPagesParents(Long id, String[] property_names);

	/**
	 * retrieves all active children of page with given id which types ...
	 * @param id id of page for searching categories
	 * @param type type of categories
	 * @return current page active children
	 */
	public List<Pages> getCategories(Long id, String type);

    /**
     * if one of parameters is null, its not used
     * @return list of pages that can be used for choosing from combobox
     */
    public List<Pages> getAllCombobox(Boolean active, Boolean last, String type);

	/**
     * the result is sorted by sort and name columns works as follows
     *1  selects all pages where id_pages=id
     *2  adds first record to rezult (as r0)
     *3  selects all pages where id_pages=r0.id
     *   go 2
	 * @param id id of page from where to start selecting
	 * @return all children of page with id_pages includes id of start page
	 */
	public List<Long> getAllActiveChildrenId(Long id);

	/**
     * submodules are pages with specified types
	 *	trying to search from last to first
	 *  set active to true if found such page
	 *  and set its Pages
	 * @param navigation id of pages where to search for submodules
	 * @param submodule_types set of types that will be activated
	 * @return all found submodules
	 */
	public List<Pages> activateSubmodules(List<Pages> navigation, Map<String, ASubmodule> submodules);

	/**
	 * recalculating last field in a page with given id;
	 * if id is null, in all pages
	 * @param id page id
	 */
	public void recalculateLast(Long id);

    public List<Pages> getPagesChildrenRecurciveOrderedWhere(String[] properties, String[] propertyNames, Object[][] propertyValues);
    public List<Pages> getPagesChildrenRecurciveOrderedWhere(String[] properties, String[] propPseudonyms, String[] propertyNames, Object[][] propertyValues);
    public List<Pages> getPagesChildrenRecurciveOrderedWhere(String[] properties, String[] propPseudonyms, String[] propertyNames, Object[][] propertyValues, Long first_id);

	public List<Pages> getShortByPropertiesValuesOrdered(String[] propNames, String[] propertyName, Object[][] propertyValue);
}
