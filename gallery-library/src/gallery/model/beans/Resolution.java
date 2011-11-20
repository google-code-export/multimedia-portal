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

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Resolution {
	private Long id;
	private Integer width;
	private Integer height;

    private Long wallpaper_count;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public Integer getWidth() {return width;}
	public void setWidth(Integer width) {this.width = width;}

	public Integer getHeight() {return height;}
	public void setHeight(Integer height) {this.height = height;}

	public Long getWallpaper_count() {return wallpaper_count;}
	public void setWallpaper_count(Long value) {this.wallpaper_count = value;}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 23 * hash + (this.width != null ? this.width.hashCode() : 0);
		hash = 23 * hash + (this.height != null ? this.height.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Resolution other = (Resolution) obj;
		if (this.width != other.width && (this.width == null || !this.width.equals(other.width))) {
			return false;
		}
		if (this.height != other.height && (this.height == null || !this.height.equals(other.height))) {
			return false;
		}
		return true;
	}

}