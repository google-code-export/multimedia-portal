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
 *  under the License.
 */

package common;

import java.util.Vector;
import javax.servlet.ServletRequest;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class CommonAttributes {
    /** an attribute where an error message is stored */
    public final static String ERROR_MESSAGE_ATTR="common_error_message";
    /**
     * an attribute where help message is stored
     * help message is stored as vector of string each string is help message
     */
    public final static String HELP_MESSAGE_ATTR="common_help_message";
    
    /**
     * adds help message to an attribute from the request
     * @param msg a message to be added
     * @param request a request where to find help messages
     */
    public static void addHelpMessage(String msg,ServletRequest request){
		if (msg==null)
			return;
        Vector<String> help=(Vector<String>)request.getAttribute(HELP_MESSAGE_ATTR);
        if (help!=null){
            help.add(msg);
        }else{
            help=new Vector<String>();
            help.add(msg);
            request.setAttribute(HELP_MESSAGE_ATTR,help);
        }
    }

    /**
     * adds error message to an attribute from the request
     * @param msg a message to be added
     * @param request a request where to find help messages
     */
    public static void addErrorMessage(String msg,ServletRequest request){
		if (msg==null)
			return;
        Vector<String> error=(Vector<String>)request.getAttribute(ERROR_MESSAGE_ATTR);
        if (error!=null){
            error.add(msg);
        }else{
            error=new Vector<String>();
            error.add(msg);
            request.setAttribute(ERROR_MESSAGE_ATTR,error);
        }
		if (msg==null)
			return;
    }

    private CommonAttributes() {
    }
}
