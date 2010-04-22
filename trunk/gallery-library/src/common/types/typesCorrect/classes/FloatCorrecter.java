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
public class FloatCorrecter implements ITypeCorrecter{
    
    /**
     * Creates a new instance of FloatCorrecter
     */
    public FloatCorrecter() {
    }

    @Override
    public synchronized String correct(String value) {
        return value.replaceFirst(",",".");
    }

	@Override
	public String recorrect(String value) {
		return value;
	}
    
}
