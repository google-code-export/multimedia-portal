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
 * like pager bean but stores id,s of required pages
 * @author demchuck.dima@gmail.com
 */
public class PagerBeanId {
	private Long currentPage;
	private Long firstPage;
	private Long lastPage;
	private Long nextPage;
	private Long prevPage;
    private int itemsCount;

    private String curPageParam;
    private String queryString;

	public Long getCurrentPage() {return currentPage;}
	public void setCurrentPage(Long currentPage) {this.currentPage = currentPage;}

	public Long getFirstPage() {return firstPage;}
	public void setFirstPage(Long firstPage) {this.firstPage = firstPage;}

	public Long getLastPage() {return lastPage;}
	public void setLastPage(Long lastPage) {this.lastPage = lastPage;}

	public int getItemsCount() {return itemsCount;}
	public void setItemsCount(int itemsCount) {this.itemsCount = itemsCount;}

	public String getCurPageParam() {return curPageParam;}
	public void setCurPageParam(String curPageParam) {this.curPageParam = curPageParam;}

	public String getQueryString() {return queryString;}
	public void setQueryString(String queryString) {this.queryString = queryString;}

	public Long getNextPage() {return nextPage;}
	public void setNextPage(Long nextPage) {this.nextPage = nextPage;}

	public Long getPrevPage() {return prevPage;}
	public void setPrevPage(Long prevPage) {this.prevPage = prevPage;}
}
