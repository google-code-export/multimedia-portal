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

package common.types;
import java.util.Hashtable;
import common.types.typesCheck.classes.*;
import common.types.typesCorrect.classes.*;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class TypesCheckAndCorrect {
    private Hashtable<String,ITypeChecker> check=new Hashtable<String,ITypeChecker>();
    private Hashtable<String,ITypeCorrecter> correct=new Hashtable<String,ITypeCorrecter>();
    
    /**
     * Creates a new instance of TypesChecker
     */
    private TypesCheckAndCorrect() {
        //checkers
        check.put("email",new EmailChecker());
        check.put("phone",new PhoneChecker());
        check.put("password",new PasswordChecker());
        check.put("text",new TextChecker());
        check.put("html",new HtmlChecker());
        check.put("url",new UrlChecker());
        check.put("date",new DateChecker());
        check.put("intArray",new IntArrayChecker());
        //correcters
        correct.put("password",new PasswordCorrecter());
        correct.put("text",new TextCorrecter());
        correct.put("html",new HtmlCorrecter());
    }
    
    public static synchronized TypesCheckAndCorrect get(){
        return new TypesCheckAndCorrect();
    }
    
    public static synchronized String[] getAllTypes(){
        String[] s={"email","integer","phone","login","password","float","text","html","url","date"};
        return s;
    }

	public ITypeChecker getChecker(String name){
		return check.get(name);
	}

	public ITypeCorrecter getCorrecter(String name){
		return correct.get(name);
	}
    
    /**
     * 
     * @param value value to check
     * @param type checking for type
     * @return null if not correct and can't be corrected, string in other cases
     */
    public synchronized String checkAndCorrect(String value,String type){
        ITypeChecker tc=check.get(type);
        if (tc!=null){
            int code=tc.check(value);
            if (code<0){
                return null;
            }else{
                if (code==0){
                    return value;
                }else{
                    ITypeCorrecter cc=correct.get(type);
                    return cc.correct(value);
                }
            }
        }else{
            return null;
        }
    }
    
}
