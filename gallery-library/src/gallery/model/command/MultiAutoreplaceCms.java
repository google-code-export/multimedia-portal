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
import gallery.model.beans.AutoreplaceL;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.map.LazyMap;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MultiAutoreplaceCms implements IMultiupdateBean<AutoreplaceL, Long>{
    /*private Long[] id;
    private String[] text;
    private String[] code;
    private Long[] sort;
    private Boolean[] active;*/

	private Map<Integer, AutoreplaceL> autoreplaces;

	public MultiAutoreplaceCms(int size) {
		autoreplaces = LazyMap.decorate(new HashMap(), FactoryUtils.instantiateFactory(AutoreplaceL.class));
		/*autoreplaces = new Vector();
		for (int i=0;i<size;i++){
			Autoreplace a = new Autoreplace();
			a.setActive(Boolean.FALSE);
			AutoreplaceL al = new AutoreplaceL();
			al.setParent(a);
			autoreplaces.add(al);
		}*/
		/*id = Long.valueOf[size];
		text = new String[size];
		code = new String[size];
		sort = Long.valueOf[size];
		active = new Boolean[size];
		java.util.Arrays.fill(active, Boolean.FALSE);*/
	}

	/*public Long[] getId() {return id;}
	public void setId(Long[] id) {this.id = id;}

	public String[] getText() {return text;}
	public void setText(String[] text) {this.text = text;}

	public String[] getCode() {return code;}
	public void setCode(String[] code) {this.code = code;}

	public Long[] getSort() {return sort;}
	public void setSort(Long[] sort) {this.sort = sort;}

	public Boolean[] getActive() {return active;}
	public void setActive(Boolean[] active) {this.active = active;}*/

    /*public String getActiveHtml() {
        if (Boolean.TRUE.equals(active))
            return "selected";
        else
            return "";
    }*/

	public Map<Integer, AutoreplaceL> getAutoreplaces() {return autoreplaces;}
	public void setAutoreplaces(Map<Integer, AutoreplaceL> autoreplaces) {this.autoreplaces = autoreplaces;}

	//public static final String[] MULTI_UPDATE_NAMES = new String[]{"text"/*,"parent.code","parent.sort","parent.active"*/};
	@Override
	public int save(IMultiupdateService<AutoreplaceL, Long> service) {
		return service.saveOrUpdateCollection(getAutoreplaces().values());
		//if (id!=null&&text!=null/*&&code!=null&&active!=null&&sort!=null*/){
		//	return service.updateObjectArrayShortById(MULTI_UPDATE_NAMES, id, text/*, code, sort, active*/);
		//}else{
		//	return -1;
		//}
	}

	public int getSize(){
		return getAutoreplaces().size();
		/*if (id!=null){
			return id.length-1;
		}else{
			return 0;
		}*/
	}

	@Override
	public boolean isModel() {return false;}

	@Override
	public Object getModel() {return autoreplaces.values();}

}
