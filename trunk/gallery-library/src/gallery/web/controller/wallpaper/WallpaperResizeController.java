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

package gallery.web.controller.wallpaper;

import com.multimedia.service.wallpaper.IWallpaperService;
import common.beans.HttpResponseOutputStreamHolder;
import common.beans.IOutputStreamHolder;
import common.services.IStaticsService;
import gallery.service.resolution.IResolutionService;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.LastModified;

/**
 *
 * @author demchuck.dima@gmail.com
 */
//@RequestMapping("/wallpaper")
@Controller(value="wallpaperResizeController")
public class WallpaperResizeController implements LastModified{
	protected Logger logger = Logger.getLogger(getClass());
	private IResolutionService resolutionService;
	private IWallpaperService wallpaperService;
	private IStaticsService statisticService;

	protected static final String[] RESOLUTION_WIDTH_HEIGHT = new String[]{"width","height"};

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(resolutionService, "resolutionService", sb);
		common.utils.MiscUtils.checkNotNull(wallpaperService, "wallpaperService", sb);
		common.utils.MiscUtils.checkNotNull(statisticService, "statisticService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@RequestMapping(value="/{resolutionX}x{resolutionY}/{wallpaper:.*}", method = RequestMethod.GET)
	public void resizeWallpaper(@PathVariable("resolutionX") Integer resolutionX, @PathVariable("resolutionY") Integer resolutionY, @PathVariable("wallpaper") String wallpaper,
			HttpServletResponse response)
	{
		if (resolutionX>0&&resolutionY>0
				&&resolutionService.getRowCount(RESOLUTION_WIDTH_HEIGHT, new Object[]{resolutionX, resolutionY})==1)
		{
			try {
				statisticService.increaseStat(resolutionX+"x"+resolutionY+":start_resize", 1);
				IOutputStreamHolder osh = HttpResponseOutputStreamHolder.getInstance(response);
				if (wallpaperService.getResizedWallpaperStream(wallpaper, resolutionX, resolutionY, osh)){
					statisticService.increaseStat(resolutionX+"x"+resolutionY+":finish_resize", 1);
				} else {
					//error handling
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
				osh.closeAndFlushOutputStream();
			} catch (IOException ex) {
				logger.error("while resizing image and writing it to output stream(response) ");
			}
			return;
		}
		try {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException ex) {
			logger.error("while sending a not found status ");
		}
	}

	@Resource(name="resolutionService")
	public void setResolutionService(IResolutionService value) {this.resolutionService = value;}
	@Resource(name="wallpaperService")
	public void setWallpaperService(IWallpaperService value) {this.wallpaperService = value;}
	@Resource(name="statsController")
	public void setStatisticService(IStaticsService value) {this.statisticService = value;}

	@Override
	public long getLastModified(HttpServletRequest request) {
		return wallpaperService.getWallpaperLastModified(request.getPathInfo());
	}

}