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

package common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class DateUtils {
	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	/**
	 * produces a string from date
	 * using a standart date/time pattern
	 * @param d
	 * @return
	 */
	public static String getString(Date d){
		return df.format(d);
	}

	/**
	 * parse given string to produce date
	 * using a standart date/time pattern
	 * @param s
	 * @return
	 */
	public static Date getDate(String s){
		try {
			return df.parse(s);
		} catch (ParseException ex) {
			return null;
		}
	}

	private DateUtils() {
	}

}
