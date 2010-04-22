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

package core.sitemap.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Sitemap {
    protected Index parent;
    private int sitemap_num;
    private int sitemap_files;

    private FileWriter last_writer = null;

    private final int sitemap_max;
    private final String url_prefix;
    private final File base;

    /**
     * creates a new sitemap object as a child of parent index object
     * @param parent index object that is parent for this object
     * @param sitemap_max max records in one sitemap file
     * @param base directory where we will store all sitemap files
     * @param url_prefix prefix that will be added to the start of 'loc' tag
     */
    protected Sitemap(Index parent, int sitemap_max, File base, String url_prefix){
        this.parent = parent;
        this.sitemap_num = 0;
        this.sitemap_files = 0;
        this.sitemap_max = sitemap_max;
        this.url_prefix = url_prefix;
        this.base = base;
    }

    /**
     * adds a new record to a sitemap
     * @param loc url_prefix is appended before it
     * @param changefreq
     * @param priority
     */
    public void addRecord(String loc, String changefreq, String priority) throws FileNotFoundException, IOException{
        check();
        last_writer.write(Config.URL_START_TAG);
        if (loc!=null){
            last_writer.write(Config.LOC_START_TAG);
            last_writer.write(url_prefix);
            last_writer.write(loc);
            last_writer.write(Config.LOC_END_TAG);
        }
        if (changefreq!=null){
            last_writer.write(Config.CHANGEFREQ_START_TAG);
            last_writer.write(changefreq);
            last_writer.write(Config.CHANGEFREQ_END_TAG);
        }
        if (priority!=null){
            last_writer.write(Config.PRIORITY_START_TAG);
            last_writer.write(priority);
            last_writer.write(Config.PRIORITY_END_TAG);
        }
        last_writer.write(Config.URL_END_TAG);
        last_writer.flush();
        sitemap_num--;
    }

    public void close() throws IOException{
        parent.close();
		if (last_writer!=null){
			last_writer.write(Config.URLSET_END_TAG);
			last_writer.flush();
			last_writer.close();
		}
    }

    protected void check() throws FileNotFoundException, IOException{
        if (sitemap_num==0){
            if (last_writer!=null){
                last_writer.write(Config.URLSET_END_TAG);
                last_writer.flush();
                last_writer.close();
            }
            File next = new File(base, Config.SITEMAP_PREFIX + sitemap_files + Config.XML_SUFFIX);
            if (next.exists()){
                next.delete();
            }
            next.createNewFile();
            next.setWritable(true);
            parent.addRecord(url_prefix + Config.SITEMAP_PREFIX + sitemap_files + Config.XML_SUFFIX);
            last_writer = new FileWriter(next, true);
            last_writer.write(Config.META);
            last_writer.write(Config.URLSET_START_TAG);
            sitemap_num=sitemap_max;
            sitemap_files++;
        }
    }

}
