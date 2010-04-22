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

import common.cms.services.ICmsService;
import gallery.model.beans.Pages;
import gallery.model.beans.Photo;
import gallery.model.misc.StatusBean;
import java.util.List;
import security.beans.User;
import gallery.model.misc.UploadBean;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IPhotoServiceCms extends ICmsService<Photo, Long>{
	/**
	 * creates folders with hierarchycal structure like in database
	 * @return list of folders
	 */
	public List<UploadBean> createPageFolders();

	/**
	 * creates folders with hierarchycal structure like in database
	 * @return list of folders
	 */
	public List<Pages> getCategoriesLayered();

	/**
	 * uploads photos
	 * @param uploader user that uploads these photos
	 * @param id_pages all photos from root dir are uploaded to this page(nowhere if null)
	 * @param usb for monitoring of upload process
	 * @return quantity of photos added
	 */
	public long uploadPhotos(User uploader, Long id_pages, StatusBean usb);

	/**
	 * deletes photos with given id
	 * @param id
	 * @return 
	 */
	public int deleteById(Long[] id);

	/**
	 * get all photos id with given id_pages
	 * if id == null get all photos
	 * @param id id_pages
	 * @return list of photos id
	 */
	public List<Long> getPhotosInCategory(Long id);

	/**
	 * @param append_all if true deletes all existing non full photos and makes re-resize
	 */
	public void reResizePhotos(boolean append_all);

	/**
	 * generates new title, tags ... for photos
	 * @param id id of photos to handle(optimize)
	 */
	//public void optimizePhoto(Long[] id);

	/**
	 * generates new title, tags ... for photos
	 * @param id id of photos to handle(optimize)
	 */
	public void optimizePhoto(Long id);

	/**
	 * generates new title, tags ... for photos
	 * @param id_pages id of catalogues(pages) to handle(optimize)
	 */
	public void optimizePhotoCategories(Long[] id_pages);

	/**
	 * change optimization for photos in pages with given ids
	 * @param id_pages id of catalogues(pages) to handle(optimize)
	 * @param optimized optimized flag to be set
	 */
	public void setPhotoOptimizationCategories(Long[] id_pages, Boolean optimized);

	/**
	 * read all photo files, and change their resolution in database to file's
	 * @return count of handled photos
	 */
	public long renewResolution(StatusBean sb);
}
