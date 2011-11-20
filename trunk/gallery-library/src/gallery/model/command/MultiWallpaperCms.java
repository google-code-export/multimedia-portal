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
import gallery.model.beans.Wallpaper;

/**
 *
 * @author demchuck.dima@gmail.com
 */
//TODO: delete, but copy smth to new cms service
public class MultiWallpaperCms implements IMultiupdateBean<Wallpaper, Long>{
    private Long[] id;
    private Boolean[] active;
    private Boolean[] optimized;

	public MultiWallpaperCms(int size) {
		id = new Long[size];
		active = new Boolean[size];
		optimized = new Boolean[size];
		java.util.Arrays.fill(active, Boolean.FALSE);
		java.util.Arrays.fill(optimized, Boolean.FALSE);
	}

	public Long[] getId() {return id;}
	public void setId(Long[] id) {this.id = id;}

	public Boolean[] getActive() {return active;}
	public void setActive(Boolean[] active) {this.active = active;}

	public Boolean[] getOptimized() {return optimized;}
	public void setOptimized(Boolean[] optimized) {this.optimized = optimized;}

	public static final String[] MULTI_UPDATE_NAMES = new String[]{"active","optimized"};
	@Override
	public int save(IMultiupdateService<Wallpaper, Long> service) {
		if (id!=null&&active!=null&&optimized!=null){
			return service.updateObjectArrayShortById(MULTI_UPDATE_NAMES, id, active, optimized);
		}else{
			return -1;
		}
	}

	public int getSize(){
		if (id==null){
			return 0;
		}else{
			return id.length-1;
		}
	}

	@Override
	public boolean isModel() {return false;}

	@Override
	public Object getModel() {return null;}

}
