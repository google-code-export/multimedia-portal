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

package common.listeners;

import java.io.File;
import java.io.FileNotFoundException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Web application lifecycle listener.
 * @author demchuck.dima@gmail.com
 */

public class Log4JInitializer implements ServletContextListener {
	/** Parameter specifying the location of the Log4J config file */
	public static final String CONFIG_LOCATION_PARAM = "log4jConfigLocation";

	/** Parameter specifying the refresh interval for checking the Log4J config file */
	public static final String REFRESH_INTERVAL_PARAM = "log4jRefreshInterval";

	/** Parameter specifying whether to expose the web app root system property */
	public static final String EXPOSE_WEB_APP_ROOT_PARAM = "log4jExposeWebAppRoot";

	/** Extension that indicates a Log4J XML config file: ".xml" */
	public static final String XML_FILE_EXTENSION = ".xml";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		setWebAppRootSystemProperty(servletContext);
		// Only perform custom Log4J initialization in case of a config file.
		String location = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if (location != null) {
			// Perform actual Log4J initialization; else rely on Log4J's default initialization.
			try {
				location = servletContext.getRealPath(location);

				// Write log message to server log.
				servletContext.log("Initializing Log4J from [" + location + "]");

				// Check whether refresh interval was specified.
				String intervalString = servletContext.getInitParameter(REFRESH_INTERVAL_PARAM);
				File file = new File(location);
				if (!file.exists()) {
					throw new FileNotFoundException("Log4J config file [" + location + "] not found");
				}
				if (intervalString != null) {
					// Initialize with refresh interval, i.e. with Log4J's watchdog thread,
					// checking the file in the background.
					try {
						long refreshInterval = Long.parseLong(intervalString);
						if (location.toLowerCase().endsWith(XML_FILE_EXTENSION)) {
							DOMConfigurator.configureAndWatch(file.getAbsolutePath(), refreshInterval);
						} else {
							PropertyConfigurator.configureAndWatch(file.getAbsolutePath(), refreshInterval);
						}
					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException("Invalid 'log4jRefreshInterval' parameter: " + ex.getMessage());
					}
				} else {
					// Initialize without refresh check, i.e. without Log4J's watchdog thread.
					if (location.toLowerCase().endsWith(XML_FILE_EXTENSION)) {
						DOMConfigurator.configure(location);
					} else {
						PropertyConfigurator.configure(location);
					}
				}
			}
			catch (FileNotFoundException ex) {
				throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex.getMessage());
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		servletContext.log("Shutting down Log4J");
		try{
			LogManager.shutdown();
		}finally{
			removeWebAppRootSystemProperty(servletContext);
		}
	}

	/**
	 * Web app root key parameter at the servlet context level
	 * (i.e. a context-param in <code>web.xml</code>): "webAppRootKey".
	 */
	public static final String WEB_APP_ROOT_KEY_PARAM = "webAppRootKey";

	/** Default web app root key: "webapp.root" */
	public static final String DEFAULT_WEB_APP_ROOT_KEY = "webapp.root";

	/**
	 * Set a system property to the web application root directory.
	 * The key of the system property can be defined with the "webAppRootKey"
	 * context-param in <code>web.xml</code>. Default is "webapp.root".
	 * <p>Can be used for tools that support substition with <code>System.getProperty</code>
	 * values, like Log4J's "${key}" syntax within log file locations.
	 * @param servletContext the servlet context of the web application
	 * @throws IllegalStateException if the system property is already set,
	 * or if the WAR file is not expanded
	 * @see #WEB_APP_ROOT_KEY_PARAM
	 * @see #DEFAULT_WEB_APP_ROOT_KEY
	 * @see WebAppRootListener
	 * @see Log4jWebConfigurer
	 */
	public static void setWebAppRootSystemProperty(ServletContext servletContext) throws IllegalStateException {
		String root = servletContext.getRealPath("/");
		if (root == null) {
			throw new IllegalStateException(
			    "Cannot set web app root system property when WAR file is not expanded");
		}
		String param = servletContext.getInitParameter(WEB_APP_ROOT_KEY_PARAM);
		String key = (param != null ? param : DEFAULT_WEB_APP_ROOT_KEY);
		String oldValue = System.getProperty(key);
		if (oldValue != null && !oldValue.equals( root)) {
			throw new IllegalStateException(
			    "Web app root system property already set to different value: '" +
			    key + "' = [" + oldValue + "] instead of [" + root + "] - " +
			    "Choose unique values for the 'webAppRootKey' context-param in your web.xml files!");
		}
		System.setProperty(key, root);
		servletContext.log("Set web app root system property: '" + key + "' = [" + root + "]");
	}

	/**
	 * Remove the system property that points to the web app root directory.
	 * To be called on shutdown of the web application.
	 * @param servletContext the servlet context of the web application
	 * @see #setWebAppRootSystemProperty
	 */
	public static void removeWebAppRootSystemProperty(ServletContext servletContext) {
		String param = servletContext.getInitParameter(WEB_APP_ROOT_KEY_PARAM);
		String key = (param != null ? param : DEFAULT_WEB_APP_ROOT_KEY);
		System.getProperties().remove(key);
	}
}