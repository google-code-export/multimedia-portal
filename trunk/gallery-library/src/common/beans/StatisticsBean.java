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
	private long total_time = 0;
	private long total_count = 0;
	//private double avg_time = 0;

	private long time_start;

	public StatisticsBean(long time_start){this.time_start = time_start;}

	/**
	 * increases total_time, total_count by given value and recalculates avg_time
	 * @param time
	 * @param count
	 */
	public synchronized void increaseStat(long time, long count){
		total_count += count;
		total_time += time;
		//avg_time = total_time / total_count;
	}

	/**
	 * increases total_time by given value and total_count by 1 and recalculates avg_time
	 * @param time
	 */
	public synchronized void increaseStat(long time){increaseStat(time, 1);}

	public synchronized long getTotal_time() {return total_time;}
	public synchronized long getTotal_count() {return total_count;}
	//public synchronized double getAvg_time() {return avg_time;}
	//public long getTime_working(){return common.web.filters.StatisticFilter.getTime() - time_start;}
	public synchronized double getPercent_working(){return ((double)total_time*100)/(common.web.filters.StatisticFilter.getTime() - time_start);}
}
