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

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MiscUtils {
	private MiscUtils(){}

	/**
	 * check if testObj is null.
	 * appends message to sb and returns false if is null.
	 * @param testObj object to be tested on null
	 * @param varName name of property to be appended to sb
	 * @param sb will contain all messages
	 * @return true if not null
	 */
	public static boolean checkNotNull(Object testObj, String varName, StringBuilder sb){
		if (testObj==null){
			sb.append(varName);
			sb.append(" not specified; ");
			return false;
		} else {
			return true;
		}
	}
}
