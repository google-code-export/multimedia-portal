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

package gallery.web.support;

import javax.servlet.http.HttpServletRequest;

/**
 * @author demchuck.dima@gmail.com
 */
public class PaginatedListUtils {
	public PaginatedListUtils(){
		page_size = DEFAULT_PAGE_SIZE_VALUE;
		navigation_size = DEFAULT_PAGE_NUMBER_BOTH_SIDES;
	}

	public PaginatedListUtils(int page_size, int navigation_size){
		this.page_size = page_size;
		this.navigation_size = navigation_size;
	}

    /** http parameter name which stores information about page number in entity set */
    public static final String PAGE_NUMBER_NAME = "page_number";

    /** default page number */
    public static final int DEFAULT_PAGE_NUMBER_VALUE = 0;

    /** default page size */
    public static final int DEFAULT_PAGE_SIZE_VALUE = 1;

	private final int page_size;

	/** quantity of pages to show for navigation */
	public static final int DEFAULT_PAGE_NUMBER_BOTH_SIDES = 2;

	private final int navigation_size;

	/**
	 * get current page number, never throws an exception
	 * @param request
	 * @return current page number
	 */
    public static int getPageNumber(HttpServletRequest request) {
		try{
			return Integer.parseInt(request.getParameter(PAGE_NUMBER_NAME),10);
		}catch(Exception nfe){
			return DEFAULT_PAGE_NUMBER_VALUE;
		}
    }

	public int getPageSize(){return this.page_size;}

	/**
	 * get current page number, never throws an exception
	 * @param request
	 * @return current page number
	 */
    public static int getFirstItemNumber(HttpServletRequest request) {
		int rez = getPageNumber(request);
		return rez * DEFAULT_PAGE_SIZE_VALUE;
    }

    /**
     * returns pager bean initialized by this class;
	 * this method uses a rule: total pages eq 2*navigation_size+1,
	 * but if less items left or right pages quantity will be reduced;
	 * ex: if cur page is 3 and we need to draw 5 items in left we will draw only 2 items ...
     * @param currentPage current page from request
     * @param itemsCount number of items for current request
     * @param keepParameters query string that will be added to all hrefs
     * @return pager bean
     */
    public common.beans.PagerBean getPagerBean(int currentPage,int itemsCount, String keepParameters)
    {
        common.beans.PagerBean count=new common.beans.PagerBean();
        count.setCurPageParam(PAGE_NUMBER_NAME);
        count.setQueryString(keepParameters);
        count.setItemsCount(itemsCount);
		count.setNavButtonsCountOneSide(navigation_size);

        if (count.getItemsCount()%page_size==0&&count.getItemsCount()>0){
            count.setPageCount((count.getItemsCount()/page_size)-1);
        }else{
            count.setPageCount(count.getItemsCount()/page_size);
        }
		if (currentPage>count.getPageCount()){
			count.setCurrentPage(count.getPageCount());
		} else {
			count.setCurrentPage(currentPage);
		}
        if (count.getPageCount()<=navigation_size||count.getCurrentPage()<=navigation_size){
            count.setFirstPage(0);
        }else{
            count.setFirstPage(count.getCurrentPage()-navigation_size);
        }
        if (count.getPageCount()<=navigation_size||count.getCurrentPage()+navigation_size>=count.getPageCount()){
            count.setLastPage(count.getPageCount());
        }else{
            count.setLastPage(count.getCurrentPage()+navigation_size);
        }

        return count;
    }

    /**
     * returns pager bean initialized by this class;
	 * this method uses a rule: total pages eq 2*navigation_size+1;
	 * if we can't draw navigation we will draw an additional pages to another side ...
     * @param currentPage current page from request
     * @param itemsCount number of items for current request
     * @param keepParameters query string that will be added to all hrefs
     * @return pager bean
     */
    public common.beans.PagerBean getPagerBean2(int currentPage,int itemsCount, String keepParameters)
    {
        common.beans.PagerBean count=new common.beans.PagerBean();
        count.setCurPageParam(PAGE_NUMBER_NAME);
        count.setQueryString(keepParameters);
        count.setItemsCount(itemsCount);
		count.setNavButtonsCountOneSide(navigation_size);

        if (count.getItemsCount()%page_size==0&&count.getItemsCount()>0){
            count.setPageCount((count.getItemsCount()/page_size)-1);
        }else{
            count.setPageCount(count.getItemsCount()/page_size);
        }
		if (currentPage>count.getPageCount()){
			count.setCurrentPage(count.getPageCount());
		} else {
			count.setCurrentPage(currentPage<0?0:currentPage);
		}
		int left_trunc = 0;//how many pages are truncated left
		int right_trunc = 0;//how many pages are truncated right
        if (count.getPageCount()<=navigation_size||count.getCurrentPage()<=navigation_size){
            count.setFirstPage(0);
			left_trunc = navigation_size - count.getCurrentPage();
        }

        if (count.getPageCount()<=navigation_size||count.getCurrentPage()+navigation_size>=count.getPageCount()){
            count.setLastPage(count.getPageCount());
			right_trunc = count.getCurrentPage() + navigation_size - count.getPageCount();
        }else{
			if (left_trunc>0){
				//if we need to draw extra items to the right
				count.setLastPage(count.getCurrentPage()+navigation_size+left_trunc);
				if (count.getLastPage()>count.getPageCount())count.setLastPage(count.getPageCount());
			} else {
				count.setLastPage(count.getCurrentPage()+navigation_size);
			}
        }

		if (count.getPageCount()>navigation_size&&count.getCurrentPage()>navigation_size){
			if (right_trunc>0){
				//if we need to draw extra items to the left
				count.setFirstPage(count.getCurrentPage()-navigation_size-right_trunc);
				if (count.getFirstPage()<0)count.setFirstPage(0);
			} else {
				count.setFirstPage(count.getCurrentPage()-navigation_size);
			}
		}

        return count;
    }
}
