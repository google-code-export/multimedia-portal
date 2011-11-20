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

import com.multimedia.model.beans.Advertisement;
import com.multimedia.model.beans.AdvertisementPages;
import com.multimedia.model.beans.Counter;
import common.services.generic.IGenericService;
import com.multimedia.service.advertisement.IAdvertisementService;
import com.multimedia.service.advertisementPages.IAdvertisementPagesService;
import gallery.model.beans.Pages;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PagesServiceViewImpl implements IPagesServiceView{
	protected Logger logger = Logger.getLogger(this.getClass());
	/** service for counters */
	protected IGenericService<Counter, Long> counter_service;
	protected IAdvertisementService advertisement_service;
	protected IAdvertisementPagesService advertisementPages_service;
	protected IPagesService pages_service;
	protected CacheManager cacheManager;
	protected Ehcache cache_advertisement;
	protected Ehcache cache_advertisement_pages;

	/** stores result for view render i.e. pairs region -- text for page */
	public static final String ADVERTISEMENT_REGION = "advertisement";
	/** stores result for children i.e. set of available advertisement ids for page */
	public static final String ADVERTISEMENT_PAGES_REGION = "advertisement_pages";

	public static final String[] COUNTERS_ORDER_BY = new String[]{"sort"};
	public static final String[] COUNTERS_ORDER_HOW = new String[]{"ASC"};

	protected static final String[] MAIN_PSEUDONYMES = new String[]{"id_pages","active"};
	protected static final Object[] MAIN_VALUES = new Object[]{null,Boolean.TRUE};

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(counter_service, "counter_service", sb);
		common.utils.MiscUtils.checkNotNull(advertisement_service, "advertisement_service", sb);
		common.utils.MiscUtils.checkNotNull(advertisementPages_service, "advertisementPages_service", sb);
		common.utils.MiscUtils.checkNotNull(pages_service, "pages_service", sb);
		common.utils.MiscUtils.checkNotNull(cacheManager, "cacheManager", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		} else {
			cache_advertisement = cacheManager.getEhcache(ADVERTISEMENT_REGION);
			cache_advertisement_pages = cacheManager.getEhcache(ADVERTISEMENT_PAGES_REGION);
			if (cache_advertisement==null){
				logger.warn("specified cacheManager has no region '"+ADVERTISEMENT_REGION+"', using default");//TODO create an util class for this
				cacheManager.addCache(ADVERTISEMENT_REGION);
				cache_advertisement = cacheManager.getEhcache(ADVERTISEMENT_REGION);
			}
			if (cache_advertisement_pages==null){
				logger.warn("specified cacheManager has no region '"+ADVERTISEMENT_PAGES_REGION+"', using default");//TODO create an util class for this
				cacheManager.addCache(ADVERTISEMENT_PAGES_REGION);
				cache_advertisement_pages = cacheManager.getEhcache(ADVERTISEMENT_PAGES_REGION);
			}
		}
	}

	@Override
	public List<Counter> getCounters() {
		return counter_service.getOrdered(null, COUNTERS_ORDER_BY, COUNTERS_ORDER_HOW);
	}

	@Override
	public Map<String, String> getAdvertisementForPage(Long id){
		Element cached_adv = cache_advertisement.get(id);
		if (cached_adv==null||cached_adv.getValue()==null){
			LinkedHashSet<Long> advertisement_pages = new LinkedHashSet<Long>(getAdvertisementsForPage(id));
			//and now adding advertisements only for this page
			List<AdvertisementPages> allow_deny = advertisementPages_service.getByPropertiesValueOrdered(null,
					new String[]{"id_pages", "useInChildren"}, new Object[]{id, Boolean.FALSE},
					null, null);//TODO: add sort

			for (AdvertisementPages ap:allow_deny){
				if (ap.getAllow()){
					//we need to make this beacuse of ordering
					// i.e. last place will be showen
					advertisement_pages.remove(ap.getId_advertisement());
					advertisement_pages.add(ap.getId_advertisement());
				} else {
					advertisement_pages.remove(ap.getId_advertisement());
				}
			}
			//logger.debug(advertisement_pages);

			//forming advertisements to show on page [position - advertisement.text]
			Map<String, String> m = new HashMap<String, String>();
			Advertisement adv;
			for (Long id_adv:advertisement_pages){
				adv = advertisement_service.getById(id_adv);
				if (adv.getActive()){
					m.put(adv.getPosition(), adv.getText());
				}
			}

			cache_advertisement.put(new Element(id, m));
			return m;
		} else {
			return (Map<String, String>)cached_adv.getValue();
		}
	}

	/**
	 * get a set of ids for advertisements on given page
	 * !!!WARNING: this method is optimized for caching, so it does not contain advertisements that are not 'useInChildren'
	 * @param id id of pages
	 * @return set of id for advertisements that may be rendered on given page
	 */
	public Set<Long> getAdvertisementsForPage(Long id){
		Element cached_adv_pages = cache_advertisement_pages.get(id);
		if (cached_adv_pages==null||cached_adv_pages.getValue()==null){
			LinkedHashSet<Long> advertisement_pages = new LinkedHashSet<Long>();
			//1-st get parent advertisement
			Long id_pages = (Long) pages_service.getSinglePropertyU("id_pages", "id", id);
			if (id_pages!=null){
				advertisement_pages.addAll(getAdvertisementsForPage(id_pages));
			} else {
				//get all advertisement that may be used in all pages
				List<Long> common = advertisement_service.getSingleProperty("id",
						new String[]{"active", "size(advertisementPages)"}, new Object[]{Boolean.TRUE, Integer.valueOf(0)},
						0, 0, null, null);//TODO: add sort
				//logger.debug(common);
				advertisement_pages.addAll(common);
			}

			List<AdvertisementPages> allow_deny = advertisementPages_service.getByPropertiesValueOrdered(null,
					new String[]{"id_pages", "useInChildren"}, new Object[]{id, Boolean.TRUE},
					null, null);//TODO: add sort

			for (AdvertisementPages ap:allow_deny){
				if (ap.getAllow()){
					//we need to make this beacuse of ordering
					// i.e. last place will be showen
					advertisement_pages.remove(ap.getId_advertisement());
					advertisement_pages.add(ap.getId_advertisement());
				} else {
					advertisement_pages.remove(ap.getId_advertisement());
				}
			}

			cache_advertisement_pages.put(new Element(id, advertisement_pages));
			//logger.debug(advertisement_pages);
			return advertisement_pages;
		} else {
			return (Set<Long>)cached_adv_pages.getValue();
		}
	}

    @Override
    public Pages getMainPage(){
        List<Pages> temp = pages_service.getByPropertiesValueOrdered(null, MAIN_PSEUDONYMES, MAIN_VALUES, null, null);
        if (temp.isEmpty()){
            return null;
        }else{
            return temp.get(0);
        }
    }

	public void setCounterService(IGenericService<Counter, Long> value){this.counter_service = value;}
	public void setAdvertisementService(IAdvertisementService value){this.advertisement_service = value;}
	public void setAdvertisementPagesService(IAdvertisementPagesService value){this.advertisementPages_service = value;}
	public void setPages_service(IPagesService value){this.pages_service = value;}
	public void setCacheManager(CacheManager value){this.cacheManager = value;}
}
