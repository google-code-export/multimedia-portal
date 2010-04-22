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

package gallery.web.support.photoRating;

import gallery.model.beans.PhotoRating;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Utils {
	public static final Long MAX_RATE = new Long(10);
	public static final Long MIN_RATE = new Long(0);

	public static void correctRate(PhotoRating pr){
		if (pr.getRate()==null||pr.getRate()<0)
			pr.setRate(MIN_RATE);
		else if (pr.getRate()>MAX_RATE) pr.setRate(MAX_RATE);
	}

	private Utils() {}
}
