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

package gallery.service.pages;

import gallery.model.beans.Pages;
import java.util.List;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IPagesServiceCms {
    public void reactivate(Long id_pages);
    /**
     * pages contain quantities of items (PagesPseudonimes, wallpapers...)
     * @return all pages, that are modules (i.e. wallpaper_gallery_type)
     */
	public List<Pages> getCategoriesFull();

    /**
     * creates an optimization phrases for all child pageses's elements
     * @param id
     */
    public void optimizeCategory(Long id);
    public void resetOptimizationCategory(Long id, Boolean optimized);
}
