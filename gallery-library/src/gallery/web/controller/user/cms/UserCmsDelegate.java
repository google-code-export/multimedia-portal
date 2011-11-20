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

package gallery.web.controller.user.cms;

import gallery.web.validator.user.UserCmsValidator;
import common.utils.RequestUtils;
import security.beans.User;
import gallery.service.pages.IPagesService;
import gallery.service.user.IUserService;
import gallery.web.controller.user.Config;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * @author demchuck.dima@gmail.com
 */
public class UserCmsDelegate{
	protected final Logger logger = Logger.getLogger(getClass());

	//fields ....
	protected IPagesService pagesService;
	protected IUserService<User, Long> userService;
	private Config config;
	private UserCmsValidator validatorInsert;
	private UserCmsValidator validatorUpdate;

	protected String navigation_url;
	protected String insert_url;
	protected String update_url;
	protected String show_url;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(pagesService, "pagesService", sb);
		common.utils.MiscUtils.checkNotNull(userService, "userService", sb);
		common.utils.MiscUtils.checkNotNull(config, "config", sb);
		common.utils.MiscUtils.checkNotNull(navigation_url, "navigation_url", sb);
		common.utils.MiscUtils.checkNotNull(insert_url, "insert_url", sb);
		common.utils.MiscUtils.checkNotNull(update_url, "update_url", sb);
		common.utils.MiscUtils.checkNotNull(show_url, "show_url", sb);
		if (common.utils.MiscUtils.checkNotNull(validatorInsert, "validatorInsert", sb)){
			validatorInsert.setUserService(userService);
		}
		if (common.utils.MiscUtils.checkNotNull(validatorUpdate, "validatorUpdate", sb)){
			validatorUpdate.setUserService(userService);
		}
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	public UserCmsDelegate(){}

	public ModelAndView doView(HttpServletRequest req, HttpServletResponse resp){
		//logger.fine("do=view");
		Map<String, Object> m = getCommonModel(req);
		m.put(config.getContentUrlAttribute(),show_url);

		Long id_pages = common.utils.RequestUtils.getLongParam(req, config.getId_pagesParamName());
		m.put("users", userService.getShortOrderedByPropertyValueCms("id_pages", id_pages));
		//m.put("users", userService.getAll());
        return new ModelAndView(config.getTemplateUrl(), m);
    }

	public ModelAndView doInsert(HttpServletRequest req, HttpServletResponse resp){
		Map<String, Object> m = getCommonModel(req);
		m.put(config.getContentUrlAttribute(),insert_url);
		m.put("editForm_topHeader", "Добавление");
		m.put("avaibleRoles", config.getAvaibleRoles());

		String action = req.getParameter(gallery.web.controller.Config.ACTION_PARAM_NAME);

		if (action!=null&&action.equals("insert")){
			/** bind command */
			User command = new User();
			BindingResult res = validatorInsert.bindAndValidate(command, req, null);
			if (res.hasErrors()){
				//m.putAll(res.getModel());
				m.put(res.MODEL_KEY_PREFIX+"command", res);
				m.put("command", command);
				common.CommonAttributes.addErrorMessage("form_errors", req);
			}else{
				userService.save(command);
				//m.put("command", new User());
				m.put("command", command);
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
			}
		}else{
			m.put("command", new User());
		}
		
        return new ModelAndView(config.getTemplateUrl(), m);
    }
	
	public ModelAndView doUpdate(HttpServletRequest req, HttpServletResponse resp){
		Map<String, Object> m = getCommonModel(req);
		m.put(config.getContentUrlAttribute(),update_url);
		m.put("editForm_topHeader", "Редактирование");
		m.put("avaibleRoles", config.getAvaibleRoles());

		String action = req.getParameter(gallery.web.controller.Config.ACTION_PARAM_NAME);
		//getting pages with an appropriate id
		User command_db = null;
		User command;
		Long id = RequestUtils.getLongParam(req, "id");
		if (id!=null){command_db = userService.getById(id);}

		if (action!=null&&action.equals("update")&&command_db!=null){
			/** bind command */
			command = new User();
			BindingResult res = validatorUpdate.bindAndValidate(command, req, command_db);

			if (res.hasErrors()){
				m.put(res.MODEL_KEY_PREFIX+"command", res);
				common.CommonAttributes.addErrorMessage("form_errors", req);
				//logger.fine("hasErrors");
			}else{
				userService.merge(command);
				//command = new User();
				common.CommonAttributes.addHelpMessage("operation_succeed", req);
				//logger.fine("not hasErrors");
			}
		}else{
			command = command_db;
		}

		if (id==null||command_db==null){
			m.put("command", new User());
			common.CommonAttributes.addErrorMessage("form_errors", req);
		}else{
			m.put("command", command);
		}

        return new ModelAndView(config.getTemplateUrl(), m);
    }

	public ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp){
		//logger.fine("do=delete");
		String action = req.getParameter("action");
		if (action != null && action.equals("delete")) {
			try{
				Long id = Long.valueOf(req.getParameter("id"));
				if (userService.deleteById(id)>0){
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

	public Map<String, Object> getCommonModel(HttpServletRequest req){
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("title","Пользователи");
		m.put("top_header","Пользователи");

		m.put(config.getNavigationUrlAttribute(), navigation_url);
		Long id_pages_nav = common.utils.RequestUtils.getLongParam(req, config.getId_pagesParamName());
		m.put(config.getNavigationDataAttribute(), pagesService.getAllPagesParents(id_pages_nav, gallery.web.controller.pages.Config.NAVIGATION_PSEUDONYMES));
		return m;
	}

	public void setPagesService(IPagesService pagesService) {this.pagesService = pagesService;}
	public void setUserService(IUserService<User, Long> userService) {this.userService = userService;}
	public void setConfig(Config config) {this.config = config;}
    public void setInsertValidator(UserCmsValidator val){this.validatorInsert = val;}
    public void setUpdateValidator(UserCmsValidator val){this.validatorUpdate = val;}

	public void setNavigationUrl(String value){this.navigation_url = value;}
	public void setInsertUrl(String value){this.insert_url = value;}
	public void setUpdateUrl(String value){this.update_url = value;}
	public void setShowUrl(String value){this.show_url = value;}

}
