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

import common.beans.KeepParameters;
import common.beans.PagerBean;
import common.bind.ABindValidator;
import gallery.model.beans.Pages;
import gallery.model.beans.Wallpaper;
import gallery.model.command.TagCloudData;
import gallery.model.command.TagCloudView;
import gallery.service.pagesPseudonym.IPagesPseudonymService;
import com.multimedia.web.support.PaginatedListUtils;
import gallery.web.support.pages.Utils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperTagCloudType extends AWallpaperType{
    /** string constant that represents type for this page */
    public static final String TYPE="general_wallpaper_tagcloud";
	/** rus type */
	public static final String TYPE_RU="Wallpapers(облако тегов)";

	@Override
	public String getType() {return TYPE;}
	@Override
	public String getTypeRu() {return TYPE_RU;}

	public static final String OPTIMIZATION_STRING = "wallpaper_opt_phraze";

	protected String contentUrl;
	protected String searchUrl;
	protected String optimizationUrl;
	protected String infoTopUrl;
	protected String infoBottomUrl;

	private IPagesPseudonymService optimizationService;
	/** pagination config */
	public static final PaginatedListUtils paginationUtils = new PaginatedListUtils(15 ,2);
	private KeepParameters keepParameters;
	private ABindValidator bindValidator;

	@Override
	public void init() {
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(contentUrl, "contentUrl", sb);
		common.utils.MiscUtils.checkNotNull(searchUrl, "searchUrl", sb);
		common.utils.MiscUtils.checkNotNull(optimizationUrl, "optimizationUrl", sb);
		common.utils.MiscUtils.checkNotNull(infoTopUrl, "infoTopUrl", sb);
		common.utils.MiscUtils.checkNotNull(infoBottomUrl, "infoBottomUrl", sb);
		common.utils.MiscUtils.checkNotNull(optimizationService, "optimizationService", sb);
		common.utils.MiscUtils.checkNotNull(keepParameters, "keepParameters", sb);
		common.utils.MiscUtils.checkNotNull(bindValidator, "bindValidator", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response, UrlBean url)
			throws Exception
	{
		TagCloudView tagCloud = new TagCloudView();
		BindingResult res = bindValidator.bindAndValidate(tagCloud, request);
		int cur_page_num;
		if (res.hasErrors()||tagCloud.isEmpty()){
			cur_page_num = 0;
			//common.CommonAttributes.addErrorMessage("form_errors", request);

			request.setAttribute(config.getContentDataAttribute(), new TagCloudData(wallpaperService));
			url.setContent(searchUrl);
		}else{
			final String[] props = new String[]{"active", "tags"};
			final String[][] relations = new String[][]{new String[]{"="}, new String[]{"like", "like", "like", "like"}};
			final Object[][] values = new Object[][]{new Object[]{Boolean.TRUE}, tagCloud.getTagsLike()};
			int totalCount =((Long)
					wallpaperService.getSinglePropertyU("count(*)", props, relations, values, 0, null, null)).intValue();
			cur_page_num = PaginatedListUtils.getPageNumber(request);
			PagerBean count = paginationUtils.getPagerBean2(cur_page_num, totalCount,
					keepParameters.getKeepParameters(request));

			int first_item = count.getCurrentPage()*paginationUtils.getItemsPerPage();
			List<Wallpaper> wallpapers = wallpaperService.getByPropertiesValuesPortionOrdered(null, null,
					props, relations, values, first_item, paginationUtils.getItemsPerPage(), null, null);

			tagCloud.setData(wallpapers);
			tagCloud.setPager(count);

			request.setAttribute(config.getContentDataAttribute(), tagCloud);

			request.setAttribute(OPTIMIZATION_STRING,
				getOptimizationPhraze(cur_page_num, Utils.getNavigation(request, super.config)));
			url.setContent(contentUrl);
			url.setPage_top(infoTopUrl);
			url.setPage_bottom(infoBottomUrl);
			url.setOptimization(optimizationUrl);
		}
	}

    public static final String[] OPTIMIZATION_WHERE = new String[]{"id_pages", "useInPages"};
	public String getOptimizationPhraze(int cur_page, List<Pages> navigation){
		if (navigation!=null&&navigation.size()>1){
			//getting pre last item
			Long id = navigation.get(navigation.size()-2).getId();
			Long count = optimizationService.getRowCount(OPTIMIZATION_WHERE, new Object[]{id, Boolean.TRUE});
			if (count>0){
				int page_num = cur_page % count.intValue();
				return (String)optimizationService.getSinglePropertyUOrdered("text", OPTIMIZATION_WHERE, new Object[]{id, Boolean.TRUE}, page_num);
			}
		}
		return null;
	}

	public void setPagesPseudonymService(IPagesPseudonymService service) {this.optimizationService = service;}
	public void setKeepParameters(KeepParameters keepParameters) {this.keepParameters = keepParameters;}
	public void setBindValidator(ABindValidator bindValidator) {this.bindValidator = bindValidator;}

	public void setContentUrl(String contentUrl) {this.contentUrl = contentUrl;}
	public void setSearchUrl(String value) {this.searchUrl = value;}
	public void setOptimizationUrl(String optimizationUrl) {this.optimizationUrl = optimizationUrl;}
	public void setInfoTopUrl(String infoTopUrl) {this.infoTopUrl = infoTopUrl;}
	public void setInfoBottomUrl(String infoBottomUrl) {this.infoBottomUrl = infoBottomUrl;}

}
