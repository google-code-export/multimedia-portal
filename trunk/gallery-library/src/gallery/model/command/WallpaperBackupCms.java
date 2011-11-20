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
public class WallpaperBackupCms {
	private String action;
	private Boolean append_all = Boolean.FALSE;
	private Boolean only_files = Boolean.FALSE;

	public String getAction() {return action;}
	public void setAction(String action) {this.action = action;}

	public Boolean getAppend_all() {return append_all;}
	public void setAppend_all(Boolean append_all) {this.append_all = append_all;}

	public Boolean getOnly_files() {return only_files;}
	public void setOnly_files(Boolean only_files) {this.only_files = only_files;}

}
