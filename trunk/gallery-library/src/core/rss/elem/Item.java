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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class that represent the <code>&lt;item&gt;</code> element.
 * 
 * @author Henrique A. Viecili
 */
public class Item {

    private String title;
    private String link;
    private String description;

    private String author;
    private List<Item.Category> categories;
    private String comments;
    private Enclosure enclosure;
    private Guid guid;
    private Date pubDate;
    private Source source;
    
    /** The Item constructor with item or description required.
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param title The title of the item
     * @param description The item synopsis
     */
    public Item(String title, String description) {
        this(title,null,description);
    }
    
    /** The Item constructor specifying a link to the item.
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param title The title of the item
     * @param link The URL of the item
     * @param description The item synopsis
     */
    public Item(String title, String link, String description) {
        super();
        
        if((description == null || "".equals(description.trim())) && 
                (title == null || "".equals(title.trim())))
            throw new InvalidRequiredParamException("title or description required: "+title+" "+description);
        
        this.title = title;
        this.link = link;
        this.description = description;
        this.categories = new ArrayList<Item.Category>();
    }
    
    /** Gets the email address of the author of the item
     * @return Returns the author.
     */
    public String getAuthor() {
        return author;
    }
    /** Sets the email address of the author of the item
     * @param author The author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    /** Gets the URL of a page for comments relating to the item
     * @return Returns the comments.
     */
    public String getComments() {
        return comments;
    }
    /** Sets the URL of a page for comments relating to the item
     * @param comments The comments to set.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    /** Gets the item synopsis
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /** Sets the item synopsis
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param description The description to set.
     */
    public void setDescription(String description) {
        if((description == null || "".equals(description.trim())) && 
                (title == null || "".equals(title.trim())))
            throw new InvalidRequiredParamException("title or description required: "+title+" "+description);
        this.description = description;
    }
    /** Gets an enclosure describing a media object that is attached to the item
     * @return Returns the enclosure.
     */
    public Enclosure getEnclosure() {
        return enclosure;
    }
    /** Sets an enclosure describing a media object that is attached to the item
     * @param enclosure The enclosure to set.
     */
    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }
    /** Gets the guid that uniquely identifies the item
     * @return Returns the guid.
     */
    public Guid getGuid() {
        return guid;
    }
    /** Sets a guid that uniquely identifies the item
     * @param guid The guid to set.
     */
    public void setGuid(Guid guid) {
        this.guid = guid;
    }
    /** Gets the URL of the item
     * @return Returns the link.
     */
    public String getLink() {
        return link;
    }
    /** Sets the URL of the item
     * @param link The link to set.
     */
    public void setLink(String link) {
        this.link = link;
    }
    /** Gets the date that indicates when the item was published
     * @return Returns the pubDate.
     */
    public Date getPubDate() {
        return pubDate;
    }
    /** Sets a date that indicates when the item was published
     * @param pubDate The pubDate to set.
     */
    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }
    /** Gets the source info of the channel that the item came from 
     * @return Returns the source.
     */
    public Source getSource() {
        return source;
    }
    /** Sets the source info of the channel that the item came from
     * @param source The source to set.
     */
    public void setSource(Source source) {
        this.source = source;
    }
    /** Gets the title of the item
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /** Sets the title of the item
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param title The title to set.
     */
    public void setTitle(String title) {
        if((description == null || "".equals(description.trim())) && 
                (title == null || "".equals(title.trim())))
            throw new InvalidRequiredParamException("title or description required: "+title+" "+description);
        this.title = title;
    }
    /** Gets the item's categories
     * @return Returns the categories.
     */
    public List<Item.Category> getCategories() {
        return categories;
    }
    /** Adds a category that the item belongs to.
     * @param category
     */
    public void addCategory(Item.Category category) {
        this.categories.add(category);
    }
    
    // Inner Classes
    /**
     * Class to define the <code>&lt;category&gt;</code> optional sub-element of <code>&lt;item&gt;</code>.
     * @see viecili.jrss.generator.elem.Category 
     * @author Henrique A. Viecili
     */
    public static class Category extends core.rss.elem.Category {

        /** The Category constructor with the required param.
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @see viecili.jrss.generator.elem.Category#Category(String)
         * @param name The name of the category
         */
        public Category(String name) {
            super(name);
        }
        /** The Category constructor specifying a domain.
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @see viecili.jrss.generator.elem.Category#Category(String, String)
         * @param name The name of the category
         * @param domain The domain URL
         */
        public Category(String name, String domain) {
            super(name, domain);
        }
    }
    
    /**
     * Class to define the <code>&lt;enclosure&gt;</code> optional sub-element of <code>&lt;item&gt;</code>.
     * <p>It can describe any type of file, enabling the aggregator can know in advance, without having 
     * to do any communication, what it's going to get, and apply scheduling and filtering rules.<br>
     * An example is:</p>
     * <blockquote><code>&lt;enclosure url=&quot;http://mp3.centurymedia.com/krisiun_Murderer_WorksofCarnage.mp3&quot; length=&quot;2629133&quot; type=&quot;audio/mpeg&quot;/></code></blockquote>
     * @author Henrique A. Viecili
     */
    public static class Enclosure {
        String url;
        long length;
        String type;

        /** The Enclosure constructor with the required params.
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param url The URL where the enclosure is located. (must be HTTP protocol)
         * @param length The length in <em>bytes</em> of the enclosure
         * @param type The MIME type of the enclosure
         */
        public Enclosure(String url, long length, String type) {
            super();
            
            if(url == null || "".equals(url.trim()))
                throw new InvalidRequiredParamException("url required: "+url);
            if(length <= 0)
                throw new InvalidRequiredParamException("length must be a positive non-zero value: "+length);
            if(type == null || "".equals(type.trim()))
                throw new InvalidRequiredParamException("type required: "+type);
            
            this.url = url;
            this.length = length;
            this.type = type;
        }
        
        /** Gets the Enclousre length
         * @return Returns the length.
         */
        public long getLength() {
            return length;
        }
        /** Sets the Enclousre length 
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param length The length in <em>bytes</em> to set.
         */
        public void setLength(long length) {
            if(length <= 0)
                throw new InvalidRequiredParamException("length must be a positive non-zero value: "+length);
            this.length = length;
        }
        /** Gets the Enclousre MIME type
         * @return Returns the type.
         */
        public String getType() {
            return type;
        }
        /** Sets the Enclousre MIME type
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param type The type to set.
         */
        public void setType(String type) {
            if(type == null || "".equals(type.trim()))
                throw new InvalidRequiredParamException("type required: "+type);
            this.type = type;
        }
        /** Gets the Enclousre URL
         * @return Returns the url.
         */
        public String getUrl() {
            return url;
        }
        /** Sets the Enclousre URL 
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param url The url to set.
         */
        public void setUrl(String url) {
            if(url == null || "".equals(url.trim()))
                throw new InvalidRequiredParamException("url required: "+url);
            this.url = url;
        }
    }
    
    /**
     * Class to define the <code>&lt;guid&gt;</code> optional sub-element of <code>&lt;item&gt;</code>.
     * <p>GUID stands for Globally Unique IDentifier. It's a string that uniquely identifies the item. 
     * When present, an aggregator may choose to use this string to determine if an item is new.</p>
     * <blockquote><code>&lt;guid&gt;http://some.server.com/weblogItem3207&lt;/guid&gt;</code></blockquote>
     * <p>There are no rules for the syntax of a guid. Aggregators must view them as a string. 
     * It's up to the source of the feed to establish the uniqueness of the string.</p> 
     * <p>If the <code>&lt;guid&gt;</code> element has an attribute named <code>isPermaLink</code> with a value of <code>true</code>, the reader may 
     * assume that it is a permalink to the item, that is, a url that can be opened in a Web browser, 
     * that points to the full item described by the <code>&lt;item&gt;</code> element. An example:</p>
     * <blockquote><code>&lt;guid isPermaLink=&quot;true&quot;>http://some.server.com/weblogItem3207&lt;/guid&gt;</code></blockquote>
     * <p><code>isPermaLink</code> is optional, its default value is <code>true</code>. If its value is <code>false</code>, 
     * the guid may not be assumed to be a url, or a url to anything in particular.</p>
     * 
     * @author Henrique A. Viecili
     */
    public static class Guid {
        
        /**
         * The default value of the <code>isPermaLink</code> attribute of the <code>&lt;guid&gt;</code> element.
         */
        public static final boolean DEFAULT_PERMA_LINK = true;
        
        String id;
        boolean permaLink;
        
        /** The Guid constructor with the required param and default permaLink
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param id The guid identifier (it should be a URL)
         */
        public Guid(String id) {
            this(id,DEFAULT_PERMA_LINK);
        }
        
        /** The Guid constructor specifying if the <code>id</code> represents a permanent link to the item.
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param id The guid identifier (it should be a URL)
         * @param permaLink Defines the <code>id</code> as a permanent link
         */
        public Guid(String id, boolean permaLink) {
            super();
            
            if(id == null || "".equals(id.trim()))
                throw new InvalidRequiredParamException("id required: "+id);
            
            this.id = id;
            this.permaLink = permaLink;
        }
        /** Gets the Guid id
         * @return Returns the id.
         */
        public String getId() {
            return id;
        }
        /** Sets the Guid id
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param id The id to set.
         */
        public void setId(String id) {
            if(id == null || "".equals(id.trim()))
                throw new InvalidRequiredParamException("id required: "+id);
            this.id = id;
        }
        /** Returns whether the id represents a permanent link
         * @return Returns the permaLink.
         */
        public boolean isPermaLink() {
            return permaLink;
        }
        /** Sets whether the id is a permanent link
         * @param permaLink The permaLink to set.
         */
        public void setPermaLink(boolean permaLink) {
            this.permaLink = permaLink;
        }
    }
    
    /**
     * Class to define the <code>&lt;source&gt;</code> optional sub-element of <code>&lt;item&gt;</code>.
     * @author Henrique A. Viecili
     */
    public static class Source {
        String value;
        String url;
        
        /** The Source constructor with the required param
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param url The link to the XMLization of the source
         */
        public Source(String url) {
            this(url,null);
        }
        
        /** The Source constructor specifying the channel name that the item came from
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param url The link to the XMLization of the source
         * @param value The channel name
         */
        public Source(String url, String value) {
            super();
            
            if(url == null || "".equals(url.trim()))
                throw new InvalidRequiredParamException("url required: "+url);
            
            this.value = value;
            this.url = url;
        }
        
        /** Gets the Source URL
         * @return Returns the url.
         */
        public String getUrl() {
            return url;
        }
        /** Sets the Source URL
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param url The url to set.
         */
        public void setUrl(String url) {
            if(url == null || "".equals(url.trim()))
                throw new InvalidRequiredParamException("url required: "+url);
            this.url = url;
        }
        /** Gets the Source value
         * @return Returns the value.
         */
        public String getValue() {
            return value;
        }
        /** Sets the Source value
         * @param value The value to set.
         */
        public void setValue(String value) {
            this.value = value;
        }
    }
    
}