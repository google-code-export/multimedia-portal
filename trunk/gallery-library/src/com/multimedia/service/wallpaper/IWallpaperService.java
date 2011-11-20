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

package com.multimedia.service.wallpaper;

import common.beans.IOutputStreamHolder;
import gallery.model.beans.Wallpaper;
import common.services.generic.IGenericService;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IWallpaperService extends IGenericService<Wallpaper, Long>{
	/**
	 * creates a file with its content (i.e. jpg image)
	 * and set's width and height of wallpaper
	 * @param p wallpaper to save
	 * @return true if ok
	 */
	public boolean getImage(Wallpaper p);

	/**
	 *if folder contains appropriate subfolders
	 *	before resizing wallpaper checks for resized images in these subfolders
	 * @param p wallpaper to save
	 * @return true if ok
	 */
	public boolean getImage(Wallpaper p, File folder);

	/**
	 * creates an uniq name for a wallpaper
	 * i.e. name that is not currently in use,
	 * contains lowercase latin letters and some spec symbols,
	 * and ends with .jpg
	 * @param old_name
	 * @return
	 */
	public String getUniqName(String old_name);

	/**
	 * deletes all copies of this wallpaper (files with resized wallpaper)
	 * @param p wallpaper to delete (must have at least name property)
	 * @return false if error
	 */
	public boolean deleteFiles(Wallpaper p);

	/**
	 * renames all copies of this wallpaper (files with resized wallpaper)
	 * and fixes a name property if it contains deprecated characters or is not uniq
	 * @param p wallpaper to delete (must have at least name property)
	 * @return false if error
	 */
	public boolean renameFiles(Wallpaper p);

	/**
	 * get all active wallpapers with given id_pages
	 * one wallpaper from each sublist
	 * @param id_pages id of pages
	 * @return list of wallpapers
	 */
	public List<Wallpaper> getMainImages(List<Long> id_pages, int count);

	/**
	 * get all active wallpapers with given id_pages
	 * @param first_num first number to select
	 * @param quantity quantity of items to select
	 * @param id_pages
	 * @return list of wallpapers
	 */
	public List<Wallpaper> getWallpapersPaginated(int first_num, int quantity, Long id_pages);

	/**
	 * get all active wallpapers with given id_pages
	 * @param first_num first number to select
	 * @param quantity quantity of items to select
	 * @param id_pages
	 * @return list of wallpapers
	 */
	public List<Wallpaper> getWallpapersPaginated(int first_num, int quantity, Long[] id_pages);

	/**
	 * get all active wallpapers with given id_pages
	 * @param id an id of wallpaper to start selecting
	 * @param quantity quantity of items to select (if less then 0 selecting  left greater then 0 selecting right)
	 * @param id_pages
	 * @return list of wallpapers
	 */
	public List<Wallpaper> getWallpapersPaginatedId(Long id, int quantity, Long id_pages);

	/**
	 * get all active wallpapers with given id_pages
	 * @param id_pages
	 * @return quantity of wallpapers
	 */
	public Long getWallpapersRowCount(Long id_pages);

	/**
	 * saves the wallpaper with given id and resized to new_width to given os
	 * @param id_wallpaper id of wallpaper to save
	 * @param new_width width to resize wallpaper
	 * @param new_height height to resize wallpaper
	 * @param os where to save result
	 * @return true if wallpapers found
	 */
	public boolean getResizedWallpaperStream(Long id_wallpaper, Integer new_width, Integer new_height, OutputStream os) throws IOException;

	/**
	 * first checks whether a photo with such name realy exists
	 * saves the wallpaper with given name and resized to new_width to given os
	 * caches image to a given folder (resized_path)
	 * @param name_wallpaper name of wallpaper
	 * @param new_width width to resize wallpaper
	 * @param new_height height to resize wallpaper
	 * @param ohs where to save result, fasade
	 * @return true if wallpapers found
	 */
	public boolean getResizedWallpaperStream(String name_wallpaper, Integer new_width, Integer new_height, IOutputStreamHolder osh) throws IOException;

	/**
	 * execute a backup of wallpapers directory is setted in services
	 * @param wallpapers list of wallpapers t backup
	 * @param append if true append all files / else new files
	 * @param files_only backup only files
	 * @return list of backuped wallpapers, wallpapers param contains only unbackuped wallpapers
	 */
	public List<Wallpaper> backupWallpapers(List<Wallpaper> wallpapers, boolean append, boolean files_only);

	/**
	 * execute a restore of wallpapers directory is setted in services
	 * @param wallpapers list of wallpapers to restore
	 * @param append if true append all files / else new files
	 * @param files_only restore only files
	 * @return list of restore wallpapers, wallpapers param contains only unrestored wallpapers
	 */
	public List<Wallpaper> restoreWallpapers(List<Wallpaper> wallpapers, boolean append, boolean files_only);

	public String getUploadPath();

	/**
	 * directory where wallpapers are stored(to show in web application)
	 * @return path to directory
	 */
	public String getStorePath();

	/**
	 * get dimmensions for this wallpaper service
	 * @return map containing dir name(key) and width(value) of dimmension
	 */
	public Map<String,Integer> getDimmensions();

	/**
	 * get number of wallpapers
	 * @param p an entity whitch number you seek
	 * @return number of entity in resultset returned by getWallpapersPaginatedId() method
	 */
	public Long getWallpaperNumber(Wallpaper p);

	/**
	 * get quantity of random wallpapers from DB
	 * @param quantity quantity of wallpapers to get
	 * @return list of wallpapers
	 */
	public List<Wallpaper> getRandomWallpapers(int quantity);

	/**
	 * get tags from all active wallpapers, tags area created by parsing 'tags' field of wallpapers
	 * @param maxTags max tag quantity to be returned
	 * @return tags from all active wallpapers
	 */
	public Map<String, Double> getTags(int maxTags);

	public void enableResolutionFilter(int width, int height);

	public void disableResolutionFilter();

	/**
	 *
	 * @return last modified for a file of wallpaper with specified name or -1 if not found
	 */
	public long getWallpaperLastModified(String name);

}
