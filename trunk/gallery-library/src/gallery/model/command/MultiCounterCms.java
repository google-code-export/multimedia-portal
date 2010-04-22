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

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MultiCounterCms implements IMultiupdateBean{
    private Long[] id;
    private Long[] sort;

	public MultiCounterCms(int size) {
		id = new Long[size];
		sort = new Long[size];
	}

	public Long[] getId() {return id;}
	public void setId(Long[] id) {this.id = id;}

	public Long[] getSort() {return sort;}
	public void setSort(Long[] value) {this.sort = value;}

	public static final String[] MULTI_UPDATE_NAMES = new String[]{"sort"};
	@Override
	public int save(IMultiupdateService service) {
		if (id!=null&&sort!=null){
			return service.updateObjectArrayShortById(MULTI_UPDATE_NAMES, id, sort);
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

}
