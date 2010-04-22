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

package gallery.service.photo;

import gallery.model.beans.Photo;
import common.services.generic.IGenericService;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IPhotoService extends IGenericService<Photo, Long>{
	/**
	 * creates a file with its content (i.e. jpg image)
	 * and set's width and height of photo
	 * @param p photo to save
	 * @return true if ok
	 */
	public boolean getPhoto(Photo p);

	/**
	 * deletes all copies of this photo (files with resized photo)
	 * @param p photo to delete (must have at least name property)
	 * @return false if error
	 */
	public boolean deleteFiles(Photo p);

	/**
	 * get all active photos with given id_pages
	 * one photo from each sublist
	 * @param id_pages id of pages
	 * @return list of photos
	 */
	public List<Photo> getMainPhotos(List<Long> id_pages, int count);

	/**
	 * get all active photos with given id_pages
	 * @param first_num first number to select
	 * @param quantity quantity of items to select
	 * @param id_pages
	 * @return list of photos
	 */
	public List<Photo> getPhotosPaginated(int first_num, int quantity, Long id_pages);

	/**
	 * get all active photos with given id_pages
	 * @param first_num first number to select
	 * @param quantity quantity of items to select
	 * @param id_pages
	 * @return list of photos
	 */
	public List<Photo> getPhotosPaginated(int first_num, int quantity, Long[] id_pages);

	/**
	 * get all active photos with given id_pages
	 * @param id an id of photo to start selecting
	 * @param quantity quantity of items to select (if less then 0 selecting  left greater then 0 selecting right)
	 * @param id_pages
	 * @return list of photos
	 */
	public List<Photo> getPhotosPaginatedId(Long id, int quantity, Long id_pages);

	/**
	 * get all active photos with given id_pages
	 * @param id_pages
	 * @return quantity of totalPages
	 */
	public Long getPhotosRowCount(Long id_pages);

	/**
	 * saves the photo with given id and resized to new_width to given os
	 * @param id_photo id of photo to save
	 * @param new_width width to whitch resize photo
	 * @param new_height height to resize photo
	 * @param os where to save result
	 * @return true if no error occur
	 */
	public boolean getResizedPhotoStream(Long id_photo, Integer new_width, Integer new_height, OutputStream os);

	/**
	 * execute a backup of photos directory is setted in services
	 * @param photos list of photos t backup
	 * @param append if true append all files / else new files
	 * @param files_only backup only files
	 * @return list of backuped photos, photos param contains only unbackuped photos
	 */
	public List<Photo> backupPhotos(List<Photo> photos, boolean append, boolean files_only);

	/**
	 * execute a restore of photos directory is setted in services
	 * @param photos list of photos to restore
	 * @param append if true append all files / else new files
	 * @param files_only restore only files
	 * @return list of restore photos, photos param contains only unrestored photos
	 */
	public List<Photo> restorePhotos(List<Photo> photos, boolean append, boolean files_only);

	public String getUploadPath();

	/**
	 * directory where photos are stored(to show in web application)
	 * @return path to directory
	 */
	public String getStorePath();

	/**
	 * get dimmensions for this photo service
	 * @return map containing dir name(key) and width(value) of dimmension
	 */
	public Map<String,Integer> getDimmensions();

	/**
	 * get number of photo
	 * @param p an entity whitch number you seek
	 * @return number of entity in resultset returned by getPhotosPaginatedId() method
	 */
	public Long getPhotoNumber(Photo p);

	/**
	 * get quantity of random photos from DB
	 * @param quantity quantity of photos to get
	 * @return list of photos
	 */
	public List<Photo> getRandomPhotos(int quantity);

	/**
	 * get tags from all active photos, tags area created by parsing 'tags' field of photos
	 * @param maxTags max tag quantity to be returned
	 * @return tags from all active photos
	 */
	public Map<String, Double> getTags(int maxTags);

	public void enableResolutionFilter(int width, int height);

	public void disableResolutionFilter();

}
