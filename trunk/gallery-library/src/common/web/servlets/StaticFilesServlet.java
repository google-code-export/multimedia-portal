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

package common.web.servlets;

import common.utils.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class StaticFilesServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(StaticFilesServlet.class);

	/**
	 * path to a properties file
	 */
	public static final String PROPERTIES_PATH_NAME = "properties_file";
	public static final String FILE_STORAGE_PROPERTY_NAME = "wallpaper.store.dir";

	protected String realPath = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (config!=null){
			String prop_file = config.getInitParameter(PROPERTIES_PATH_NAME);
			if (prop_file==null||prop_file.equals(""))
				throw new ServletException("properties_file not specified");
			//loadFromPropertiesFile( config.getServletContext().getRealPath(prop_file) );
			loadFromPropertiesFile( prop_file );

			config.getServletContext().setAttribute("NewIO", newStat);
			config.getServletContext().setAttribute("OldIO", oldStat);
		}
		if (realPath==null)
			throw new ServletException("path to directory not found");
	}

	/**
	 * try to load real path to store dir from properties file
	 * @param fileName name of properties file from where to load path to storage dir
	 * @return true if loadded successfully
	 */
	protected boolean loadFromPropertiesFile(String fileName){
		try {
			FileInputStream fis = new FileInputStream(ResourceUtils.getFile(fileName));
			Properties prop = new Properties();
			prop.load(fis);
			realPath = ResourceUtils.getFile(prop.getProperty(FILE_STORAGE_PROPERTY_NAME)).getAbsolutePath();
			fis.close();
			logger.info("directory path="+realPath);
			return true;
		} catch (FileNotFoundException ex) {
			logger.error("file could not be loaded "+fileName);
			realPath = null;
		}catch (IOException ex) {
			logger.error("file could not be read or parsed "+fileName);
			realPath = null;
		}

		return false;
	}

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String name = getResourceName(request.getPathInfo());

		if (name==null){
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
			return;
		}

		File file = new File(realPath, name);

		if (!file.exists()){
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
			return;
		}

		//delete after testing
		//synchronized(semaphore){
			long time = System.nanoTime();
			if (logger.isDebugEnabled()){
				logger.debug("get resource="+name);
				logger.debug("get file="+file.getAbsolutePath());
			}
			OutputStream os = response.getOutputStream();
			//if (useNew){
			//	FileUtils.loadFromFileNew(file, os);
			//} else {
				FileUtils.loadFromFileOld(file, os);
			//}
			os.flush();
			os.close();

			time = System.nanoTime() - time;
			//if (useNew){
				//logger.info("proceed time new = "+time+"; avg = "+(servTimeNew/servQuantityNew)+"; count="+servQuantityNew+"; size = "+servSizeNew);
			//	newStat.increaseCount();
			//	newStat.increaseSize(file.length());
			//	newStat.increaseTime(time);
			//	useNew = false;
			//} else {
				//logger.info("proceed time old = "+time+"; avg = "+(servTimeOld/servQuantityOld)+"; count="+servQuantityOld+"; size = "+servSizeOld);
				oldStat.increaseCount();
				oldStat.increaseSize(file.length());
				oldStat.increaseTime(time);
			//	useNew = true;
			//}
		//}
    }
	final Object semaphore = new Object();
	//volatile boolean useNew = true;

	StatBean newStat = new StatBean();
	StatBean oldStat = new StatBean();

	protected String getResourceName(String path_info)
	{
		String name = path_info;

		if (name==null || name.length()==0)
			return null;

		while (name.startsWith("/"))
			name = name.substring(1);

		if (name.startsWith("../"))
			return null;

		return name;
	}

	@Override
	protected long getLastModified(HttpServletRequest request) {
		String name = getResourceName(request.getPathInfo());
		if (name==null)
			return -1;
		File file = new File(realPath, name);
		if (file.exists()){
			return file.lastModified();
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("resource not found:"+request.getPathInfo());
			}
			return -1;
		}
	}

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }

}
