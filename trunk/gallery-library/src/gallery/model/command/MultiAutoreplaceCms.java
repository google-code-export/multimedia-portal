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

import common.beans.IMultiupdateBean;
import common.services.IMultiupdateService;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MultiAutoreplaceCms implements IMultiupdateBean{
    private Long[] id;
    private String[] code;
    private String[] text;
    private Long[] sort;
    private Boolean[] active;

	public MultiAutoreplaceCms(int size) {
		id = new Long[size];
		code = new String[size];
		text = new String[size];
		sort = new Long[size];
		active = new Boolean[size];
		java.util.Arrays.fill(active, Boolean.FALSE);
	}

	public Long[] getId() {return id;}
	public void setId(Long[] id) {this.id = id;}

	public String[] getCode() {return code;}
	public void setCode(String[] code) {this.code = code;}

	public String[] getText() {return text;}
	public void setText(String[] text) {this.text = text;}

	public Long[] getSort() {return sort;}
	public void setSort(Long[] sort) {this.sort = sort;}

	public Boolean[] getActive() {return active;}
	public void setActive(Boolean[] active) {this.active = active;}

    /*public String getActiveHtml() {
        if (Boolean.TRUE.equals(active))
            return "selected";
        else
            return "";
    }*/

	public static final String[] MULTI_UPDATE_NAMES = new String[]{"code","text","sort","active"};
	@Override
	public int save(IMultiupdateService service) {
		if (id!=null&&code!=null&&text!=null&&active!=null&&sort!=null){
			return service.updateObjectArrayShortById(MULTI_UPDATE_NAMES, id, code, text, sort, active);
		}else{
			return -1;
		}
	}

	public int getSize(){
		if (id!=null){
			return id.length-1;
		}else{
			return 0;
		}
	}

	@Override
	public boolean isModel() {return true;}

	/*@Override
	public List<Autoreplace> getItems(){
		List<Autoreplace> rez = new Vector<Autoreplace>();
		if (id!=null){
			for (int i=0;i<id.length;i++){
				Autoreplace a = new Autoreplace();
				a.setId(id[i]);
				rez.add(a);
			}
			if (sort!=null&&sort.length==id.length){
				for (int i=0;i<id.length;i++)
					rez.get(i).setSort(sort[i]);
			}else{
				for (int i=0;i<id.length;i++)
					rez.get(i).setSort(null);
			}
			if (active!=null&&active.length==id.length){
				for (int i=0;i<id.length;i++)
					rez.get(i).setActive(active[i]);
			}else{
				for (int i=0;i<id.length;i++)
					rez.get(i).setActive(null);
			}
			if (text!=null&&text.length==id.length){
				for (int i=0;i<id.length;i++)
					rez.get(i).setText(text[i]);
			}else{
				for (int i=0;i<id.length;i++)
					rez.get(i).setText(null);
			}
			if (code!=null&&code.length==id.length){
				for (int i=0;i<id.length;i++)
					rez.get(i).setCode(code[i]);
			}else{
				for (int i=0;i<id.length;i++)
					rez.get(i).setCode(null);
			}
		}else{
			return null;
		}
		return rez;
	}*/

}
