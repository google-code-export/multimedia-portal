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

package common.dao;

import common.beans.AFileItem;
import org.apache.log4j.Logger;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class FileDAOImpl implements IFileDAO{
	final Logger logger = Logger.getLogger(getClass());

	@Override
	public boolean save(AFileItem item) {
		if (item.getContent()!=null){
			logger.info("file saved");
		} else if (item.getContent_file()!=null){
			logger.info("file saved");
		}

		throw new NullPointerException("file item contains no file");
	}

	@Override
	public boolean delete(AFileItem item) {
		if (item.getContent()!=null){
			logger.info("file deleted:"+item.getContent().getOriginalFilename());
		} else if (item.getContent_file()!=null){
			logger.info("file deleted:"+item.getContent_file().getAbsolutePath());
		} else if (item.getFile_name()!=null){
			logger.info("file deleted:"+item.getFile_name());
		}
		throw new NullPointerException("no file can be found by this file item");
	}

}
