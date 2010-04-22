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

package gallery.web.controller.pages.filters;

import common.utils.RequestUtils;
import gallery.model.beans.Resolution;
import gallery.service.photo.IPhotoService;
import gallery.service.resolution.IResolutionService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperResolutionFilter implements IFilter{
	protected static final String NAME = "WallpaperResolutionFilter";
	public static final String REGION_KEY = "wallpaper_resolutions";

	String query_param;
	Long id_resolution_nav;
	Ehcache cache;

	protected IResolutionService<Resolution, Long> resolutionService;
	protected IPhotoService photoService;

	public WallpaperResolutionFilter(IResolutionService service, IPhotoService photo_service, HttpServletRequest request){
		this.resolutionService = service;
		this.photoService = photo_service;
		id_resolution_nav = RequestUtils.getLongParam(request, "id_resolution_nav");
		if (id_resolution_nav == null){
			this.query_param = null;
		}else{
			this.query_param = "&id_resolution_nav="+id_resolution_nav;
		}
		cache = CacheManager.getInstance().getCache(REGION_KEY);
		if (cache==null){
			throw new NullPointerException("no cache region defined for: "+REGION_KEY);
		}
	}

	@Override
	public void enableFilters(){
		if (id_resolution_nav!=null){
			Resolution r = resolutionService.getById(id_resolution_nav);
			photoService.enableResolutionFilter(r.getWidth(), r.getHeight());
		}
	}

	@Override
	public void disableFilters(){
		if (id_resolution_nav!=null){
			photoService.disableResolutionFilter();
		}
	}

	public List<Resolution> getData(){
		Element resolutions = cache.get(REGION_KEY);
		List<Resolution> rez;
		if (resolutions==null||resolutions.getObjectValue()==null){
			rez = resolutionService.getShortByPropertyValueOrdered(null, null, null,
					gallery.web.controller.resolution.Config.ORDER_BY, gallery.web.controller.resolution.Config.ORDER_HOW);
			cache.put(new Element(REGION_KEY, rez));
			//System.out.println("WallpaperResolutionSubmodule from DB");
		} else {
			rez = (List<Resolution>)resolutions.getValue();
			//System.out.println("WallpaperResolutionSubmodule from cache");
		}
		return rez;
	}

	@Override
	public String getQueryParam(){return this.query_param;}

	@Override
	public String getFilterName() {return NAME;}
}
