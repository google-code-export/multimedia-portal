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
 * Class that represent the <code>&lt;channel&gt;</code> element.
 * 
 * @author Henrique A. Viecili
 */
public class Channel {

    // Private Constants
    private static final String GENERATOR = "jRSSGenerator by Henrique A. Viecili";
    private static final String DOCS = "http://blogs.law.harvard.edu/tech/rss";
    
    // Public Constants
    /**
     * Constant indicating that Sunday must be skipped.
     */
    public static final byte SKIP_SUNDAY = 1;
    /**
     * Constant indicating that Monday must be skipped.
     */
    public static final byte SKIP_MONDAY = 2;
    /**
     * Constant indicating that Tuesday must be skipped.
     */
    public static final byte SKIP_TUESDAY = 4;
    /**
     * Constant indicating that Wednesday must be skipped.
     */
    public static final byte SKIP_WEDNESDAY = 8;
    /**
     * Constant indicating that Thursday must be skipped.
     */
    public static final byte SKIP_THURSDAY = 16;
    /**
     * Constant indicating that Friday must be skipped.
     */
    public static final byte SKIP_FRIDAY = 32;
    /**
     * Constant indicating that Saturday must be skipped.
     */
    public static final byte SKIP_SATURDAY = 64;
    /**
     * Constant indicating that the whole Week must be skipped. (do not use, unless you want no refreshing)
     */
    public static final byte SKIP_WEEK = 127;
    
    // Class attributes
    private String title;
    private String link;
    private String description;
    
    private String language;
    private String copyright;
    private String managingEditor;
    private String webMaster;
    private Date pubDate;
    private Date lastBuildDate;
    private List<Channel.Category> categories;
    private String generator;
    private String docs;
    private Cloud cloud;
    private int ttl = -1;
    private Image image;
    private String rating;
    private TextInput textInput;
    private String skipHours;
    private byte skipDays;
    
    private List<Item> items;
    
    // Constructors
    /**
     * The Channel contructor with the three required params.
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param title The name of the channel
     * @param link The URL to the HTML website corresponding to the channel
     * @param description Phrase or sentence describing the channel
     */
    public Channel(String title, String link, String description) {
        super();
        
        if(title == null || "".equals(title.trim()))
            throw new InvalidRequiredParamException("title required: "+title);
        if(link == null || "".equals(link.trim()))
            throw new InvalidRequiredParamException("link required: "+link);
        if(description == null || "".equals(description.trim()))
            throw new InvalidRequiredParamException("description required: "+description);
        
        this.title = title;
        this.link = link;
        this.description = description;
        this.docs = DOCS;
        this.generator = GENERATOR;
        this.categories = new ArrayList<Channel.Category>();
        this.items = new ArrayList<Item>();
    }

    // Getters and Setters
    /** Gets the Channel's items
     * @return Returns the items
     */
    public List getItems() {
        return this.items;
    }
    /** Adds an item to the channel 
     * @param item The item to add.
     */
    public void addItem(Item item) {
        this.items.add(item);
    }    
    /** Gets the Channel's categories.
     * @return Returns the categories.
     */
    public List getCategories() {
        return categories;
    }
    /** Adds a category that the channel belongs to. 
     * @param category The category to add.
     */
    public void addCategory(Channel.Category category) {
        this.categories.add(category);
    }
    /** Gets the cloud of the channel.
     * @see viecili.jrss.generator.elem.Channel.Cloud
     * @return Returns the cloud.
     */
    public Cloud getCloud() {
        return cloud;
    }
    /** Sets the cloud of the channel.
     * @see viecili.jrss.generator.elem.Channel.Cloud
     * @param cloud The cloud to set.
     */
    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }
    /** Gets the copyright notice for content in the channel.
     * @return Returns the copyright.
     */
    public String getCopyright() {
        return copyright;
    }
    /** Sets the copyright notice for content in the channel.
     * @param copyright The copyright to set.
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
    /** Gets the Channel's description.
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /** Sets a phrase or sentence describing the channel (REQUIRED).
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param description The description to set.
     */
    public void setDescription(String description) {
        if(description == null || "".equals(description.trim()))
            throw new InvalidRequiredParamException("description required: "+description);
        this.description = description;
    }
    /** Gets the channel's image.
     * @see viecili.jrss.generator.elem.Channel.Image
     * @return Returns the image.
     */
    public Image getImage() {
        return image;
    }
    /** Sets an image that can be displayed with the channel.
     * @see viecili.jrss.generator.elem.Channel.Image
     * @param image The image to set.
     */
    public void setImage(Image image) {
        this.image = image;
    }
    /** Gets the language the channel is written in.
     * @return Returns the language.
     */
    public String getLanguage() {
        return language;
    }
    /** Sets the language the channel is written in.
     * <p>A list of allowable values for this element, provided by W3C, is
     * <a href="http://www.w3.org/TR/REC-html40/struct/dirlang.html#langcodes" target="_blank">here</a>.
     * @param language The language to set.
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    /** Gets the last time the content of the channel changed.
     * @return Returns the lastBuildDate.
     */
    public Date getLastBuildDate() {
        return lastBuildDate;
    }
    /** Sets the last time the content of the channel changed.
     * @param lastBuildDate The lastBuildDate to set.
     */
    public void setLastBuildDate(Date lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }
    /** Gets the corresponding website link of the Channel.
     * @return Returns the link.
     */
    public String getLink() {
        return link;
    }
    /** Sets the URL to the HTML website corresponding to the channel (REQUIRED).
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param link The link to set.
     */
    public void setLink(String link) {
        if(link == null || "".equals(link.trim()))
            throw new InvalidRequiredParamException("link required: "+link);
        this.link = link;
    }
    /** Gets the <em>email</em> address for person responsible for editorial content.
     * @return Returns the managingEditor's email.
     */
    public String getManagingEditor() {
        return managingEditor;
    }
    /** Sets the <em>email</em> address for person responsible for editorial content.
     * @param email The managingEditor's email to set.
     */
    public void setManagingEditor(String email) {
        this.managingEditor = email;
    }
    /** Gets the publication date for the content in the channel.
     * @return Returns the pubDate.
     */
    public Date getPubDate() {
        return pubDate;
    }
    /** Sets the publication date for the content in the channel.
     * @param pubDate The pubDate to set.
     */
    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }
    /** Gets the PICS rating for the channel.
     * @return Returns the rating.
     */
    public String getRating() {
        return rating;
    }
    /** Sets the PICS rating for the channel.
     * <p>Example:<br><code>(PICS-1.1 &quot;http://www.classify.org/safesurf/&quot; l r (SS~~000 1))</code></p>
     * <p>More info about the <em>Platform for Internet Content Selection</em> (PICS) can be found 
     * <a href="http://www.w3.org/PICS/" target="_blank">here</a>.</p>
     * @param rating The rating to set.
     */
    public void setRating(String rating) {
        this.rating = rating;
    }
    /** Gets the skip days set.
     * @return Returns the skipDays.
     */
    public byte getSkipDays() {
        return skipDays;
    }
    /** Sets wich days of week the aggregators can skip when refreshing.
     * <p>The days of week to be skipped must be passed using the | (or) operator. By example:</p>
     * <blockquote><code>setSkipDays(Channel.SKIP_SUNDAY | Channel.SKIP_SATURDAY);</code></blockquote>
     * @param skipDays The skipDays to set.
     */
    public void setSkipDays(byte skipDays) {
        this.skipDays = skipDays;
    }
    /** Gets the skip hours set.
     * @return Returns the skipHours.
     */
    public String getSkipHours() {
        return skipHours;
    }
    /** Sets wich hours the aggregators can skip when refreshing.
     * <p>The hours can be passed as interval or as list. By example:</p>
     * <blockquote><code>setSkipHours("22-6");<br>
     * setSkipHours("22,0,2,4");<br>setSkipHours("22-6,12,13");</code></blockquote> 
     * @param skipHours The skipHours to set.
     */
    public void setSkipHours(String skipHours) {
        this.skipHours = skipHours;
    }
    /** Gets the channel's text input.
     * @see viecili.jrss.generator.elem.Channel.TextInput
     * @return Returns the textInput.
     */
    public TextInput getTextInput() {
        return textInput;
    }
    /** Sets a text input box that can be displayed with the channel.
     * @see viecili.jrss.generator.elem.Channel.TextInput
     * @param textInput The textInput to set.
     */
    public void setTextInput(TextInput textInput) {
        this.textInput = textInput;
    }
    /** Gets the Channel's title.
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /** Sets the name of the channel (REQUIRED).
     * <p>It's how people refer to your service. If you have an HTML website 
     * that contains the same information as your RSS file, the title of your 
     * channel should be the same as the title of your website.</p>
     * @exception InvalidRequiredParamException if some parameter passed is invalid
     * @param title The title to set.
     */
    public void setTitle(String title) {
        if(title == null || "".equals(title.trim()))
            throw new InvalidRequiredParamException("title required: "+title);
        this.title = title;
    }
    /** Gets the channel's <em>time to live</em>.
     * @return Returns the ttl.
     */
    public int getTtl() {
        return ttl;
    }
    /** Sets the channel's <em>time to live</em>.
     * <p>It's the number of <em>minutes</em> that indicates how long a channel can be cached before refreshing from the source.</p>
     * @param ttl The ttl in <em>minutes</em> to set.
     */
    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
    /** Gets the <em>email</em> address for person responsible for technical issues relating to channel.
     * @return Returns the webMaster's email.
     */
    public String getWebMaster() {
        return webMaster;
    }
    /** Sets the <em>email</em> address for person responsible for technical issues relating to channel.
     * @param email The webMaster's email to set.
     */
    public void setWebMaster(String email) {
        this.webMaster = email;
    }
    /** Gets the URL that points to the documentation for the format used in the generation of the RSS file.
     * @return Returns the docs' URL.
     */
    public String getDocs() {
        return docs;
    }
    /** Gets the string indicating the name of this generator
     * @return Returns the generator.
     */
    public String getGenerator() {
        return generator;
    }    
    
    // Inner Classes
    /**
     * Class to define the <code>&lt;cloud&gt;</code> optional sub-element of <code>&lt;channel&gt;</code>.
     * <p>It specifies a web service that supports the rssCloud interface which can be implemented in HTTP-POST, XML-RPC or SOAP 1.1.</p>
     * <p>Its purpose is to allow processes to register with a cloud to be notified of updates to the channel, implementing a lightweight publish-subscribe protocol for RSS feeds.</p>
     * <blockquote><code>&lt;cloud domain=&quot;rpc.sys.com&quot; port=&quot;80&quot; path=&quot;/RPC2&quot; registerProcedure=&quot;myCloud.rssPleaseNotify&quot; protocol=&quot;xml-rpc&quot; /&gt;</code></blockquote>
     * <p>In this example, to request notification on the channel it appears in, you would send an XML-RPC message to rpc.sys.com on port 80, with a path of /RPC2. The procedure to call is myCloud.rssPleaseNotify.</p>
     * A full explanation of this element and the rssCloud interface is <a href="http://blogs.law.harvard.edu/tech/soapMeetsRss#rsscloudInterface" target="_blank">here</a>.
     * 
     * @author Henrique A. Viecili
     */
    public static class Cloud {
        String domain;
        int port;
        String path;
        String registerProcedure;
        String protocol;

        /**
         * Constant indicating the xml-rpc protocol
         */
        public static final String XML_RPC_PROTOCOL = "xml-rpc";
        /**
         * Constant indicating the soap protocol
         */
        public static final String SOAP_PROTOCOL = "soap";
        
        /** The Cloud constructor with the required params.
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param domain The workstation's domain
         * @param port The TCP port the workstation is listening on
         * @param path The path to its responder
         * @param registerProcedure The name of the procedure that the cloud should call to notify the workstation of changes
         * @param protocol String indicating which protocol to use
         */
        public Cloud(String domain, int port, String path,
                String registerProcedure, String protocol) {
            super();
            
            if(domain == null || "".equals(domain.trim()))
                throw new InvalidRequiredParamException("domain required: "+domain);
            if(port <= 0 || port > 65000)
                throw new InvalidRequiredParamException("port must be between 1 and 65000: "+port);
            if(path == null || "".equals(path.trim()))
                throw new InvalidRequiredParamException("path required: "+path);
            if(registerProcedure == null || "".equals(registerProcedure.trim()))
                throw new InvalidRequiredParamException("registerProcedure required: "+registerProcedure);
            if(!XML_RPC_PROTOCOL.equals(protocol) || !SOAP_PROTOCOL.equals(protocol) )
                throw new InvalidRequiredParamException("protocol must be '"+XML_RPC_PROTOCOL+"' or '"+SOAP_PROTOCOL+"': "+protocol);
            
            this.domain = domain;
            this.port = port;
            this.path = path;
            this.registerProcedure = registerProcedure;
            this.protocol = protocol;
        }
        
        /** Gets the Cloud domain
         * @return Returns the domain.
         */
        public String getDomain() {
            return domain;
        }
        /** Sets the Cloud domain 
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param domain The domain to set.
         */
        public void setDomain(String domain) {
            if(domain == null || "".equals(domain.trim()))
                throw new InvalidRequiredParamException("domain required: "+domain);
            this.domain = domain;
        }
        /** Gets the Cloud path 
         * @return Returns the path.
         */
        public String getPath() {
            return path;
        }
        /** Sets the Cloud path
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param path The path to set.
         */
        public void setPath(String path) {
            if(path == null || "".equals(path.trim()))
                throw new InvalidRequiredParamException("path required: "+path);
            this.path = path;
        }
        /** Gets the Cloud port
         * @return Returns the port.
         */
        public int getPort() {
            return port;
        }
        /** Sets the Cloud port
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param port The port to set.
         */
        public void setPort(int port) {
            if(port <= 0 || port > 65000)
                throw new InvalidRequiredParamException("port must be between 1 and 65000: "+port);
            this.port = port;
        }
        /** Gets the Cloud protocol 
         * @return Returns the protocol.
         */
        public String getProtocol() {
            return protocol;
        }
        /** Sets the Cloud protocol
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param protocol The protocol to set.
         */
        public void setProtocol(String protocol) {
            if(!XML_RPC_PROTOCOL.equals(protocol) || !SOAP_PROTOCOL.equals(protocol) )
                throw new InvalidRequiredParamException("protocol must be '"+XML_RPC_PROTOCOL+"' or '"+SOAP_PROTOCOL+"': "+protocol);
            this.protocol = protocol;
        }
        /** Gets the Cloud register procedure 
         * @return Returns the registerProcedure.
         */
        public String getRegisterProcedure() {
            return registerProcedure;
        }
        /** Sets the Cloud register procedure
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param registerProcedure The registerProcedure to set.
         */
        public void setRegisterProcedure(String registerProcedure) {
            if(registerProcedure == null || "".equals(registerProcedure.trim()))
                throw new InvalidRequiredParamException("registerProcedure required: "+registerProcedure);
            this.registerProcedure = registerProcedure;
        }
    }
    
    /**
     * Class to define the <code>&lt;image&gt;</code> optional sub-element of <code>&lt;channel&gt;</code>.
     * It specifies a GIF, JPEG or PNG image that can be displayed with the channel.
     * 
     * @author Henrique A. Viecili
     */
    public static class Image {

        /**
         * Constant indicating the default width value of the image
         */
        public static final int DEFAULT_WIDTH = 0;
        /**
         * Constant indicating the default height value of the image
         */
        public static final int DEFAULT_HEIGHT = 0;
        /**
         * Constant indicating the max width value of the image
         */
        public static final int MAX_WIDTH = 144;
        /**
         * Constant indicating the max height value of the image
         */
        public static final int MAX_HEIGHT = 400;
        
        String url;
        String title;
        String link;
        int width;
        int height;
        String description;
        
        /** The Image constructor with the minimum params required.
         * <p><em>Note, in practice the image <code>&lt;title&gt;</code> and <code>&lt;link&gt;</code> 
         * should have the same value as the channel's <code>&lt;title&gt;</code> and <code>&lt;link&gt;</code></em></p>
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param url The URL of a GIF, JPEG or PNG image that represents the channel
         * @param title Describes the image, it's used in the ALT attribute of the HTML <code>&lt;img&gt;</code> tag when the channel is rendered in HTML
         * @param link The URL of the site, when the channel is rendered, the image is a link to the site
         */
        public Image(String url, String title, String link) {
            this(url,title,link,DEFAULT_WIDTH,DEFAULT_HEIGHT,null);
        }
        
        /** The Image constructor with all params.
         * <p><em>Note, in practice the image <code>&lt;title&gt;</code> and <code>&lt;link&gt;</code> 
         * should have the same value as the channel's <code>&lt;title&gt;</code> and <code>&lt;link&gt;</code></em></p>
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param url The URL of a GIF, JPEG or PNG image that represents the channel
         * @param title Describes the image, it's used in the ALT attribute of the HTML <code>&lt;img&gt;</code> tag when the channel is rendered in HTML
         * @param link The URL of the site, when the channel is rendered, the image is a link to the site
         * @param width The width of the image in pixels (max = 144; default = 88)
         * @param height The height of the image in pixels (max = 400; default = 31)
         * @param description The text that is included in the TITLE attribute of the link formed around the image in the HTML rendering
         */
        public Image(String url, String title, String link, int width,
                int height, String description) {
            super();
            
            if(url == null || "".equals(url.trim()))
                throw new InvalidRequiredParamException("url required: "+url);
            if(title == null || "".equals(title.trim()))
                throw new InvalidRequiredParamException("title required: "+title);
            if(link == null || "".equals(link.trim()))
                throw new InvalidRequiredParamException("link required: "+link);
            if(width < 0 || width > MAX_WIDTH)
                throw new InvalidRequiredParamException("width must be between 0 and "+MAX_WIDTH+": "+width);
            if(height < 0 || height > MAX_HEIGHT)
                throw new InvalidRequiredParamException("height must be between 0 and "+MAX_HEIGHT+": "+height);
            
            this.url = url;
            this.title = title;
            this.link = link;
            this.width = width;
            this.height = height;
            this.description = description;
        }
        
        /** Gets the Image description
         * @return Returns the description.
         */
        public String getDescription() {
            return description;
        }
        /** Sets the Image description
         * @param description The description to set.
         */
        public void setDescription(String description) {
            this.description = description;
        }
        /** Gets the Image height 
         * @return Returns the height.
         */
        public int getHeight() {
            return height;
        }
        /** Sets the Image height
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param height The height to set.
         */
        public void setHeight(int height) {
            if(height < 0 || height > MAX_HEIGHT)
                throw new InvalidRequiredParamException("height must be between 0 and "+MAX_HEIGHT+": "+height);
            this.height = height;
        }
        /** Gets the Image link 
         * @return Returns the link.
         */
        public String getLink() {
            return link;
        }
        /** Sets the Image link
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param link The link to set.
         */
        public void setLink(String link) {
            if(link == null || "".equals(link.trim()))
                throw new InvalidRequiredParamException("link required: "+link);
            this.link = link;
        }
        /** Gets the Image title 
         * @return Returns the title.
         */
        public String getTitle() {
            return title;
        }
        /** Sets the Image title
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param title The title to set.
         */
        public void setTitle(String title) {
            if(title == null || "".equals(title.trim()))
                throw new InvalidRequiredParamException("title required: "+title);
            this.title = title;
        }
        /** Gets the Image url 
         * @return Returns the url.
         */
        public String getUrl() {
            return url;
        }
        /** Sets the Image url
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param url The url to set.
         */
        public void setUrl(String url) {
            if(url == null || "".equals(url.trim()))
                throw new InvalidRequiredParamException("url required: "+url);
            this.url = url;
        }
        /** Gets the Image width 
         * @return Returns the width.
         */
        public int getWidth() {
            return width;
        }
        /** Sets the Image width
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param width The width to set.
         */
        public void setWidth(int width) {
            if(width < 0 || width > MAX_WIDTH)
                throw new InvalidRequiredParamException("width must be between 0 and "+MAX_WIDTH+": "+width);
            this.width = width;
        }
    }
    
    /**
     * Class to define the <code>&lt;textInput&gt;</code> optional sub-element of <code>&lt;channel&gt;</code>.
     * <p>The purpose of the <code>&lt;textInput&gt;</code> element is something of a mystery. 
     * You can use it to specify a search engine box. Or to allow a reader to provide feedback.<br> 
     * Most aggregators ignore it.</p>
     * 
     * @author Henrique A. Viecili
     */
    public static class TextInput {
        String title;
        String description;
        String name;
        String link;
        
        /** The TextInput constructor with required params.
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param title The label of the Submit button in the text input area
         * @param description Explains the text input area
         * @param name The name of the text object in the text input area
         * @param link The URL of the CGI script that processes text input requests
         */
        public TextInput(String title, String description, String name,
                String link) {
            super();
            
            if(title == null || "".equals(title.trim()))
                throw new InvalidRequiredParamException("title required: "+title);
            if(description == null || "".equals(description.trim()))
                throw new InvalidRequiredParamException("description required: "+description);
            if(name == null || "".equals(name.trim()))
                throw new InvalidRequiredParamException("name required: "+name);
            if(link == null || "".equals(link.trim()))
                throw new InvalidRequiredParamException("link required: "+link);
            
            this.title = title;
            this.description = description;
            this.name = name;
            this.link = link;
        }
        
        /** Gets the TextInput description
         * @return Returns the description.
         */
        public String getDescription() {
            return description;
        }
        /** Sets the TextInput description
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param description The description to set.
         */
        public void setDescription(String description) {
            if(description == null || "".equals(description.trim()))
                throw new InvalidRequiredParamException("description required: "+description);
            this.description = description;
        }
        /** Gets the TextInput link
         * @return Returns the link.
         */
        public String getLink() {
            return link;
        }
        /** Sets the TextInput link
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param link The link to set.
         */
        public void setLink(String link) {
            if(link == null || "".equals(link.trim()))
                throw new InvalidRequiredParamException("link required: "+link);
            this.link = link;
        }
        /** Gets the TextInput name
         * @return Returns the name.
         */
        public String getName() {
            return name;
        }
        /** Sets the TextInput name
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param name The name to set.
         */
        public void setName(String name) {
            if(name == null || "".equals(name.trim()))
                throw new InvalidRequiredParamException("name required: "+name);
            this.name = name;
        }
        /** Gets the TextInput title
         * @return Returns the title.
         */
        public String getTitle() {
            return title;
        }
        /** Sets the TextInput title
         * @exception InvalidRequiredParamException if some parameter passed is invalid
         * @param title The title to set.
         */
        public void setTitle(String title) {
            if(title == null || "".equals(title.trim()))
                throw new InvalidRequiredParamException("title required: "+title);
            this.title = title;
        }
    }
    
    /**
     * Class to define the <code>&lt;category&gt;</code> optional sub-element of <code>&lt;channel&gt;</code>.
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
}
