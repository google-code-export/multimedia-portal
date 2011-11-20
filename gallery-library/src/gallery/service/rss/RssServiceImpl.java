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

package gallery.service.rss;

import com.multimedia.service.wallpaper.IWallpaperService;
import core.rss.RSSFeedGeneratorImpl;
import core.rss.elem.Channel;
import core.rss.elem.Item;
import core.rss.elem.RSS;
import gallery.model.beans.Pages;
import gallery.service.autoreplace.IAutoreplaceService;
import gallery.service.pages.IPagesService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class RssServiceImpl implements IRssService{
	Logger logger = Logger.getLogger(RssServiceImpl.class.getName());

    private IPagesService pages_service;
    private IWallpaperService wallpaper_service;
	private IAutoreplaceService autoreplace_service;
	private String path;
	private boolean generating = false;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		common.utils.MiscUtils.checkNotNull(wallpaper_service, "wallpaper_service", sb);
		common.utils.MiscUtils.checkNotNull(autoreplace_service, "autoreplace_service", sb);
		common.utils.MiscUtils.checkNotNull(path, "path", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	public void setPath(Resource res) {
		try{
			File f = res.getFile();
			if (f.exists()&&f.isDirectory())
				path = f.getCanonicalPath() + "/";
			else
				throw new NullPointerException("xml path not found for RSS");
		} catch (IOException e){
			path = null;
			throw new NullPointerException("xml path not found for RSS");
		}
		//System.out.println("----------------------------path = "+this.path);
	}

	public void setPages_service(IPagesService value){this.pages_service = value;}
	public void setWallpaper_service(IWallpaperService value){this.wallpaper_service = value;}
	public void setAutoreplace_service(IAutoreplaceService service) {this.autoreplace_service = service;}

	protected static final String[] MAIN_PROPS = new String[]{"title", "description"};
	protected static final String[] MAIN_PSEUDONYMES = new String[]{"id_pages","active","type"};
	protected static final Object[] MAIN_VALUES = new Object[]{null,Boolean.TRUE,"general_wallpaper_gallery"};
	@Override
	@Transactional(readOnly=true)
	public void create() {
		if (generating){
			logger.info("xml is allready generating ...");
			return;
		}
		try{
			generating = true;
			logger.info("start generate xml");
			long time = System.currentTimeMillis();

			File img_dir = new File(wallpaper_service.getStorePath(), Config.ENCLOSURE_IMG_SUBDIR);

			//get main wallpaper page
			Channel chan;
			List<Pages> temp = pages_service.getByPropertiesValueOrdered(null, MAIN_PSEUDONYMES, MAIN_VALUES, null, null);
			if (temp.isEmpty()){
				chan = new Channel(gallery.web.Config.SITE_NAME, gallery.web.Config.SITE_NAME, gallery.web.Config.SITE_NAME);
			} else{
				//TODO localize it !!!
				IAutoreplaceService.IReplacement repl = autoreplace_service.getAllReplacements("ru");
				String title = repl.replaceAll(temp.get(0).getTitle());
				String description = repl.replaceAll(temp.get(0).getDescription());
				chan = new Channel(title, gallery.web.Config.SITE_NAME, description);
			}

			RSS rss = new RSS();

			chan.setImage(new Channel.Image(gallery.web.Config.SITE_NAME+Config.LOGO_WEBDIR, chan.getTitle(), chan.getLink(), 0, 0, null));
			chan.setLastBuildDate(new java.util.Date());
			rss.addChannel(chan);

			ScrollableResults sr = wallpaper_service.getScrollableResults("id, id_pages, description, title, date_upload, name", "active", Boolean.TRUE, new String[]{"date_upload"}, new String[]{"DESC"});
			int max_elements = 100;
			sr.beforeFirst();
			while (sr.next()&&(max_elements-->0)){
				try{
					Item item = new Item(sr.getString(2),gallery.web.Config.SITE_NAME+"index.htm?id_pages_nav="+sr.getLong(1)+"&id_photo_nav="+sr.getLong(0), sr.getString(3));
					item.setPubDate(sr.getDate(4));
					long fileLen = (new File(img_dir, sr.getString(5))).length();
					if (fileLen>0){
						item.setEnclosure(new Item.Enclosure(gallery.web.Config.SITE_NAME+Config.ENCLOSURE_IMG_WEBDIR+sr.getString(5), fileLen, "image/jpeg"));
					}
					//item.addCategory(new Item.Category("test"));
					chan.addItem(item);
				} finally {
					//TODO: mb add some logging here
				}
			}
			sr.close();
			try {
				new RSSFeedGeneratorImpl().generateToFile(rss, new File(path, Config.RSS_FILE_NAME), "UTF-8");
			} catch (Exception e) {
				logger.error("error while saving rss to file", e);
			}
			time = System.currentTimeMillis() - time;
			logger.info("end generate xml. generated in: "+time);
		}finally{
			generating = false;
		}
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public static final class Config{
		//subdirectory of store path where to get images
		public static final String ENCLOSURE_IMG_SUBDIR = "medium";
		//subdirectory of store path where to get images
		public static final String ENCLOSURE_IMG_WEBDIR = "images/wallpaper/medium/";
		//web path to logo
		public static final String LOGO_WEBDIR = "img/top/logo.jpg";
		//web path to logo
		public static final String RSS_FILE_NAME = "rss.xml";
		private Config(){};
	}

}
