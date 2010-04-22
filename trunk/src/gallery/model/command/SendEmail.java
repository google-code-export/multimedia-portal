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
public class SendEmail {
	private String text;
	private String email_from;
	private String email_to;

	public String getText() {return text;}
	public void setText(String text) {this.text = text;}

	public String getEmail_from() {return email_from;}
	public void setEmail_from(String email_from) {this.email_from = email_from;}

	public String getEmail_to() {return email_to;}
	public void setEmail_to(String email_to) {this.email_to = email_to;}
}
