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

package gallery.web.controller.pages.types;

import com.multimedia.core.pages.types.IPagesType;
import com.multimedia.service.wallpaper.IWallpaperService;
import gallery.model.beans.WallpaperRating;
import gallery.service.wallpaper.rating.IWallpaperRatingService;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperRateType implements IPagesType{
	protected final Logger logger = Logger.getLogger(getClass());
    /** string constant that represents type for this page */
    public static final String TYPE="system_rate_wallpaper";
	/** rus type */
	public static final String TYPE_RU="---Оценка фото---";

	@Override
	public String getType() {return TYPE;}
	@Override
	public String getTypeRu() {return TYPE_RU;}

	protected IWallpaperRatingService wallpaperRatingService;

	protected IWallpaperService wallpaperService;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(wallpaperRatingService, "wallpaperRatingService", sb);
		common.utils.MiscUtils.checkNotNull(wallpaperService, "wallpaperService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	protected static final String[] RATE_WALLPAPER_WHERE = new String[]{"id_photo","ip"};
	public static final String[] REQUIRED_FIELDS = new String[]{"id_photo","rate"};
	@Override
	public UrlBean execute(HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
			throws Exception
	{
		/** bind command */
		WallpaperRating command = new WallpaperRating();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(command);
		binder.setRequiredFields(REQUIRED_FIELDS);

		binder.bind(request);
		BindingResult res = binder.getBindingResult();

		int error = '1';
		if (res.hasErrors()){
			common.CommonAttributes.addErrorMessage("form_errors", request);
		}else{
			//correcting rating
			gallery.web.support.wallpaper.rating.Utils.correctRate(command);

			command.setIp(request.getRemoteAddr());
			if (wallpaperService.getRowCount("id", command.getId_photo())==1){
				Object[] values = new Object[]{command.getId_photo(),command.getIp()};
				if (wallpaperRatingService.getRowCount(RATE_WALLPAPER_WHERE, values)>0){
					common.CommonAttributes.addErrorMessage("duplicate_ip", request);
				}else{
					wallpaperRatingService.save(command);
					common.CommonAttributes.addHelpMessage("operation_succeed", request);
					error = '0';
				}
			}else{
				common.CommonAttributes.addErrorMessage("not_exists.wallpaper", request);
			}
		}
		OutputStream os = response.getOutputStream();
		os.write(error);
		os.flush();

        return null;
	}

	public void setWallpaperRatingService(IWallpaperRatingService service) {this.wallpaperRatingService = service;}
	public void setWallpaperService(IWallpaperService service) {this.wallpaperService = service;}
}
