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

package core.service;

import com.multimedia.service.wallpaper.IWallpaperService;
import common.utils.FileUtils;
import common.utils.ImageUtils;
import common.utils.image.BufferedImageHolder;
import core.model.beans.Pages;
import gallery.model.beans.Wallpaper;
import gallery.service.pages.IPagesService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;


/**
 * @author demchuck.dima@gmail.com
 */
public class RubricImageCachingService implements IRubricImageService{
    protected final Logger logger = Logger.getLogger(this.getClass());
    /** path to folder where the cached images will be stored */
    protected String path;
    /** relative path in web application */
    protected String web_path;
    /** quantity of images that will be cached for each rubric */
    protected Short image_quantity;
    /** width of created images */
    protected Integer image_width;
    /** height of created images */
    protected Integer image_height;

    protected IWallpaperService wallpaper_service;
    protected IPagesService pages_service;

    protected Random rnd_gen = new Random();

    public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(path, "path", sb);
		common.utils.MiscUtils.checkNotNull(web_path, "web_path", sb);
		common.utils.MiscUtils.checkNotNull(image_quantity, "image_quantity", sb);
		common.utils.MiscUtils.checkNotNull(image_width, "image_width", sb);
		common.utils.MiscUtils.checkNotNull(image_height, "image_height", sb);
		common.utils.MiscUtils.checkNotNull(wallpaper_service, "wallpaper_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
    }

	public static void createFolders(File dst, List<? extends Pages> pages){
		File dst_dir;
		if (!dst.exists()||dst.isFile()){
			dst.delete();
			dst.mkdir();
		}
        for (Pages p:pages){
            dst_dir = new File(dst, p.getId().toString());
			if (!dst_dir.exists())dst_dir.mkdir();
        }
	}

	/**
	 * generates image_quantity images for current page the images are resized to image_width * image_height resolution
	 * @param p page to take images for
	 * @param dst_dir dir where to save resulting images
	 * @return false if any error occurs
	 */
    protected boolean generateImages(Pages p, File dst_dir){
        boolean succeed = true;
        List<Long> ids = pages_service.getAllActiveChildrenId(p.getId());
        List<Wallpaper> wallpapers = wallpaper_service.getMainImages(ids, image_quantity);

        File src_dir = new File(wallpaper_service.getStorePath(), "full");
        File src;
        File dst;
        BufferedImageHolder holder;
        BufferedImage rez;
        for (Wallpaper wallpaper:wallpapers){
            src = new File(src_dir, wallpaper.getName());
            dst = new File(dst_dir, wallpaper.getName());
            if (src.exists()){
                try {
                    holder = ImageUtils.readImage(src);
                    rez = new BufferedImage(image_width, image_height, BufferedImage.TYPE_3BYTE_BGR);
                    ImageUtils.getScaledImageDimmension(holder.getImage(), rez);
                    ImageUtils.writeImage(rez, 1, dst);
                } catch (IOException ex) {
                    logger.error("error while creating image for rubric; ", ex);
                    succeed = false;
                }
            } else {
                logger.error("error while creating image for rubric; " + p.getId() + "; " + p.getName()+ "; " + src.getName());
                succeed = false;
            }
        }
        return succeed;
    }

	/**
	 * generates image_quantity images for current page
	 * image is made by drawing 4 images on one same images
	 * the resulting images are in image_width * image_height resolution
	 * @param p page to take images for
	 * @param dst_dir dir where to save resulting images
	 * @return false if any error occurs
	 */
    protected boolean generateImages4(Pages p, File dst_dir){
        boolean succeed = true;
        List<Long> ids = pages_service.getAllActiveChildrenId(p.getId());
        List<Wallpaper> wallpapers = wallpaper_service.getMainImages(ids, 4);

        File src_dir = new File(wallpaper_service.getStorePath(), "full");
        File src;
        BufferedImageHolder holder;
		BufferedImage[] src_img = new BufferedImage[wallpapers.size()];
        BufferedImage rez;
		//creating src images
        for (int i=0;i<wallpapers.size();i++){
			Wallpaper wallpaper = wallpapers.get(i);
            src = new File(src_dir, wallpaper.getName());
            if (src.exists()){
                try {
                    holder = ImageUtils.readImage(src);
                    rez = new BufferedImage((int)(image_width/1.65), (int)(image_height/1.65), BufferedImage.TYPE_3BYTE_BGR);
                    ImageUtils.getScaledImageDimmension(holder.getImage(), rez);
					src_img[i] = rez;
                } catch (IOException ex) {
                    logger.error("error while creating image for rubric; ", ex);
                    succeed = false;
                }
            } else {
                logger.error("error while creating image for rubric; " + p.getId() + "; " + p.getName()+ "; " + src.getName());
                succeed = false;
            }
        }
		//drawing
		try{
			File dst = new File(dst_dir, Long.toString(System.nanoTime(), 10));
			rez = new BufferedImage(image_width, image_height, BufferedImage.TYPE_3BYTE_BGR);
			ImageUtils.draw4on1(src_img, rez);
			ImageUtils.writeImage(rez, 1, dst);
		} catch (IOException ex) {
			logger.error("error while creating image for rubric; ", ex);
			succeed = false;
		}
        return succeed;
    }

    /**
	 * generates an url for this page
	 * if directory for this page exists -- try to get an url
	 * if no directory -- generating new images
	 * @param p page
	 */
	@Override
    public String getImageUrl(Pages p) {
        //TODO remake
        File dst_dir = new File(path, p.getId().toString());
        if (!dst_dir.exists()||dst_dir.isFile()){
            dst_dir.delete();
            dst_dir.mkdirs();
            generateImages(p, dst_dir);
        }
        File[] files = dst_dir.listFiles();
        if (files.length>0){
            int i = rnd_gen.nextInt(files.length);
            return web_path + p.getId() + "/" + files[i].getName();
        } else {
            logger.error("can't generate image url for rubric; " + p.getId() + p.getName());
            return null;
        }
    }

	  /**
	 * generates urls for pages in list
	 * @param list pages
	 * @see core.service.RubricImageCachingService.getImageUrl
	 */
    @Override
    public List<String> getImageUrls(List<? extends Pages> list) {
        List<String> rez = new LinkedList<String>();
        for (Pages p:list){rez.add(getImageUrl(p));}
        return rez;
    }

    @Override
    public boolean clearImages(Long id) {
        File dir = new File(path);
        if (!dir.exists()) return false;
        boolean rez = true;
        if (id==null){
            File[] files = dir.listFiles();
            for (File f:files){
                rez = FileUtils.deleteFiles(f, true)&rez;
            }
        }else{
            File dst = new File(dir, id.toString());
            rez = FileUtils.deleteFiles(dst, true);
        }
        return rez;
    }

    public static final String[] PAGES_NAMES = new String[]{"id", "name", "type"};
	/**
	 * 1-st creates folders for all pages that will be affected(if they do not have any folders allready)
	 * 2-nd for each page clear folder and generate images
	 * so if folder has some images they will be deleted only before creating new images for it
	 * if you want to clear all images first call 'clearImages' method for this class
	 * @param id
	 * @return
	 */
    @Override
    public boolean refreshImages(Long id) {
        //TODO remake
        boolean rez = true;
        File dst_dir = new File(path);
        List<gallery.model.beans.Pages> pages;
        if (id==null){
            pages = pages_service.getByPropertyValueOrdered(PAGES_NAMES, "type", gallery.web.controller.pages.types.WallpaperGalleryType.TYPE, null, null);
        }else{
            pages = pages_service.getByPropertyValueOrdered(PAGES_NAMES, "id", id, null, null);
        }
		createFolders(dst_dir, pages);
        for (Pages p:pages){
            dst_dir = new File(path, p.getId().toString());
            FileUtils.deleteFiles(dst_dir, false);
            rez = generateImages(p, dst_dir)&rez;
        }
        return rez;
    }

	public void setPath(Resource res) {
		try{
			File f = res.getFile();
			if (f.exists()){
				if (f.isFile()){
					f.delete();
					f.mkdir();
				}
			} else {
				f.mkdirs();
				//f.mkdir();
			}
			if (f.exists()&&f.isDirectory())
				path = f.getCanonicalPath() + "/";
			else
				throw new NullPointerException("cache folder not found for "+getClass().getName());
		} catch (IOException e){
			path = null;
			throw new NullPointerException("cache folder not found for "+getClass().getName());
		}
		//System.out.println("----------------------------path = "+this.path);
	}

    public void setWeb_path(String value){this.web_path = value;}
    public void setImage_quantity(Short value){this.image_quantity = value;}
    public void setImage_width(Integer value){this.image_width = value;}
    public void setImage_height(Integer value){this.image_height = value;}
    public void setWallpaperService(IWallpaperService value){this.wallpaper_service = value;}
    public void setPagesService(IPagesService value){this.pages_service = value;}
}
