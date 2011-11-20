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

package common.web.servlets;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * 
 * @author dima
 */
public class RandomImageServ extends HttpServlet {
//default parameter values
	public static final String IMAGE_DIR_DEFAULT = "WEB-INF/CodeImages";
	public static final int DIGITS_COUNT_DEFAULT = 4;//how many digits will be draw in antibotcode
//names of init parameters where to take values
	public static final String IMAGE_DIR_NAME = "image_directory";
    //public static final String CODE_ATTR_NAME  = "code_attribute";
	public static final String DIGITS_COUNT_NAME = "digits_count";//how many digits will be draw in antibotcode

	private String imgDir;
	//private String codeAttr;
	private int digitsCount;

    @Override
	public void service(HttpServletRequest request, javax.servlet.http.HttpServletResponse response) 
		throws javax.servlet.ServletException, java.io.IOException
	{
		ServletContext application = getServletConfig().getServletContext();

		//creating a filter file only for jpg BEGIN
		FilenameFilter onlyJPGImagesFilter = new FilenameFilter() {
            @Override
			public boolean accept(File path, String name){
				return ( name.endsWith("jpg") );
			}
		};
		//creating a filter file only for jpg END

		File dir = new File( application.getRealPath(imgDir) );//directory with images for antibot code

		if( dir.isDirectory() ){
			BufferedImage temp_bi = null;
			Random random = new Random();
			File[] fileListPattern = dir.listFiles(onlyJPGImagesFilter);
			int digitInCode[] = new int[digitsCount];//result array of indexes for filelist

			//filling of result array for filelist (shuffle of original filelist)
			for (int i=0; i < digitsCount; i++){
				digitInCode[i] = random.nextInt( fileListPattern.length );
			}

			OutputStream os = response.getOutputStream();

			byte[] rez = new byte[1024];

			temp_bi = ImageIO.read( fileListPattern[0] );

			BufferedImage main_bi = new BufferedImage( temp_bi.getWidth() * digitsCount, temp_bi.getHeight(), 4 );
			Graphics g = main_bi.getGraphics();

			int tempWidth  = 0;// X axial displacement

			//loading and drawing of images for antibot code BEGIN
			for (int i=0; i < digitInCode.length; i++){
				temp_bi = ImageIO.read( fileListPattern[ digitInCode[i] ] );
				g.drawImage( temp_bi, tempWidth, 0, null  );
				tempWidth += temp_bi.getWidth();
			}
			//loading and drawing of images for antibot code END

			//forming a string representation of graphical antibotcode BEGIN
			String file_name = "";
			String result_code = "";
			int delimiter_index = 0;
			for (int i=0; i < digitInCode.length; i++){
				file_name = fileListPattern[ digitInCode[i] ].getName() ;
				delimiter_index = file_name.indexOf("_");
				result_code += file_name.substring(0, delimiter_index);
			}

			common.web.filters.Antispam.setCode(request ,result_code);

			//transform BufferedImage to byte[] BEGIN
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( main_bi, "jpg", baos  ) ;
			rez = baos.toByteArray();
			//transform BufferedImage to byte[] END

			os.write( rez, 0, rez.length );
			os.flush();
			os.close();
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		if (config!=null){
			super.init(config);
			String temp; //= config.getInitParameter(CODE_ATTR_NAME);
			//if (temp==null)	codeAttr = CODE_ATTR_DEFAULT;
			//else codeAttr = temp;
			temp = config.getInitParameter(DIGITS_COUNT_NAME);
			if (temp==null) digitsCount = DIGITS_COUNT_DEFAULT;
			else digitsCount = Integer.parseInt(temp, 10);
			temp = config.getInitParameter(IMAGE_DIR_NAME);
			if (temp==null) imgDir = IMAGE_DIR_DEFAULT;
			else imgDir = IMAGE_DIR_DEFAULT;
		}
	}

} 
