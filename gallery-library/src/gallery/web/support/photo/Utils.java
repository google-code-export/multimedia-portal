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

package gallery.web.support.photo;

import common.utils.FileUtils;
import common.utils.ImageUtils;
import common.utils.image.BufferedImageHolder;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import gallery.model.beans.Photo;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

/**
 * implements differ actions with file( such as save, delete...)
 * @author dima
 */
public class Utils {

	protected static Logger logger = Logger.getLogger(Utils.class.getName());
	public static final String FULL_DIMMENSION_NAME = "full";

    private Utils() {}

    /**
     * resize photo file to new dimensions(declared in config) and saves it to disk
     * @param photo Photo object
	 * @param dimmensions map that contains name(folder)/resolution of resized photo
	 * @param basepath path of folder where to create subdirectories
	 * @return dimmensions of saved photo
     */
    public static Dimension saveScaledPhotoFileToDisk(Photo photo, Map<String,Integer> dimmensions, String basepath)
	{
		Set<Entry<String,Integer>> entrySet = dimmensions.entrySet();
		Iterator<Entry<String,Integer>> entries = entrySet.iterator();
		Vector<File> files = new Vector<File>(entrySet.size());
		try {
			long ms = System.currentTimeMillis();
			BufferedImageHolder holder = null;
			if (photo.getContent()!=null){
				holder = ImageUtils.readImage(photo.getContent());
			} else if (photo.getContent_file()!=null){
				holder = ImageUtils.readImage(photo.getContent_file());
			}
			ms = System.currentTimeMillis() - ms;
			logger.debug("reading photo: "+ms+"ms");
			if (holder!=null){
				while (entries.hasNext()){
					Entry<String,Integer> e = entries.next();
					File rez = new File(basepath + File.separator + e.getKey() + File.separator + photo.getName());
					double scale_factor = ImageUtils.getScaling(holder.getImage().getWidth(), e.getValue());
					//if scale factor is 1 then saving src file
					ms = System.currentTimeMillis();
					if ((scale_factor<1&&scale_factor>0)||(holder.needEncode())){
						logger.debug("encoded: "+holder.getFormat_name());
						ImageUtils.saveScaledImageWidth(holder.getImage(), scale_factor, rez);
					} else {
						logger.debug("not encoded: "+holder.getFormat_name());
						InputStream is = holder.getInputStream();
						FileUtils.saveToFile(is, rez);
						is.close();
					}
					ms = System.currentTimeMillis() - ms;
					logger.debug("resizing-->"+e.getKey()+": "+ms+"ms");
					files.add(rez);
				}
				int max_width = dimmensions.get(FULL_DIMMENSION_NAME);
				double scale_factor = ImageUtils.getScaling(holder.getImage().getWidth(), max_width);
				if (scale_factor<1&&scale_factor>0){
					return ImageUtils.getDimmension(scale_factor, holder.getImage());
				} else {
					return new Dimension(holder.getImage().getWidth(), holder.getImage().getHeight());
				}
			} else {
				return null;
			}
		} catch (IOException ex) {
			logger.error("saving scaled photo to disk", ex);
			// deleting all files if exception occurs
			for (File f:files){f.delete();}
			//false indicates an error
			return null;
		}
    }

	/**
	 * deletes a photo and all its copies
     * @param photo Photo object
	 * @param dimmensions map that contains name(folder)/resolution of resized photo
	 * @param basepath path of folder where to look for subdirectories
	 * @return
	 */
	public static boolean deletePhoto(Photo photo, Map<String,Integer> dimmensions, String basepath){
		Set<Entry<String,Integer>> entrySet = dimmensions.entrySet();
		Iterator<Entry<String,Integer>> entries = entrySet.iterator();
		while (entries.hasNext()) {
			Entry<String,Integer> e = entries.next();
			File rez = new File(basepath + File.separator + e.getKey() + File.separator + photo.getName());
			if (rez.exists()&&!rez.delete()){
				//one file exists but is not deleted error here :(
				// may be its better try to delete other copies ...
				return false;
			}
		}
		return true;
    }

	/**
	 *
     * @param photo list of photos to copy
	 * @param dimmensions map that contains name(folder)/resolution of resized photo
	 * @param src_path source path to copy
	 * @param dst_path destination path for copy
	 * @param append if true append all files, else only newer
	 * @param remove_copied if true then delete photo from list after copying its file (no delete if no file)
	 * @return listof copied photos
	 */
	public static List<Photo> copyPhoto(Collection<Photo> photo, Map<String,Integer> dimmensions,
			String src_path, String dst_path, boolean append, boolean remove_copied)
	{
		List<Photo> rez = new Vector<Photo>();
		Set<Entry<String,Integer>> entrySet = dimmensions.entrySet();
		Iterator<Entry<String,Integer>> entries = entrySet.iterator();
		Iterator<Photo> photos = photo.iterator();
		try{
			//creating folder for backup if not exits
			//System.out.println("backup_path="+backup_path);
			File dst_folder = new File(dst_path);
			if (!dst_folder.exists()) dst_folder.mkdirs();
			//System.out.println(dst_folder.getCanonicalPath());
			while (entries.hasNext()) {
				Entry<String,Integer> e = entries.next();
				//System.out.println("path="+File.separator+e.getKey());
				File dst_subfolder = new File(dst_path + File.separator + e.getKey());
				if (!dst_subfolder.exists()) dst_subfolder.mkdir();
				//System.out.println(dst_subfolder.getCanonicalPath());
			}
			while (photos.hasNext()){
				Photo p = photos.next();
				boolean copied = false;
				entries = entrySet.iterator();
				while (entries.hasNext()) {
					Entry<String,Integer> e = entries.next();
					File src = new File(src_path + File.separator + e.getKey() + File.separator + p.getName());
					File dst = new File(dst_path + File.separator + e.getKey() + File.separator + p.getName());
					if (src.exists()){
						boolean copy = true;
						if (!append && dst.exists() && (src.lastModified()<=dst.lastModified())){
							copy = false;
							copied = true;
						}
						//logger.info("copy="+copy+"; append="+append+"; src="+src.getCanonicalPath()+"; dst="+dst.getCanonicalPath());
						if (copy){
							FileUtils.copyFile(src, dst);
							copied = true;
							dst.setLastModified(src.lastModified());
						}
					}
				}
				if (remove_copied&&copied){
					rez.add(p);
					photos.remove();
				}
			}
		} catch (FileNotFoundException ex){
			logger.error("backingup photo", ex);
		} catch (IOException ex){
			logger.error("backingup photo", ex);
		}
		return rez;
    }

	/**
	 * the start photo is dimmension is 'full' size
     * @param photo list of photos to copy
	 * @param dimmensions map that contains name(folder)/resolution of resized photo
	 * @param path source path to get photos
	 * @param append if true append all files, else only newer
	 * @return false if folder in path or folder with full pictures does not exists
	 */
	public static boolean resizePhoto(Collection<Photo> photo, Map<String,Integer> dimmensions,
			String path, boolean append)
	{
		Set<Entry<String,Integer>> entrySet = dimmensions.entrySet();
		Iterator<Entry<String,Integer>> entries = entrySet.iterator();
		Vector<Entry<String,Integer>> new_entries = new Vector<Entry<String,Integer>>(entrySet.size());
		while (entries.hasNext()) {
			Entry<String,Integer> e = entries.next();
			if (!e.getKey().equals(FULL_DIMMENSION_NAME)){
				new_entries.add(e);
			}
		}
		entries = new_entries.iterator();
		Iterator<Photo> photos = photo.iterator();
		//if folder not exists return false
		File folder = new File(path);
		File full_folder = new File(path+File.separator+FULL_DIMMENSION_NAME);
		if ((!folder.exists())||(!full_folder.exists()))
			return false;
		//System.out.println(dst_folder.getCanonicalPath());
		while (entries.hasNext()) {
			Entry<String,Integer> e = entries.next();
			//System.out.println("path="+File.separator+e.getKey());
			File subfolder = new File(path + File.separator + e.getKey());
			if (!subfolder.exists()) subfolder.mkdir();
			//System.out.println(dst_subfolder.getCanonicalPath());
		}
		long ms;
		while (photos.hasNext()){
			Photo p = photos.next();
			BufferedImageHolder holder = null;

			File src = new File(path + File.separator + FULL_DIMMENSION_NAME + File.separator + p.getName());
			if (src.exists()){
				int i=0;
				while (new_entries.size()>i) {
					Entry<String,Integer> e = new_entries.get(i);
					File dst = new File(path + File.separator + e.getKey() + File.separator + p.getName());
					if (append || (!dst.exists())){
						try{
							//reading image only once
							if (holder==null) holder = ImageUtils.readImage(src);
							if (holder!=null){
								double scale_factor = ImageUtils.getScaling(holder.getImage().getWidth(), e.getValue());
								//if scale factor is 1 then saving src file
								ms = System.currentTimeMillis();
								if ((scale_factor<1&&scale_factor>0)||(holder.needEncode())){
									ImageUtils.saveScaledImageWidth(holder.getImage(), scale_factor, dst);
								} else {
									InputStream is = holder.getInputStream();
									FileUtils.saveToFile(is, dst);
									is.close();
								}
								ms = System.currentTimeMillis() - ms;
								logger.debug("resizing-->"+e.getKey()+": "+ms+"ms");
							}
						}catch (IOException ex){
							logger.error("re resizing photo", ex);
						}
					}
					i++;
				}
			} else {
				//error
			}
		}
		return true;
    }

}
