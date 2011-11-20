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

package core.rss.elem;

/**
 * Class to define the <code>&lt;category&gt;</code> element.
 * <p>It has one optional attribute, domain, a string that identifies a categorization taxonomy.</p> 
 * <p>The value of the element is a forward-slash-separated string that identifies a hierarchic location 
 * in the indicated taxonomy. Processors may establish conventions for the interpretation of categories.</p> 
 * @author Henrique A. Viecili
 */
public abstract class Category {
    String name;
    String domain;
    
    /** The Category constructor with the required param.
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param name The name of the category
     */
    public Category(String name) {
        this(name,null);
    }
    
    /** The Category constructor specifying a domain.
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param name The name of the category
     * @param domain The domain URL
     */
    public Category(String name, String domain) {
        super();
        
        if(name == null || "".equals(name.trim()))
            throw new InvalidRequiredParamException("name required: "+name);
        
        this.name = name;
        this.domain = domain;
    }
    
    /** Gets the category domain
     * @return Returns the domain.
     */
    public String getDomain() {
        return domain;
    }
    /** Sets the category domain
     * @param domain The domain to set.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }
    /** Gets the category name
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /** Sets the category name
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param name The name to set.
     */
    public void setName(String name) {
        if(name == null || "".equals(name.trim()))
            throw new InvalidRequiredParamException("name required: "+name);
        this.name = name;
    }
}