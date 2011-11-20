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

package common.types.typesCheck.classes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class EmailChecker implements ITypeChecker {
    //uncomment this to get email only with sub domain (.net,.kiev.ua,.com,...)
    //private final Pattern pattern = Pattern.compile("[a-zA-Z0-9\u005F\u002E\u002D]+@[a-zA-Z0-9._-]+([.][a-zA-Z]{2,4})+");
    private final Pattern pattern = Pattern.compile("[a-zA-Z0-9\u005F\u002E\u002D]+@[a-zA-Z0-9._-]+");

    /** Creates a new instance of EmailChecker */
    public EmailChecker(){
    }

    @Override
    public synchronized int check(String value){
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches())
            return 0;
        else
            return -2;
    }

}
