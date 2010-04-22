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
 *  under the License.
 */

package common.beans;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PagerBean {
    private int itemsCount;
    private int firstPage;
    private int lastPage;
    private int currentPage;
    private int pageCount;
    private int navButtonsCountOneSide;

    private String curPageParam;
    private String queryString;

    /**
     * @return quantity of items total (not selected)
     */
    public int getItemsCount() {return itemsCount;}

    /**
     * @param itemsCount quantity of items total (not selected)
     */
    public void setItemsCount(int itemsCount) {this.itemsCount = itemsCount;}

    /**
     * @return current page displayed
     */
    public int getCurrentPage() {return currentPage;}

    /**
     * @param currentPage current page displayed
     */
    public void setCurrentPage(int currentPage) {this.currentPage = currentPage;}

    /**
     * @return parameter that contains current page
     */
    public String getCurPageParam() {return curPageParam;}

    /**
     * @param curPageParam parameter that contains current page
     */
    public void setCurPageParam(String curPageParam) {this.curPageParam = curPageParam;}

    /**
     * @return number of first page displayed on this page
     */
    public int getFirstPage() {return firstPage;}

    /**
     * @param firstPage number of first page displayed on this page
     */
    public void setFirstPage(int firstPage) {this.firstPage = firstPage;}

    /**
     * @return number of last page displayed on this page
     */
    public int getLastPage() {return lastPage;}

    /**
     * @param lastPage number of last page displayed on this page
     */
    public void setLastPage(int lastPage) {this.lastPage = lastPage;}

    /**
     * @return total count of pages for this result set
     */
    public int getPageCount() {return pageCount;}

    /**
     * @param pageCount total count of pages for this result set
     */
    public void setPageCount(int pageCount) {this.pageCount = pageCount;}

    /**
     * @return a part of uri thjat will be added for each href in pager
     */
    public String getQueryString() {return queryString;}

    /**
     * @param queryString a part of uri that will be added to each href in pager
     */
    public void setQueryString(String queryString) {this.queryString = queryString;}

	/**
	 * @return quantity of buttons that will be drawn for pagination
	 */
	public int getNavButtonsCount(){return 2*navButtonsCountOneSide+1;}

	/**
	 * @return quantity of buttons drawn in one side of cur page
	 */
	public int getNavButtonsCountOneSide(){return navButtonsCountOneSide;}

	/**
	 * @param value quantity of buttons drawn in one side of cur page
	 */
	public void setNavButtonsCountOneSide(int value){this.navButtonsCountOneSide = value;}

}
