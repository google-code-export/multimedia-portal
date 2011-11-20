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

package common.utils;

import common.utils.image.BufferedImageHolder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class ImageUtils {

    private ImageUtils() {}

    /**
	 * Считает отношение новой высоты к старой
     * @param origin оригинальная ширина картинки
     * @param scaled требуемая ширина
     * @return возвращает промасштабирование
     */
    public static double getScaling(int origin, int scaled) {

        if (  scaled >= origin || scaled <= 0  ) {
            return 1;
        }
        double scaling = scaled;
        return scaling / (double) origin;
    }

    /**
	 * Считает отношение новой высоты к старой
	 * @param scaling scaling
	 * @param image picture
     * @return возвращает промасштабирование
     */
    public static Dimension getDimmension(double scaling, BufferedImage image) {
		return new Dimension((int) (scaling*image.getWidth()), (int) (scaling*image.getHeight()));
    }

    /**
     * resize input image to new dinesions(only smaller) and save it to file
     * @param file_stream input image for scaling
     * @param scale_factor desired value of width in result image
	 * @param rez writes resulting image to a file
	 * @throws IOException 
     */
    public static void saveScaledImageWidth(InputStream file_stream, double scale_factor, File rez)
			throws IOException
	{
		//long time = System.nanoTime();
        BufferedImage image = ImageIO.read(file_stream);
		FileOutputStream fos = new FileOutputStream(rez);
		saveScaledImageWidth(image, scale_factor, fos);
		fos.close();
    }

    /**
     * resize input image to new dinesions(only smaller) and save it to file
     * @param image input image for scaling
     * @param scale_factor desired value of width in result image
	 * @param rez writes resulting image to a file
	 * @throws IOException
     */
    public static void saveScaledImageWidth(BufferedImage image, double scale_factor, File rez)
			throws IOException
	{
		//long time = System.nanoTime();
		FileOutputStream fos = new FileOutputStream(rez);
		saveScaledImageWidth(image, scale_factor, fos);
		fos.close();
    }


    /**
     * resize input image to new dinesions(only smaller) and save it to outputStream
     * @param file_stream input image for scaling
     * @param new_width desired value of width in result image
	 * @param rez writes resulting image to a file
	 * @throws IOException
     */
    public static void saveScaledImageWidth(File file_stream, int new_width, OutputStream rez)
			throws IOException
	{
		//long time = System.nanoTime();
        BufferedImage image = ImageIO.read(file_stream);
		double scale_factor = getScaling(image.getWidth(), new_width);
		saveScaledImageWidth(image, scale_factor, rez);
    }

	/**
     * resize input image to new dinesions(only smaller) and save it to file
     * @param image input image for scaling
     * @param scale_factor factor for scaling image
	 * @param rez writes resulting image to a file
	 * @throws IOException
     */
    public static void saveScaledImageWidth(BufferedImage image, double scale_factor, OutputStream rez)
			throws IOException
	{
		//long time_resize = System.currentTimeMillis();
        if (  scale_factor == 1  ){
			writeImage(image, 1, rez);
        }else{
			int width = (int) (scale_factor*image.getWidth());
			int height = (int) (scale_factor*image.getHeight());
			//BufferedImage scaled_bi = new BufferedImage( image.getWidth(), image.getHeight(), image.getType() );
			int type = image.getType();
			BufferedImage scaled_bi;
			if (type==BufferedImage.TYPE_CUSTOM){
				scaled_bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			}else{
				scaled_bi = new BufferedImage(width, height, type);
			}

			Graphics2D g2 = scaled_bi.createGraphics();

			//g2.drawImage(image, at, null);
			g2.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
			writeImage(scaled_bi, 1, rez);

			g2.dispose();
		}
		//time_resize = System.currentTimeMillis() - time_resize;
		//System.out.print("time_resize=" + (time_resize) + "; ");
    }

	/**
     * resize input image to new dinesions(only smaller) into rez parameter
     * @param image input image for scaling
	 * @param rez resulting image. must have required width and height
	 * @throws IOException
     */
    public static void getScaledImageDimmension(BufferedImage image, BufferedImage rez)
			throws IOException
	{
        Graphics2D g2 = rez.createGraphics();
		if (rez.getHeight()>image.getHeight()||rez.getWidth()>image.getWidth()){
			//rez image is bigger no resize
			g2.drawImage(image, 0, 0, null);
			return;
		}
        //1-st getting first side to resize (width or height)
        double scale_factor;
        if (getScaling(image.getHeight(), rez.getHeight())>getScaling(image.getWidth(), rez.getWidth())){
            //resize height
            scale_factor = getScaling(image.getHeight(), rez.getHeight());
			int width = (int) (scale_factor*image.getWidth());
            //cut width
            int x = (rez.getWidth() - width)/2;
			g2.drawImage(image.getScaledInstance(width, rez.getHeight(), Image.SCALE_SMOOTH), x, 0, null);
			//System.out.println("resizing height: h="+image.getHeight()+"/"+rez.getHeight()+"; x="+x);
        }else{
            //resize width
            scale_factor = getScaling(image.getWidth(), rez.getWidth());
			int height = (int) (scale_factor*image.getHeight());
            //cut height
            int y = (rez.getHeight() - height)/2;
			g2.drawImage(image.getScaledInstance(rez.getWidth(), height, Image.SCALE_SMOOTH), 0, y, null);
			//System.out.println("resizing width: w="+image.getWidth()+"/"+rez.getWidth()+"; y="+y);
        }
        g2.dispose();
    }

	/**
     * resize input image to new dinesions(only smaller) into rez parameter
     * @param image input image for scaling
	 * @param rez resulting image. must have required width and height
	 * @throws IOException
     */
    public static void getScaledImageDimmension(BufferedImage image, int new_width, int new_height, OutputStream rez)
			throws IOException
	{
		BufferedImage dst = new BufferedImage(new_width, new_height, BufferedImage.TYPE_3BYTE_BGR);
		getScaledImageDimmension(image,dst);
		writeImage(dst, 1, rez);
	}

	/**
     * resize input image to new dinesions(only smaller) into rez parameter
     * @param image input image for scaling
	 * @param rez resulting image. must have required width and height
	 * @throws IOException
     */
    public static void getScaledImageDimmension(File src_file, int new_width, int new_height, OutputStream rez)
			throws IOException
	{
        BufferedImage src = ImageIO.read(src_file);
		getScaledImageDimmension(src, new_width, new_height, rez);
	}

	public static ImageWriter getImageWriter(){
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
		if (iter.hasNext()) {
			return iter.next();
		} else {
			return null;
		}
		//throw new IllegalStateException("No writers found");
	}

	public static ImageReader getImageReader(ImageInputStream is){
		//Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("jpeg");
		Iterator<ImageReader> iter = ImageIO.getImageReaders(is);
		if (iter.hasNext()) {
			return iter.next();
		} else {
			return null;
		}
		//throw new IllegalStateException("No readers found");
	}

	public static void writeImage(BufferedImage image, float quality, OutputStream out) 
		throws IOException
	{
			ImageWriter writer = getImageWriter();
			if (writer==null)
				return;
			ImageOutputStream ios = ImageIO.createImageOutputStream(out);
			writer.setOutput(ios);
			ImageWriteParam param = writer.getDefaultWriteParam();
			//JPEGImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
			//param.setOptimizeHuffmanTables(true);
			if (quality >= 0) {
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionQuality(quality);
				//param.setCompressionMode(ImageWriteParam.MODE_COPY_FROM_METADATA);
				//param.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
				//param.setTilingMode(ImageWriteParam.MODE_DISABLED);
			}
			writer.write(null, new IIOImage(image, null, null), param);
	}

	public static void writeImage(BufferedImage image, float quality, File out)
		throws IOException
	{
		FileOutputStream fos = new FileOutputStream(out);
		writeImage(image, quality, fos);
		fos.close();
	}

	/**
	 * closes iis
	 * @param iis
	 * @return image from given stream or null if no readers found
	 * @throws java.io.IOException
	 */
	public static BufferedImageHolder readImage(ImageInputStream iis)
		throws IOException
	{
		ImageReader reader = getImageReader(iis);
		if (reader==null){
			iis.close();
			return null;
		}
		reader.setInput(iis,true,true);
		ImageReadParam param = reader.getDefaultReadParam();
		String format_name = reader.getFormatName();
		BufferedImage bi;
		try {
			bi = reader.read(0, param);
		} finally {
			reader.dispose();
			iis.close();
		}
		return new BufferedImageHolder(bi, format_name);
	}

	/**
	 *
	 * @param in
	 * @return image from given stream or null if no readers found
	 * @throws java.io.IOException
	 */
	public static BufferedImageHolder readImage(File in)
		throws IOException
	{
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		BufferedImageHolder bih = readImage(iis);
		if (bih!=null) bih.setFile(in);
		return bih;
	}

	/**
	 *
	 * @param in
	 * @return image from given stream or null if no readers found
	 * @throws java.io.IOException
	 */
	public static BufferedImageHolder readImage(MultipartFile in)
		throws IOException
	{
		InputStream is = in.getInputStream();
		ImageInputStream iis = ImageIO.createImageInputStream(is);
		BufferedImageHolder bih = readImage(iis);
		is.close();
		if (bih!=null) bih.setMultipart(in);
		return bih;
	}

	/**
	 *
	 * @param src images to draw, they must be resized to an appropriate size
	 * @param dst image where given images will be drawen
	 */
	public static void draw4on1(BufferedImage[] src, BufferedImage dst){
		Graphics2D g2 = dst.createGraphics();
		g2.setColor(java.awt.Color.WHITE);
		g2.fillRect(0, 0, dst.getWidth(), dst.getHeight());
		int dxi;
		int dyi = 0;

		int x0 = dst.getWidth() - 5;
		int y0 = 5;
		int x = x0;
		int y = y0;
		for (int i=0;i<src.length;i++){
			if (i%2==0){
				dxi = -10;
			}else{
				dxi = 0;
			}
			//g2.draw3DRect(dx - 1 , dy-tmp_bi.getHeight() - 1, tmp_bi.getWidth() + 1 , tmp_bi.getHeight() + 1, true);
			g2.drawImage(src[i], x - src[i].getWidth() + dxi, y + dyi, null);
			g2.drawString("#"+i, x - src[i].getWidth() + dxi, y + dyi+20);
			//g2.rotate(Math.toRadians(4));
			y = y + src[i].getHeight()/2;
			if (y>dst.getHeight() - src[i].getHeight()){
				y = y0;
				if (dyi==0)
					dyi = 10;
				else
					dyi = 0;
				if (x<src[i].getWidth()){
					x = dst.getWidth();
				}
				x = x - src[i].getWidth()/2;
			}
		}
		g2.setColor(Color.gray);
		g2.drawRect(0, 0, dst.getWidth()-1, dst.getHeight()-1);
		g2.dispose();
	}

}
