/*
 *  Copyright 2010 demchuck.dima@gmail.com.
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

package com.multimedia.config.cms.wallpaper;

import com.multimedia.config.cms.CommonCmsConfig;
import org.springframework.context.annotation.Configuration;

/**
 * is unmodifiable at runtime
 * @author demchuck.dima@gmail.com
 */
@Configuration(value="wallpaperCmsConfig")
public class WallpaperCmsConfig extends CommonCmsConfig implements IWallpaperCmsConfig{
	protected final String contentViewTemplate = "/WEB-INF/jspf/cms/wallpaper/show.jsp";
	protected final String contentMultiupdateTemplate = "/WEB-INF/jspf/cms/wallpaper/show.jsp";
	protected final String contentInsertTemplate = "/WEB-INF/jspf/cms/wallpaper/insert.jsp";
	protected final String contentUpdateTemplate = "/WEB-INF/jspf/cms/wallpaper/update.jsp";
	protected final String contentNavigationTemplate = "/WEB-INF/jspf/cms/wallpaper/navigation.jsp";

	protected final String nameRu = "Wallpapers(обои)";

	protected final String prepareUrl = "/WEB-INF/jspf/cms/wallpaper/prepare.jsp";
	protected final String ajaxListUrl = "/WEB-INF/jspf/cms/wallpaper/ajax_list.jsp";
	protected final String ajaxWallpaperUrl = "/WEB-INF/jspf/cms/wallpaper/ajax_wallpaper.jsp";
	protected final String viewOnlyImgUrl = "/WEB-INF/jspf/cms/wallpaper/ajax_view.jsp";
	protected final String updateAjaxFormUrl = "/WEB-INF/jspf/cms/wallpaper/update_ajax.jsp";
	protected final String insertMultiFormUrl = "/WEB-INF/jspf/cms/wallpaper/multi_insert.jsp";
	protected final String ajaxRespUrl = "/WEB-INF/jspf/cms/wallpaper/ajax_resp.jsp";
	protected final String uploadPrepareUrl = "/WEB-INF/jspf/cms/wallpaper/prepare_upload.jsp";
	protected final String uploadProcessUrl = "/WEB-INF/jspf/cms/wallpaper/process_upload.jsp";
	protected final String duplicatesShowUrl = "/WEB-INF/jspf/cms/wallpaper/duplicates.jsp";
	protected final String prepareOptimizationUrl = "/WEB-INF/jspf/cms/wallpaper/prepare_optimization.jsp";

	protected final String uploadAttributeName = "wallpaper_upload_progress";
	protected final String renewResolutionAttributeName = "wallpaper_renewResolution_progress";

	public WallpaperCmsConfig(){}

	@Override
	public String getContentViewTemplate() {return contentViewTemplate;}

	@Override
	public String getContentMultiupdateTemplate() {return contentMultiupdateTemplate;}

	@Override
	public String getContentInsertTemplate() {return contentInsertTemplate;}

	@Override
	public String getContentUpdateTemplate() {return contentUpdateTemplate;}

	@Override
	public String getNavigationTemplate() {return contentNavigationTemplate;}

	@Override
	public String getNameRu() {return nameRu;}

	@Override
	public String getPrepareUrl() {return prepareUrl;}

	@Override
	public String getAjaxListUrl() {return ajaxListUrl;}

	@Override
	public String getAjaxWallpaperUrl() {return ajaxWallpaperUrl;}

	@Override
	public String getViewOnlyImgUrl() {return viewOnlyImgUrl;}

	@Override
	public String getUpdateAjaxForm() {return updateAjaxFormUrl;}

	@Override
	public String getAjaxRespUrl() {return ajaxRespUrl;};

	@Override
	public String getInsertMultiFormUrl() {return insertMultiFormUrl;}

	@Override
	public String getUploadPrepareUrl() {return uploadPrepareUrl;}

	@Override
	public String getUploadProcessUrl() {return uploadProcessUrl;}

	@Override
	public String getUploadAttributeName() {return uploadAttributeName;}

	@Override
	public String getRenewResolutionAttributeName() {return renewResolutionAttributeName;}

	@Override
	public String getDuplicatesShowUrl() {return duplicatesShowUrl;}

	@Override
	public String getOptimizationPrepareUrl() {return prepareOptimizationUrl;}

}
