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

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MultiInsertWallpaperCms{
	private Long id_pages_one;

	public MultiInsertWallpaperCms(Long id_pages_one) {
		this.id_pages_one = id_pages_one;
	}

	public Long getId_pages_one() {return id_pages_one;}
	public void setId_pages_one(Long value) {this.id_pages_one = value;}
}
