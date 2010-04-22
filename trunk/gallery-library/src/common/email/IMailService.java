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

package common.email;

import gallery.model.command.SendEmail;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface IMailService {

    /**
     * this method loads email template into StringBuilder
     * this method sends email using a given template
     * @param subject subject of a mail
     * @param recipientEmail email receiver adress
     * @param params names of parameters to replace in template
     * @param values values by whitch to replace names
     * @param from will be set
     * @param pathToTemplate path to template for email message
	 * @return stringBuilder this email
	 * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
	public boolean postMail(String subject,String recipientEmail,String from,
            Vector<String> params,Vector<String> values,
            String pathToTemplate)
        throws FileNotFoundException, IOException;


    /**
     * this method loads email template into StringBuilder
     * this method sends email using a given template
     * @param subject subject of a mail
     * @param recipientEmail email receiver adress
     * @param params names of parameters to replace in template
     * @param values values by whitch to replace names
     * @param from will be set
     * @param pathToTemplate path to template for email message
	 * @return stringBuilder this email
	 * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
	public boolean postMail(String subject,String recipientEmail,String from,
            String[] params,String[] values,
            String pathToTemplate)
        throws FileNotFoundException, IOException;


    /**
     * this method loads email template into StringBuilder
     * this method sends email using a given template
     * @param subject subject of a mail
     * @param recipientEmail email receiver adress
     * @param params names of parameters to replace in template
     * @param values values by whitch to replace names
     * @param from will be set
     * @param pathToTemplate path to template for email message
	 * @return stringBuilder this email
	 * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
	public boolean postMail(String subject, SendEmail command) throws IOException;

	public String getAutoanswerEmail();
}
