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

import common.web.controller.StatisticController;
import gallery.service.pages.IPagesService;
import gallery.service.photo.IPhotoService;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class GalleryStatisticController extends StatisticController{
	private IPhotoService photo_service;
	private IPagesService pages_service;
	private String model_name;

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(photo_service, "photo_service", sb);
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
        HashMap hm = new HashMap();
        hm.put("photo_count", photo_service.getSinglePropertyU("count(*)"));
        hm.put("pages_count", pages_service.getSinglePropertyU("count(*)"));
		request.setAttribute(model_name, hm);
		return rez;
	}

	public void setPhoto_service(IPhotoService value) {this.photo_service = value;}
	public void setPages_service(IPagesService value) {this.pages_service = value;}
	public void setModel_name(String value){this.model_name = value;}
}
