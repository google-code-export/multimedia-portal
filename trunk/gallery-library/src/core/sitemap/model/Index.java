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
public class Index {
    protected Sitemap child;
    private int index_num;
    private int index_files;

    private File base;
    private FileWriter last_writer = null;

    private final int index_max;
    private final String url_prefix;

    /**
     * creates a new sitemap object as a child of parent index object
     * @param sitemap_max max records in one sitemap file
     * @param base directory where we will store all sitemap files
     * @param url_prefix prefix that will be added to the start of 'loc' tag
     */
    public Index(int index_max, File base, String url_prefix){
        this.child = new Sitemap(this, index_max, base, url_prefix);
        this.base = base;
        this.index_num = 0;
        this.index_files = 0;
        this.index_max = index_max;
        this.url_prefix = url_prefix;
    }

    protected void addRecord(String loc) throws FileNotFoundException, IOException{
        check();
        last_writer.write(Config.SITEMAP_START_TAG);
        if (loc!=null){
            last_writer.write(Config.LOC_START_TAG);
            last_writer.write(loc);
            last_writer.write(Config.LOC_END_TAG);
        }
        last_writer.write(Config.SITEMAP_END_TAG);
        last_writer.flush();
        index_num--;
    }

    protected void close() throws IOException{
		if (last_writer!=null){
			last_writer.write(Config.INDEX_END_TAG);
			last_writer.flush();
			last_writer.close();
		}
    }

    protected void check() throws FileNotFoundException, IOException{
        if (index_num==0){
            if (last_writer!=null){
                last_writer.write(Config.SITEMAP_START_TAG);
                last_writer.write(Config.LOC_START_TAG);
                last_writer.write(url_prefix + Config.INDEX_PREFIX + index_files + Config.XML_SUFFIX);
                last_writer.write(Config.LOC_END_TAG);
                last_writer.write(Config.SITEMAP_END_TAG);
                last_writer.write(Config.INDEX_END_TAG);
                last_writer.flush();
                last_writer.close();
            }
            File next = new File(base, Config.INDEX_PREFIX + index_files + Config.XML_SUFFIX);
            if (next.exists()){
                next.delete();
            }
            next.createNewFile();
            next.setWritable(true, false);
            last_writer = new FileWriter(next, true);
            last_writer.write(Config.META);
            last_writer.write(Config.INDEX_START_TAG);
            index_num = index_max;
            index_files++;
        }
    }

    public Sitemap getChild(){return this.child;}
}
