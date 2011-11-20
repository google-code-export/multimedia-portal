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

package gallery.model.command;

import common.beans.PagerBean;
import gallery.model.beans.Wallpaper;
import java.util.List;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class TagCloudView {
	/*tag must be binded from the request parameters */
	protected String tag;
	/*exect must be binded from the request parameters */
	protected Boolean exect;

	protected List<Wallpaper> data;
	protected PagerBean pager;

	public String getTag() {return tag;}
	public void setTag(String val) {this.tag = val;}

	public Boolean getExect() {return exect;}
	public void setExect(Boolean exect) {this.exect = exect;}

	/**
	 *
	 * @return string to be used in Like statement
	 */
	public String[] getTagsLike(){
		if ((exect==null||!exect)&&(tag!=null&&!tag.equals(""))){
			return new String[]{tag+",%", "%, "+tag, "%, "+tag+",%", tag};
		}else{
			return new String[]{"%"+tag+"%"};
		}
	}

	public List<Wallpaper> getData() {return data;}
	public void setData(List<Wallpaper> data) {this.data = data;}

	public PagerBean getPager() {return pager;}
	public void setPager(PagerBean pager) {this.pager = pager;}

	public boolean isEmpty(){return (tag==null||tag.equals(""))&&(exect==null||!exect);}
	
}
