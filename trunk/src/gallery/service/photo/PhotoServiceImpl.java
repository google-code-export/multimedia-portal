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

import common.utils.FileUtils;
import common.utils.ImageUtils;
import gallery.model.beans.Photo;
import common.services.generic.GenericServiceImpl;
import gallery.web.support.photo.Utils;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import org.hibernate.ScrollableResults;
import org.springframework.core.io.Resource;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PhotoServiceImpl extends GenericServiceImpl<Photo, Long> implements IPhotoService{
	/** specifies the path to folder with photos within server */
	private String path;
	/** specifies the path to folder where to backup photos */
	private String backup_path;
	/** specifies the path to folder where to get photos for multi upload photos */
	private String upload_path;
	/** are paths to folders with resized images i.e. small or medium */
	private Map<String,Integer> dimensions;
	/** forbidden words for tag cloud. tags with this words will not be added to the tag cloud */
	private Set<String> black_word_list;

	@Override
	public void init(){
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(path, "path", sb);
		common.utils.MiscUtils.checkNotNull(backup_path, "backup_path", sb);
		common.utils.MiscUtils.checkNotNull(upload_path, "upload_path", sb);
		if (black_word_list==null){
			black_word_list = java.util.Collections.emptySet();
		}
		if (common.utils.MiscUtils.checkNotNull(dimensions, "dimensions", sb)){
			Set<Entry<String,Integer>> entrySet = dimensions.entrySet();
			Iterator<Entry<String,Integer>> entries = entrySet.iterator();
			while (entries.hasNext()) {
				Entry<String,Integer> e = entries.next();
				//System.out.println("path="+File.separator+e.getKey());
				File dst_subfolder = new File(path + File.separator + e.getKey());
				if (!dst_subfolder.exists()) dst_subfolder.mkdir();
				//System.out.println(dst_subfolder.getCanonicalPath());
			}
		}
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	public static final String[] ORDER_BY = new String[]{"id"};
	public static final String[] ORDER_HOW = new String[]{"ASC"};
	public static final String[] ORDER_HOW_REVERSE = new String[]{"DESC"};

	public void setBlackWords(Set value){this.black_word_list = value;}

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
				throw new NullPointerException("image folder not found for PhotoServiceImpl");
		} catch (IOException e){
			path = null;
			throw new NullPointerException("image folder not found for PhotoServiceImpl");
		}
		//System.out.println("----------------------------path = "+this.path);
	}

	public void setBackup_path(Resource res) {
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
				backup_path = f.getCanonicalPath() + "/";
			else
				throw new NullPointerException("backup folder not found for PhotoServiceImpl");
		} catch (IOException e){
			backup_path = null;
			throw new NullPointerException("backup folder not found for PhotoServiceImpl");
		}
		//System.out.println("----------------------------path = "+this.path);
	}

	public void setUpload_path(Resource res) {
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
				upload_path = f.getCanonicalPath() + "/";
			else
				throw new NullPointerException("backup folder not found for PhotoServiceImpl");
		} catch (IOException e){
			upload_path = null;
			throw new NullPointerException("backup folder not found for PhotoServiceImpl");
		}
		//System.out.println("----------------------------path = "+this.path);
	}

	@Override
	public boolean getPhoto(Photo p) {
		//1-st generating an uniq name
		//String name = System.nanoTime() + postfix;
		String orig_file_name = null;
		if (p.getContent()!=null){
			orig_file_name = p.getContent().getOriginalFilename();
		} else if (p.getContent_file()!=null)
			orig_file_name = p.getContent_file().getAbsolutePath();

		int pos = orig_file_name.lastIndexOf(".");
		if (pos>0){
			String ext = ".jpg";
			orig_file_name = orig_file_name.substring(0, pos);
			String old_name = FileUtils.checkFileNameSpelling(orig_file_name);
			//2-nd checking name
			orig_file_name = old_name + ext;
			int i = 0;
			while (dao.getRowCount("name", orig_file_name)>0){
				orig_file_name = old_name + "_" + i + ext;
				i++;
			}
			//setting name
			p.setName(orig_file_name);
			p.setDate_upload(new Date());
			//3-rd creating file
			//p.getContent().transferTo(tmp);
			Dimension d = gallery.web.support.photo.Utils.saveScaledPhotoFileToDisk(p, dimensions, path);
			if (d != null){
				//if all files created saving photo instance to database
				//detecting type
				p.setType("unknown");
				//set resolution
				p.setWidth(d.width);
				p.setHeight(d.height);
				return true;
			}
		}
		return false;
	}

	public static final String[] PHOTOS_WHERE = new String[]{"id_pages","active"};

	/**
     * get random photos from given with given
	 * @param id_pages
	 * @return
	 */
	@Override
	public List<Photo> getMainPhotos(List<Long> id_pages, int count) {
		Object[][] values = new Object[][]{null, new Object[]{Boolean.TRUE}};

		Long[] id_pages_a = new Long[id_pages.size()];
		values[0] = id_pages.toArray(id_pages_a);
		int	size = dao.getRowCount(PHOTOS_WHERE, values).intValue();

		Random r = new Random();
		List<Photo> temp_photo;
        HashSet<Integer> generated = new HashSet<Integer>(count+1);
        //generating list of uniq values from 0 to count
        if (size>count){
            List<Photo> rez = new Vector<Photo>();
            for (int i=0;i<count;i++){
                Integer num = r.nextInt(size);
                while (generated.contains(num)){
                    num = r.nextInt(size);
                }
                generated.add(num);
				temp_photo = dao.getByPropertiesValuesPortionOrdered(null, null, PHOTOS_WHERE, values, num, 1, null, null);
				rez.add(temp_photo.get(0));
            }
            return rez;
		}else{
			return dao.getByPropertiesValuesPortionOrdered(null, null, PHOTOS_WHERE, values, 0, -1, null, null);
		}
	}

	/**
	 * are paths to folders with resized images i.e. small or medium
	 * @param dimensions the dimensions to set
	 */
	public void setDimensions(Map<String,Integer> dimensions) {
		this.dimensions = dimensions;
	}

	public static final String[] PHOTOS_PAGINATED_WHERE = new String[]{"id_pages","active"};
	@Override
	public List<Photo> getPhotosPaginated(int first_num, int quantity, Long id_pages) {
		return dao.getByPropertiesValuePortionOrdered(null, null, PHOTOS_PAGINATED_WHERE, new Object[]{id_pages,Boolean.TRUE}, first_num, quantity, ORDER_BY, ORDER_HOW);
	}

	@Override
	public List<Photo> getPhotosPaginated(int first_num, int quantity, Long[] id_pages) {
		return dao.getByPropertiesValuePortionOrdered(null, null, PHOTOS_PAGINATED_WHERE, new Object[][]{id_pages,new Object[]{Boolean.TRUE}}, first_num, quantity, ORDER_BY, ORDER_HOW);
	}

	public static final String[] RELATIONS_ASC = new String[]{"=","=",">"};
	public static final String[] RELATIONS_DESC = new String[]{"=","=","<"};
	public static final String[] PHOTOS_PAGINATED_RELATIONS_WHERE = new String[]{"id_pages","active","id"};
	@Override
	public List<Photo> getPhotosPaginatedId(Long id, int quantity, Long id_pages){
		if (quantity>0){
			return dao.getByPropertiesValuePortionOrdered(null, null, PHOTOS_PAGINATED_RELATIONS_WHERE, new Object[]{id_pages,Boolean.TRUE, id}, RELATIONS_ASC, 0, quantity, ORDER_BY, ORDER_HOW);
		} else {
			return dao.getByPropertiesValuePortionOrdered(null, null, PHOTOS_PAGINATED_RELATIONS_WHERE, new Object[]{id_pages,Boolean.TRUE, id}, RELATIONS_DESC, 0, -quantity, ORDER_BY, ORDER_HOW_REVERSE);
		}
	}

	@Override
	public Long getPhotosRowCount(Long id_pages) {
		return dao.getRowCount(PHOTOS_PAGINATED_WHERE, new Object[]{id_pages,Boolean.TRUE});
	}

	public static final String[] NAME = new String[]{"name"};
	@Override
	public boolean getResizedPhotoStream(Long id_photo, Integer new_width, Integer new_height, OutputStream os) {
		List<Photo> photo = dao.getByPropertyValuePortionOrdered(NAME, NAME, "id", id_photo, 0, -1, null, null);
		if (photo.size()>0){
			File src = new File(path + File.separator + "full" + File.separator + photo.get(0).getName());
			if (src.exists())
				try {
					//ImageUtils.saveScaledImageWidth(rez, new_width, os);
					ImageUtils.getScaledImageDimmension(src, new_width, new_height, os);
					return true;
				} catch (IOException ex) {
					logger.error("while resizing photo", ex);
				}
		}
		return false;
	}

	public static final String[] MULTI_DELETE_PSEUDO = {"id","name"};
	@Override
	public int deleteById(Long[] id) {
		if (id!=null){
			List<Photo> photos = dao.getByPropertyValues(MULTI_DELETE_PSEUDO,"id", id);
			for (int i=0;i<photos.size();i++){
				deleteFiles(photos.get(i));
			}
			return dao.deleteById(id);
		} else{
			return -1;
		}
	}

	@Override
	public int deleteById(Long id) {
		Photo p = dao.getById(id);
		if (deleteFiles(p)){
			return dao.deleteById(id);
		}else{
			return -1;
		}
	}

	@Override
	public boolean deleteFiles(Photo p) {
		return gallery.web.support.photo.Utils.deletePhoto(p, dimensions, path);
	}

	@Override
	public List<Photo> backupPhotos(List<Photo> photos, boolean append, boolean only_files) {
		if (!only_files){
			throw new NullPointerException("not supported yet");
		}
		return Utils.copyPhoto(photos, dimensions, path, backup_path, false, true);
	}

	@Override
	public List<Photo> restorePhotos(List<Photo> photos, boolean append, boolean only_files) {
		if (!only_files){
			throw new NullPointerException("not supported yet");
		}
		return Utils.copyPhoto(photos, dimensions, backup_path, path, false, true);
	}

	@Override
	public String getUploadPath() {return upload_path;}

	@Override
	public String getStorePath(){return path;}

	@Override
	public Map<String,Integer> getDimmensions(){return dimensions;}

	@Override
	public Long getPhotoNumber(Photo p) {
		//1-st get values for sort
		Object[] obj = new Object[ORDER_BY.length];
		Map m = Photo.toMap(p);
		for (int i=0;i<ORDER_BY.length;i++){
			obj[i] = m.get(ORDER_BY[i]);
		}
		return (Long)dao.getRowNumber(obj, ORDER_BY, ORDER_HOW, PHOTOS_PAGINATED_WHERE, new Object[]{p.getId_pages(), Boolean.TRUE});
	}

	public static final String[] RANDOM_PHOTOS_PROPERTIES = new String[]{"id","id_pages","name","title"};
	@Override
	public List<Photo> getRandomPhotos(int quantity) {
		List ids = dao.getSinglePropertyOrderRand("id", "active", Boolean.TRUE, 0, quantity);
		return dao.getByPropertyValues(RANDOM_PHOTOS_PROPERTIES, "id", ids);
	}

	@Override
	public Map<String, Double> getTags(int maxTags) {
		ScrollableResults photo_tags = dao.getScrollableResults("tags", "active", Boolean.TRUE, null, null);
		Map<String, Double> tags = new HashMap<String, Double>();
		if (photo_tags.first()){
			String tag;
			Double score;
			String[] tags_parsed;
			String tag_parsed;
			do{
				tag = photo_tags.getString(0);
				if (tag!=null){
					tags_parsed = tag.split(",");
					for(int i=1;i<tags_parsed.length;i++){
						tag_parsed = tags_parsed[i].trim();
						if (!black_word_list.contains(tag_parsed)){
							score = tags.get(tag_parsed);
							if (score==null){
								tags.put(tag_parsed, new Double(1.0));
							}else{
								tags.put(tag_parsed, (score+1));
							}
						}
					}
				}
			}while(photo_tags.next());
		}
		photo_tags.close();
		//keeping only maxTags quantity
		Set<Entry<String,Double>> i = tags.entrySet();
		List<Entry<String,Double>> l = new LinkedList(i);
		java.util.Collections.sort(l, new PhotoServiceImpl.EntryComparatorDesc());

		for (int j=maxTags;j<l.size();j++){
			tags.remove(l.get(j).getKey());
		}
		return tags;
	}

	/*@Override
	public Photo getById(Long id) {
		Photo p = super.getById(id);
		System.out.println("number = " + dao.getRowNumber(new Object[]{p.getId()}, ORDER_BY, ORDER_HOW, "active", Boolean.TRUE));
		return p;
	}*/


	/**
	 * Compares two tags by score in descending order
	 */
	static public class EntryComparatorDesc implements Comparator<Entry<String,Double>> {

		@Override
		public int compare(Entry<String,Double> o1, Entry<String,Double> o2) {
			int scoreComparison = Double.compare(o2.getValue(), o1.getValue());
			
			// if the score is the same sort by name
			if (scoreComparison == 0) {
				return String.CASE_INSENSITIVE_ORDER.compare(o2.getKey(), o1.getKey());
			} else {
				return scoreComparison;
			}
		}
		
	}

	public static String[] RESOLUTION_NAME = new String[]{"width", "height"};
	@Override
	public void enableResolutionFilter(int width, int height){
		dao.enableFilter("resolution_id", RESOLUTION_NAME, new Object[]{width, height});
		//dao.enableFilter("resolution_id", null, null);
	}

	@Override
	public void disableResolutionFilter() {
		dao.disableFilter("resolution_id");
	}

}