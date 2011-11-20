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
public class WallpaperRating {

	private Long id;
	private Long id_photo;

	private String ip;
	private Long rate;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public Long getId_photo() {return id_photo;}
	public void setId_photo(Long id_photo) {this.id_photo = id_photo;}

	public String getIp() {return ip;}
	public void setIp(String ip) {this.ip = ip;}

	public Long getRate() {return rate;}
	public void setRate(Long rate) {this.rate = rate;}

}
