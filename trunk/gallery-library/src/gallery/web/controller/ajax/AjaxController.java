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

package gallery.web.controller.ajax;

import common.utils.RequestUtils;
import gallery.model.beans.Pages;
import gallery.service.pages.IPagesService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.Controller;
//import org.springframework.web.servlet.mvc.LastModified;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class AjaxController implements Controller{ //extends AbstractController implements LastModified{
	/** service for working with pages */
	protected IPagesService pagesService;

	protected String template;

	public static final String[] TYPES =
			new String[]{gallery.web.controller.pages.types.WallpaperGalleryType.TYPE, com.multimedia.core.pages.types.CommonItemType.TYPE};

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(pagesService, "pagesService", sb);
		common.utils.MiscUtils.checkNotNull(template, "template", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	protected static String[] RUBRIC_WHERE = new String[]{"active", "type", "id_pages"};
	protected static String[] RUBRIC_PSEUDONYMES = new String[]{"id","id_pages","name","type","last"};

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		Long id_pages = RequestUtils.getLongParam(request, "id_pages");
		List<Pages> rez =  pagesService.getShortByPropertiesValuesOrdered(RUBRIC_PSEUDONYMES, RUBRIC_WHERE,
				new Object[][]{new Object[]{Boolean.TRUE}, TYPES, new Object[]{id_pages}});
        return new ModelAndView(template, "pages", rez);
	}

	public void setPagesService(IPagesService service) {this.pagesService = service;}
	public void setTemplate(String value){template = value;}

}
