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
import java.util.Random;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class CodeCorrecter implements ITypeCorrecter{
    private int digits=36;
    private static final String[] validChars=
                   {"0","1","2","3","4","5","6","7","8","9",
                    "a","b","c","d","e","f","g","h","i","j",
                    "k","l","m","n","o","p","q","r","s","t",
                    "u","v","w","x","y","z",
                    "A","B","C","D","E","F","G","H","I","J",
                    "K","L","M","N","O","P","Q","R","S","T",
                    "U","V","W","X","Y","Z"};

    /** Creates a new instance of CodeCorrecter */
    public CodeCorrecter() {}
    
    /**
     * generates a string code of 36 characters
     */
    @Override
    public synchronized String correct(String value) {return generate(digits);}
    
    /**
     * generates a string code of N characters
     * @param value number of digits to generate
     * @return string with code
     */
    public static String generate(int value) {
        StringBuilder result=new StringBuilder();
        Random r=new Random();
        for (int i=0;i<value;i++)
            result.append(validChars[r.nextInt(validChars.length)]);
        return result.toString();
    }

	@Override
	public String recorrect(String value) {return null;}
    
}
