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

package gallery.model.command;

import common.beans.IMultiupdateBean;
import common.services.IMultiupdateService;
import gallery.model.beans.PagesPseudonym;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MultiPagesPseudonymCms implements IMultiupdateBean<PagesPseudonym, Long>{
    private Long[] id;
    private Long[] sort;
    private String[] text;
    private Boolean[] use_in_pages;
    private Boolean[] use_in_items;

	public MultiPagesPseudonymCms(int size) {
		id = new Long[size];
		sort = new Long[size];
		text = new String[size];
        use_in_pages = new Boolean[size];
        use_in_items = new Boolean[size];
		java.util.Arrays.fill(use_in_pages, Boolean.FALSE);
		java.util.Arrays.fill(use_in_items, Boolean.FALSE);
	}

	public Long[] getId() {return id;}
	public void setId(Long[] id) {this.id = id;}

	public Long[] getSort() {return sort;}
	public void setSort(Long[] sort) {this.sort = sort;}

	public String[] getText() {return text;}
	public void setText(String[] text) {this.text = text;}

	public Boolean[] getUseInPages() {return use_in_pages;}
	public void setUseInPages(Boolean[] val) {this.use_in_pages = val;}

	public Boolean[] getUseInItems() {return use_in_items;}
	public void setUseInItems(Boolean[] val) {this.use_in_items = val;}

	public static final String[] MULTI_UPDATE_NAMES = new String[]{"text", "sort","useInPages","useInItems"};
	@Override
	public int save(IMultiupdateService<PagesPseudonym, Long> service) {
		if (id!=null&&text!=null&&sort!=null&&use_in_items!=null&&use_in_pages!=null){
			return service.updateObjectArrayShortById(MULTI_UPDATE_NAMES, id, text, sort, use_in_pages, use_in_items);
		}else{
			return -1;
		}
	}

	public int getSize(){
		if (id!=null){
			return id.length-1;
		}else{
			return 0;
		}
	}

	@Override
	public boolean isModel() {return false;}

	@Override
	public Object getModel() {return null;}
}
