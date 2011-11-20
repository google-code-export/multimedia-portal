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

package gallery.service.sitemap;

import com.multimedia.service.wallpaper.IWallpaperService;
import core.sitemap.model.Index;
import core.sitemap.model.Sitemap;
import gallery.model.beans.Pages;
import gallery.service.pages.IPagesService;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class SitemapServiceImpl implements ISitemapService{
	Logger logger = Logger.getLogger(SitemapServiceImpl.class.getName());

    protected IPagesService pages_service;
    protected IWallpaperService wallpaper_service;
	protected String path;
	protected String path_tmp;
	private boolean generating;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		common.utils.MiscUtils.checkNotNull(wallpaper_service, "wallpaper_service", sb);
		common.utils.MiscUtils.checkNotNull(path, "path", sb);
		common.utils.MiscUtils.checkNotNull(path_tmp, "path_tmp", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

    protected static final String[] SITEMAP_NAMES = new String[]{"id","last", "type"};
    protected static final String[] SITEMAP_WHERE = new String[]{"id_pages","active"};
    @Override
	@Transactional(readOnly=true)
    public void createSitemap() {
		if (generating){
			logger.info("xml is allready generating ...");
			return;
		}
		try{
			logger.info("start generate xml");
			generating = true;
			long time = System.currentTimeMillis();
			File base = new File(path_tmp);
			try {
				if (base.exists()){
					Index index = new Index(10000, base, gallery.web.Config.SITE_NAME);
					Sitemap sitemap = index.getChild();

					List<Pages> pages = pages_service.getByPropertiesValueOrdered(SITEMAP_NAMES, SITEMAP_WHERE, new Object[]{null, Boolean.TRUE}, null, null);
					LinkedList<Long> pages_unhandled = new LinkedList<Long>();
					int k = 0;
					while (k<pages.size()){
						Pages p = pages.get(k);
						if (!p.getLast()){
							pages_unhandled.add(p.getId());
							sitemap.addRecord("index.htm?id_pages_nav="+p.getId(), "daily", "0.8");
						} else {
							//check type
							if (p.getType().equals(gallery.web.controller.pages.types.WallpaperGalleryType.TYPE)){
								Long id_pages = p.getId();
								List ids = wallpaper_service.getSingleProperty("id", SITEMAP_WHERE, new Object[]{id_pages, Boolean.TRUE}, 0, 0, null, null);
								int j=0;
								for (int i=0;i<ids.size();i=i+gallery.web.controller.pages.types.WallpaperGalleryType.CATEGORY_WALLPAPERS){
									sitemap.addRecord("index.htm?id_pages_nav="+id_pages+"&amp;page_number="+j, "daily", "0.9");
									j++;
								}
								for (Object id_wallpaper:ids){
									sitemap.addRecord("index.htm?id_pages_nav="+id_pages+"&amp;id_photo_nav="+id_wallpaper, "daily", "0.7");
								}
							} else {
								sitemap.addRecord("index.htm?id_pages_nav="+p.getId(), "daily", "0.9");
							}
						}
						k++;
						if ((k>=pages.size())&&(!pages_unhandled.isEmpty())){
							pages = pages_service.getByPropertiesValueOrdered(SITEMAP_NAMES,
									SITEMAP_WHERE, new Object[]{pages_unhandled.removeFirst(), Boolean.TRUE},
									null, null);
							k=0;
						}
					}
					sitemap.close();
					//moving to web content
					clearSitemap();
					File f = new File(path_tmp);
					File[] files = f.listFiles();
					for (File file:files){
						String name = file.getName();
						if ((name.startsWith(core.sitemap.model.Config.SITEMAP_PREFIX)||name.startsWith(core.sitemap.model.Config.INDEX_PREFIX))&&name.endsWith(core.sitemap.model.Config.XML_SUFFIX)){
							File new_file = new File(path, file.getName());
							file.renameTo(new_file);
						}
					}
				}
			} catch (IOException ex) {
				logger.error("error while generating sitemap",ex);
			}
			time = System.currentTimeMillis() - time;
			logger.info("end generate xml. generated in: "+time);
		}finally{
			generating = false;
		}
    }

    @Override
    public void clearSitemap() {
        File f = new File(path);
        File[] files = f.listFiles();
        for (File file:files){
            String name = file.getName();
            if ((name.startsWith(core.sitemap.model.Config.SITEMAP_PREFIX)||name.startsWith(core.sitemap.model.Config.INDEX_PREFIX))&&name.endsWith(core.sitemap.model.Config.XML_SUFFIX)){
                file.delete();
            }
        }
    }

	public void setPath(Resource res) {
		try{
			File f = res.getFile();
			if (f.exists()&&f.isDirectory())
				path = f.getCanonicalPath() + "/";
			else
				throw new NullPointerException("xml path not found for Sitemap");
		} catch (IOException e){
			path = null;
			throw new NullPointerException("xml path not found for Sitemap");
		}
		//System.out.println("----------------------------path = "+this.path);
	}

	public void setPath_tmp(Resource res) {
		try{
			File f = res.getFile();
			if (f.exists()&&f.isDirectory())
				path_tmp = f.getCanonicalPath() + "/";
			else
				throw new NullPointerException("xml path not found for Sitemap");
		} catch (IOException e){
			path_tmp= null;
			throw new NullPointerException("xml path not found for Sitemap");
		}
		//System.out.println("----------------------------path = "+this.path);
	}
	public void setPages_service(IPagesService value){this.pages_service = value;}
	public void setWallpaper_service(IWallpaperService value){this.wallpaper_service = value;}
}
