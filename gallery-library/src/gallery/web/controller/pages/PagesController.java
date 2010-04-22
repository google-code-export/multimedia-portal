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

package gallery.web.controller.pages;

import common.CommonAttributes;
import common.services.generic.ISingletonInstanceService;
import gallery.model.beans.Pages;
import core.model.beans.SiteConfig;
import gallery.service.autoreplace.IAutoreplaceService;
import gallery.service.pages.IPagesService;
import gallery.service.pages.IPagesServiceView;
import gallery.service.pages.IRubricationService;
import gallery.web.controller.pages.submodules.EmptySubmodule;
import gallery.web.controller.pages.submodules.ASubmodule;
import gallery.web.controller.pages.types.APagesType;
import gallery.web.controller.pages.types.IPagesType;
import gallery.web.controller.pages.types.UrlBean;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PagesController implements Controller{
	protected final Logger logger = Logger.getLogger(getClass());
	/** config class is used to store some constants */
	protected Config config;
	/** types of pages that will be handled */
	protected HashMap<String,IPagesType> types;
	/** service for working with pages */
	protected IPagesService pagesService;
	/** service for autoreplacement */
	protected IAutoreplaceService autoreplaceService;
	/** view service for pages */
	protected IPagesServiceView pagesViewService;
	/** for site config */
	private ISingletonInstanceService<SiteConfig> siteConfigService;

	protected String default_optimization_url;
	protected String default_navigation_url;

    protected IRubricationService rubrication_service;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(config, "config", sb);
		common.utils.MiscUtils.checkNotNull(types, "types", sb);
		common.utils.MiscUtils.checkNotNull(pagesService, "pagesService", sb);
		common.utils.MiscUtils.checkNotNull(autoreplaceService, "autoreplaceService", sb);
		common.utils.MiscUtils.checkNotNull(pagesViewService, "pagesViewService", sb);
		common.utils.MiscUtils.checkNotNull(siteConfigService, "siteConfigService", sb);
		common.utils.MiscUtils.checkNotNull(default_optimization_url, "default_optimization_url", sb);
		common.utils.MiscUtils.checkNotNull(default_navigation_url, "default_navigation_url", sb);
		common.utils.MiscUtils.checkNotNull(rubrication_service, "rubrication_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}else{
			//logger.debug("initialized successfully. ");
			//trying to init config for all APagesType subclasses
			Collection<IPagesType> c = types.values();
			for (IPagesType it:c){
				if (it instanceof APagesType){
					((APagesType)it).setConfig(config);
				}
			}
		}
	}

	public static final String COUNTERS_ATTRIBUTE = "counters";

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String lang = RequestContextUtils.getLocaleResolver(request).resolveLocale(request).getLanguage();
		//getting current page
		Pages page = getCurPage(request.getParameter(config.getId_pagesParamName()), request);

		request.setAttribute(Config.CURRENT_PAGE_ATTRIBUTE, page);

		//getting its type
		UrlBean url = null;

		if (page!=null){
			request.setAttribute(config.getSiteConfigAttribute(), siteConfigService.getInstance());
			//getting navigation
			List<Pages> navigation = pagesService.getAllPagesParents(page.getId(), Config.NAVIGATION_PSEUDONYMES);
			request.setAttribute(config.getNavigationDataAttribute(), navigation);

			url = getType(page).execute(request, response);

			//if url == null that meens that we do not need to render any view in thid method
			// and thus we do not need to load any submodules, rubrications etc
			if (url!=null){
				makeReplacement(page, lang);

				loadSubmodules(navigation, request, url.getSubmodules());

				createRubrication(request, page);

				request.setAttribute(COUNTERS_ATTRIBUTE, pagesViewService.getCounters());
			}
		}
		if (url==null){
			return null;
		}else{
			saveUrlBean(request, url);
			return new ModelAndView(config.getTemplateUrl());
		}
	}

	//TODO: remake as random_wallpaper_gallery in WallpaperGalleryType
	public static final Map<String, ASubmodule> COMMON_SUBMODULES;
	static{
		COMMON_SUBMODULES = new HashMap<String, ASubmodule>();
		COMMON_SUBMODULES.put(gallery.web.controller.pages.types.UserType.TYPE, new EmptySubmodule());
		COMMON_SUBMODULES.put(gallery.web.controller.pages.types.ErrorType.TYPE,  new EmptySubmodule());
		COMMON_SUBMODULES.put(gallery.web.controller.pages.types.PhotoAddType.TYPE,  new EmptySubmodule());
	}
	protected void loadSubmodules(List<Pages> navigation, HttpServletRequest request, Map<String, ASubmodule> submodules){
		if (submodules!=null){
			Iterator<Entry<String,ASubmodule>> i = COMMON_SUBMODULES.entrySet().iterator();
			while (i.hasNext()){
				Entry<String,ASubmodule> s = i.next();
				submodules.put(s.getKey(), s.getValue());
			}
		} else {
			submodules = new HashMap<String, ASubmodule>(COMMON_SUBMODULES);
		}
		if (security.Utils.getCurrentUser(request)==null){
			submodules.put(gallery.web.controller.pages.types.RecoverPassType.TYPE,  new EmptySubmodule());
		} else {
			submodules.put(gallery.web.controller.pages.types.PhotoListType.TYPE,  new EmptySubmodule());
		}
		submodules.put(gallery.web.controller.pages.types.EMailSendType.TYPE, new EmptySubmodule());
		pagesService.activateSubmodules(navigation, submodules);
		//TODO: remake
		for (Entry<String,ASubmodule> sub:submodules.entrySet()){
			ASubmodule subm = sub.getValue();
			if (subm.getActive()){
				if (subm.getEmpty()){
					request.setAttribute(subm.getPage().getType()+"_page", subm.getPage());
				}else{
					request.setAttribute(sub.getKey()+"_submodule", sub.getValue());
				}
			}
		}
	}

    protected Pages getCurPage(String id, HttpServletRequest request){
		if (id==null){
			Pages p = pagesViewService.getMainPage();
            pagesService.deattach(p);
			return p;
		}
		Long _id = null;
		try{
			_id = Long.parseLong(id,10);
		}catch(Exception e){
			_id = null;
		}
		if (_id!=null){
			Pages p = pagesService.getById(_id);
            if (p!=null&&p.getActive()){
                pagesService.deattach(p);
                return p;
            }
		}
        CommonAttributes.addErrorMessage("not_exists.page", request);
		Pages p = pagesViewService.getMainPage();
		pagesService.deattach(p);
		return p;
    }

	protected void makeReplacement(Pages p, String lang){
		IAutoreplaceService.IReplacement repl = autoreplaceService.getAllReplacements(lang);
		p.setTitle(repl.replaceAll(p.getTitle()));
		p.setDescription(repl.replaceAll(p.getDescription()));
		p.setKeywords(repl.replaceAll(p.getKeywords()));
		p.setInfo_top(repl.replaceAll(p.getInfo_top()));
		p.setInfo_bottom(repl.replaceAll(p.getInfo_bottom()));
	}

	public static final String RUBRICATOR_ATTRIBUTE = "rubrication";
	protected void createRubrication(HttpServletRequest request, Pages p){
        List<Pages> pages = rubrication_service.getCurrentBranch(p.getId());
		request.setAttribute(RUBRICATOR_ATTRIBUTE, pages);
	}

	/**
	 * checks if navigation bean has an appropriate urls
	 * if not sets defaults
	 * @param r where to set urls
	 * @param url bean from where to get them
	 */
	protected void saveUrlBean(HttpServletRequest r, UrlBean url){
		if (url.getNavigation()==null)
            url.setNavigation(default_navigation_url);
		if (url.getOptimization()==null)
            url.setOptimization(default_optimization_url);
		r.setAttribute(config.getUrlAttribute(), url);
	}

	/**
	 * get type of current page
	 * @param p page
	 * @return type of page
	 */
	protected IPagesType getType(Pages p){
		try{
			return types.get(p.getType());
		}catch(Exception e){
			return null;
		}
	}

	public void setTypes(IPagesType[] types){
		this.types = new HashMap<String, IPagesType>();
		for (IPagesType type:types){
			this.types.put(type.getType(), type);
		}
	}

	public void setPagesService(IPagesService service) {this.pagesService = service;}
	public void setPagesViewService(IPagesServiceView service) {this.pagesViewService = service;}
	public void setAutoreplaceService(IAutoreplaceService service) {this.autoreplaceService = service;}
	public void setConfig(Config config){this.config = config;}
	public void setSiteConfigService(ISingletonInstanceService<SiteConfig> value) {this.siteConfigService = value;}
	public void setRubrication_service(IRubricationService value){this.rubrication_service = value;}

	public void setDefaultOptimizationUrl(String value){this.default_optimization_url = value;}
	public void setDefaultNavigationUrl(String value){this.default_navigation_url = value;}

}
