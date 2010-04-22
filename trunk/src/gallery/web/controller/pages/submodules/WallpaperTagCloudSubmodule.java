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

package gallery.web.controller.pages.submodules;

import gallery.service.photo.IPhotoService;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

/**
 * is needed to draw a tag cloud
 * @author demchuck.dima@gmail.com
 */
public class WallpaperTagCloudSubmodule extends ASubmodule{
	Ehcache cache;
	String query_param;
	protected IPhotoService photoService;
	protected int tag_quantity;
	protected double tag_max_weight;
	protected double tag_min_weight;
	public static final String REGION_KEY = "tag_cloud";

	public WallpaperTagCloudSubmodule(IPhotoService photoService, HttpServletRequest request) {
		this.photoService = photoService;
		tag_quantity = 45;
		tag_max_weight = 23.0;
		tag_min_weight = 9.0;

		this.query_param = request.getParameter("tag");
		if (this.query_param==null||"".equals(this.query_param)){
			this.query_param = null;
		}else{
			this.query_param = "&tag="+this.query_param;
		}
		cache = CacheManager.getInstance().getCache(REGION_KEY);
		if (cache==null){
			throw new NullPointerException("no cache region defined for: "+REGION_KEY);
		}
	}

	/**
	 * this method is very
	 */
	public List<Tag> getData(){
		Element elem = cache.get(REGION_KEY);
		List<Tag> rez;
		if (elem==null||elem.getObjectValue()==null){
			Cloud cloud = new Cloud();

			cloud.setMaxWeight(tag_max_weight);
			cloud.setMinWeight(tag_min_weight);

			cloud.setMaxTagsToDisplay(1000);

			fillCloudFromMap(cloud, photoService.getTags(tag_quantity));
			rez = cloud.tags(new Tag.NameComparatorAsc());
			cache.put(new Element(REGION_KEY, rez));
			//System.out.println("WallpaperTagCloudSubmodule from DB");
		}else{
			rez = (List<Tag>)elem.getValue();
			//System.out.println("WallpaperTagCloudSubmodule from cache");
		}
		return rez;
	}

	public String getQueryParam(){return this.query_param;}

	public static void fillCloudFromMap(Cloud cloud, Map<String, Double> values){
		for(Entry<String,Double> e:values.entrySet()){
			cloud.addTag(new Tag(e.getKey(), e.getValue()));
		}
	}

	/**
	 * only for test
	 * @param cloud
	 */
	public static void addRandomTags(Cloud cloud){
		Random r = new Random();
		cloud.addTag(new Tag("europe",r.nextDouble()));
		cloud.addTag(new Tag("family",r.nextDouble()));
		cloud.addTag(new Tag("festival",r.nextDouble()));
		cloud.addTag(new Tag("flower",r.nextDouble()));
		cloud.addTag(new Tag("flowers",r.nextDouble()));
		cloud.addTag(new Tag("food",r.nextDouble()));
		cloud.addTag(new Tag("france",r.nextDouble()));
		cloud.addTag(new Tag("friends",r.nextDouble()));
		cloud.addTag(new Tag("fun",r.nextDouble()));
		cloud.addTag(new Tag("germany",r.nextDouble()));
		cloud.addTag(new Tag("holiday",r.nextDouble()));
		cloud.addTag(new Tag("india",r.nextDouble()));
		cloud.addTag(new Tag("italy",r.nextDouble()));
		cloud.addTag(new Tag("art",r.nextDouble()));
		cloud.addTag(new Tag("australia",r.nextDouble()));
		cloud.addTag(new Tag("baby",r.nextDouble()));
		cloud.addTag(new Tag("beach",r.nextDouble()));
		cloud.addTag(new Tag("beach",r.nextDouble()));  // if we add a tag that is already present, the scores are summed
		cloud.addTag(new Tag("birthday",r.nextDouble()));
		cloud.addTag(new Tag("blue",r.nextDouble()));
		cloud.addTag(new Tag("bw",r.nextDouble()));
		cloud.addTag(new Tag("california",r.nextDouble()));
		cloud.addTag(new Tag("canada",r.nextDouble()));
		cloud.addTag(new Tag("music",r.nextDouble()));
		cloud.addTag(new Tag("nature",r.nextDouble()));
		cloud.addTag(new Tag("new",r.nextDouble()));
		cloud.addTag(new Tag("newyork",r.nextDouble()));
		cloud.addTag(new Tag("night",r.nextDouble()));
		cloud.addTag(new Tag("nikon",r.nextDouble()));
		cloud.addTag(new Tag("nyc",r.nextDouble()));
		cloud.addTag(new Tag("paris",r.nextDouble()));
		cloud.addTag(new Tag("canon",r.nextDouble()));
		cloud.addTag(new Tag("cat",r.nextDouble()));
		cloud.addTag(new Tag("chicago",r.nextDouble()));
		cloud.addTag(new Tag("china",r.nextDouble()));
		cloud.addTag(new Tag("christmas",r.nextDouble()));
		cloud.addTag(new Tag("city",r.nextDouble()));
		cloud.addTag(new Tag("dog",r.nextDouble()));
		cloud.addTag(new Tag("england",r.nextDouble()));
		cloud.addTag(new Tag("japan",r.nextDouble()));
		cloud.addTag(new Tag("london",r.nextDouble()));
		cloud.addTag(new Tag("1me",r.nextDouble()));
		cloud.addTag(new Tag("2mexico",r.nextDouble()));
		cloud.addTag(new Tag("3city",r.nextDouble()));
		cloud.addTag(new Tag("4dog",r.nextDouble()));
		cloud.addTag(new Tag("5england",r.nextDouble()));
		cloud.addTag(new Tag("6japan",r.nextDouble()));
		cloud.addTag(new Tag("7london",r.nextDouble()));
		cloud.addTag(new Tag("8me",r.nextDouble()));
		cloud.addTag(new Tag("9mexico",r.nextDouble()));
	}

	@Override
	public boolean getEmpty() {return false;}

}
