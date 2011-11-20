/*
 *  Copyright 2010 demchuck.dima@gmail.com.
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

import common.cms.services2.AGenericCmsService;
import common.utils.FileUtils;
import common.utils.ImageUtils;
import gallery.model.active.PagesCombobox;
import gallery.model.beans.Pages;
import gallery.model.beans.Wallpaper;
import gallery.model.misc.StatusBean;
import gallery.model.misc.UploadBean;
import gallery.service.pages.IPagesService;
import gallery.service.pagesPseudonym.IPagesPseudonymService;
import gallery.service.resolution.IResolutionService;
import gallery.web.support.wallpaper.Utils;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;
import org.springframework.stereotype.Service;
import security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value="cmsWallpaperService")
public class CmsWallpaperService extends AGenericCmsService<Wallpaper, Long> implements ICmsWallpaperService{
	private static final Logger logger = Logger.getLogger(CmsWallpaperService.class);
	protected final List<String> CMS_SHORT_ALIAS = java.util.Arrays.asList("id","name","id_pages","active", "optimized", "width", "height");

	protected IResolutionService resolution_service;//TODO: mb delete if not used
	protected IPagesService pages_service;
	protected IPagesPseudonymService pages_pseudo_service;
	protected IWallpaperService wallpaper_service;

	@Override
	public void init() {
		super.init();
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(wallpaper_service, "wallpaper_service", sb);
		common.utils.MiscUtils.checkNotNull(resolution_service, "resolution_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_pseudo_service, "pages_pseudo_service", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	//-------------------------------------------- overriden common cms methods ----------------
	protected final String[] CMS_EDIT_NAMES = new String[]{"active", "optimized"};
	@Override
	public int saveOrUpdateCollection(Collection<Wallpaper> c) {
		return wallpaper_service.updateCollection(c, CMS_EDIT_NAMES);
	}

	@Override
	public Wallpaper getInsertBean(Wallpaper obj) {
		if (obj==null)
			obj = new Wallpaper();
		obj.setActive(true);
		return obj;
	}

	@Override
	public int update(Wallpaper command) {
		if (command.getContent()==null){
			if (wallpaper_service.renameFiles(command)){
				//we just need to update rows in database
				wallpaper_service.save(command);
			}
		}else{
			//1-st delete old wallpapers
			if (wallpaper_service.deleteFiles(command)&&wallpaper_service.getImage(command)){
				//2-nd update rows in database, create new, and count new resolution
				wallpaper_service.save(command);
			}else{
				return -1;
			}
		}
		return 1;
	}

	protected final String[] content_fields = new String[]{"width", "height", "date_upload", "type"};
	@Override
	public int update(Wallpaper command, String... names) {
		if (command.getContent()==null){
			if (wallpaper_service.renameFiles(command)){
				//we just need to update rows in database
				wallpaper_service.update(command, names);
			}
		}else{
			//1-st delete old wallpapers
			if (wallpaper_service.deleteFiles(command)&&wallpaper_service.getImage(command)){
				//2-nd update rows in database, create new, and count new resolution
				wallpaper_service.update(command, names);
				//3-rd updating fields that have changed because of file
				wallpaper_service.update(command, content_fields);
			}else{
				return -1;
			}
		}
		return 1;
	}

	@Override
	public boolean insert(Wallpaper obj) {
		if (wallpaper_service.getImage(obj)){
			//TODO: mb remove
			if (obj.getDescription()==null)
				obj.setDescription(obj.getName());
			if (obj.getTitle()==null)
				obj.setTitle(obj.getName());
			if (obj.getTags()==null)
				obj.setTags(obj.getName());

			wallpaper_service.save(obj);
			return true;
		} else {
			return false;
		}
	}
	public boolean insert(Wallpaper obj, File resized_folder) {
		if (wallpaper_service.getImage(obj, resized_folder)){
			//TODO: mb remove
			if (obj.getDescription()==null)
				obj.setDescription(obj.getName());
			if (obj.getTitle()==null)
				obj.setTitle(obj.getName());
			if (obj.getTags()==null)
				obj.setTags(obj.getName());

			wallpaper_service.save(obj);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int insert(List<Wallpaper> list, Long id_pages) {
		int rez = 0;
		for (Wallpaper obj:list){
			if (wallpaper_service.getImage(obj)){
				if (id_pages!=null){
					obj.setId_pages(id_pages);
				}
				//TODO: mb remove
				if (obj.getDescription()==null)
					obj.setDescription(obj.getName());
				if (obj.getTitle()==null)
					obj.setTitle(obj.getName());
				if (obj.getTags()==null)
					obj.setTags(obj.getName());

				//wallpaper_service.save(obj, "active", "date_upload", "description", "id_pages");
				wallpaper_service.save(obj);
				rez++;
			}
		}
		return rez;
	}

	@Override
	public void initUpdate(Map<String, Object> model) {
		model.put("categories_wallpaper_select", pages_service.getAllCombobox(null, Boolean.TRUE, gallery.web.controller.pages.types.WallpaperGalleryType.TYPE));
	}

	@Override
	public void initInsert(Map<String, Object> model) {
		model.put("categories_wallpaper_select", pages_service.getAllCombobox(null, Boolean.TRUE, gallery.web.controller.pages.types.WallpaperGalleryType.TYPE));
	}

	@Override
	public void initView(Map<String, Object> model) {
		model.put("categories_wallpaper_select", new PagesCombobox(null, pages_service));
	}

	@Override
	public List<String> getListPropertyNames() {return CMS_SHORT_ALIAS;}

	@Override
	public List<String> getListPropertyAliases() {return CMS_SHORT_ALIAS;}
	//-------------------------------------------- wallpaper specific methods -------------------

	protected final String[] FOLDERS_PSEUDO = {"id","name"};
	/** name of file with  */
	protected final String DESCRIPTION_FILE = "name.txt";
	@Override
	public List<UploadBean> createPageFolders() {
		OnlyFilesFilter filenameFilter = new OnlyFilesFilter();
		//getting layered pages
		List<Pages> pages = getCategoriesLayered();
		//creating vector for results
		LinkedList<UploadBean> rez = new LinkedList<UploadBean>();
		//creating an upload dir if it doesn't exists
		File upload_dir = new File(wallpaper_service.getUploadPath());
		if (!upload_dir.exists()) upload_dir.mkdirs();
		//for saving parent catalogues
		Long last_layer = Long.valueOf(-1);
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
			createDescriptionFile(cur_dir, p.getId(), p.getName(), false);

			UploadBean ub = new UploadBean();
			ub.setId(p.getId());
			ub.setFolder_name(cur_dir.getName());
			ub.setPage_name(p.getName());
			ub.setItem_count(cur_dir.listFiles(filenameFilter).length-1);
			rez.addLast(ub);

			parents.addLast(cur_dir);
			last_layer = p.getLayer();
		}
		return rez;
	}

	/**
	 * scans upload folder for files
	 * and returns total quantity of files exept description file
	 * @param base_dir directory where to scan
	 * @return
	 */
	public long scanFolder(File base_dir){
		File[] files = base_dir.listFiles();
		long count = 0;
		for (File file:files){
			if (file.isDirectory()){
				count += scanFolder(file);
			} else if (!DESCRIPTION_FILE.equals(file.getName())){
				count++;
			}
		}
		return count;
	}

	protected final String[] RUBRIC_WHERE = new String[]{"type"};
	protected final String[] RUBRIC_PSEUDONYMES = new String[]{"id","id_pages","name","type","last"};
	@Override
	public List<Pages> getCategoriesLayered() {
        return pages_service.getPagesChildrenRecurciveOrderedWhere(RUBRIC_PSEUDONYMES, RUBRIC_WHERE,
				new Object[][]{new String[]{gallery.web.controller.pages.types.WallpaperGalleryType.TYPE}});
	}

	protected final String[] PAGES_WHERE2 = {"type", "id"};
	protected final String[] PAGES_VAUE2 = {gallery.web.controller.pages.types.WallpaperGalleryType.TYPE};
    protected final long UPLOAD_RESTART_COUNT = 10;
	@Override
	public long uploadWallpapers(User uploader, Long id_pages, StatusBean usb) {
		File upload_dir = new File(wallpaper_service.getUploadPath());
		OnlyFilesFilter filenameFilter = new OnlyFilesFilter();
        usb.setDone(0);
		usb.setTotal(scanFolder(upload_dir));
		//logger.debug("starting upload process id_pages="+id_pages);
		if (upload_dir.exists()){
			File description_file;
			Long id_pages_cur;
			boolean pre_uploaded;
			Wallpaper wallpaper;
			LinkedList<File> files = new LinkedList<File>();
			files.addLast(upload_dir);

			Set<String> dimmensions_set = wallpaper_service.getDimmensions().keySet();

            int restart_count=0;

			while (!files.isEmpty()){
				File f = files.removeLast();
				pre_uploaded = false;
				//logger.debug("test file: '"+f.getAbsolutePath()+"'");
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
								} else if (line.startsWith("pre_uploaded=true")){
									//means that this folder contains subfolders with pre uploaded images
									//i.e. wallpapers are allready resized and are stored in an appropriate folders
									//but they still must be checked
									pre_uploaded = true;
								}
							}
						} catch (IOException ex) {
							logger.error("", ex);
						}
					}else{
						id_pages_cur = id_pages;
					}
					File[] files_temp = f.listFiles();
					for (File tmp:files_temp){
						if (tmp.isFile()){
							if (!tmp.getName().equals(DESCRIPTION_FILE)&&id_pages_cur!=null){
								wallpaper = new Wallpaper();
								wallpaper.setUser(uploader);
								wallpaper.setId_pages(id_pages_cur);
								wallpaper.setActive(Boolean.TRUE);
								wallpaper.setContent_file(tmp);

								usb.setCur_name(tmp.getAbsolutePath());
								logger.debug("normal file uploading: '"+tmp.getAbsolutePath()+"'");

								if (insert(wallpaper)){
									tmp.delete();
									usb.increaseDone(1);
									restart_count++;
									if (restart_count==UPLOAD_RESTART_COUNT){
										restart_count = 0;
										wallpaper_service.restartTransaction();
									}
								}
							}//else error
						} else if (!pre_uploaded){
							files.addLast(tmp);
						}
					}
					if (pre_uploaded){
						//uploading pre_uploaded files if any
						File pre_uploaded_folder = new File(f, Utils.FULL_DIMMENSION_NAME);
						if (pre_uploaded_folder.exists()&&pre_uploaded_folder.isDirectory()){
							files_temp = pre_uploaded_folder.listFiles(filenameFilter);
							for (File tmp:files_temp){
								wallpaper = new Wallpaper();
								wallpaper.setUser(uploader);
								wallpaper.setId_pages(id_pages_cur);
								wallpaper.setActive(Boolean.TRUE);
								wallpaper.setContent_file(tmp);

								logger.debug("pre_uploaded file uploading: '"+tmp.getAbsolutePath()+"'");
								if (insert(wallpaper, f)){
									Iterator<String> dimmensions = dimmensions_set.iterator();
									while (dimmensions.hasNext()){
										String dimmension = dimmensions.next();
										File pre_uploaded_image = new File(f, dimmension+File.separator+tmp.getName());
										if (!pre_uploaded_image.delete()){
											pre_uploaded_image.deleteOnExit();
										}
									}
									usb.increaseDone(1);
									restart_count++;
									if (restart_count==UPLOAD_RESTART_COUNT){
										restart_count = 0;
										wallpaper_service.restartTransaction();
									}
								}
							}
							//deleting pre_uploaded folder if it contains no images
							if (pre_uploaded_folder.listFiles(filenameFilter).length==0){
								FileUtils.deleteFiles(f, true);
							}
						}
					}
				}
			}
		}
		return usb.getDone();
	}

	@Override
	public void preUploadWallpapers(StatusBean usb) {
		File upload_dir = new File(wallpaper_service.getUploadPath());
		OnlyFilesFilter filenameFilter = new OnlyFilesFilter();
        usb.setDone(0);
		usb.setTotal(scanFolder(upload_dir));
		if (upload_dir.exists()){
			boolean upload_made = true;
			int upload_count = 0;

			File pre_upload_directory = new File(wallpaper_service.getUploadPath(), "pre_upload");
			if (!pre_upload_directory.exists())
				pre_upload_directory.mkdir();

			File cur_dir = null;

			File description_file;
			boolean pre_uploaded;

			Long id_pages_cur;
			String page_name;

			LinkedList<File> files = new LinkedList<File>();
			files.addLast(upload_dir);

			while (!files.isEmpty()){
				if (upload_made){
					cur_dir = new File(pre_upload_directory, String.valueOf(upload_count));
					while (cur_dir.exists()){
						cur_dir = new File(pre_upload_directory, String.valueOf(upload_count));
						upload_count++;
					}
					cur_dir.mkdir();
					Iterator<String> dimmensions = wallpaper_service.getDimmensions().keySet().iterator();
					while (dimmensions.hasNext()){
						String dimmension = dimmensions.next();
						File pre_uploaded_dimm = new File(cur_dir, dimmension);
						if (!pre_uploaded_dimm.exists()) pre_uploaded_dimm.mkdir();
					}
					upload_count++;
				}

				File f = files.removeLast();
				pre_uploaded = false;
				upload_made = false;
				//logger.debug("test file: '"+f.getAbsolutePath()+"'");
				if (f.isDirectory()){
					id_pages_cur = null;
					page_name = null;
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
								} else if (line.startsWith("name=")){
									page_name = line.substring(5);
								} else if (line.startsWith("pre_uploaded=true")){
									//means that this folder contains subfolders with pre uploaded images
									//i.e. wallpapers are allready resized and are stored in an appropriate folders
									//but they still must be checked
									pre_uploaded = true;
								}
							}
						} catch (IOException ex) {
							logger.error("", ex);
						}
					}

					File[] files_temp = f.listFiles();

					for (File tmp:files_temp){
						if (tmp.isFile()){
							if (!tmp.getName().equals(DESCRIPTION_FILE)&&id_pages_cur!=null){
								usb.setCur_name(tmp.getAbsolutePath());
								logger.debug("preparing upload file: '"+tmp.getAbsolutePath()+"'");

								if (Utils.saveScaledImageFileToDisk(tmp, wallpaper_service.getDimmensions(), cur_dir)){
									tmp.delete();
									usb.increaseDone(1);
									upload_made = true;
								}
							}//else error
						} else if (!pre_uploaded){
							files.addLast(tmp);
						}
					}
					//create a description file
					if (upload_made){
						createDescriptionFile(cur_dir, id_pages_cur, page_name, true);
						cur_dir = null;
					}
				}
			}
			if (cur_dir!=null){
				description_file = new File(cur_dir, DESCRIPTION_FILE);
				if (!description_file.exists())
					FileUtils.deleteFiles(cur_dir, true);
			}
		}
	}

	protected boolean createDescriptionFile(File cur_dir, Long page_id, String page_name, boolean prepared){
		File description = new File(cur_dir, DESCRIPTION_FILE);
		if (!description.exists()){
			try {
				description.createNewFile();
				OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(description), "UTF-8");
				fos.write("id=");
				fos.write(String.valueOf(page_id));
				fos.write("\r\nname=");
				fos.write(page_name);
				fos.write("\r\nname_translit=");
				fos.write(FileUtils.toTranslit(page_name));
				if (prepared){
					fos.write("\r\npre_uploaded=true");
				}
				fos.close();
			} catch (IOException ex) {
				logger.error("", ex);
				return false;
			}
		}
		return true;
	}

	@Override
	public int deleteById(Long[] id) {return wallpaper_service.deleteById(id);}

	protected final String[] WALLPAPER_ID_PAGES = {"id_pages"};
	@Override
	public List<Long> getWallpapersInCategory(Long id) {
		if (id==null){
			return wallpaper_service.getSingleProperty("id", null, null, 0, 0, null, null);
		} else {
			return wallpaper_service.getSingleProperty("id", WALLPAPER_ID_PAGES, new Object[]{id}, 0, 0, null, null);
		}
	}

	public static final String[] RESIZE_PSEUDONYMES = new String[]{"id","name"};
	@Override
	public void reResizeWallpapers(boolean append_all) {
		List<Wallpaper> wallpapers = wallpaper_service.getOrdered(RESIZE_PSEUDONYMES, null, null);
		gallery.web.support.wallpaper.Utils.resizeWallpaper(wallpapers, wallpaper_service.getDimmensions(), wallpaper_service.getStorePath(), append_all);
	}

	protected final String[] PAGES_PROP_NAMES = new String[]{"id","id_pages","name"};
	protected final String[] PAGES_PSEDO_WHERE = new String[]{"id_pages","useInItems"};
	@Override
	public void optimizeWallpaper(Long id) {
		Wallpaper p = wallpaper_service.getById(id);
		if (p.getOptimized_manual()||p.getOptimized())
			return;
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

    protected final String[] WALLPAPER_OPTIMIZED_CATEGORY_WHERE = new String[]{"id_pages", "optimized", "optimized_manual"};
	@Override
	public void optimizeWallpaperCategories(Long[] id_pages) {
		HashMap<Long, Pages> page_ids = new HashMap<Long, Pages>();
		List<Wallpaper> wallpapers;
		StringBuilder title;
		String name;
		StringBuilder tags;
		Random r = new Random();
		Pages page;
		int layer;

		for (int i=0;i<id_pages.length;i++){
            wallpapers = wallpaper_service.getByPropertiesValueOrdered(null, WALLPAPER_OPTIMIZED_CATEGORY_WHERE, new Object[]{id_pages[i], Boolean.FALSE, Boolean.FALSE}, null, null);
			if (wallpapers.size()>0){
				//getting all parents with optimization
				Long id_pages_tmp = id_pages[i];
				while (id_pages_tmp!=null&&!page_ids.containsKey(id_pages_tmp)){
					Object[] page_a = (Object[])pages_service.getSinglePropertyU("id, id_pages, name", "id", id_pages_tmp);
					Pages page_o = new Pages();
					page_o.setId((Long)page_a[0]);page_o.setId_pages((Long)page_a[1]);page_o.setName((String)page_a[2]);
					page_o.setPseudonyms(
							pages_pseudo_service.getByPropertiesValueOrdered(null,PAGES_PSEDO_WHERE, new Object[]{page_o.getId(),Boolean.TRUE}, null, null)
						);
					page_ids.put(page_o.getId(), page_o);
					id_pages_tmp = page_o.getId_pages();
				}

				boolean rop_set = false;
				for (Wallpaper p:wallpapers){
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

    protected final String[] OPTIMIZATION_CLEAR1 = new String[]{"optimized"};
	@Override
	public void setWallpaperOptimizationCategories(Long[] id_pages, Boolean optimized) {
		wallpaper_service.updateObjectArrayShortByProperty(OPTIMIZATION_CLEAR1, new Object[]{optimized}, "id_pages", id_pages);
	}

    protected final String[] WALLPAPER_RESOLUTION = new String[]{"width", "height"};
	@Override
	public long renewResolution(StatusBean sb) {
		File f;
		BufferedImage bi;
		sb.setTotal((Long)wallpaper_service.getSinglePropertyU("count(*)"));
		ScrollableResults sr = wallpaper_service.getScrollableResults("id, name", null, null, null, null);
		File img_dir = new File(wallpaper_service.getStorePath(), "full");

		sr.beforeFirst();
		while (sr.next()){
			f = new File(img_dir, sr.getString(1));
			try {
				bi = ImageUtils.readImage(f).getImage();
				wallpaper_service.updateObjectArrayShortByProperty(WALLPAPER_RESOLUTION, new Object[]{bi.getWidth(), bi.getHeight()}, "id", new Object[]{sr.getLong(0)});
				sb.increaseDone(1);
			} catch (Exception ex) {
				logger.error("while trying to read wallpaper's resolution id = " + sr.getLong(0), ex);
			}
		}
		return sb.getDone();
	}

	@Override
	public List<List<Wallpaper>> findWallpaperDuplicates(int quantity) {
		List<Wallpaper> wallpapers = wallpaper_service.getOrdered(null, null, null);
		List<List<Wallpaper>> result = new LinkedList<List<Wallpaper>>();
		Set<Long> processed_wallpapers = new HashSet<Long>();
		Wallpaper wallpaper_obj;
		File wallpaper_file;

		Wallpaper cur_item;
		File cur_file;

		File full_images_dir = new File(wallpaper_service.getStorePath(), "full");
		List<Wallpaper> duplicates = null;
		for (int i=0;i<wallpapers.size();i++){
			wallpaper_obj = wallpapers.get(i);
			wallpaper_file = new File(full_images_dir, wallpaper_obj.getName());
			if (!processed_wallpapers.contains(wallpaper_obj.getId())){
				if (wallpaper_file.exists()){
					duplicates = null;
					for (int j=i+1;j<wallpapers.size();j++){
						cur_item = wallpapers.get(j);
						if (cur_item.getWidth().equals(wallpaper_obj.getWidth())&&
							cur_item.getHeight().equals(wallpaper_obj.getHeight()))
						{
							//System.out.println("files compare: "+i+", "+j);
							cur_file = new File(full_images_dir, cur_item.getName());
							//System.out.println("file "+i+": "+wallpaper_file.getAbsolutePath());
							//System.out.println("file "+j+": "+cur_file.getAbsolutePath());
							if (wallpaper_file.length()==cur_file.length()){
								if (duplicates == null){
									duplicates = new LinkedList<Wallpaper>();
									processed_wallpapers.add(wallpaper_obj.getId());
									duplicates.add(wallpaper_obj);
								}
								processed_wallpapers.add(cur_item.getId());
								duplicates.add(cur_item);
							}
						}
					}
					if (duplicates!=null&&duplicates.size()>1){
						result.add(duplicates);
					}
				} else {
					result.add(java.util.Arrays.asList(wallpaper_obj));
				}
				if (quantity>0&&result.size()>quantity){
					break;
				}
			}
		}
		return result;
	}

	@Override
	public int moveWallpapersToPage(Long[] id, Long id_pages_new) {
		Object page_type = pages_service.getSinglePropertyU("type", "id", id_pages_new);
		if (gallery.web.controller.pages.types.WallpaperGalleryType.TYPE.equals(page_type)){
			return wallpaper_service.updatePropertiesById(WALLPAPER_ID_PAGES, new Object[]{id_pages_new}, id);
		} else {
			return -1;
		}
	}

	//-------------------------------------------- initialization -------------------------------
	@Resource(name="wallpaperService")
	public void setService(IWallpaperService service){
		this.service = service;
		this.wallpaper_service = service;
	}
	@Resource(name="resolutionService")
	public void setResolutionService(IResolutionService value){this.resolution_service = value;}
	@Resource(name="pagesServices")
	public void setPagesService(IPagesService value){this.pages_service = value;}
	@Resource(name="pagesPseudonymServices")
	public void setPagesPseudonymService(IPagesPseudonymService value){this.pages_pseudo_service = value;}
}

final class OnlyFilesFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		return pathname.isFile();
	}

}