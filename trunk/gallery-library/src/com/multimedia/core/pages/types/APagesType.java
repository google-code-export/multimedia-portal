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

package com.multimedia.core.pages.types;

import gallery.web.controller.pages.Config;
import gallery.web.controller.pages.types.WallpaperGalleryType;
import gallery.web.controller.pages.types.WallpaperRandomType;
import gallery.web.controller.pages.types.WallpaperTagCloudType;
import java.util.Hashtable;
import java.util.Map;

/**
 * only provides an access to parent config object
 * @author demchuck.dima@gmail.com
 */
public abstract class APagesType implements IPagesType{
	public static final Map<String,String> modules;
	static{
		modules = new Hashtable<String, String>();
		modules.put(WallpaperGalleryType.TYPE, "wallpaper");
		modules.put(WallpaperRandomType.TYPE, "wallpaper");
		modules.put(WallpaperTagCloudType.TYPE, "wallpaper");
	}

	public void init(){}

	protected Config config;
    /**
     * config of parent controller
	 * is set inside of PagesController
	 * @param conf config to set
	 */
    public void setConfig(Config conf){
		this.config = conf;
	}
}
