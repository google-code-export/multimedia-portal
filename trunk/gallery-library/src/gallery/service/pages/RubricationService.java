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

import common.services.generic.GenericCacheService;
import gallery.model.beans.Pages;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class RubricationService extends GenericCacheService<List<Pages>> implements IRubricationService {
    protected Object[] pages_types;
    protected IPagesService pages_service;

    @Override
	public void init(){
        super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(pages_types, "pages_types", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	protected static String[] RUBRIC_WHERE = new String[]{"active","type"};
	protected static String[] RUBRIC_PSEUDONYMES = new String[]{"id","id_pages","name","type","last"};
    @Override
    public List<Pages> getFromDB() {
		List<Pages> rez = pages_service.getPagesChildrenRecurciveOrderedWhere(RUBRIC_PSEUDONYMES, RUBRIC_WHERE,
				new Object[][]{new Object[]{Boolean.TRUE},pages_types});
		return rez;
    }

    public void setPages_types(Object[] value){this.pages_types = value;}
    public void setPages_service(IPagesService value){this.pages_service = value;}

	@Override
	public List<Pages> getObjectClone() {
		List<Pages> tmp =  getObject();
		Vector<Pages> rez = new Vector<Pages>(tmp.size());
		for (Pages p:tmp){
			rez.add((Pages)p.clone());
		}
		return rez;
	}

	@Override
	public List<Pages> getCurrentBranch(Long id_pages) {
		List<Pages> tmp =  getObject();
		List<Pages> rez = getBranchUnsorted(tmp, id_pages);
		if (rez.isEmpty()){
			Pages cur_page;
			boolean selected = true;
			for (Pages p:tmp){
				if (p.getLayer()<3){
					cur_page = (Pages)p.clone();
					cur_page.setSelected(selected);
					rez.add(cur_page);
					selected = false;
				}
			}
		}
		return rez;
	}

	/**
	 * the order is natural
	 * @param clone pages list
	 * @param id_pages current page's id
	 * @return list of pages in an appropriate order, with selected flag
	 */
	protected List<Pages> getBranchUnsorted(List<Pages> object, Long id_pages){
		LinkedList<Pages> rez = new LinkedList<Pages>();
		int i=object.size();
		Long cur_id = id_pages;
		//1-st collecting all parents
		HashSet<Long> parents_id = new HashSet<Long>();
		parents_id.add(id_pages);
		while (i>0){
			i--;
			if (cur_id.equals(object.get(i).getId())){
				cur_id = object.get(i).getId_pages();
				if (cur_id == null){
					break;
				} else {
					parents_id.add(cur_id);
				}
			}
		}
		//2-nd adding required
		i=object.size();
		Pages cur_page;
		while (i>0){
			i--;
			cur_page = object.get(i);
			if (parents_id.contains(cur_page.getId())){
				cur_page = (Pages)cur_page.clone();
				rez.addFirst(cur_page);
				cur_page.setSelected(Boolean.TRUE);
			} else if (parents_id.contains(cur_page.getId_pages())) {
				rez.addFirst((Pages)cur_page.clone());
			} else if (cur_page.getId_pages()==null){
				rez.addLast((Pages)cur_page.clone());
			}
		}
		return rez;
	}

	/**
	 * sortes sets selected pages to the top of list
	 * @param clone pages list
	 * @param id_pages current page's id
	 * @return list of pages in an appropriate order, with selected flag
	 */
	protected List<Pages> getBranchSortedTop(List<Pages> object, Long id_pages){
		LinkedList<Pages> rez = new LinkedList<Pages>();
		//marking current navigation branch
		Long cur_id = id_pages;
		Pages cur_page;
		int i=object.size();
		int last = -1;
		int pos = 0;
		//marking selected branch
		while (i>0){
			i--;
			cur_page = object.get(i);
			if (cur_id.equals(cur_page.getId_pages())&&last!=i){
				rez.add(pos, (Pages)cur_page.clone());
			} else
				if (cur_id.equals(cur_page.getId())){
					cur_page = (Pages)cur_page.clone();
					cur_page.setSelected(Boolean.TRUE);
					rez.addFirst(cur_page);

					cur_id = cur_page.getId_pages();
					last = i;
					pos = rez.size();
					i=object.size();
					if (cur_id==null){
						break;
					}
				}
		}
		return rez;
	}

}
