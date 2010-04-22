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

package common.services.generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

/**
 *
 * @param <T> 
 * @author demchuck.dima@gmail.com
 */
public abstract class GenericCacheService<T> implements IGenericCacheService<T>{
    protected final Logger logger = Logger.getLogger(this.getClass());
    /* path to folder where the cache will be stored */
    protected String path;
    protected T cached_object;

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(path, "path", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

    @Override
    public synchronized void clearCache() {
		File f = new File(path);
		File[] files = f.listFiles();
		for (File file:files){
			file.delete();
		}
		cached_object = null;
    }

    @Override
    public synchronized void refreshCache(){
        T tmp = getFromDB();
        cached_object = tmp;
        saveToFile();
    }

    public synchronized T getObject() {
		if (cached_object!=null){
			//logger.info("rubrication from class");
			return cached_object;
		} else {
            cached_object = getFromFile();
            if (cached_object!=null){
                //logger.info("rubrication from file");
                return cached_object;
            } else {
                cached_object = getFromDB();
                if (cached_object!=null){
                    //logger.info("rubrication from DB");
                    saveToFile();
                    return cached_object;
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public T getFromFile() {
        File f = new File(path,"cache.tmp");
        if (!f.exists()) return null;
        try {
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream si = new ObjectInputStream(fi);
            T tmp = (T)si.readObject();
            si.close();
            fi.close();
            return tmp;
        } catch (IOException ex) {
            logger.error("failed to load cache path = "+path,ex);
            return null;
        } catch (ClassNotFoundException ex) {
            logger.error("failed to load cache path = "+path,ex);
            return null;
        }
    }

    @Override
    public boolean saveToFile(){
        if (cached_object==null) return false;
        try {
            File f = new File(path,"cache.tmp");
            if (f.exists()) f.delete();
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            ObjectOutputStream so = new ObjectOutputStream(fo);
            so.writeObject(cached_object);
            so.flush();
            so.close();
            fo.close();
            return true;
        } catch (IOException ex) {
            logger.error("failed to save cache path = "+path,ex);
            return false;
        }
    }

	public void setPath(Resource res) {
		try{
			File f = res.getFile();
			if (f.exists()){
				if (f.isFile()){
					f.delete();
					f.mkdir();
				}
			} else {
				f.mkdirs();
				//f.mkdir();
			}
			if (f.exists()&&f.isDirectory())
				path = f.getCanonicalPath() + "/";
			else
				throw new NullPointerException("cache folder not found for "+getClass().getName());
		} catch (IOException e){
			path = null;
			throw new NullPointerException("cache folder not found for "+getClass().getName());
		}
		//System.out.println("----------------------------path = "+this.path);
	}
}
