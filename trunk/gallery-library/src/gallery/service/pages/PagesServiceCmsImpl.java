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

package gallery.service.pages;

import com.multimedia.service.wallpaper.ICmsWallpaperService;
import com.multimedia.service.wallpaper.IWallpaperService;
import gallery.model.beans.Pages;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PagesServiceCmsImpl implements IPagesServiceCms{
	protected IPagesService pages_service;
	protected IWallpaperService wallpaper_service;
    protected ICmsWallpaperService wallpaper_service_cms;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		common.utils.MiscUtils.checkNotNull(wallpaper_service, "wallpaper_service", sb);
		common.utils.MiscUtils.checkNotNull(wallpaper_service_cms, "wallpaper_service_cms", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

    public final String[] REACTIVATE_NAMES = new String[]{"id","id_pages","active","last"};
    public final String[] REACTIVATE_TYPE = new String[]{"type"};
    public final String[] REACTIVATE_PROPS = new String[]{"active"};
    public final Object[] REACTIVATE_VALS_FALSE = new Object[]{Boolean.FALSE};
    public final Object[] REACTIVATE_VALS_TRUE = new Object[]{Boolean.TRUE};
    @Override
    public void reactivate(Long id_pages) {
        //now it reactivates all pages
        //TODO: remake
        List<Pages> rubrication = pages_service.getPagesChildrenRecurciveOrderedWhere(REACTIVATE_NAMES, REACTIVATE_TYPE,
                new Object[][]{new Object[]{gallery.web.controller.pages.types.WallpaperGalleryType.TYPE}});
        //List<Pages> rubrication = pages_service.getRubrication(gallery.web.controller.pages.types.WallpaperGalleryType.TYPE);
        //marking current navigation branch
        Pages cur_page;
        Pages prev_page = null;
        int i=rubrication.size();
        Vector<Long> empty_pages = new Vector<Long>(rubrication.size());
        Vector<Long> filled_pages = new Vector<Long>(rubrication.size());
        Long count;
        //marking selected branch
        while (i>0){
            i--;
            cur_page = rubrication.get(i);
            if (cur_page.getLast()){
                count = wallpaper_service.getRowCount("id_pages", cur_page.getId());
                if (count<1){
                    if (cur_page.getActive())
                        empty_pages.add(cur_page.getId());
                    rubrication.remove(i);
                } else {
                    if (!cur_page.getActive())
                        filled_pages.add(cur_page.getId());
                    prev_page = cur_page;
                }
            } else {
                if (prev_page!=null&&prev_page.getId_pages().equals(cur_page.getId())){
                    if (!cur_page.getActive())
                        filled_pages.add(cur_page.getId());
                    prev_page = cur_page;
                } else {
                    if (cur_page.getActive())
                        empty_pages.add(cur_page.getId());
                    rubrication.remove(i);
                }
            }
        }
        //set empty pages not active
        Long[] ids = new Long[0];
        ids = empty_pages.toArray(ids);
        pages_service.updateObjectArrayShortByProperty(REACTIVATE_PROPS, REACTIVATE_VALS_FALSE, "id", ids);
        //set filled pages to active
        ids = new Long[0];
        ids = filled_pages.toArray(ids);
        pages_service.updateObjectArrayShortByProperty(REACTIVATE_PROPS, REACTIVATE_VALS_TRUE, "id", ids);
    }


	public final static String[] FULL_WHERE = new String[]{"type"};
	public final static String[] FULL_NAMES =       new String[]{"id","id_pages","name","type","last","active","pseudonyms.size"};
	public final static String[] FULL_PSEUDONYMES = new String[]{"id","id_pages","name","type","last","active","pseudonymsCount"};
	public final static String[] FULL_WALLPAPER = new String[]{"optimized","id_pages"};
	@Override
	public List<Pages> getCategoriesFull(){
		List<Pages> pages = pages_service.getPagesChildrenRecurciveOrderedWhere(FULL_NAMES, FULL_PSEUDONYMES, FULL_WHERE,
				new Object[][]{new String[]{gallery.web.controller.pages.types.WallpaperGalleryType.TYPE}});
		for (Pages p:pages){
			if (p.getLast()){
				//because its only one page type now, we do not check it
				//get pictures quantity
				p.setWallpaperCount(wallpaper_service.getRowCount("id_pages", p.getId()).intValue());
				p.setOptimized((Boolean)wallpaper_service.getSinglePropertyU("optimized", FULL_WALLPAPER, new Object[]{Boolean.FALSE,p.getId()}, 0));
			}
		}
        return pages;
	}

    public final static String[] OPTIMIZE_NAMES = new String[]{"id","id_pages","type","last","pseudonyms.size"};
    public final static String[] OPTIMIZE_PSEUD = new String[]{"id","id_pages","type","last","pseudonymsCount"};
    @Override
    public void optimizeCategory(Long id){
        //1-st checking if current category has any optimization
        //Integer count = (Integer)pages_service.getSinglePropertyU("pseudonyms.size","id",id);
		List<Pages> children = pages_service.getByPropertyValueOrdered(OPTIMIZE_NAMES, OPTIMIZE_PSEUD, "id", id, null, null);
        if (children.size()<1||children.get(0).getPseudonymsCount()<1)
            return;
        LinkedList<Long> ids_tmp = new LinkedList<Long>();
        int k = 0;
        //2-nd getting all subpages
        while (children.size()>k){
            Pages p = children.get(k);
            //TODO mb check type here ...
            if (p.getPseudonymsCount()>0){
                if (p.getLast()){
                    ids_tmp.add(p.getId());
                } else {
                    children.addAll(pages_service.getByPropertyValueOrdered(OPTIMIZE_NAMES, OPTIMIZE_PSEUD, "id_pages", p.getId(), null, null));
                }
            }
            k++;
        }
        Long[] ids = new Long[0];
        ids = ids_tmp.toArray(ids);
        wallpaper_service_cms.optimizeWallpaperCategories(ids);
    }

    @Override
    public void resetOptimizationCategory(Long id, Boolean optimized){
        //1-st getting all last subpages
        List<Pages> children = pages_service.getPagesChildrenRecurciveOrderedWhere(OPTIMIZE_NAMES, OPTIMIZE_PSEUD, null, null, id);
        //2-nd callecting only last pages ids
        Long[] ids = new Long[children.size()];
        int k = 0;
        for (Pages p:children){
            if (p.getLast()){//TODO mb check type here ...
                ids[k] = p.getId();
                k++;
            }
        }
        ids = java.util.Arrays.copyOf(ids, k);
        wallpaper_service_cms.setWallpaperOptimizationCategories(ids, optimized);
    }

	public void setWallpaper_service(IWallpaperService value){this.wallpaper_service = value;}
	public void setWallpaper_service_cms(ICmsWallpaperService value){this.wallpaper_service_cms = value;}
	public void setPages_service(IPagesService pages_service){this.pages_service = pages_service;}

}
