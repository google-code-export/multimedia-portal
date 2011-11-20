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

package com.netstorm.localization.unused;

import java.io.Serializable;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class LocaleId implements Serializable{

	public LocaleId() {
	}

	public LocaleId(Long id, String lang) {
		this.id = id;
		this.lang = lang;
	}

	private Long id;
	private String lang;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getLang() {return lang;}
	public void setLang(String lang) {this.lang = lang;}

	@Override
	public String toString() {return getClass().getCanonicalName()+":id:"+id+", lang:"+lang;}

}