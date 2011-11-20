/*
 *  Copyright 2010 demchuck.dima@gmail.com.
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

package common.beans;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class HttpResponseOutputStreamHolder implements IOutputStreamHolder{
	protected HttpServletResponse response;
	protected OutputStream os;

	private HttpResponseOutputStreamHolder(HttpServletResponse response){
		this.response = response;
	}

	@Override
	public OutputStream getOutputStream()
			throws IOException
	{
		os = response.getOutputStream();
		return os;
	}

	public static HttpResponseOutputStreamHolder getInstance(HttpServletResponse response){
		return new HttpResponseOutputStreamHolder(response);
	}

	@Override
	public void closeAndFlushOutputStream() throws IOException {
		if (os!=null){
			os.flush();
			os.close();
		}
	}

}
