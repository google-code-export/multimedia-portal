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

import gallery.model.command.MultiPhotoCms;
import common.beans.IFilterBean;
import common.beans.IMultiupdateBean;
//import common.utils.FileUtils;
import common.utils.FileUtils;
import common.utils.ImageUtils;
import gallery.model.active.PagesCombobox;
import gallery.model.beans.Pages;
import gallery.model.beans.Photo;
import gallery.model.command.FilterPhotoCms;
import gallery.model.misc.StatusBean;
import gallery.service.pages.IPagesService;
import gallery.service.resolution.IResolutionService;
import java.io.File;
//import java.io.IOException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;
import security.beans.User;
import gallery.model.misc.UploadBean;
import gallery.service.pagesPseudonym.IPagesPseudonymService;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;
import org.hibernate.ScrollableResults;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PhotoServiceCmsImpl implements IPhotoServiceCms{
	protected IPhotoService photo_service;
	protected IResolutionService resolution_service;
	protected IPagesService pages_service;
	protected IPagesPseudonymService pages_pseudo_service;

	public static final String[] ORDER_BY = null;
	public static final String[] ORDER_HOW = null;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(photo_service, "photo_service", sb);
		common.utils.MiscUtils.checkNotNull(resolution_service, "resolution_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_pseudo_service, "pages_pseudo_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	public void setService(IPhotoService value){this.photo_service = value;}
	public void setResolution_service(IResolutionService value){this.resolution_service = value;}
	public void setPages_service(IPagesService value){this.pages_service = value;}
	public void setPagesPseudonym_service(IPagesPseudonymService value){this.pages_pseudo_service = value;}

	String[] s = new String[]{"id","name","id_pages","active", "optimized", "width", "height"};
	@Override
	public List<Photo> getAllShortCms() {
		return photo_service.getAllShortOrdered(s, ORDER_BY, ORDER_HOW);
	}

	@Override
	public int updateObjectArrayShortById(String[] propertyNames, Long[] idValues, Object[]... propertyValues) {
		return photo_service.updateObjectArrayShortById(propertyNames, idValues, propertyValues);
	}

	@Override
	public IMultiupdateBean getMultiupdateBean(int size) {return new MultiPhotoCms(size);}

	@Override
	public int deleteById(Long id){return photo_service.deleteById(id);}

	@Override
	public Photo getUpdateBean(Long id) {return photo_service.getById(id);}

	@Override
	public Photo getInsertBean() {
		Photo p = new Photo();
		p.setActive(true);
		return p;
	}

	@Override
	public Map initUpdate() {
		HashMap m = new HashMap();
		m.put("categories_photo_select", pages_service.getAllCombobox(null, Boolean.TRUE, gallery.web.controller.pages.types.WallpaperGalleryType.TYPE));
		return m;
	}

	@Override
	public Map initInsert() {
		HashMap m = new HashMap();
		m.put("categories_photo_select", pages_service.getAllCombobox(null, Boolean.TRUE, gallery.web.controller.pages.types.WallpaperGalleryType.TYPE));
		return m;
	}

	@Override
	public int update(Photo command) {
		if (command.getContent()==null){
			//we just need to update rows in database
			photo_service.save(command);
		}else{
			//1-st delete old photos
			if (photo_service.deleteFiles(command)&&photo_service.getPhoto(command)){
				//2-nd update rows in database, create new, and count new resolution
				photo_service.save(command);
			}else{
				return -1;
			}
		}
		return 1;
	}

	@Override
	public boolean insert(Photo obj) {
		if (photo_service.getPhoto(obj)){
			photo_service.save(obj);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map initFilter() {
		HashMap m = new HashMap();
		//m.put("categories_photo_select", pages_service.getAllCombobox(null, null, gallery.web.controller.pages.types.WallpaperGalleryType.TYPE));
		m.put("categories_photo_select", new PagesCombobox(null, pages_service));
		return m;}

	@Override
	public IFilterBean getFilterBean() {return new FilterPhotoCms();}

	@Override
	public List<Photo> getFilteredByPropertyValue(String propertyName, Object propertyValue) {
		return photo_service.getShortByPropertyValueOrdered(s, propertyName, propertyValue, ORDER_BY, ORDER_BY);
	}

	@Override
	public List<Photo> getFilteredByPropertiesValue(String[] propertyName, Object[] propertyValue) {
		return photo_service.getShortByPropertiesValueOrdered(s, propertyName, propertyValue, ORDER_BY, ORDER_BY);
	}

	public static final String[] FOLDERS_PSEUDO = {"id","name"};
	/** name of file with  */
	public static final String DESCRIPTION_FILE = "name.txt";
	@Override
	public List<UploadBean> createPageFolders() {
		//getting layered pages
		List<Pages> pages = getCategoriesLayered();
		//creating vector for results
		List<UploadBean> rez = new Vector<UploadBean>();
		//creating an upload dir if it doesn't exists
		File upload_dir = new File(photo_service.getUploadPath());
		if (!upload_dir.exists()) upload_dir.mkdirs();
		//for saving parent catalogues
		Long last_layer = new Long(-1);
		LinkedList<File> parents = new LinkedList<File>();
		parents.addLast(upload_dir);
		for (Pages p:pages){
			while (p.getLayer()<=last_layer){
				//removing last parent
				parents.removeLast();
				last_layer--;
			}
			boolean b = false;
			File cur_dir = new File(parents.getLast(), String.valueOf(p.getId())+"_"+FileUtils.toTranslit(p.getName()));
			if (cur_dir.exists()){
				if (!cur_dir.isDirectory()){
					cur_dir.delete();
					b = cur_dir.mkdir();
				}
			}else{
				b = cur_dir.mkdir();
			}
			File description = new File(cur_dir, DESCRIPTION_FILE);
			if (!description.exists()){
				try {
					description.createNewFile();
					OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(description), "UTF-8");
					fos.write("id=");
					fos.write(String.valueOf(p.getId()));
					fos.write("\r\nname=");
					fos.write(p.getName());
					fos.write("\r\nname_translit=");
					fos.write(FileUtils.toTranslit(p.getName()));
					fos.close();
				} catch (IOException ex) {
					Logger.getLogger(PhotoServiceCmsImpl.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

			UploadBean ub = new UploadBean();
			ub.setId(p.getId());
			ub.setFolder_name(cur_dir.getName());
			ub.setPage_name(p.getName());
			ub.setItem_count(cur_dir.listFiles().length-1);
			rez.add(ub);

			parents.addLast(cur_dir);
			last_layer = p.getLayer();
		}
		return rez;
	}

	public static final String[] PAGES_WHERE2 = {"type", "id"};
	public static final String[] PAGES_VAUE2 = {gallery.web.controller.pages.types.WallpaperGalleryType.TYPE};
    public static final long UPLOAD_RESTART_COUNT = 50;
	@Override
	public long uploadPhotos(User uploader, Long id_pages, StatusBean usb) {
		File upload_dir = new File(photo_service.getUploadPath());
        usb.setDone(0);
		if (upload_dir.exists()){
			File description_file;
			Long id_pages_cur;
			LinkedList<File> files = new LinkedList<File>();
			files.addLast(upload_dir);

            int restart_count=0;

			while (!files.isEmpty()){
				File f = files.removeLast();
				if (f.isDirectory()){
					//search for DESCRIPTION_FILE
					description_file = new File(f, DESCRIPTION_FILE);
					if (description_file.exists()){
						id_pages_cur = null;
						try {
							BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(description_file), "UTF-8"));
							String line;
							while ((line=reader.readLine())!=null){
								if (line.startsWith("id=")){
									id_pages_cur = Long.parseLong(line.substring(3), 10);
								}
							}
						} catch (IOException ex) {
							Logger.getLogger(PhotoServiceCmsImpl.class.getName()).log(Level.SEVERE, null, ex);
						}
					}else{
						id_pages_cur = id_pages;
					}
					File[] files_temp = f.listFiles();
					for (File tmp:files_temp){
						if (tmp.isFile()){
							if (!tmp.getName().equals(DESCRIPTION_FILE)&&id_pages_cur!=null){
								Photo photo = new Photo();
								photo.setUser(uploader);
								photo.setId_pages(id_pages_cur);
								photo.setActive(Boolean.TRUE);
								photo.setDate_upload(new Date());
								photo.setContent_file(tmp);
								usb.setCur_name(tmp.getAbsolutePath());
								if (insert(photo)){
									tmp.delete();
									usb.increaseDone(1);
                                    restart_count++;
                                    if (restart_count==UPLOAD_RESTART_COUNT){
                                        restart_count = 0;
                                        photo_service.restartTransaction();
                                    }
								}
							}//else error
						} else {
							files.addLast(tmp);
						}
					}
				}
			}
		}
		return usb.getDone();
	}

	@Override
	public int deleteById(Long[] id) {return photo_service.deleteById(id);}

	public static final String[] PHOTO_ID = {"id"};
	@Override
	public List<Long> getPhotosInCategory(Long id) {
		List<Photo> photos;
		if (id==null){
			photos = photo_service.getAllShortOrdered(PHOTO_ID, null, null);
		} else {
			photos = photo_service.getShortByPropertyValueOrdered(PHOTO_ID, "id_pages", id, null, null);
		}
		List<Long> rez = new Vector<Long>(photos.size()+1);
		for (Photo p : photos) rez.add(p.getId());
		return rez;
	}

	public static final String[] RESIZE_PSEUDONYMES = new String[]{"id","name"};
	@Override
	public void reResizePhotos(boolean append_all) {
		List<Photo> photos = photo_service.getAllShortOrdered(RESIZE_PSEUDONYMES, null, null);
		gallery.web.support.photo.Utils.resizePhoto(photos, photo_service.getDimmensions(), photo_service.getStorePath(), append_all);
	}

	protected static final String[] PAGES_PROP_NAMES = new String[]{"id","id_pages","name"};
	/** id_pages, useInItems*/
	protected static final String[] PAGES_PSEDO_WHERE = new String[]{"id_pages","useInItems"};
    protected static final String[] PHOTO_OPTIMIZED_WHERE = new String[]{"id","optimized"};
	/*@Override
	public void optimizePhoto(Long[] ids){
		//List<Photo> photos = photo_service.getShortByPropertyValues(null, "id", ids);
        List<Photo> photos = photo_service.getShortByPropertiesValuesOrdered(null, PHOTO_OPTIMIZED_WHERE, new Object[][]{ids, new Object[]{Boolean.FALSE}}, null, null);
		//getting id_pages of all given photos
		HashMap<Long, Pages> page_ids = new HashMap();

		Random r = new Random();
		Pages page;
		StringBuilder title;
		StringBuilder name;
		StringBuilder tags;
		int layer;
		//LinkedList<Photo>
		for (Photo p:photos){
			Long id_pages = p.getId_pages();
			//getting all parents with optimization
			while (id_pages!=null&&!page_ids.containsKey(id_pages)){
				Object[] page_a = (Object[])pages_service.getSinglePropertyU("id, id_pages, name", "id", id_pages);
				Pages page_o = new Pages();
				page_o.setId((Long)page_a[0]);page_o.setId_pages((Long)page_a[1]);page_o.setName((String)page_a[2]);
				page_o.setPseudonyms(
						pages_pseudo_service.getShortByPropertiesValueOrdered(null,PAGES_PSEDO_WHERE, new Object[]{page_o.getId(),Boolean.TRUE}, null, null)
					);
				page_ids.put(page_o.getId(), page_o);
				id_pages = page_o.getId_pages();
			}
			title = new StringBuilder();
			name = new StringBuilder();
			tags = new StringBuilder();
			layer = 0;
			id_pages = p.getId_pages();
			while (id_pages!=null){
				page = page_ids.get(id_pages);
				if (!page.getPseudonyms().isEmpty()){
					int num = r.nextInt(page.getPseudonyms().size());
					String ROP = page.getPseudonyms().get(num).getText();
					if (layer!=0){
						title.insert(0," ");
						tags.insert(0,", ");
					}
					if (layer<2){
						if (layer!=0) name.insert(0," ");
						name.insert(0,ROP);
					}
					title.insert(0,ROP);
					tags.insert(0,page.getName());
					layer++;
				}

				id_pages = page.getId_pages();
			}

            //System.out.println("id="+p.getId()+"; optimized");
			p.setDescription(name.toString());
			p.setTags(tags.toString());
			p.setTitle(title.toString());
			p.setOptimized(Boolean.TRUE);
		}
	}*/

	@Override
	public void optimizePhoto(Long id){
		Photo p = photo_service.getById(id);
		List<Pages> parents = pages_service.getAllPagesParents(p.getId_pages(), PAGES_PROP_NAMES);
		//selecting random optimization phrase(page pseudonym) for each parent
		StringBuilder title = new StringBuilder();
		String name = null;
		StringBuilder tags = new StringBuilder();
		String rop = null;
		Random r = new Random();

        int size = parents.size()-1;
		for (int i=0;i<size;i++){
			Object[] where_vals = new Object[]{parents.get(i).getId(), Boolean.TRUE};
			Long count = pages_pseudo_service.getRowCount(PAGES_PSEDO_WHERE, where_vals);
			if (count>0){
				//random optimization phrase
				String ROP = (String) pages_pseudo_service.getSinglePropertyU("text", PAGES_PSEDO_WHERE, where_vals, r.nextInt(count.intValue()));
				if (ROP!=null&&!ROP.equals("")){
					title.append(ROP);title.append(" - ");
					rop = ROP;
				}
				tags.append(parents.get(i).getName());tags.append(", ");
			}
		}
		size++;
        if (size>0){
			size--;
			Object[] where_vals = new Object[]{parents.get(size).getId(), Boolean.TRUE};
			Long count = pages_pseudo_service.getRowCount(PAGES_PSEDO_WHERE, where_vals);
			if (count>0){
				//random optimization phrase
				String ROP = (String) pages_pseudo_service.getSinglePropertyU("text", PAGES_PSEDO_WHERE, where_vals, r.nextInt(count.intValue()));
				if (ROP!=null&&!ROP.equals("")){
					title.append(ROP);
					name=ROP;
				} else if (rop!=null){
					name=rop;
				} else {
					name=parents.get(size).getName();
				}
				tags.append(parents.get(size).getName());
			}
		}

		p.setDescription(name.toString());
		p.setTags(tags.toString());
		p.setTitle(title.toString());
		p.setOptimized(Boolean.TRUE);
	}

    public static final String[] PHOTO_OPTIMIZED_CATEGORY_WHERE = new String[]{"id_pages","optimized"};
	@Override
	public void optimizePhotoCategories(Long[] id_pages) {
		HashMap<Long, Pages> page_ids = new HashMap();
		List<Photo> photos;
		StringBuilder title;
		String name;
		StringBuilder tags;
		Random r = new Random();
		Pages page;
		int layer;

		for (int i=0;i<id_pages.length;i++){
			//photos = photo_service.getShortByPropertyValue(null, "id_pages", id_pages[i]);
            photos = photo_service.getShortByPropertiesValueOrdered(null, PHOTO_OPTIMIZED_CATEGORY_WHERE, new Object[]{id_pages[i], Boolean.FALSE}, null, null);
			if (photos.size()>0){
				//getting all parents with optimization
				Long id_pages_tmp = id_pages[i];
				while (id_pages_tmp!=null&&!page_ids.containsKey(id_pages_tmp)){
					Object[] page_a = (Object[])pages_service.getSinglePropertyU("id, id_pages, name", "id", id_pages_tmp);
					Pages page_o = new Pages();
					page_o.setId((Long)page_a[0]);page_o.setId_pages((Long)page_a[1]);page_o.setName((String)page_a[2]);
					page_o.setPseudonyms(
							pages_pseudo_service.getShortByPropertiesValueOrdered(null,PAGES_PSEDO_WHERE, new Object[]{page_o.getId(),Boolean.TRUE}, null, null)
						);
					page_ids.put(page_o.getId(), page_o);
					id_pages_tmp = page_o.getId_pages();
				}

				boolean rop_set = false;
				for (Photo p:photos){
                    //System.out.println("id="+p.getId()+"; optimized");
                    title = new StringBuilder();
                    name = null;
                    tags = new StringBuilder();
                    layer = 0;
					rop_set = false;
                    id_pages_tmp = id_pages[i];
                    while (id_pages_tmp!=null){
                        page = page_ids.get(id_pages_tmp);
                        if (!page.getPseudonyms().isEmpty()){
                            int num = r.nextInt(page.getPseudonyms().size());
                            String ROP = page.getPseudonyms().get(num).getText();
							if (ROP!=null&&!ROP.equals("")){
								if (!rop_set){
									name = ROP;
									rop_set = true;
								} else {
									title.insert(0," - ");
								}
								title.insert(0,ROP);
                            } else if (layer==0){
								name = page.getName();
							}

                            if (layer!=0){
                                tags.insert(0,", ");
							}
                            tags.insert(0,page.getName());
                            layer++;
                        }

                        id_pages_tmp = page.getId_pages();
                    }

                    p.setDescription(name);
                    p.setTags(tags.toString());
                    p.setTitle(title.toString());
                    p.setOptimized(Boolean.TRUE);
				}
			}
		}
	}

	public static final String[] OPTIMIZATION_CLEAR1 = new String[]{"optimized"};
	@Override
	public void setPhotoOptimizationCategories(Long[] id_pages, Boolean optimized) {
		photo_service.updateObjectArrayShortByProperty(OPTIMIZATION_CLEAR1, new Object[]{optimized}, "id_pages", id_pages);
	}

	public static String[] RUBRIC_WHERE = new String[]{"type"};
	public static String[] RUBRIC_PSEUDONYMES = new String[]{"id","id_pages","name","type","last"};
	@Override
	public List<Pages> getCategoriesLayered() {
        return pages_service.getPagesChildrenRecurciveOrderedWhere(RUBRIC_PSEUDONYMES, RUBRIC_WHERE,
				new Object[][]{new String[]{gallery.web.controller.pages.types.WallpaperGalleryType.TYPE}});
	}

	public static String[] PHOTO_RESOLUTION = new String[]{"width", "height"};
	@Override
	public long renewResolution(StatusBean sb) {
		File f;
		BufferedImage bi;
		ScrollableResults sr = photo_service.getScrollableResults("id, name", null, null, null, null);
		File img_dir = new File(photo_service.getStorePath(), "full");

		sr.beforeFirst();
		while (sr.next()){
			f = new File(img_dir, sr.getString(1));
			try {
				bi = ImageUtils.readImage(f).getImage();
				photo_service.updateObjectArrayShortByProperty(PHOTO_RESOLUTION, new Object[]{bi.getWidth(), bi.getHeight()}, "id", new Object[]{sr.getLong(0)});
				sb.increaseDone(1);
			} catch (Exception ex) {
				Logger.getLogger(PhotoServiceCmsImpl.class.getName()).log(Level.SEVERE, "while trying to read photo's resolution id = " + sr.getLong(0), ex);
			}
		}
		return sb.getDone();
	}

}
