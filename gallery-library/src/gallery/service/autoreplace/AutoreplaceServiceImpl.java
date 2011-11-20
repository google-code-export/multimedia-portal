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

package gallery.service.autoreplace;

import common.services.generic.GenericLocalizedServiceImpl;
import gallery.model.beans.Autoreplace;
import gallery.model.beans.AutoreplaceL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class AutoreplaceServiceImpl extends GenericLocalizedServiceImpl<AutoreplaceL, Long, Autoreplace, Long> implements IAutoreplaceService{
	public static final String[] WHERE_NAMES = new String[]{"localeParent.active", "lang"};
	public static final String[] ORDER_BY = new String[]{"localeParent.sort"};
	public static final String[] ORDER_HOW = new String[]{"asc"};

	/**
	 * get all values for auto replacing
	 * @return class for replacing 
	 */
	@Override
	public IReplacement getAllReplacements(String lang){
		//connects to a DB and gets new values and names from there
		List<AutoreplaceL> data=dao.getByPropertiesValuePortionOrdered(
				null, null, WHERE_NAMES, new Object[]{Boolean.TRUE, lang}, 0, -1, ORDER_BY, ORDER_HOW);
		List<String> values=new ArrayList<String>(data.size());
		List<Pattern> names_pattern=new ArrayList<Pattern>(data.size());
		for (int i=0;i<data.size();i++){
			names_pattern.add(Pattern.compile(Pattern.quote(data.get(i).getLocaleParent().getCode())));
			values.add(data.get(i).getText());
		}
		return new Replacement(values, names_pattern);
	}

	class Replacement implements IReplacement{
		protected List<String> values;
		protected List<Pattern> names_pattern;

		protected Replacement(List<String> values,List<Pattern> names_pattern){
			this.values = values;
			this.names_pattern = names_pattern;
		}

		
		@Override
		public String replaceAll(String str){
			if (str==null||values==null||values.isEmpty())
				return str;
			boolean notReplaced=true;
			String rez=str;
			Matcher m;
			while (notReplaced){
				notReplaced=false;
				for (int i=0;i<values.size();i++){
					m=names_pattern.get(i).matcher(rez);

					StringBuffer sb = new StringBuffer();
					boolean result = m.find();
					if (result) {
						do {
							m.appendReplacement(sb, values.get(i));
							result = m.find();
						} while (result);
						m.appendTail(sb);
						rez=sb.toString();
						notReplaced=true;
					}
				}
			}
			return rez;
		}
	}

}