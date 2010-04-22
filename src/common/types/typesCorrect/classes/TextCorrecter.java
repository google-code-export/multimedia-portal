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
public class TextCorrecter implements ITypeCorrecter{
    //characters to be replaced
    private static final String[] specChars={"&",">","<","\"","'","\\\\"};
    //charackters will be replaced by these charackters
    private static final String[] replaceBy={"&amp;","&gt;","&lt;","&quot;","&#039;","\\\\\\\\"};
    
    /**
     * Creates a new instance of TextCorrecter
     */
    public TextCorrecter() {
    }

    @Override
    public synchronized String correct(String value) {
        String res=value;
        for (int i=0;i<specChars.length;i++){
            res=res.replaceAll(specChars[i],replaceBy[i]);
        }
        return res;
    }

	@Override
	public String recorrect(String value) {
        String res=value;
		int i = specChars.length;
		while (i>0){
			i--;
            res=res.replaceAll(replaceBy[i],specChars[i]);
        }
        return res;
	}
    
}
