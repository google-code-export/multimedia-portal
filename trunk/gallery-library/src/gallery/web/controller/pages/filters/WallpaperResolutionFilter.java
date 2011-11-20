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

import com.multimedia.service.wallpaper.IWallpaperService;
import common.utils.RequestUtils;
import gallery.model.beans.Resolution;
import gallery.service.resolution.IResolutionService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperResolutionFilter implements IFilter{
	protected static final String NAME = "WallpaperResolutionFilter";
	protected static final String SESSION_KEY = "WallpaperResolution";
	protected static final Long REMOVE_VALUE = Long.valueOf(0);
	public static final String REGION_KEY = "wallpaper_resolutions";

	protected Long id_resolution_nav;
	protected Ehcache cache;

	protected Resolution cur_resolution;
	protected List<Resolution> resolutions;

	protected IResolutionService resolutionService;
	protected IWallpaperService wallpaperService;

	public WallpaperResolutionFilter(IResolutionService service, IWallpaperService wallpaper_service, HttpServletRequest request){
		this.resolutionService = service;
		this.wallpaperService = wallpaper_service;
		id_resolution_nav = RequestUtils.getLongParam(request, "id_resolution_nav");
		HttpSession sess = request.getSession();
		if (id_resolution_nav == null){
			id_resolution_nav = (Long) sess.getAttribute(SESSION_KEY);
		}
		if (id_resolution_nav != null){
			//TODO: optimize, or mb cash results or mb xz
			if (id_resolution_nav.equals(REMOVE_VALUE)){
				this.id_resolution_nav = null;
				this.cur_resolution = null;
			}else{
				this.cur_resolution = resolutionService.getById(id_resolution_nav);
				if (cur_resolution == null){
					this.id_resolution_nav = null;
				}
			}
		} else {
			this.cur_resolution = null;
		}
		sess.setAttribute(SESSION_KEY, id_resolution_nav);

		cache = CacheManager.getInstance().getEhcache(REGION_KEY);//TODO: replace with non singleton instance
		if (cache==null){
			throw new NullPointerException("no cache region defined for: "+REGION_KEY);
		}
	}

	@Override
	public void enableFilters(){
		if (id_resolution_nav!=null){
			Resolution r = resolutionService.getById(id_resolution_nav);
			wallpaperService.enableResolutionFilter(r.getWidth(), r.getHeight());
		}
	}

	@Override
	public void disableFilters(){
		if (id_resolution_nav!=null){
			wallpaperService.disableResolutionFilter();
		}
	}

	public Resolution getCurrent(){return cur_resolution;}

	public List<Resolution> getData(){
		if (resolutions==null){
			Element rez = cache.get(REGION_KEY);
			if (rez==null||rez.getObjectValue()==null){
				resolutions = resolutionService.getByPropertyValueOrdered(null, null, null,
						gallery.web.controller.resolution.Config.ORDER_BY, gallery.web.controller.resolution.Config.ORDER_HOW);
				cache.put(new Element(REGION_KEY, resolutions));
				//System.out.println("WallpaperResolutionSubmodule from DB");
			} else {
				resolutions = (List<Resolution>)rez.getValue();
				//System.out.println("WallpaperResolutionSubmodule from cache");
			}
		}
		return resolutions;

	}

	@Override
	public String getQueryParam(){return null;}

	@Override
	public String getFilterName() {return NAME;}
}
