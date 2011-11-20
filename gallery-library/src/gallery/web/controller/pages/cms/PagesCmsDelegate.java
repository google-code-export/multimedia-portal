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

package gallery.web.controller.pages.cms;

import common.bind.ABindValidator;
import common.utils.RequestUtils;
import core.PagesCmsConfig;
import gallery.model.beans.Pages;
import gallery.service.pages.IPagesService;
import gallery.service.pages.IPagesServiceCms;
import gallery.web.controller.pages.Config;
import com.multimedia.core.pages.types.IPagesType;
import com.multimedia.model.beans.PagesFolder;
import common.utils.FileUtils;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PagesCmsDelegate {
	protected final Logger logger = Logger.getLogger(getClass());
	/** config class is used to store some constants */
	protected PagesCmsConfig config;
	/** types of pages that will be handled */
    protected IPagesType[] types;
	/** types of pages that will be handled */
	private HashMap<String, String> typesRus;
	/** service for working with data */
	protected IPagesService service;
	/** service for working with data */
	protected IPagesServiceCms service_cms;

	protected ABindValidator insertValidator;
	protected ABindValidator updateValidator;

	protected String showUrl;
	protected String insertUrl;
	protected String updateUrl;
	protected String relocateUrl;
	protected String megaModuleUrl;
	protected String navigationUrl;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(config, "config", sb);
		common.utils.MiscUtils.checkNotNull(types, "types", sb);
		common.utils.MiscUtils.checkNotNull(typesRus, "typesRus", sb);
		common.utils.MiscUtils.checkNotNull(service, "service", sb);
		common.utils.MiscUtils.checkNotNull(service_cms, "serviceCms", sb);
		common.utils.MiscUtils.checkNotNull(insertValidator, "insertValidator", sb);
		common.utils.MiscUtils.checkNotNull(updateValidator, "updateValidator", sb);
		common.utils.MiscUtils.checkNotNull(showUrl, "showUrl", sb);
		common.utils.MiscUtils.checkNotNull(insertUrl, "insertUrl", sb);
		common.utils.MiscUtils.checkNotNull(updateUrl, "updateUrl", sb);
		common.utils.MiscUtils.checkNotNull(relocateUrl, "relocateUrl", sb);
		common.utils.MiscUtils.checkNotNull(megaModuleUrl, "megaModuleUrl", sb);
		common.utils.MiscUtils.checkNotNull(navigationUrl, "navigationUrl", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	public static final String TYPES_ATTR = "types";
	public ModelAndView doView(HttpServletRequest req, HttpServletResponse resp){
		//logger.fine("do=view");
		HashMap<String, Object> m = getCommonModel(req);
		m.put(config.getContentUrlAttribute(), showUrl);
		m.put(TYPES_ATTR, typesRus);
		Long id_pages = null;
		try{
			String id_pages_temp = req.getParameter(config.getId_pagesParamName());
			if (id_pages_temp==null||id_pages_temp.equals("")){
				id_pages = null;
			}else{
				id_pages = Long.valueOf(id_pages_temp);
			}
			m.put("pages", service.getShortOrderedByPropertyValueCms("id_pages", id_pages));
		}catch(NumberFormatException nfe){
			common.CommonAttributes.addErrorMessage("form_errors", req);
			m.put("pages", service.getShortOrderedByPropertyValueCms("id_pages", null));
			//logger.log(Level.FINE,"id must not be null",nfe);
		}

        return new ModelAndView(config.getTemplateUrl(), m);
    }

	public ModelAndView doInsert(HttpServletRequest req, HttpServletResponse resp/*, Pages command*/){
		HashMap<String, Object> m = getCommonModel(req);
		m.put(config.getContentUrlAttribute(), insertUrl);
		m.put("types", types);
		m.put("editForm_topHeader", "Добавление");

		String action = req.getParameter(gallery.web.controller.Config.ACTION_PARAM_NAME);
		/** bind command */
		Pages command = new Pages();
        BindingResult res = insertValidator.bindAndValidate(command, req);

		if ("insert".equals(action)){
			if (res.hasErrors()){
				//m.putAll(res.getModel());
				m.put(res.MODEL_KEY_PREFIX+"command", res);
				m.put("command", command);
				common.CommonAttributes.addErrorMessage("form_errors", req);
			}else{
				if (command.getPagesFolder()==null||command.getPagesFolder().getName().isEmpty()){
					PagesFolder pf = new PagesFolder();
					pf.setPages(command);
					pf.setName(FileUtils.toTranslit(command.getName()));
					command.setPagesFolder(pf);
				}
				service.save(command);
				m.put("command", command);
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
			}
		}else{
			Long sort = (Long)service.getSinglePropertyU("max(sort)","id_pages",command.getId_pages());
			if (sort==null) sort = Long.valueOf(0);
			else sort++;
			command.setSort(sort);
			m.put("command", command);
		}
		
        return new ModelAndView(config.getTemplateUrl(), m);
    }

	public ModelAndView doUpdate(HttpServletRequest req, HttpServletResponse resp){
		HashMap<String, Object> m = getCommonModel(req);
		m.put(config.getContentUrlAttribute(), updateUrl);
		m.put("types", types);
		m.put("editForm_topHeader", "Редактирование");

		String action = req.getParameter(gallery.web.controller.Config.ACTION_PARAM_NAME);
		boolean b = false;
		//getting pages with an appropriate id
		Pages command;
		try{
			Long id = Long.valueOf(req.getParameter("id"));
			command = service.getById(id);
			if (command==null) command = new Pages();
		}catch(NumberFormatException nfe){
			command = new Pages();
			b = true;
			common.CommonAttributes.addErrorMessage("form_errors", req);
			//logger.log(Level.FINE,"id must not be null",nfe);
		}

		if ("update".equals(action)){
			/** bind command */
            BindingResult res = updateValidator.bindAndValidate(command, req);

			if (res.hasErrors()){
				m.put(res.MODEL_KEY_PREFIX+"command", res);
				if (!b) common.CommonAttributes.addErrorMessage("form_errors", req);
				//logger.fine("hasErrors");
			}else{
				service.save(command);
				//command = new Pages();
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
				//logger.fine("not hasErrors");
			}
		}
		m.put("command", command);

        return new ModelAndView(config.getTemplateUrl(), m);
    }

	public ModelAndView doMultiUpdate(HttpServletRequest req, HttpServletResponse resp){
		//logger.fine("do=multi update");
		String action = req.getParameter("action");
		if ("multiUpdate".equals(action)) {
			PagesCms command = new PagesCms();
			BindingResult res = null;

			ServletRequestDataBinder binder = new ServletRequestDataBinder(command);
			binder.bind(req);
			res = binder.getBindingResult();
			if (res.hasErrors()) {
				common.CommonAttributes.addErrorMessage("form_errors", req);
			//logger.fine("hasErrors");
			} else {
				int rez = service.updateObjectArrayShortById(new String[]{"sort", "active", "last"}, command.getId(), command.getSort(), command.getActive(), command.getLast());
				if (rez > 0) {
					common.CommonAttributes.addHelpMessage("operation_succeed", req);
				//logger.fine("not hasErrors");
				} else {
					common.CommonAttributes.addErrorMessage("operation_fail", req);
				//logger.fine("hasErrors");
				}
			}
		}
		//after updating values shoving all pages(doView)
        return doView(req, resp);
    }

	public ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp){
		//logger.fine("do=delete");
		String action = req.getParameter("action");
		if ("delete".equals(action)) {
			try{
				Long id = Long.valueOf(req.getParameter("id"));
				if (service.deleteById(id)>0){
					common.CommonAttributes.addHelpMessage("operation_succeed", req);
					//logger.fine("not hasErrors");
				}else{
					common.CommonAttributes.addErrorMessage("operation_fail", req);
					//logger.fine("hasErrors");
				}
			}catch(NumberFormatException nfe){
				common.CommonAttributes.addErrorMessage("form_errors", req);
				//logger.log(Level.FINE,"id must not be null",nfe);
			}
		}
        return doView(req, resp);
    }

	/**
	 * recalculate 'last' field in all pages
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doLast(HttpServletRequest req, HttpServletResponse resp){
		String action = req.getParameter("action");
		if ("last".equals(action)) {
			service.recalculateLast(null);
			common.CommonAttributes.addHelpMessage("operation_succeed", req);
		}
        return doView(req, resp);
    }

	/**
	 * recalculate 'active' field in all pages
     * true if a page itself or its child pages contains any elements.
     * false if a page and its child pages contains no elements.
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doReactivate(HttpServletRequest req, HttpServletResponse resp){
		String action = req.getParameter("action");
		if ("reactivate".equals(action)) {
			service_cms.reactivate(null);
			common.CommonAttributes.addHelpMessage("operation_succeed", req);
		}
        return doView(req, resp);
    }

	public ModelAndView doRelocate(HttpServletRequest req, HttpServletResponse resp){
		//logger.fine("do=relocate");
		HashMap<String, Object> m = getCommonModel(req);
		m.put(config.getContentUrlAttribute(), relocateUrl);
		m.put("editForm_topHeader", "Перемещение");

		String action = req.getParameter("action");
		Long id = null;
		try{
			id = Long.valueOf(req.getParameter("id"));
		}catch(Exception nfe){
			common.CommonAttributes.addErrorMessage("form_errors", req);
			//logger.log(Level.FINE,"id must not be null",nfe);
			action = null;
		}
		if ("relocate".equals(action)) {
			Long id_pages = RequestUtils.getLongParam(req, "id_pages");
			if (service.relocatePages(id, id_pages)){
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
			}else{
				common.CommonAttributes.addErrorMessage("operation_fail", req);
				common.CommonAttributes.addErrorMessage("error_recursion", req);
			}
			//logger.fine("not hasErrors");
		}
		m.put("pages", service.getPagesForRelocate(id));

        return new ModelAndView(config.getTemplateUrl(), m);
    }

    public ModelAndView doMegaModule(HttpServletRequest req, HttpServletResponse resp){
		HashMap<String, Object> m = getCommonModel(req);
		req.setAttribute(config.getContentUrlAttribute(), megaModuleUrl);
        req.setAttribute(config.getContentDataAttribute(), service_cms.getCategoriesFull());
        return new ModelAndView(config.getTemplateUrl(),m);
    }

    public ModelAndView doOptimize(HttpServletRequest req, HttpServletResponse resp){
        Long id = common.utils.RequestUtils.getLongParam(req, config.getId_pagesParamName());
		String action = req.getParameter("action");
        if (id!=null&&"optimize".equals(action)){
            service_cms.optimizeCategory(id);
			common.CommonAttributes.addHelpMessage("operation_succeed", req);
        } else {
			common.CommonAttributes.addErrorMessage("operation_fail", req);
        }
        return doMegaModule(req, resp);
    }

    public ModelAndView doResetOptimize(HttpServletRequest req, HttpServletResponse resp){
        Long id = common.utils.RequestUtils.getLongParam(req, config.getId_pagesParamName());
		String action = req.getParameter("action");
        if (id!=null&&"optimize".equals(action)){
            service_cms.resetOptimizationCategory(id, Boolean.FALSE);
			common.CommonAttributes.addHelpMessage("operation_succeed", req);
        } else {
			common.CommonAttributes.addErrorMessage("operation_fail", req);
        }
        return doMegaModule(req, resp);
    }

	public void setConfig(PagesCmsConfig config){this.config = config;}
	public void setService(IPagesService service) {this.service = service;}
	public void setServiceCms(IPagesServiceCms service) {this.service_cms = service;}
	public void setTypes(IPagesType[] types){
		this.types = types;
		this.typesRus = new HashMap<String, String>();
		for (IPagesType type:types){
			this.typesRus.put(type.getType(), type.getTypeRu());
		}
	}

	public HashMap<String, Object> getCommonModel(HttpServletRequest req){
		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("title","Страницы");
		m.put("top_header","Страницы");

		m.put(config.getNavigationUrlAttribute(), navigationUrl);
		Long id_pages_nav = common.utils.RequestUtils.getLongParam(req, config.getId_pagesParamName());
		m.put(config.getNavigationDataAttribute(), service.getAllPagesParents(id_pages_nav, Config.NAVIGATION_PSEUDONYMES));
		return m;
	}

	public void setInsertBindValidator(ABindValidator validator){this.insertValidator = validator;}
	public void setUpdateBindValidator(ABindValidator validator){this.updateValidator = validator;}

	public void setShowUrl(String showUrl) {this.showUrl = showUrl;}
	public void setInsertUrl(String insertUrl) {this.insertUrl = insertUrl;}
	public void setUpdateUrl(String updateUrl) {this.updateUrl = updateUrl;}
	public void setRelocateUrl(String relocateUrl) {this.relocateUrl = relocateUrl;}
	public void setMegaModuleUrl(String megaModuleUrl) {this.megaModuleUrl = megaModuleUrl;}
	public void setNavigationUrl(String navigationUrl) {this.navigationUrl = navigationUrl;}

}
class PagesCms{
	protected Vector<Pages> pages;

	private Long[] id;
	private Long[] sort;
	private Boolean[] active;
	private Boolean[] last;

	public Long[] getId() {return id;}
	public void setId(Long[] id) {this.id = id;}

	public Long[] getSort() {return sort;}
	public void setSort(Long[] sort) {this.sort = sort;}

	public Boolean[] getActive() {return active;}
	public void setActive(Boolean[] active) {this.active = active;}

	public Boolean[] getLast() {return last;}
	public void setLast(Boolean[] value) {this.last = value;}

	public Vector<Pages> getPages() {
		if (sort==null||id==null||active==null||last==null){
			return null;
		}else{
			pages = new Vector<Pages>();
			int size = sort.length;
			if (id.length<size){
				size=id.length;
			}
			if (active.length<size){
				size=active.length;
			}
			if (last.length<size){
				size=last.length;
			}
			for (int i=0;i<size;i++){
				Pages p= new Pages();
				p.setActive(active[i]);
				p.setId(id[i]);
				p.setSort(sort[i]);
				p.setLast(last[i]);
				pages.add(p);
			}
			return pages;
		}
	}
}
