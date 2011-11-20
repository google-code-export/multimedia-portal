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

import com.sun.mail.smtp.SMTPTransport;
import gallery.model.command.SendEmail;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.util.ByteArrayDataSource;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class MailServiceImpl implements IMailService{
	Logger logger = Logger.getLogger(getClass().getName());

    private String mailHost;
    private String user;
    private String password;
	private String templatePath;

    /**
     * this method sends email using a given template
     * @param subject subject of a mail
     * @param recipientEmail email receiver adress
     * @param mail mail text to send
     * @param from will be set
	 * @return true if send succeed
	 * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    protected boolean postMail(String subject, String recipientEmail, String from, String mail)
        throws IOException
    {
        try {
            Properties props = new Properties();
            //props.put("mailHost", mailHost);

            Session session = Session.getInstance(props);
            // construct the message
            javax.mail.Message msg = new javax.mail.internet.MimeMessage(session);
            if (from == null) {
                msg.setFrom();
            } else {
                try {
                    msg.setFrom(new InternetAddress(from));
                } catch (MessagingException ex) {
                    logger.error(ex);
                }
            }
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
            msg.setSubject(subject);
            msg.setSentDate(new Date());
			//msg.setHeader("", user)
            msg.setDataHandler(new DataHandler(new ByteArrayDataSource(mail, "text/html; charset=UTF-8")));

            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
            t.connect(mailHost, user, password);
			Address[] a = msg.getAllRecipients();
            t.sendMessage(msg, a);
            t.close();
			return true;
        } catch (MessagingException ex) {
            logger.error(ex);
			ex.printStackTrace();
			return false;
        }

    }

    /**
     * this method loads email template into StringBuilder
     * this method sends email using a given template
     * @param params names of parameters to replace in template
     * @param values values by whitch to replace names
     * @param pathToTemplate path to template for email message
	 * @return stringBuilder this email
	 * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    protected static StringBuilder getMailMsg(
            Vector<String> params,
            Vector<String> values,
            String pathToTemplate)
        throws FileNotFoundException, IOException
    {
		String line = "";
		StringBuilder rez = new StringBuilder();
		if (params != null && values != null) {
			BufferedReader read = new BufferedReader(new InputStreamReader( new FileInputStream( pathToTemplate ),  "UTF-8" ));
			while ((line = read.readLine()) != null) {
				for (int i = 0; i < params.size(); i++) {
					line=line.replaceAll(params.get(i), values.get(i));
				}
				rez.append(line);
				rez.append("\n");
			}
			read.close();
		}else{
			BufferedReader read = new BufferedReader(new InputStreamReader( new FileInputStream( pathToTemplate ),  "UTF-8" ));
			while ((line = read.readLine()) != null) {
				rez.append(line);
				rez.append("\n");
			}
			read.close();
		}
		return rez;
    }

    /**
     * this method loads email template into StringBuilder
     * this method sends email using a given template
     * @param params names of parameters to replace in template
     * @param values values by whitch to replace names
     * @param pathToTemplate path to template for email message
	 * @return stringBuilder this email
	 * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    protected static StringBuilder getMailMsg(
            String[] params,
            String[] values,
            String pathToTemplate)
        throws FileNotFoundException, IOException
    {
		String line = "";
		StringBuilder rez = new StringBuilder();
		if (params != null && values != null) {
			BufferedReader read = new BufferedReader(new InputStreamReader( new FileInputStream( pathToTemplate ),  "UTF-8" ));
			while ((line = read.readLine()) != null) {
				for (int i = 0; i < params.length; i++) {
					line=line.replaceAll(params[i], values[i]);
				}
				rez.append(line);
				rez.append("\n");
			}
			read.close();
		}else{
			BufferedReader read = new BufferedReader(new InputStreamReader( new FileInputStream( pathToTemplate ),  "UTF-8" ));
			while ((line = read.readLine()) != null) {
				rez.append(line);
				rez.append("\n");
			}
			read.close();
		}
		return rez;
    }


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
	@Override
	public boolean postMail(
            String subject,
            String recipientEmail,
            String from,
            Vector<String> params,
            Vector<String> values,
            String pathToTemplate)
        throws FileNotFoundException, IOException
	{
		StringBuilder m=getMailMsg(params, values, templatePath + pathToTemplate);
		return postMail(subject, recipientEmail, from, m.toString());
	}


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
	@Override
	public boolean postMail(
            String subject,
            String recipientEmail,
            String from,
            String[] params,
            String[] values,
            String pathToTemplate)
        throws FileNotFoundException, IOException
	{
		StringBuilder m=getMailMsg(params, values, templatePath + pathToTemplate);
		return postMail(subject, recipientEmail, from, m.toString());
	}

	public void setMailHost(String mailHost) {this.mailHost = mailHost;}
	public void setUser(String user) {this.user = user;}
	public void setPassword(String password) {this.password = password;}
	public void setPath(Resource res) {
		try{
			File f = res.getFile();
			this.templatePath = f.getCanonicalPath() + "/";
		} catch (IOException e){
			throw new NullPointerException("folder not found for Email templates: "+res);
		}
		//System.out.println("----------------------------path = "+this.path);
	}

	@Override
	public String getAutoanswerEmail() {return "admin@download-multimedia.com";}

	@Override
	public boolean postMail(String subject, SendEmail command)
        throws IOException
	{
		return postMail(subject, command.getEmail_to(), getAutoanswerEmail(), command.getText());
	}
}
