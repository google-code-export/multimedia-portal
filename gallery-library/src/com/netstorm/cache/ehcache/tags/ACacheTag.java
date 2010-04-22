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

package com.netstorm.cache.ehcache.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public abstract class ACacheTag extends BodyTagSupport{
	public static final String DEFAULT_GENERATION_TEXT = "Генерируется";
	protected String region_key;

	/* the text that will be shown while cached content is generated */
	protected String generation_text = DEFAULT_GENERATION_TEXT;

	protected Object element_key;

    /**
     * The cache holding the web pages. Ensure that all threads for a given cache name are using the same instance of this.
     */
    protected BlockingCache blockingCache;

    /**
     * Creates new instance of tag handler
     */
    public ACacheTag() {super();}

    /**
     * Fill in this method to process the body content of the tag.
     * You only need to do this if the tag's BodyContent property
     * is set to "JSP" or "tagdependent."
     * If the tag's bodyContent is set to "empty," then this method
     * will not be called.
     */
    protected void writeTagBodyContent(JspWriter out, BodyContent bodyContent) throws IOException {
        // insert code to write html before writing the body content.
        // e.g.:
        //
        // out.println("<strong>" + attribute_1 + "</strong>");
        // out.println("   <blockquote>");

        // write the body content (after processing by the JSP engine) on the output Writer
        bodyContent.writeOut(out);

        // Or else get the body content as a string and process it, e.g.:
        //     String bodyStr = bodyContent.getString();
        //     String result = yourProcessingMethod(bodyStr);
        //     out.println(result);

        // insert code to write html after writing the body content.
        // e.g.:
        //
        // out.println("   </blockquote>");


        // clear the body content for the next time through.
        bodyContent.clearBody();
    }

    /**
     * This method is called when the JSP engine encounters the start tag,
     * after the attributes are processed.
     * Scripting variables (if any) have their values set here.
     * @return EVAL_BODY_BUFFERED if the JSP engine should evaluate the tag body, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
	 * Instead, modify the methods that this method calls.
	 * @throws JspException
     */
    @Override
    public int doStartTag() throws JspException {
		//initializing cache
        synchronized (this.getClass()) {
			Ehcache cache = getCacheManager().getEhcache(region_key);
			if (cache==null){
				throw new JspException("no cache region defined for: "+region_key);
			}
			if (!(cache instanceof BlockingCache)) {
				//decorate and substitute
				BlockingCache newBlockingCache = new BlockingCache(cache);
				getCacheManager().replaceCacheWithDecoratedCache(cache, newBlockingCache);
			}
			blockingCache = (BlockingCache) getCacheManager().getEhcache(region_key);
			/*Integer blockingTimeoutMillis = parseBlockingCacheTimeoutMillis(filterConfig);
			if (blockingTimeoutMillis != null && blockingTimeoutMillis > 0) {
				blockingCache.setTimeoutMillis(blockingTimeoutMillis);
			}*/
        }

        if (theBodyShouldBeEvaluated()) {
            return EVAL_BODY_BUFFERED;
        } else {
            return SKIP_BODY;
        }
    }

    /**
     * This method is called after the JSP engine finished processing the tag.
     * @return EVAL_PAGE if the JSP engine should continue evaluating the JSP page, otherwise return SKIP_PAGE.
     * This method is automatically generated. Do not modify this method.
	 * Instead, modify the methods that this method calls.
	 * @throws JspException
     */
    @Override
    public int doEndTag() throws JspException {return EVAL_PAGE;}

    /**
     * This method is called after the JSP engine processes the body content of the tag.
     * @return EVAL_BODY_AGAIN if the JSP engine should evaluate the tag body again, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
	 * Instead, modify the methods that this method calls.
	 * @throws JspException
     */
    @Override
    public int doAfterBody() throws JspException {
		//System.out.println("after body");
        String body = null;
        try {
            // This code is generated for tags whose bodyContent is "JSP"
            // if we have a body, and we have not been told to use the cached version
            if ((body = bodyContent.getString()) != null) {
				blockingCache.put(new Element(element_key, body));
				BodyContent bodyCont = getBodyContent();
				JspWriter out = bodyCont.getEnclosingWriter();

				writeTagBodyContent(out, bodyCont);
            }
			//else we have allready used the cached version
        } catch (Exception ex) {
            handleBodyContentException(ex);
        }
        return SKIP_BODY;
    }

    /**
     * Handles exception from processing the body content.
     */
    protected void handleBodyContentException(Exception ex) throws JspException {
        // Since the doAfterBody method is guarded, place exception handing code here.
        throw new JspException("Error in ACacheTag tag", ex);
    }

	private boolean theBodyShouldBeEvaluated() throws JspException {
		//System.out.println("the body should be evaluated?");
		element_key = generateKey();
        Element element = blockingCache.get(element_key);
        if (element == null || element.getObjectValue() == null) {
			//try {
				//System.out.println("eval");
				blockingCache.put(new Element(element_key, generation_text));
			//	Thread.sleep(10000);
			//} catch (InterruptedException ex) {
			//	Logger.getLogger(ACacheTag.class.getName()).log(Level.SEVERE, null, ex);
			//}
			return true;
		} else {
			try {
				String content = (String)element.getObjectValue();
				pageContext.getOut().write(content);
			} catch (IOException ex) {
				handleBodyContentException(ex);
			}
			//System.out.println("not eval");
			return false;
		}

	}

	@Override
	protected void finalize() throws Throwable {
		this.region_key = null;
		this.element_key = null;
		this.blockingCache = null;
		this.generation_text = null;
		super.finalize();
	}

	@Override
	public void release() {
		this.region_key = null;
		this.element_key = null;
		this.generation_text = DEFAULT_GENERATION_TEXT;
		super.release();
	}

	/**
	 * override this method to generate a key for body content of a tag
	 * this method is called only once, in the theBodyShouldBeEvaluated() method
	 * and its result is put into element_key property
	 * @return key
	 */
	protected abstract Object generateKey();

    /**
     * Gets the CacheManager for this CachingFilter. It is therefore up to subclasses what CacheManager to use.
     * <p/>
     * This method was introduced in ehcache 1.2.1. Older versions used a singleton CacheManager instance created with
     * the default factory method.
     *
     * @return the CacheManager to be used
     * @since 1.2.1
     */
    protected CacheManager getCacheManager() {return CacheManager.getInstance();}

	/**
	 * the key of region
	 * @param key
	 */
	public void setKey(String key) {this.region_key = key;}

	/* the text that will be shown while cached content is generated */
	public void setGenerationText(String generation_text) {this.generation_text = generation_text;}
	
}
