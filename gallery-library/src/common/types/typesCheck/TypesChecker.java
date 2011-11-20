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

package common.types.typesCheck;
import java.util.Hashtable;
import common.types.typesCheck.classes.*;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class TypesChecker {
    private Hashtable<String,ITypeChecker> types=new Hashtable<String,ITypeChecker>();
    
    /**
     * Creates a new instance of TypesChecker
     */
    private TypesChecker() {
        //here you need to put all types that you need to correct
        types.put("email",new EmailChecker());
        types.put("phone",new PhoneChecker());
        types.put("password",new PasswordChecker());
        types.put("text",new TextChecker());
        types.put("html",new HtmlChecker());
        types.put("url",new UrlChecker());
        types.put("date",new DateChecker());
        types.put("intArray",new IntArrayChecker());
    }
    
    public static synchronized TypesChecker getTypesChecker(){
        return new TypesChecker();
    }
    
    /**
     * correct the value
     * @param value value to check
     * @param type checking for type
     * @return 0 value is of needed type, -1 has wrong type >0 can be corrected
     */
    public synchronized int check(String value,String type){
        ITypeChecker tc=types.get(type);
        if (tc!=null){
            return tc.check(value);
        }else
            return -1;
    }
    
}
