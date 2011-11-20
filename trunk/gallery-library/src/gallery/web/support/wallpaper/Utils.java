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

package gallery.web.support.wallpaper;

import common.utils.FileUtils;
import common.utils.ImageUtils;
import common.utils.image.BufferedImageHolder;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import gallery.model.beans.Wallpaper;
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
     * resize wallpaper file to new dimensions(declared in config) and saves it to disk
     * @param wallpaper Wallpaper object
	 * @param dimmensions map that contains name(folder)/resolution of resized wallpaper
	 * @param basepath path of folder where to create subdirectories
	 * @param resized_folder may be null. contains resized copies of wallpaper
	 * @param name may be null. original file name for this wallpaper, used when uploading a pre uploaded wallpaper
	 * @return dimmensions of saved wallpaper
     */
    public static Dimension saveScaledWallpaperFileToDisk(Wallpaper wallpaper, Map<String,Integer> dimmensions, String basepath, File resized_folder, String orig_file_name)
	{
		Set<Entry<String,Integer>> entrySet = dimmensions.entrySet();
		Iterator<Entry<String,Integer>> entries = entrySet.iterator();
		Vector<File> files = new Vector<File>(entrySet.size());
		try {
			long ms = System.currentTimeMillis();
			BufferedImageHolder holder = null;
			if (wallpaper.getContent()!=null){
				holder = ImageUtils.readImage(wallpaper.getContent());
			} else if (wallpaper.getContent_file()!=null){
				holder = ImageUtils.readImage(wallpaper.getContent_file());
			}
			ms = System.currentTimeMillis() - ms;
			logger.debug("reading wallpaper: "+ms+"ms");
			if (holder!=null){
				while (entries.hasNext()){
					Entry<String,Integer> e = entries.next();
					File rez = new File(basepath + File.separator + e.getKey() + File.separator + wallpaper.getName());
					File rezult_candidate = null;
					if (resized_folder!=null){
						rezult_candidate = new File(resized_folder, e.getKey()+File.separator+orig_file_name);
						//logger.debug("pre uploaded "+e.getKey()+" image = "+rezult_candidate.getAbsolutePath());
					}
					if (rezult_candidate!=null&&rezult_candidate.exists()){
						//resized file found - copying
						FileUtils.copyFile(rezult_candidate, rez);
					} else {
						//no resized file found resizing ...
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
			logger.error("saving scaled wallpaper to disk", ex);
			// deleting all files if exception occurs
			for (File f:files){f.delete();}
			//false indicates an error
			return null;
		}
    }

    /**
     * resize wallpaper file to all dimensions( and saves it to disk
     * @param image_file file with image to resize
	 * @param dimmensions map that contains name(folder)/resolution for resized images
	 * @param result_dir path of folder where to create subdirectories with resulting images
	 * @param resized_folder may be null. contains resized copies of wallpaper
	 * @param name may be null. original file name for this wallpaper, used when uploading a pre uploaded wallpaper
	 * @return true if succeed
     */
    public static boolean saveScaledImageFileToDisk(File image_file, Map<String,Integer> dimmensions, File result_dir)
	{
		Set<Entry<String,Integer>> entrySet = dimmensions.entrySet();
		Iterator<Entry<String,Integer>> entries = entrySet.iterator();
		Vector<File> files = new Vector<File>(entrySet.size());
		try {
			long ms = System.currentTimeMillis();
			BufferedImageHolder holder = ImageUtils.readImage(image_file);
			ms = System.currentTimeMillis() - ms;
			logger.debug("reading image: "+ms+"ms");
			if (holder!=null){
				while (entries.hasNext()){
					Entry<String,Integer> e = entries.next();
					File rez = new File(result_dir, e.getKey() + File.separator + image_file.getName());

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
				return true;
			} else {
				return false;
			}
		} catch (IOException ex) {
			logger.error("saving scaled image to disk", ex);
			// deleting all files if exception occurs
			for (File f:files){f.delete();}
			//false indicates an error
			return false;
		}
    }

	/**
	 * deletes a wallpaper and all its copies
     * @param wallpaper Wallpaper object
	 * @param dimmensions map that contains name(folder)/resolution of resized wallpaper
	 * @param basepath path of folder where to look for subdirectories
	 * @param resolution_directories directories for wallpapers resized to user resolutions
	 * @return
	 */
	public static boolean deleteWallpaper(String file_name, Map<String,Integer> dimmensions, String basepath, List<File> resolution_directories){
		Set<Entry<String,Integer>> entrySet = dimmensions.entrySet();
		Iterator<Entry<String,Integer>> entries = entrySet.iterator();
		while (entries.hasNext()) {
			Entry<String,Integer> e = entries.next();
			File rez = new File(basepath + File.separator + e.getKey() + File.separator + file_name);
			if (rez.exists()&&!rez.delete()){
				rez.deleteOnExit();
				//one file exists but is not deleted error here :(
				// may be its better try to delete other copies ...
				return false;
			}
		}
		for (File f:resolution_directories){
			File image = new File(f, file_name);
			if (image.exists()&&!image.delete()){
				image.deleteOnExit();
			}
		}
		return true;
    }

	/**
	 * reames a wallpaper and all its copies
     * @param wallpaper Wallpaper object
	 * @param dimmensions map that contains name(folder)/resolution of resized wallpaper
	 * @param basepath path of folder where to look for subdirectories
	 * @param resolution_directories directories for wallpapers resized to user resolutions
	 * @return
	 */
	public static boolean renameWallpaper(String new_file_name, String old_file_name, Map<String,Integer> dimmensions, String basepath, List<File> resolution_directories){
		Iterator<Entry<String,Integer>> entries = dimmensions.entrySet().iterator();
		boolean ok_1 = true;
		while (entries.hasNext()) {
			Entry<String,Integer> e = entries.next();
			File old_file = new File(basepath + File.separator + e.getKey() + File.separator + old_file_name);
			File new_file = new File(basepath + File.separator + e.getKey() + File.separator + new_file_name);
			if (ok_1&&new_file.exists()){
				//because new file is allways uniq, delete new_file if exists
				ok_1 = ok_1 && new_file.delete();
			}
			if (ok_1&&old_file.exists()){
				ok_1 = ok_1 && old_file.renameTo(new_file);
			}
		}
		boolean ok_2 = true;
		if (ok_1){
			for (File f:resolution_directories){
				File old_file = new File(f, old_file_name);
				File new_file = new File(f, new_file_name);
				if (ok_2&&new_file.exists()){
					//because new file is allways uniq, delete new_file if exists
					ok_2 = ok_2 && new_file.delete();
				}
				if (ok_2&&old_file.exists()){
					ok_2 = ok_2 && old_file.renameTo(new_file);
				}
			}
		}
		//reverting changes in case of an error
		if (!ok_1||!ok_2){
			entries = dimmensions.entrySet().iterator();
			while (entries.hasNext()) {
				Entry<String,Integer> e = entries.next();
				File old_file = new File(basepath + File.separator + e.getKey() + File.separator + old_file_name);
				File new_file = new File(basepath + File.separator + e.getKey() + File.separator + new_file_name);
				if (new_file.exists()){
					new_file.renameTo(old_file);//TODO: mb make some checking here
				}
			}
		}
		if (!ok_2){
			for (File f:resolution_directories){
				File old_file = new File(f, old_file_name);
				File new_file = new File(f, new_file_name);
				if (ok_2&&new_file.exists()){
					new_file.renameTo(old_file);//TODO: mb make some checking here
				}
			}
		}
		return ok_1&&ok_2;
    }

	/**
	 *
     * @param wallpaper list of wallpapers to copy
	 * @param dimmensions map that contains name(folder)/resolution of resized wallpaper
	 * @param src_path source path to copy
	 * @param dst_path destination path for copy
	 * @param append if true append all files, else only newer
	 * @param remove_copied if true then delete wallpaper from list after copying its file (no delete if no file)
	 * @return list of copied wallpapers
	 */
	public static List<Wallpaper> copyWallpaper(Collection<Wallpaper> wallpaper, Map<String,Integer> dimmensions,
			String src_path, String dst_path, boolean append, boolean remove_copied)
	{
		List<Wallpaper> rez = new Vector<Wallpaper>();
		Set<Entry<String,Integer>> entrySet = dimmensions.entrySet();
		Iterator<Entry<String,Integer>> entries = entrySet.iterator();
		Iterator<Wallpaper> wallpapers = wallpaper.iterator();
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
			while (wallpapers.hasNext()){
				Wallpaper p = wallpapers.next();
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
					wallpapers.remove();
				}
			}
		} catch (FileNotFoundException ex){
			logger.error("backing up wallpaper", ex);
		} catch (IOException ex){
			logger.error("backing up wallpaper", ex);
		}
		return rez;
    }

	/**
	 * the start wallpaper is dimmension is 'full' size
     * @param wallpaper list of wallpapers to copy
	 * @param dimmensions map that contains name(folder)/resolution of resized wallpaper
	 * @param path source path to get wallpapers
	 * @param append if true append all files, else only newer
	 * @return false if folder in path or folder with full pictures does not exists
	 */
	public static boolean resizeWallpaper(Collection<Wallpaper> wallpaper, Map<String,Integer> dimmensions,
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
		Iterator<Wallpaper> wallpapers = wallpaper.iterator();
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
		while (wallpapers.hasNext()){
			Wallpaper p = wallpapers.next();
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
							logger.error("re resizing wallpaper", ex);
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
