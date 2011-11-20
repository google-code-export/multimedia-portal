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

import common.services.generic.IGenericLocalizedService;
import gallery.model.beans.AutoreplaceL;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IAutoreplaceService extends IGenericLocalizedService<AutoreplaceL, Long>{
	public IReplacement getAllReplacements(String lang);

	interface IReplacement{
		/**
		 * replaces all names by values in given string
		 * @param str a string where to make a replacement
		 * @return string with replaced values
		 */
		public String replaceAll(String str);
	}
}
