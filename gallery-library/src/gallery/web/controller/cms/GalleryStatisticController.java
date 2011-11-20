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

package gallery.web.controller.cms;

import com.multimedia.service.wallpaper.IWallpaperService;
import common.services.IStaticsService;
import common.web.controller.StatisticController;
import gallery.service.pages.IPagesService;
import java.util.Collections;
import java.util.SortedMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.TreeMap;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class GalleryStatisticController extends StatisticController implements IStaticsService{
	private IWallpaperService wallpaper_service;
	private IPagesService pages_service;
	private String model_name;

	protected final SortedMap<String, Long> statistics = Collections.synchronizedSortedMap(new TreeMap<String, Long>(Collections.reverseOrder()));

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(wallpaper_service, "wallpaper_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		common.utils.MiscUtils.checkNotNull(model_name, "model_name", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		ModelAndView rez = super.handleRequest(request, response);
		statistics.put("wallpaper_count", (Long)wallpaper_service.getSinglePropertyU("count(*)"));
		statistics.put("pages_count", (Long)pages_service.getSinglePropertyU("count(*)"));
		request.setAttribute(model_name, statistics);
		return rez;
	}


	@Override
	public void increaseStat(String statName, long value){
		Long l = statistics.get(statName);
		if (l==null){
			statistics.put(statName, Long.valueOf(1));
		} else {
			statistics.put(statName, ++l);
		}
	}

	public void setWallpaper_service(IWallpaperService value) {this.wallpaper_service = value;}
	public void setPages_service(IPagesService value) {this.pages_service = value;}
	public void setModel_name(String value){this.model_name = value;}
}
