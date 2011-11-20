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

package com.multimedia.web.controller.wallpaper;

import com.multimedia.config.cms.wallpaper.IWallpaperCmsConfig;
import com.multimedia.service.wallpaper.ICmsWallpaperService;
import common.beans.MultiObjectCommand;
import core.cms.controller2.AGenericPagesCmsController;
import gallery.model.beans.Wallpaper;
import gallery.model.command.MultiInsertWallpaperCms;
import gallery.model.misc.StatusBean;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Controller(value="wallpaperCmsController")
public class CmsController extends AGenericPagesCmsController<Wallpaper> implements ServletContextAware{
	protected final List<String> filterProperties = java.util.Arrays.asList("id_pages_nav");//TODO: add id_users here mb ...
	protected final List<String> filterAliases = java.util.Arrays.asList("this.id_pages");

	protected IWallpaperCmsConfig wallpaper_config;
	protected ICmsWallpaperService wallpaper_service;

	protected ServletContext servletContext;

	@Override
	public void init() {
		super.init();
		StringBuilder sb = new StringBuilder();

		common.utils.MiscUtils.checkNotNull(wallpaper_config, "wallpaper_config", sb);
		common.utils.MiscUtils.checkNotNull(wallpaper_service, "wallpaper_service", sb);
		common.utils.MiscUtils.checkNotNull(servletContext, "servletContext", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	//-------------------------------------- request extra methods for wallpaper cms controller ---

	@RequestMapping(params="do=prepare")
	public String doPrepare(Map<String, Object> model){
		getKeepParameters(model);
		model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getPrepareUrl());
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params="do=get_wallpaper")
	public String doAjaxGetWallpaper(Map<String, Object> model, @RequestParam(value="id", required=false) String id){
		logger.debug("do=get_wallpaper");
		Long _id = common.utils.StringUtils.getLong(id);
		if (_id==null){
			_id = (Long)model.get("id_pages_nav");
			//logger.info("getting wallpapers from page="+id);
			if (_id!=null){
				model.put(wallpaper_config.getContentDataAttribute(), wallpaper_service.getWallpapersInCategory(_id));
			}
			return wallpaper_config.getAjaxListUrl();
		} else {
			model.put(wallpaper_config.getContentDataAttribute(), wallpaper_service.getUpdateBean(_id));
			return wallpaper_config.getAjaxWallpaperUrl();
		}
	}

	@RequestMapping(params={"do=ajax_update","action=ajax_update"})
	public String doAjaxUpdate(Map<String, Object> model, @Valid Wallpaper obj, BindingResult res){
		logger.debug("do=ajax_update; action=ajax_update");
		if (res.hasErrors()){
			model.put(wallpaper_config.getContentDataAttribute(), "0");
		} else {
			if (wallpaper_service.update(obj, "name", "description", "title", "tags", "id_pages", "active", "optimized", "optimized_manual")==1){
				model.put(wallpaper_config.getContentDataAttribute(), "1");
			} else {
				model.put(wallpaper_config.getContentDataAttribute(), "0");
			}
		}
		return wallpaper_config.getAjaxRespUrl();
	}

	@RequestMapping(params="do=view_onlyImages")
	public String doAjaxView(Map<String, Object> model){
		super.doView(model);
		model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getViewOnlyImgUrl());
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params="do=updateAjaxForm")
	public String doUpdateAjaxForm(Map<String, Object> model){
		logger.debug("do=updateAjaxForm");
		getKeepParameters(model);
		wallpaper_service.initUpdate(model);

		model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getUpdateAjaxForm());
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params="do=insertMulti")
	public String doInsertMultiPrepare(Map<String, Object> model, @RequestParam(value="id_pages_one", required=false) Long id_pages_one){
		logger.debug("do=insertMulti");
		getKeepParameters(model);
		wallpaper_service.initInsert(model);

		model.put(wallpaper_config.getContentDataAttribute(), new MultiInsertWallpaperCms(id_pages_one));
		model.put("empty_object", new Wallpaper());
		model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getInsertMultiFormUrl());
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params={"do=insertMulti", "action=insertMulti"})
	public String doInsertMulti(Map<String, Object> model, @ModelAttribute("multiInsert") MultiObjectCommand<Wallpaper> command, BindingResult res, @RequestParam(value="id_pages_one", required=false) Long id_pages_one){
		logger.debug("do=insertMulti; action=insertMulti");

		if (res.hasErrors()){
			common.CommonAttributes.addErrorMessage("form_errors", model);
		} else {
			int count = wallpaper_service.insert(command.getData(), id_pages_one);
			if (count>0){
				model.put("wallpaper_count", Integer.valueOf(count));
				model.put("wallpaper_quantity", Integer.valueOf(command.getData().size()));
				common.CommonAttributes.addHelpMessage("operation_succeed", model);
			} else {
				common.CommonAttributes.addErrorMessage("operation_fail", model);
			}
		}

		return doInsertMultiPrepare(model, id_pages_one);
	}

	@RequestMapping(params={"do=ajax_insert","action=ajax_insert"})
	public String doAjaxInsert(Map<String, Object> model, @Valid Wallpaper obj, BindingResult res){
		logger.debug("do=ajax_insert; action=ajax_insert");
		if (res.hasErrors()){
			model.put(wallpaper_config.getContentDataAttribute(), "0");
		} else {
			if (wallpaper_service.insert(obj)){
				model.put(wallpaper_config.getContentDataAttribute(), "1");
			} else {
				model.put(wallpaper_config.getContentDataAttribute(), "0");
			}
		}
		return wallpaper_config.getAjaxRespUrl();
	}

	@RequestMapping(params="do=upload")
	public String doUpload(Map<String, Object> model){
		logger.debug("do=upload");
        Object o = servletContext.getAttribute(wallpaper_config.getUploadAttributeName());

        if (o==null){
			//upload not started
            model.put(wallpaper_config.getContentDataAttribute(), wallpaper_service.createPageFolders());
			model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getUploadPrepareUrl());
        } else {
			//upload in progress -- show progress
            common.CommonAttributes.addHelpMessage("operation_in_progress", model);
			model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getUploadProcessUrl());
        }
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params={"do=upload", "action=upload"})
	public String doUploadStart(Map<String, Object> model, @RequestParam(value="id", required=false) Long id){
		logger.debug("do=upload; action=upload");
        Object o = servletContext.getAttribute(wallpaper_config.getUploadAttributeName());

        if (o==null){
			//start upload
			StatusBean usb = new StatusBean();
			servletContext.setAttribute(wallpaper_config.getUploadAttributeName(), usb);
			try{
				wallpaper_service.uploadWallpapers(null, id, usb);
				common.CommonAttributes.addHelpMessage("operation_succeed", model);
			}finally{
				servletContext.removeAttribute(wallpaper_config.getUploadAttributeName());
			}
			model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getPrepareUrl());
		} else {
			//upload in progress -- show progress
            common.CommonAttributes.addHelpMessage("operation_in_progress", model);
			model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getUploadProcessUrl());
		}
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params="do=pre_upload")
	public String doPreUpload(Map<String, Object> model){
		logger.debug("do=pre_upload");
        Object o = servletContext.getAttribute(wallpaper_config.getUploadAttributeName());

        if (o==null){
			//start upload prepare
			StatusBean usb = new StatusBean();
			servletContext.setAttribute(wallpaper_config.getUploadAttributeName(), usb);
			try{
				wallpaper_service.preUploadWallpapers(usb);
				common.CommonAttributes.addHelpMessage("operation_succeed", model);
			}finally{
				servletContext.removeAttribute(wallpaper_config.getUploadAttributeName());
			}
			model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getPrepareUrl());
        } else {
			//upload in progress -- show progress
            common.CommonAttributes.addHelpMessage("operation_in_progress", model);
			model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getUploadProcessUrl());
        }
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params="do=wallpaper_resolution")
	public String doRenewResolution(Map<String, Object> model){
		logger.debug("do=wallpaper_resolution");

        Object o = servletContext.getAttribute(wallpaper_config.getRenewResolutionAttributeName());

		if (o==null){
			StatusBean sb = new StatusBean();
			servletContext.setAttribute(wallpaper_config.getRenewResolutionAttributeName(), sb);
			try{
				wallpaper_service.renewResolution(sb);
			}finally{
				servletContext.removeAttribute(wallpaper_config.getRenewResolutionAttributeName());
			}
			if (sb.getDone()>0){
				common.CommonAttributes.addHelpMessage("operation_succeed", model);
			}else{
				common.CommonAttributes.addErrorMessage("operation_fail", model);
			}
			model.put(wallpaper_config.getContentDataAttribute(), sb);
		}else{
            common.CommonAttributes.addHelpMessage("operation_in_progress", model);
			model.put(wallpaper_config.getContentDataAttribute(), o);
		}

		model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getPrepareUrl());
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params="do=find_duplicates")
	public String doFindDuplicates(Map<String, Object> model){
		logger.debug("do=find_duplicates");

		model.put(wallpaper_config.getContentDataAttribute(), wallpaper_service.findWallpaperDuplicates(0));
		model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getDuplicatesShowUrl());
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params={"do=deleteMulti", "action=deleteMulti"})
	public String doDeleteMulti(Map<String, Object> model,  @RequestParam(value="id") Long[] id){
		logger.debug("do=deleteMulti; action=deleteMulti");
		if (wallpaper_service.deleteById(id)>0){
			common.CommonAttributes.addHelpMessage("operation_succeed", model);
		} else {
			common.CommonAttributes.addErrorMessage("operation_fail", model);
		}
		return doView(model);
	}

	@RequestMapping(params={"do=moveMulti", "action=moveMulti"})
	public String doMoveMulti(Map<String, Object> model, @RequestParam(value="id") Long[] id, @RequestParam(value="id_pages_new") Long id_pages_new){
		logger.debug("do=moveMulti; action=moveMulti");
		if (wallpaper_service.moveWallpapersToPage(id, id_pages_new)>0){
			common.CommonAttributes.addHelpMessage("operation_succeed", model);
		} else {
			common.CommonAttributes.addErrorMessage("operation_fail", model);
		}
		return doView(model);
	}

	@RequestMapping(params={"do=optimize", "action=optimize"})
	public String doOptimizeWallpaper(Map<String, Object> model, @RequestParam(value="id") Long id, @RequestParam(value="ajax", required=false) String ajax){
		logger.debug("do=optimize; action=optimize");
		wallpaper_service.optimizeWallpaper(id);
		if (ajax==null){
			common.CommonAttributes.addHelpMessage("operation_succeed", model);
			return doView(model);
		} else {
			model.put(wallpaper_config.getContentDataAttribute(), wallpaper_service.getUpdateBean(id));
			return wallpaper_config.getAjaxWallpaperUrl();
		}
	}

	@RequestMapping(params="do=optimize_wallpapers")
	public String doOptimizePrepare(Map<String, Object> model){
		logger.debug("do=optimize_wallpapers");

		model.put(wallpaper_config.getContentDataAttribute(), wallpaper_service.getCategoriesLayered());
		model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getOptimizationPrepareUrl());
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params={"do=optimize_wallpapers", "action=optimize_wallpapers"})
	public String doOptimizeWallpapers(Map<String, Object> model, @RequestParam(value="id") Long[] id){
		logger.debug("do=optimize_wallpapers; action=optimize_wallpapers");

		wallpaper_service.optimizeWallpaperCategories(id);
		common.CommonAttributes.addHelpMessage("operation_succeed", model);

		model.put(wallpaper_config.getContentDataAttribute(), wallpaper_service.getCategoriesLayered());
		model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getOptimizationPrepareUrl());
		return wallpaper_config.getTemplateUrl();
	}

	@RequestMapping(params={"do=optimize_wallpapers", "action=set_optimized"})
	public String doSetOptimized(Map<String, Object> model, @RequestParam(value="id") Long[] id, @RequestParam(value="type", required=false) Boolean type){
		logger.debug("do=optimize_wallpapers; action=set_optimized");

		if (type==null)
			wallpaper_service.setWallpaperOptimizationCategories(id, Boolean.FALSE);
		else
			wallpaper_service.setWallpaperOptimizationCategories(id, type);

		common.CommonAttributes.addHelpMessage("operation_succeed", model);

		model.put(wallpaper_config.getContentDataAttribute(), wallpaper_service.getCategoriesLayered());
		model.put(wallpaper_config.getContentUrlAttribute(), wallpaper_config.getOptimizationPrepareUrl());
		return wallpaper_config.getTemplateUrl();
	}

	//-------------------------------------- overriden methods from parent ------------------------

	@Override
	public List<String> getFilterProperties() {return filterProperties;}
	@Override
	public List<String> getFilterAliases() {return filterAliases;}

	//TODO: mb - add 'do' param to global keep parameters maybe
	@Override
	public Set<String> getKeepParameterNames() {
		Set<String> tmp = super.getKeepParameterNames();
		tmp.add("do");
		return tmp;
	}

	//-------------------------------------- initialization ---------------------------------------
	@Resource(name="wallpaperCmsConfig")
	public void setWallpaperConfig(IWallpaperCmsConfig value) {
		this.config = value;
		this.wallpaper_config = value;
	}
	@Resource(name="cmsWallpaperService")
	public void setCmsWallpaperService(ICmsWallpaperService value) {
		this.cmsService = value;
		this.wallpaper_service = value;
	}

	@Override
	public void setServletContext(ServletContext value) {
		this.servletContext = value;
	}
}