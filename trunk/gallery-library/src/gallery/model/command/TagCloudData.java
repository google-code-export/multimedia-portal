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

package gallery.model.command;

import com.multimedia.service.wallpaper.IWallpaperService;
import common.services.IServiceBean;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class TagCloudData implements IServiceBean{
	protected IWallpaperService wallpaperService;

	public TagCloudData(IWallpaperService wallpaperService){
		this.wallpaperService = wallpaperService;
	}

	@Override
	public Object getData() {
		List<String> tags = new LinkedList<String>(wallpaperService.getTags(0).keySet());
		//java.util.Collections.sort(tags, new DataEntryComparator());
		/** key is first letter of word, List are words*/
		Map<Character, List<String>> tagsByABC = new TreeMap<Character, List<String>>(new DataEntryComparator2());
		List<String> tmp;
		for (String tag:tags){
			char first_letter = Character.toLowerCase(tag.charAt(0));
			Character c;
			if (Character.isLetter(first_letter)){
				c = Character.valueOf(first_letter);
			} else if (Character.isDigit(first_letter)){
				c = Character.valueOf("#".charAt(0));
			} else {
				c = Character.valueOf(".".charAt(0));
			}
			tmp = tagsByABC.get(c);
			if (tmp==null){
				tmp = new LinkedList<String>();
				tagsByABC.put(c, tmp);
			}
			tmp.add(tag);
		}
		return tagsByABC;
	}


	/**
	 * Compares two tags by score in descending order
	 */
	static public class DataEntryComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
		}

	}

	/**
	 * Compares two tags by score in descending order
	 */
	static public class DataEntryComparator2 implements Comparator<Character> {

		@Override
		public int compare(Character o1, Character o2) {
			return o1.compareTo(o2);
		}

	}
}
