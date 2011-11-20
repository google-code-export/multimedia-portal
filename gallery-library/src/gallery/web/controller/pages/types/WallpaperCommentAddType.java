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

import com.multimedia.core.pages.types.APagesType;
import common.bind.ABindValidator;
import common.services.IInsertService;
import common.web.controller.CommonActions;
import gallery.model.beans.WallpaperComment;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperCommentAddType extends APagesType{
    /** string constant that represents type for this page */
    public static final String TYPE="system_wallpaperComment_add";
	/** rus type */
	public static final String TYPE_RU="---Добавление комментария к фото---";

	private ABindValidator validator;

	private IInsertService<WallpaperComment> service;

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(validator, "validator", sb);
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public String getType() {return TYPE;}

	@Override
	public String getTypeRu() {return TYPE_RU;}

	@Override
	public UrlBean execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		PrintWriter pw = response.getWriter();
		if (CommonActions.doInsert(service, config, validator, request)){
			pw.write("1");
		}else{
			pw.write("0");
		}
		pw.flush();
		pw.close();
        return null;
	}

	public void setValidator(ABindValidator value) {this.validator = value;}
	public void setViewService(IInsertService<WallpaperComment> value) {this.service = value;}

}
