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

package gallery.service.newPassword;

import common.services.notificationUser.IInsertServiceNotificationUser;
import gallery.model.beans.NewPassword;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface INewPasswordViewService extends IInsertServiceNotificationUser<NewPassword> {

    /**
     * try to find password with given code
     * and if it exists then reset users password and sets this one
     * @param code code of new password(from table) to apply
     * @return true if succeed
     */
    public boolean applyNewPassword(String code);
}
