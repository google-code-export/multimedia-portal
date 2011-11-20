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

package gallery.model.beans;

import com.netstorm.localization.LocalizedBean;
import java.io.Serializable;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class AutoreplaceL extends LocalizedBean<Autoreplace> implements Serializable{
	private Long id;

	//private Long id_rel;
	private String lang;

	private String text;

	/*public Long getId_rel() {return id_rel;}
	public void setId_rel(Long id_rel) {this.id_rel = id_rel;}*/

	public String getLang() {return lang;}
	public void setLang(String lang) {this.lang = lang;}

	public String getText() {return text;}
	public void setText(String text) {this.text = text;}

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@Override
	public String toString() {
		return getClass().getName()+"("+(id==null?"":id.toString())+"), ("+(localeParent==null?"":localeParent.toString())+"), text = "+text;
	}

}
