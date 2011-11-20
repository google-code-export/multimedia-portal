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

package common.types.typesCorrect;
import java.util.Hashtable;
import common.types.typesCorrect.classes.*;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class TypesCorrecter {
    private Hashtable<String,ITypeCorrecter> types=new Hashtable<String,ITypeCorrecter>();
    
    /**
     * Creates a new instance of TypesCorrecter
     */
    private TypesCorrecter() {
        //here you need to put all types that you need to check
        types.put("password",new PasswordCorrecter());
        types.put("text",new TextCorrecter());
        types.put("html",new HtmlCorrecter());
        //types.put("code",new CodeCorrecter());
    }
    
    public static synchronized TypesCorrecter getTypesCorrecter(){
        return new TypesCorrecter();
    }
    
    /**
     * correct value
     * @param value value to correct
     * @param type correct for type
     * @return corrected value, or null if not needs to be corrected
     */
    public synchronized String correct(String value,String type){
        ITypeCorrecter tc=types.get(type);
        if (tc!=null){
            return tc.correct(value);
        }else{
            return null;
		}
    }

    /**
     * correct value
     * @param value value to correct
     * @param type correct for type
     * @return corrected value, or null if not needs to be recorrected
     */
    public synchronized String recorrect(String value,String type){
        ITypeCorrecter tc=types.get(type);
        if (tc!=null){
            return tc.recorrect(value);
        }else{
            return null;
		}
    }
    
}
