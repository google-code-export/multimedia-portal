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

package common.types.typesCorrect.classes;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class PasswordCorrecter implements ITypeCorrecter{
    
    /**
     * Creates a new instance of PasswordCorrecter
     */
    public PasswordCorrecter() {
    }
    
    /**
     * calculate MD5
     */
    @Override
    public synchronized String correct(String value) {
        if (value.equals(""))
            return null;
        else{
            String md5=org.apache.catalina.realm.RealmBase.Digest(value,"MD5","UTF-8");
            return md5;
        }
    }

	/**
	 * its changes cannto be undone so ...
	 * @param value
	 * @return null
	 */
	@Override
	public String recorrect(String value) {return value;}

    /**
     * use this method if you need to generate MD5 hash for password
     * calculate MD5
     * @param value
     * @return
     */
    public static String correctStatic(String value){
        if (value==null&&value.equals(""))
            return null;
        else{
            return org.apache.catalina.realm.RealmBase.Digest(value,"MD5","UTF-8");
        }
    }
    
}
