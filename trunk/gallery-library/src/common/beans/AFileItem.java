/*
 *  Copyright 2010 demchuck.dima@gmail.com.
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

package common.beans;

import java.io.File;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public abstract class AFileItem {
	private String file_name;
	private MultipartFile content;
	private File content_file;

	public String getFile_name() {return file_name;}
	public void setFile_name(String file_name) {this.file_name = file_name;}

	public MultipartFile getContent() {return content;}
	public void setContent(MultipartFile content) {this.content = content;}

	public File getContent_file() {return content_file;}
	public void setContent_file(File content_file) {this.content_file = content_file;}

}
