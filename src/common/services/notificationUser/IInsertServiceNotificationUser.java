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

package common.services.notificationUser;

import common.services.IInsertService;
import java.util.List;

/**
 *
 * @param <T> 
 * @author demchuck.dima@gmail.com
 */
public interface IInsertServiceNotificationUser<T> extends IInsertService<T>{
	/**
	 * sends a notification, may also make an insertion itself
	 * @param obj command
	 * @param server name of server
	 * @param siteName name of site
	 * @param id_groups groups for this site
	 * @return true if notification send
	 */
	public boolean sendInsertNotificationUser(T obj, String server, String siteName, List<String> id_groups);
}
