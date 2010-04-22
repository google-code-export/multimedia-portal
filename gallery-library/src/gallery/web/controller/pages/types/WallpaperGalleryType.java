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
import gallery.model.beans.Photo;
import gallery.service.pages.IPagesService;
import gallery.service.pagesPseudonym.IPagesPseudonymService;
import gallery.web.controller.pages.submodules.EmptySubmodule;
import gallery.web.controller.pages.submodules.ASubmodule;
import gallery.web.support.PaginatedListUtils;
import gallery.web.support.pages.Utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class WallpaperGalleryType extends AWallpaperType{
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
	public static final String PHOTOS_ATTRIBUTE = "photos";
	/** attribute for storing categories vector for view */
	public static final String PAGENATION_ATTRIBUTE = "count";
	public static final String CUR_PHOTO_ATTRIBUTE = "big_photo";
	public static final String CUR_PAGE_NUMBER_ATTRIBUTE = "cur_page_number";
	public static final String OPTIMIZATION_STRINGS_FULL = "wallpaper_optmization_full";
	public static final String OPTIMIZATION_STRINGS_SHORT = "wallpaper_optmization_short";
	public static final String OPTIMIZATION_STRINGS_TAGS = "wallpaper_optmization_tags";
	/** details (after clicking on a photo of a category) */
	public static final int DETAILS_NAV_BUTTONS = 2;
    /** quantity of wallpapers(items) that will be showen in category */
    public static final int CATEGORY_WALLPAPERS = 15;
	/** main view of a category */
	public static final PaginatedListUtils paginatedListUtilsCategory = new PaginatedListUtils(CATEGORY_WALLPAPERS ,2);
	//first and last photo
	private Long FIRST_PHOTO_PARAM = new Long(-1);
	private Long LAST_PHOTO_PARAM = new Long(-2);

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

	/** if has no child pages and an id_photo param is passed */
	protected String contentDetailsUrl;
	protected String optimizationDetailsUrl;

	/** if has no child pages and no id_photo param is passed */
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
			Long id_photo = RequestUtils.getLongParam(request, "id_photo_nav");
			int totalCount = photoService.getPhotosRowCount(p.getId()).intValue();
			if (id_photo==null){
				category(request, response, p, navigation, totalCount, url);
			}else{
				details(request, response, id_photo, p, navigation, totalCount, url);
				Map<String, ASubmodule> hs = url.getSubmodules();
				hs = (hs==null?new HashMap():hs);
				hs.put(PhotoRateType.TYPE, new EmptySubmodule());
				hs.put(PhotoCommentAddType.TYPE, new EmptySubmodule());
				url.setSubmodules(hs);
			}
		}
	}

	public void main(HttpServletRequest request, HttpServletResponse response, List<Pages> categories, UrlBean url)
		throws Exception
	{
		request.setAttribute(CATEGORIES_ATTRIBUTE, categories);
		request.setAttribute(PHOTOS_ATTRIBUTE, rubricImageService.getImageUrls(categories));

		url.setContent(contentMainUrl);
		url.setOptimization(optimizationMainUrl);
        url.setPage_top(infoTopMainUrl);
        url.setPage_bottom(infoBottomMainUrl);
	}

	public static final String[] DET_RES_WHERE = new String[]{"width", "height"};
	public static final String[] DET_RES_RELAT = new String[]{"<=", "<="};
	public void details(HttpServletRequest request,javax.servlet.http.HttpServletResponse response,
			Long id_photo, Pages p, List<Pages> navigation, int totalCount, UrlBean url)
	{
		//creating bean for pagination
		PagerBeanId count = new PagerBeanId();
		count.setCurPageParam("id_photo_nav");
		count.setItemsCount(totalCount);
		count.setQueryString(detailsKeepParameters.getKeepParameters(request));
		//finding an id_photo if it is first or last page
		Long id_photo2;
		if (id_photo.equals(FIRST_PHOTO_PARAM)){
			id_photo2 = (Long)photoService.getSinglePropertyU("min(id)", DETAILS_WHERE, new Object[]{p.getId(),Boolean.TRUE}, 0);
		} else if (id_photo.equals(LAST_PHOTO_PARAM)){
			id_photo2 = (Long)photoService.getSinglePropertyU("max(id)", DETAILS_WHERE, new Object[]{p.getId(),Boolean.TRUE}, 0);
		} else {
			id_photo2 = id_photo;
		}
		Photo cur_photo = photoService.getById(id_photo2==null?id_photo:id_photo2);
		if ((cur_photo==null)||(!cur_photo.getId_pages().equals(p.getId()))){
			//and set an error if this photo not exists or is not from this pages
			CommonAttributes.addErrorMessage("not_exists.photo", request);
			category(request, response, p, navigation, totalCount, url);
			return;
		}
		//setting resolutions to be avaible with current photo
		List res = resolutionService.getByPropertiesValuePortionOrdered(null, null,
				DET_RES_WHERE, DET_RES_RELAT, new Object[]{cur_photo.getWidth(), cur_photo.getHeight()},
				0, 0, gallery.web.controller.resolution.Config.ORDER_BY, gallery.web.controller.resolution.Config.ORDER_HOW);
		cur_photo.setResolutions(res);
		//get page of current photo
		Long cur_number = photoService.getPhotoNumber(cur_photo);
		request.setAttribute(CUR_PAGE_NUMBER_ATTRIBUTE, (cur_number-1)/paginatedListUtilsCategory.getPageSize());

		//selecting left and right phoytos for pagination
		List<Photo> photos_left = photoService.getPhotosPaginatedId(cur_photo.getId(), -DETAILS_NAV_BUTTONS, p.getId());
		List<Photo> photos_right = photoService.getPhotosPaginatedId(cur_photo.getId(), DETAILS_NAV_BUTTONS, p.getId());

		//setting photos collection
		Vector<Photo> photos = new Vector(2*DETAILS_NAV_BUTTONS+1);
		for (int i=0, size=DETAILS_NAV_BUTTONS-photos_left.size();i<size;i++)	photos.add(null);
		//reverting collection ...
		for (int k=photos_left.size();k>0;)		photos.add(photos_left.get(--k));
		photos.add(cur_photo);
		photos.addAll(photos_right);
		for (int i=0, size=DETAILS_NAV_BUTTONS-photos_right.size();i<size;i++)	photos.add(null);
		//setting navigation buttons
		if (photos_left.size()>0)	count.setPrevPage(photos_left.get(0).getId());
		else						count.setPrevPage(LAST_PHOTO_PARAM);
		if (photos_right.size()>0)	count.setNextPage(photos_right.get(0).getId());
		else						count.setNextPage(FIRST_PHOTO_PARAM);
		count.setCurrentPage(cur_photo.getId());
		count.setLastPage(LAST_PHOTO_PARAM);
		count.setFirstPage(FIRST_PHOTO_PARAM);
		//saving photos in request attributes
		request.setAttribute(CUR_PHOTO_ATTRIBUTE, cur_photo);
		request.setAttribute(PHOTOS_ATTRIBUTE, photos);
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

		int first_item = count.getCurrentPage()*paginatedListUtilsCategory.getPageSize();
		List<Photo> photos = photoService.getPhotosPaginated(first_item,
				//paginatedListUtilsCategory.getPageSize(), subpages);
				paginatedListUtilsCategory.getPageSize(), p.getId());

		request.setAttribute(PHOTOS_ATTRIBUTE, photos);
		request.setAttribute(PAGENATION_ATTRIBUTE,count);
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
