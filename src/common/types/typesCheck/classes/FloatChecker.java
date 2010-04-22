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
public class FloatChecker implements ITypeChecker{
    private final Pattern pattern = Pattern.compile("[+-]?[0-9]+([.,][0-9]+)?");
    
    /** Creates a new instance of FloatChecker */
    public FloatChecker() {
    }
    
    @Override
    public synchronized int check(String value) {
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches())
            if (value.indexOf(",")==-1)
                return 0;
            else
                return 4;
        else
            return -7;
    }
    
}
