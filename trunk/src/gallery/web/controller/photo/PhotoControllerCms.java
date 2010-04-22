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

package gallery.web.controller.photo;

import common.bind.ABindValidator;
import common.utils.RequestUtils;
import common.web.ControllerConfig;
import common.web.IServletContextGetter;
import gallery.model.beans.Photo;
import gallery.model.command.MultiInsertPhotoCms;
import gallery.model.misc.StatusBean;
import gallery.service.photo.IPhotoServiceCms;
import gallery.web.controller.Config;
import gallery.web.controller.cms.CommonPagesCmsDelegate;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PhotoControllerCms extends CommonPagesCmsDelegate{
	protected IPhotoServiceCms photo_service;
	protected ABindValidator multiInsertValidator;
	protected IServletContextGetter contextGetter;

	/** view for insert multi where all photos being set for insert */
	protected String insertMultiUrl;
	/** a responce that is send when photos are send to server by one */
	protected String multiAjaxUrl;
	/** a responce that is send when photos are send to server by one */
	protected String updateMultiAjaxUrl;
	/** an jsp page where user chooses type of insertion */
	protected String prepareUrl;
	/** view for preparation of optimization */
	protected String prepareOptimizationUrl;
	/** view for preparation of uploading using ftp */
	protected String prepareUploadUrl;
	/** view for preparation of uploading using ftp */
	protected String processUploadUrl;
	/** draws just a collection with , delimiter */
	protected String ajaxListUrl;
	/** draws an alternative view with javascript */
	protected String ajaxViewUrl;
	/** jsp for rendering one photo template for ajax responce */
	protected String ajaxPhotoUrl;

	@Override
	public void init() {
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(photo_service, "photo_service", sb);
		common.utils.MiscUtils.checkNotNull(multiInsertValidator, "multiInsertValidator", sb);
		common.utils.MiscUtils.checkNotNull(contextGetter, "servletContext", sb);
		common.utils.MiscUtils.checkNotNull(insertMultiUrl, "insertMultiUrl", sb);
		common.utils.MiscUtils.checkNotNull(multiAjaxUrl, "multiAjaxUrl", sb);
		common.utils.MiscUtils.checkNotNull(updateMultiAjaxUrl, "updateMultiAjaxUrl", sb);
		common.utils.MiscUtils.checkNotNull(prepareUrl, "prepareUrl", sb);
		common.utils.MiscUtils.checkNotNull(prepareOptimizationUrl, "prepareOptimizationUrl", sb);
		common.utils.MiscUtils.checkNotNull(prepareUploadUrl, "prepareUploadUrl", sb);
		common.utils.MiscUtils.checkNotNull(processUploadUrl, "processUploadUrl", sb);
		common.utils.MiscUtils.checkNotNull(ajaxListUrl, "ajaxListUrl", sb);
		common.utils.MiscUtils.checkNotNull(ajaxViewUrl, "ajaxViewUrl", sb);
		common.utils.MiscUtils.checkNotNull(ajaxPhotoUrl, "ajaxPhotoUrl", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	/**
	 * must show items associated with this table
	 * that matches given criteria
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doDeleteAll(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		req.setAttribute(config.getContentUrlAttribute(), config.getContentViewTemplate());
		String action = req.getParameter(Config.ACTION_PARAM_NAME);

		Long[] id = RequestUtils.getLongParameters(req, MARKER_PARAM);

		if ("deleteAll".equals(action)&&id!=null&&photo_service.deleteById(id)>=0)
			common.CommonAttributes.addHelpMessage("operation_succeed", req);
		else
			common.CommonAttributes.addErrorMessage("form_errors", req);

		return doFilteredView(req, resp);
    }


	public static final String[] MOVEALL_PSEUDO = {"id_pages"};
	/**
	 * must show items associated with this table
	 * that matches given criteria
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doMoveAll(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		req.setAttribute(config.getContentUrlAttribute(), config.getContentViewTemplate());
		String action = req.getParameter(Config.ACTION_PARAM_NAME);

		Long[] id = RequestUtils.getLongParameters(req, MARKER_PARAM);
		Long id_pages_new = RequestUtils.getLongParam(req, "id_pages_new");
		Long[] id_pages = new Long[id.length];
		java.util.Arrays.fill(id_pages, id_pages_new);

		if ("moveAll".equals(action)&&id!=null&&id_pages_new!=null&&photo_service.updateObjectArrayShortById(MOVEALL_PSEUDO, id, id_pages)>=0)
			common.CommonAttributes.addHelpMessage("operation_succeed", req);
		else
			common.CommonAttributes.addErrorMessage("form_errors", req);

		return doFilteredView(req, resp);
    }

	public static final String INSERT_MULTI_PARAM = "insertMulti";
	public static final String OPTIMIZE_PARAM = "optimize";

	/**
	 * user can upload photos using this method
	 * i.e. it draws a form for uploading and actually insert any photos if they are in request
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doInsertMulti(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);
		String[] _marker = req.getParameterValues(_MARKER_PARAM);

		if (INSERT_MULTI_PARAM.equals(action) && _marker!=null && _marker.length>0){
			req.setAttribute(config.getContentUrlAttribute(), config.getContentMultiupdateTemplate());
			MultiInsertPhotoCms command = new MultiInsertPhotoCms(_marker.length);

			BindingResult res = multiInsertValidator.bindAndValidate(command, req);

			command.setUser(security.Utils.getCurrentUser(req));

			if (res.hasErrors()) {
				common.CommonAttributes.addErrorMessage("form_errors", req);
				req.setAttribute(res.MODEL_KEY_PREFIX+config.getContentDataAttribute(), res);
			} else {
				if (command.save(service) > 0) {
					common.CommonAttributes.addHelpMessage("operation_succeed", req);
				} else {
					common.CommonAttributes.addErrorMessage("operation_fail", req);
				}
			}

			req.setAttribute(config.getContentDataAttribute(), command);
		}else{
            Long l = RequestUtils.getLongParam(req, "id_pages_one");
            MultiInsertPhotoCms command = new MultiInsertPhotoCms(0);
            command.setId_pages_one(l);
			req.setAttribute(config.getContentDataAttribute(),command);
		}

		RequestUtils.copyRequestAttributesFromMap(req, service.initInsert());
		req.setAttribute("empty_object", new Photo());
		req.setAttribute(config.getContentUrlAttribute(), insertMultiUrl);

		return new ModelAndView(config.getTemplateUrl());
    }
	/**
	 * simply draws a form that contains links to differend insertion methods
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doPrepare(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		req.setAttribute(config.getContentUrlAttribute(), prepareUrl);
		return new ModelAndView(config.getTemplateUrl());
	}

	/**
	 * inserts one photo that is received with request
	 * and sends a responce indicating insertion status (0 - errors, 1 - inserted)
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doAjaxInsert(HttpServletRequest req, HttpServletResponse resp){
		if ("ajax_insert".equals(req.getParameter(Config.ACTION_PARAM_NAME))){
			Photo command = new Photo();
			BindingResult res = insertValidator.bindAndValidate(command, req);
			if (res.hasErrors()){
				req.setAttribute(config.getContentDataAttribute(), "0");
			}else{
				service.insert(command);
				req.setAttribute(config.getContentDataAttribute(), "1");
			}
		}
		return new ModelAndView(multiAjaxUrl);
	}

	@Override
	public ModelAndView doFilteredView(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = super.doFilteredView(req, resp);
		if ("ajax".equals(req.getParameter("view"))){
			req.setAttribute(config.getContentUrlAttribute(), ajaxViewUrl);
		}
		return mv;
	}


	/**
	 * draws a form for multi updating
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doUpdateMultiForm(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);

		RequestUtils.copyRequestAttributesFromMap(req, service.initInsert());
		req.setAttribute(config.getContentUrlAttribute(), updateMultiAjaxUrl);
		return new ModelAndView(config.getTemplateUrl());
	}

	/**
	 * updates one photo that is received with request
	 * and sends a responce indicating status (0 - errors, 1 - updated)
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doAjaxUpdate(HttpServletRequest req, HttpServletResponse resp){
		Long id = RequestUtils.getLongParam(req, "id");
		if ("ajax_update".equals(req.getParameter(Config.ACTION_PARAM_NAME))){
			if (id==null){
				req.setAttribute(config.getContentDataAttribute(), "0");
			} else {
				Photo command = photo_service.getUpdateBean(id);
				BindingResult res = updateValidator.bindAndValidate(command, req);
				if (res.hasErrors()){
					req.setAttribute(config.getContentDataAttribute(), "0");
				}else{
					service.update(command);
					req.setAttribute(config.getContentDataAttribute(), "1");
				}
			}
			return new ModelAndView(multiAjaxUrl);
		} else {
			//just getting another photo
			if (id==null){
				id = RequestUtils.getLongParam(req, "id_pages_nav");
				req.setAttribute(config.getContentDataAttribute(), photo_service.getPhotosInCategory(id));
				return new ModelAndView(ajaxListUrl);
			} else {
				req.setAttribute(config.getContentDataAttribute(), photo_service.getUpdateBean(id));
				return new ModelAndView(ajaxPhotoUrl);
			}
		}
	}

	//upload attribute if exists then upload is allready in progress
	public static String UPLOAD_ATTRIBUTE = "PhotoControllerCms_Upload_Attribute";
	/**
	 * reads upload dir for photos and shows list to user
	 * also inserts photos if an action param is present
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doUpload(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);
        ServletContext sc = contextGetter.getContext();
        Object o = sc.getAttribute(UPLOAD_ATTRIBUTE);

        if (o==null){
            if (INSERT_MULTI_PARAM.equals(action)){
                StatusBean usb = new StatusBean();
                sc.setAttribute(UPLOAD_ATTRIBUTE, usb);
                try{
                    Long id = RequestUtils.getLongParam(req, "id");
                    User uploader = security.Utils.getCurrentUser(req);
                    photo_service.uploadPhotos(uploader, id, usb);
                    common.CommonAttributes.addHelpMessage("operation_succeed", req);
                }finally{
                    sc.removeAttribute(UPLOAD_ATTRIBUTE);
                }
            }
            req.setAttribute(config.getContentUrlAttribute(), prepareUploadUrl);
            req.setAttribute(config.getContentDataAttribute(), photo_service.createPageFolders());
            return new ModelAndView(config.getTemplateUrl());
        } else {
            common.CommonAttributes.addHelpMessage("operation_in_progress", req);
            req.setAttribute(config.getContentUrlAttribute(), processUploadUrl);
            return new ModelAndView(config.getTemplateUrl());
        }

		//throw new UnsupportedOperationException("not supported yet");
    }

	/**
	 * generates photo title, tags ... fields
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doOptimizePhoto(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);
		Long id = RequestUtils.getLongParam(req, "id");
		if (OPTIMIZE_PARAM.equals(action)&&id!=null){
			photo_service.optimizePhoto(id);
			common.CommonAttributes.addHelpMessage("operation_succeed", req);
		}
        return doFilteredView(req, resp);
    }

	/**
	 * generates photo title, tags ... fields
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doOptimizePhotos(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);
		Long[] id = RequestUtils.getLongParameters(req, "id");
		if (OPTIMIZE_PARAM.equals(action)&&id!=null){
			photo_service.optimizePhotoCategories(id);
			common.CommonAttributes.addHelpMessage("operation_succeed", req);
		}
		req.setAttribute(config.getContentDataAttribute(), photo_service.getCategoriesLayered());
		req.setAttribute(config.getContentUrlAttribute(), prepareOptimizationUrl);
		return new ModelAndView(config.getTemplateUrl());
    }

	/**
	 * clears optimized field of all photos in pages with given ids
	 * @param req
	 * @param resp
	 * @return
	 */
	public ModelAndView doClearPhotoOptimization(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);
		String action = req.getParameter(ControllerConfig.ACTION_PARAM_NAME);
		Long[] id = RequestUtils.getLongParameters(req, "id");
		if (OPTIMIZE_PARAM.equals(action)&&id!=null){
			Boolean type = RequestUtils.getBoolParam(req, "type");
			if (type!=null)
				photo_service.setPhotoOptimizationCategories(id, type);
			else
				photo_service.setPhotoOptimizationCategories(id, Boolean.FALSE);
			common.CommonAttributes.addHelpMessage("operation_succeed", req);
		}
		req.setAttribute(config.getContentDataAttribute(), photo_service.getCategoriesLayered());
		req.setAttribute(config.getContentUrlAttribute(), prepareOptimizationUrl);
		return new ModelAndView(config.getTemplateUrl());
    }

	//upload attribute if exists then upload is allready in progress
	public static String RENEW_RESOLUTION_ATTRIBUTE = "PhotoControllerCms_RenewResolution_Attribute";
	public ModelAndView doRenewResolution(HttpServletRequest req, HttpServletResponse resp){
		getCommonModel(req);

        ServletContext sc = contextGetter.getContext();
        Object o = sc.getAttribute(RENEW_RESOLUTION_ATTRIBUTE);

		if (o==null){
			StatusBean sb = new StatusBean();
			sc.setAttribute(RENEW_RESOLUTION_ATTRIBUTE, sb);
			try{
				photo_service.renewResolution(sb);
			}finally{
				sc.removeAttribute(RENEW_RESOLUTION_ATTRIBUTE);
			}
			if (sb.getDone()>0){
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
			}else{
				common.CommonAttributes.addErrorMessage("operation_fail", req);
			}
			req.setAttribute(config.getContentDataAttribute(), sb);
		}else{
			req.setAttribute(config.getContentDataAttribute(), o);
            common.CommonAttributes.addHelpMessage("operation_in_progress", req);
		}

		req.setAttribute(config.getContentUrlAttribute(), prepareUrl);
		return new ModelAndView(config.getTemplateUrl());
	}

	public void setPhoto_service(IPhotoServiceCms photo_service) {this.photo_service = photo_service;}
	public void setMultiInsertValidator(ABindValidator value) {this.multiInsertValidator = value;}
	public void setServletContext(IServletContextGetter value) {this.contextGetter = value;}

	public void setInsertMultiUrl(String insertMultiUrl) {this.insertMultiUrl = insertMultiUrl;}
	public void setMultiAjaxUrl(String multiAjaxUrl) {this.multiAjaxUrl = multiAjaxUrl;}
	public void setUpdateMultiAjaxUrl(String updateMultiAjaxUrl) {this.updateMultiAjaxUrl = updateMultiAjaxUrl;}
	public void setPrepareUrl(String prepareUrl) {this.prepareUrl = prepareUrl;}
	public void setPrepareOptimizationUrl(String prepareOptimizationUrl) {this.prepareOptimizationUrl = prepareOptimizationUrl;}
	public void setPrepareUploadUrl(String prepareUploadUrl) {this.prepareUploadUrl = prepareUploadUrl;}
	public void setProcessUploadUrl(String processUploadUrl) {this.processUploadUrl = processUploadUrl;}
	public void setAjaxListUrl(String ajaxListUrl) {this.ajaxListUrl = ajaxListUrl;}
	public void setAjaxViewUrl(String ajaxViewUrl) {this.ajaxViewUrl = ajaxViewUrl;}
	public void setAjaxPhotoUrl(String ajaxPhotoUrl) {this.ajaxPhotoUrl = ajaxPhotoUrl;}
}
