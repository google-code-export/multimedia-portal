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

package common.beans;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class StatisticsBean {
	private long total_time;
	private long total_time_handle;
	private long total_time_view;
	private long total_count;
	private double avg_time;
	private double avg_time_handle;
	private double avg_time_view;

	public StatisticsBean(long total_time, long total_count, long handle_time, long view_time){
		this.total_count = total_count;
		this.total_time = total_time;
		this.total_time_handle = handle_time;
		this.total_time_view = view_time;

		double total_count2 = total_count;

		avg_time = total_time;
		avg_time = avg_time / total_count2;
		avg_time_handle = handle_time;
		avg_time_handle = avg_time_handle / total_count2;
		avg_time_view = view_time;
		avg_time_view = avg_time_view / total_count2;
	}

	public long getTotal_time() {return total_time;}
	public long getTotal_time_handle() {return total_time_handle;}
	public long getTotal_time_view() {return total_time_view;}
	public long getTotal_count() {return total_count;}
	public double getAvg_time() {return avg_time;}
	public double getAvg_time_handle() {return avg_time_handle;}
	public double getAvg_time_view() {return avg_time_view;}
}
