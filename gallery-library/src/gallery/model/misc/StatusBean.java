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

package gallery.model.misc;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class StatusBean {
	private Long total;
	private long done = 0;
	private String cur_name;

	private Date start_date;

	public StatusBean() {
		start_date = new Timestamp(System.currentTimeMillis());
	}

	public Long getTotal() {return total;}
	public void setTotal(Long value) {this.total = value;}

	public long getDone() {return done;}
	public void setDone(long value) {this.done = value;}
	public void increaseDone(long value) {this.done += value;}

	public String getCur_name() {return cur_name;}
	public void setCur_name(String value) {this.cur_name = value;}

	public Date getStartDate() {return start_date;}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("start_time: ");
		sb.append(start_date);
		sb.append("; done: ");
		sb.append(done);
		sb.append("; total: ");
		sb.append(total);
		sb.append("; name: ");
		sb.append(cur_name);
		return sb.toString();
	}
}
