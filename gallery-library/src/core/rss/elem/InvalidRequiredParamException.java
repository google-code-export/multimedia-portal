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

package core.rss.elem;

/**
 * Thrown when a required parameter is passed to a constructor or method and its value is not valid or is <code>null</code>.
 * 
 * @author Henrique A. Viecili
 */
public class InvalidRequiredParamException extends RuntimeException {

    /**
     * Constructs an <code>InvalidRequiredParamException</code> with no detail message.
     */
    public InvalidRequiredParamException() {
        super();
    }

    /**
     * Constructs an <code>InvalidRequiredParamException</code> with the specified detail message.
     * @param message The detail message
     */
    public InvalidRequiredParamException(String message) {
        super(message);
    }
}
