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

import common.CommonAttributes;
import common.beans.KeepParameters;
import common.beans.PagerBean;
import common.beans.PagerBeanId;
import common.utils.RequestUtils;
import core.service.IRubricImageService;
import gallery.model.beans.Pages;
import gallery.model.beans.Resolution;
import gallery.model.beans.Wallpaper;
import gallery.service.pages.IPagesService;
import gallery.service.pagesPseudonym.IPagesPseudonymService;
import gallery.web.controller.pages.submodules.EmptySubmodule;
import gallery.web.controller.pages.submodules.ASubmodule;
import com.multimedia.web.support.PaginatedListUtils;
import gallery.web.support.pages.Utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperGalleryType extends AWallpaperType{
	Logger logger = Logger.getLogger(this.getClass());
    /** string constant that represents type for this page */
    public static final String TYPE="general_wallpaper_gallery";
	/** rus type */
	public static final String TYPE_RU="Wallpapers(галерея)";

	@Override
	public String getType() {return TYPE;}
	@Override
	public String getTypeRu() {return TYPE_RU;}

	/** attribute for storing categories vector for view */
	public static final String CATEGORIES_ATTRIBUTE = "categories";
	/** attribute for storing categories vector for view */
	public static final String WALLPAPERS_ATTRIBUTE = "wallpapers";
	/** attribute for storing categories vector for view */
	public static final String PAGENATION_ATTRIBUTE = "count";
	public static final String CUR_WALLPAPER_ATTRIBUTE = "big_wallpaper";
	public static final String CUR_PAGE_NUMBER_ATTRIBUTE = "cur_page_number";
	public static final String OPTIMIZATION_STRINGS_FULL = "wallpaper_optmization_full";
	public static final String OPTIMIZATION_STRINGS_SHORT = "wallpaper_optmization_short";
	public static final String OPTIMIZATION_STRINGS_TAGS = "wallpaper_optmization_tags";
	/** details (after clicking on a wallpaper of a category) */
	public static final int DETAILS_NAV_BUTTONS = 2;
    /** quantity of wallpapers(items) that will be showen in category */
    public static final int CATEGORY_WALLPAPERS = 15;
	/** main view of a category */
	public static final PaginatedListUtils paginatedListUtilsCategory = new PaginatedListUtils(CATEGORY_WALLPAPERS ,2);
	//first and last wallpaper
	private Long FIRST_PARAM = Long.valueOf(-1);
	private Long LAST_PARAM = Long.valueOf(-2);

	/** for keeping parameters */
	private KeepParameters detailsKeepParameters;
	private KeepParameters categoryKeepParameters;
	// service for working with data
	private IPagesService pagesService;
	private IPagesPseudonymService optimizationService;
    private IRubricImageService rubricImageService;


	/** if pages contatains a child page */
	protected String contentMainUrl;
	protected String optimizationMainUrl;
	protected String infoTopMainUrl;
	protected String infoBottomMainUrl;

	/** if has no child pages and an wallpaper param is passed */
	protected String contentDetailsUrl;
	protected String optimizationDetailsUrl;

	/** if has no child pages and no wallpaper param is passed */
	protected String contentCategoryUrl;
	protected String optimizationCategoryUrl;
	protected String infoTopCategoryUrl;
	protected String infoBottomCategoryUrl;

	@Override
    public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(contentMainUrl, "contentMainUrl", sb);
		common.utils.MiscUtils.checkNotNull(optimizationMainUrl, "optimizationMainUrl", sb);
		common.utils.MiscUtils.checkNotNull(infoTopMainUrl, "infoTopMainUrl", sb);
		common.utils.MiscUtils.checkNotNull(infoBottomMainUrl, "infoBottomMainUrl", sb);

		common.utils.MiscUtils.checkNotNull(contentDetailsUrl, "contentDetailsUrl", sb);
		common.utils.MiscUtils.checkNotNull(optimizationDetailsUrl, "optimizationDetailsUrl", sb);

		common.utils.MiscUtils.checkNotNull(contentCategoryUrl, "contentCategoryUrl", sb);
		common.utils.MiscUtils.checkNotNull(optimizationCategoryUrl, "optimizationCategoryUrl", sb);
		common.utils.MiscUtils.checkNotNull(infoTopCategoryUrl, "infoTopCategoryUrl", sb);
		common.utils.MiscUtils.checkNotNull(infoBottomCategoryUrl, "infoBottomCategoryUrl", sb);

		common.utils.MiscUtils.checkNotNull(detailsKeepParameters, "detailsKeepParameters", sb);
		common.utils.MiscUtils.checkNotNull(categoryKeepParameters, "categoryKeepParameters", sb);
		common.utils.MiscUtils.checkNotNull(pagesService, "pagesService", sb);
		common.utils.MiscUtils.checkNotNull(optimizationService, "optimizationService", sb);
		common.utils.MiscUtils.checkNotNull(rubricImageService, "rubricImageService", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
    }

	public static final String[] DETAILS_WHERE = new String[]{"id_pages","active"};
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response, UrlBean url)
		throws Exception
	{
		Pages p = Utils.getCurrentPage(request);

		List<Pages> categories = pagesService.getCategories(p.getId(), TYPE);
        List<Pages> navigation = Utils.getNavigation(request, super.config);

		if (categories!=null&&categories.size()>0){
            setOptimization(0, navigation, request);
			main(request, response, categories, url);
		} else {
			//determining if we need details ...
			Long id_wallpaper = RequestUtils.getLongParam(request, "id_photo_nav");
			int totalCount = wallpaperService.getWallpapersRowCount(p.getId()).intValue();
			if (id_wallpaper==null){
				//logger.info("Pages --> category; id="+p.getId()+"; count = "+totalCount);
				category(request, response, p, navigation, totalCount, url);
			}else{
				//logger.info("Pages --> details; id="+p.getId()+"; count = "+totalCount);
				details(request, response, id_wallpaper, p, navigation, totalCount, url);
				Map<String, ASubmodule> hs = url.getSubmodules();
				hs = (hs==null?new HashMap<String, ASubmodule>():hs);
				hs.put(WallpaperRateType.TYPE, new EmptySubmodule());
				hs.put(WallpaperCommentAddType.TYPE, new EmptySubmodule());
				url.setSubmodules(hs);
			}
		}
	}

	public void main(HttpServletRequest request, HttpServletResponse response, List<Pages> categories, UrlBean url)
		throws Exception
	{
		request.setAttribute(CATEGORIES_ATTRIBUTE, categories);
		request.setAttribute(WALLPAPERS_ATTRIBUTE, rubricImageService.getImageUrls(categories));

		url.setContent(contentMainUrl);
		url.setOptimization(optimizationMainUrl);
        url.setPage_top(infoTopMainUrl);
        url.setPage_bottom(infoBottomMainUrl);
	}

	public static final String[] DET_RES_WHERE = new String[]{"width", "height"};
	public static final String[] DET_RES_RELAT = new String[]{"<=", "<="};
	public void details(HttpServletRequest request,javax.servlet.http.HttpServletResponse response,
			Long id_wallpaper, Pages p, List<Pages> navigation, int totalCount, UrlBean url)
	{
		//creating bean for pagination
		PagerBeanId count = new PagerBeanId();
		count.setCurPageParam("id_photo_nav");
		count.setItemsCount(totalCount);
		count.setQueryString(detailsKeepParameters.getKeepParameters(request));
		//finding an id_wallpaper if it is first or last page
		Long id_wallpaper2;
		if (id_wallpaper.equals(FIRST_PARAM)){
			id_wallpaper2 = (Long)wallpaperService.getSinglePropertyU("min(id)", DETAILS_WHERE, new Object[]{p.getId(),Boolean.TRUE}, 0);
		} else if (id_wallpaper.equals(LAST_PARAM)){
			id_wallpaper2 = (Long)wallpaperService.getSinglePropertyU("max(id)", DETAILS_WHERE, new Object[]{p.getId(),Boolean.TRUE}, 0);
		} else {
			id_wallpaper2 = id_wallpaper;
		}
		Wallpaper cur_wallpaper = wallpaperService.getById(id_wallpaper2==null?id_wallpaper:id_wallpaper2);
		if ((cur_wallpaper==null)||(!cur_wallpaper.getId_pages().equals(p.getId()))){
			//and set an error if this wallpaper not exists or is not from this pages
			CommonAttributes.addErrorMessage("not_exists.wallpaper", request);
			category(request, response, p, navigation, totalCount, url);
			return;
		}
		//setting resolutions to be avaible with current wallpaper
		List<Resolution> res = resolutionService.getByPropertiesValuePortionOrdered(null, null,
				DET_RES_WHERE, DET_RES_RELAT, new Object[]{cur_wallpaper.getWidth(), cur_wallpaper.getHeight()},
				0, 0, gallery.web.controller.resolution.Config.ORDER_BY, gallery.web.controller.resolution.Config.ORDER_HOW);
		cur_wallpaper.setResolutions(res);
		wallpaperService.updatePropertyById("views", Long.valueOf(1), id_wallpaper2);
		//get page of current wallpaper
		Long cur_number = wallpaperService.getWallpaperNumber(cur_wallpaper);
		request.setAttribute(CUR_PAGE_NUMBER_ATTRIBUTE, (cur_number-1)/paginatedListUtilsCategory.getItemsPerPage());

		//selecting left and right wallpapers for pagination
		List<Wallpaper> wallpapers_left = wallpaperService.getWallpapersPaginatedId(cur_wallpaper.getId(), -DETAILS_NAV_BUTTONS, p.getId());
		List<Wallpaper> wallpapers_right = wallpaperService.getWallpapersPaginatedId(cur_wallpaper.getId(), DETAILS_NAV_BUTTONS, p.getId());

		//setting wallpapers collection
		Vector<Wallpaper> wallpapers = new Vector<Wallpaper>(2*DETAILS_NAV_BUTTONS+1);
		for (int i=0, size=DETAILS_NAV_BUTTONS-wallpapers_left.size();i<size;i++)	wallpapers.add(null);
		//reverting collection ...
		for (int k=wallpapers_left.size();k>0;)		wallpapers.add(wallpapers_left.get(--k));
		wallpapers.add(cur_wallpaper);
		wallpapers.addAll(wallpapers_right);
		for (int i=0, size=DETAILS_NAV_BUTTONS-wallpapers_right.size();i<size;i++)	wallpapers.add(null);
		//setting navigation buttons
		if (wallpapers_left.size()>0)	count.setPrevPage(wallpapers_left.get(0).getId());
		else						count.setPrevPage(LAST_PARAM);
		if (wallpapers_right.size()>0)	count.setNextPage(wallpapers_right.get(0).getId());
		else						count.setNextPage(FIRST_PARAM);
		count.setCurrentPage(cur_wallpaper.getId());
		count.setLastPage(LAST_PARAM);
		count.setFirstPage(FIRST_PARAM);
		//saving wallpapers in request attributes
		request.setAttribute(CUR_WALLPAPER_ATTRIBUTE, cur_wallpaper);
		request.setAttribute(WALLPAPERS_ATTRIBUTE, wallpapers);
		request.setAttribute(PAGENATION_ATTRIBUTE, count);
		//creating url bean
		url.setContent(contentDetailsUrl);
		url.setOptimization(optimizationDetailsUrl);
	}

	public void category(HttpServletRequest request,javax.servlet.http.HttpServletResponse response,
			Pages p, List<Pages> navigation, int totalCount, UrlBean url)
	{
		int cur_page_num = PaginatedListUtils.getPageNumber(request);
		PagerBean count = paginatedListUtilsCategory.getPagerBean2(cur_page_num, totalCount,
				categoryKeepParameters.getKeepParameters(request));

		int first_item = count.getCurrentPage()*paginatedListUtilsCategory.getItemsPerPage();
		List<Wallpaper> wallpapers = wallpaperService.getWallpapersPaginated(first_item,
				//paginatedListUtilsCategory.getPageSize(), subpages);
				paginatedListUtilsCategory.getItemsPerPage(), p.getId());

		request.setAttribute(WALLPAPERS_ATTRIBUTE, wallpapers);
		request.setAttribute(PAGENATION_ATTRIBUTE, count);
        setOptimization(cur_page_num, navigation, request);
		url.setContent(contentCategoryUrl);
		if (cur_page_num==0){
			url.setOptimization(optimizationMainUrl);
		}else{
			url.setOptimization(optimizationCategoryUrl);
		}
        url.setPage_top(infoTopCategoryUrl);
        url.setPage_bottom(infoBottomCategoryUrl);
	}

    public static final String[] OPTIMIZATION_WHERE = new String[]{"id_pages", "useInPages"};
	/**
	 * get optimization phrazes
	 * @param cur_page cur page in pagination (0, if no pagination)
	 * @param navigation navigation chain for current wallpapers page
	 * @param request
	 * @return
	 */
	protected List<String> setOptimization(int cur_page, List<Pages> navigation, HttpServletRequest request){
		if (navigation==null) return null;
		//1-st get count of optimizaton for each parent page
		Vector<String> rez = new Vector<String>(navigation.size());
		for (Pages p: navigation){
			Long count = optimizationService.getRowCount(OPTIMIZATION_WHERE, new Object[]{p.getId(),Boolean.TRUE});
			if (count>0){
				int page_num = cur_page % count.intValue();
				String text = (String)optimizationService.getSinglePropertyUOrdered("text", OPTIMIZATION_WHERE, new Object[]{p.getId(),Boolean.TRUE}, page_num);
				if (text!=null&&!text.equals("")){
					rez.add(text);
				}
			}
		}
		if (rez.size()>0){
            StringBuilder full = new StringBuilder(rez.get(0));
            StringBuilder tags = new StringBuilder(rez.get(0));
            for (int i=1;i<rez.size();i++){
                full.append(" - ");
                full.append(rez.get(i));
				tags.append(", ");
                tags.append(rez.get(i));
            }
            request.setAttribute(OPTIMIZATION_STRINGS_FULL, full.toString());
            request.setAttribute(OPTIMIZATION_STRINGS_TAGS, tags.toString());
            request.setAttribute(OPTIMIZATION_STRINGS_SHORT, rez.lastElement());
			return rez;
		} else {
			return null;
		}
	}

	public void setPagesService(IPagesService service) {this.pagesService = service;}
	public void setPagesPseudonymService(IPagesPseudonymService service) {this.optimizationService = service;}
	public void setRubricImageService(IRubricImageService service) {this.rubricImageService = service;}

	public void setDetailsKeepParameters (KeepParameters value) {this.detailsKeepParameters = value;}
	public void setCategoryKeepParameters(KeepParameters value) {this.categoryKeepParameters = value;}

	public void setContentMainUrl(String contentMainUrl) {this.contentMainUrl = contentMainUrl;}
	public void setContentCategoryUrl(String contentCategoryUrl) {this.contentCategoryUrl = contentCategoryUrl;}
	public void setContentDetailsUrl(String contentDetailsUrl) {this.contentDetailsUrl = contentDetailsUrl;}
	public void setOptimizationMainUrl(String optimizationMainUrl) {this.optimizationMainUrl = optimizationMainUrl;}
	public void setInfoTopMainUrl(String infoTopMainUrl) {this.infoTopMainUrl = infoTopMainUrl;}
	public void setInfoBottomMainUrl(String infoBottomMainUrl) {this.infoBottomMainUrl = infoBottomMainUrl;}
	public void setOptimizationDetailsUrl(String optimizationDetailsUrl) {this.optimizationDetailsUrl = optimizationDetailsUrl;}
	public void setOptimizationCategoryUrl(String optimizationCategoryUrl) {this.optimizationCategoryUrl = optimizationCategoryUrl;}
	public void setInfoTopCategoryUrl(String infoTopCategoryUrl) {this.infoTopCategoryUrl = infoTopCategoryUrl;}
	public void setInfoBottomCategoryUrl(String infoBottomCategoryUrl) {this.infoBottomCategoryUrl = infoBottomCategoryUrl;}
}
