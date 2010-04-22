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

package common.utils.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class BufferedImageHolder {
	private BufferedImage bi;
	private String format_name;

	private MultipartFile multipart;
	private File file;

	public static final Pattern pattern = Pattern.compile("(jpg){1}|(jpeg){1}", Pattern.CASE_INSENSITIVE);

	/**
	 * used for holding image and its format
	 * @param bi image
	 * @param format_name format
	 */
	public BufferedImageHolder(BufferedImage bi, String format_name){
		this.bi = bi;
		this.format_name = format_name;
	}

	public BufferedImage getImage() {return bi;}
	public String getFormat_name() {return format_name;}

	/**
	 * true if src image was encoded
	 * @return
	 */
	public boolean needEncode() {
		return !pattern.matcher(format_name).matches();
	}

	public void setMultipart(MultipartFile multipart) {this.multipart = multipart;}
	public void setFile(File file) {this.file = file;}

	/**
	 * get input stream for file that is associated with this resource
	 * user is responcible for closing sream
	 * @return
	 * @throws java.io.IOException
	 */
	public InputStream getInputStream()
			throws IOException
	{
		if (multipart==null){
			if (file!=null){
				return new FileInputStream(file);
			} else {
				throw new NullPointerException("either file nor multpart file is associated with this holder");
			}
		} else {
			return multipart.getInputStream();
		}
	}

}
