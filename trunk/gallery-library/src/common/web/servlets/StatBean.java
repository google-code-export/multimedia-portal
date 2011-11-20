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

package common.web.servlets;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class StatBean {
	private long size = 1;
	private long time = 1;
	private long count = 1;

	public long getSize() {return size;}

	public void increaseSize(long size) {
		this.size += size;
	}

	/**
	 *
	 * @return average time
	 */
	public long getTime() {return time/count;}

	/**
	 * @param time the time to set
	 */
	public void increaseTime(long time) {
		this.time += time;
	}

	public long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */ 
	public void increaseCount() {this.count++;}
}
