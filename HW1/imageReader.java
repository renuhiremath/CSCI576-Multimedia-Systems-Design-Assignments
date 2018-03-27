import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;


public class imageReader {

	public void nearestNeighbor(byte[] bytes, int width, int height, int newWidth, int newHeight)
	{
		BufferedImage finalImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		int xdash=0, ydash=0, ind=0;
		for(int y = 0; y < newHeight; y++)
		{
			for(int x = 0; x < newWidth; x++)
			{
				xdash = (int)((x*width)/(float)newWidth);
				ydash = (int)((y*height)/(float)newHeight);

				ind = ydash*width + xdash;
				//System.out.println(ind+ "  " + xdash +" " + ydash);
				byte a = 0;
				byte r = bytes[ind];
				byte g = bytes[ind+height*width];
				byte b = bytes[ind+height*width*2];
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

				finalImg.setRGB(x,y,pix);
			}
		}
		try
		{
			ImageIO.write(finalImg, "bmp",new File("output.bmp"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageReader imgReader = new imageReader();
		//imgReader.display(finalImg);
	}

	public void bilinearIntrapolation(byte[] bytes, int width, int height, int newWidth, int newHeight)
	{
		BufferedImage finalImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		int xdash=0, ydash=0, ind=0;
		int pixels[] = new int[2073600];
		for(int y = 0; y < newHeight; y+=((int)((float)newHeight/height)+1))
		{
			for(int x = 0; x < newWidth; x+=((int)((float)newWidth/width)+1))
			{
				byte a = 0;
				byte r = bytes[ind];
				byte g = bytes[ind+height*width];
				byte b = bytes[ind+height*width*2];
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				pixels[x+(y*newWidth)] = pix;
				ind++;
			}
		}

		float y_ratio = (float)newHeight/height;
		float x_ratio = (float)newWidth/width;
		int y1 = 0,y2 =((int)((float)newHeight/height)+1);
		for(int y = 0; y < newHeight; y++)
		{
			if (y % ((int)(y_ratio)+1) ==0 )
				continue;
			int x2=0,x1=((int)((float)newWidth/width)+1);
			for(int x = 0; x < newWidth; x++)
			{
				if (x % ((int)(x_ratio)+1) ==0 )
					continue;
				pixels[x+(y*newWidth)] = (int)((float)1/(y_ratio*x_ratio))*(pixels[x1+(y1*newWidth)]*(x2-x)*(y2-y)
														   + pixels[x2+(y1*newWidth)]*(x-x1)*(y2-y)
														   + pixels[x1+(y2*newWidth)]*(x2-x)*(y-y1)
														   + pixels[x2+(y2*newWidth)]*(x-x1)*(y-y1) );
				x1=x2;
				x2+=((int)((float)newWidth/width)+1);
			}
			y1 = y2;
			y2 +=((int)((float)newHeight/height)+1);
		}
		for(int y = 0; y < newHeight; y++)
		{
			for(int x = 0; x < newWidth; x++)
				finalImg.setRGB(x,y,pixels[x+(y*newWidth)]);
		}
		try
		{
			ImageIO.write(finalImg, "bmp",new File("output.bmp"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageReader imgReader = new imageReader();
		//imgReader.display(finalImg);
	}

	public void specificSampling(byte[] bytes, int width, int height, int newWidth, int newHeight)
	{
		BufferedImage finalImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		int xdash=0, ydash=0, ind=0;

		for(int y = 0; y < newHeight; y++)
		{
			for(int x = 0; x < newWidth; x++)
			{
				xdash = (int)((x*width)/(float)newWidth);
				ydash = (int)((y*height)/(float)newHeight);

				ind = ydash*width + xdash;
				//System.out.println(ind+ "  " + xdash +" " + ydash);
				byte a = 0;
				byte r = bytes[ind];
				byte g = bytes[ind+height*width];
				byte b = bytes[ind+height*width*2];
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

				finalImg.setRGB(x,y,pix);
			}
		}
		try
		{
			ImageIO.write(finalImg, "bmp",new File("output.bmp"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageReader imgReader = new imageReader();
		//imgReader.display(finalImg);
	}

	public void gaussianSmoothing(byte[] bytes, int width, int height, int newWidth, int newHeight)
	{
		BufferedImage finalImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		int xdash=0, ydash=0, ind=0;

		for(int y = 0; y < newHeight; y++)
		{
			for(int x = 0; x < newWidth; x++)
			{
				xdash = (int)((x*width)/(float)newWidth);
				ydash = (int)((y*height)/(float)newHeight);

				ind = ydash*width + xdash;
				//System.out.println(ind+ "  " + xdash +" " + ydash);
				int pix=0;
				if (x>0 && y>0 && x<newWidth-1 && y<newHeight-1)
				{
					byte r = bytes[ind-1];
					byte g = bytes[ind-1+height*width];
					byte b = bytes[ind-1+height*width*2];
					pix += (0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff));
					r = bytes[ind];
					g = bytes[ind+height*width];
					b = bytes[ind+height*width*2];
					pix += (0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff));
					r = bytes[ind+1];
					g = bytes[ind+1+height*width];
					b = bytes[ind+1+height*width*2];
					pix += (0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff));
					r = bytes[ind-1-width];
					g = bytes[ind-1-width+height*width];
					b = bytes[ind-1-width+height*width*2];
					pix += 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					r = bytes[ind-width];
					g = bytes[ind-width+height*width];
					b = bytes[ind-width+height*width*2];
					pix += 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					r = bytes[ind+1-width];
					g = bytes[ind+1-width+height*width];
					b = bytes[ind+1-width+height*width*2];
					pix += 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					r = bytes[ind-1+width];
					g = bytes[ind-1+width+height*width];
					b = bytes[ind-1+width+height*width*2];
					pix += 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					r = bytes[ind+width];
					g = bytes[ind+width+height*width];
					b = bytes[ind+width+height*width*2];
					pix += 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					r = bytes[ind+1+width];
					g = bytes[ind+1+width+height*width];
					b = bytes[ind+1+width+height*width*2];
					pix += 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					pix/=9;
				}
				else
				{
					byte r = bytes[ind];
					byte g = bytes[ind+height*width];
					byte b = bytes[ind+height*width*2];
					pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				}

				finalImg.setRGB(x,y,pix);
			}
		}

		try
		{
			ImageIO.write(finalImg, "bmp",new File("output.bmp"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageReader imgReader = new imageReader();
		//imgReader.display(finalImg);
	}
	public void display(BufferedImage image)
	{
		// Use a panel and label to display the image
		JPanel  panel = new JPanel ();
		panel.add (new JLabel (new ImageIcon (image)));

		JFrame frame = new JFrame("Display images");

		frame.getContentPane().add (panel);
		frame.pack();
		frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

   public static void main(String[] args)
   {
		imageReader imgReader = new imageReader();
		String fileName = args[0];
		int width = Integer.parseInt(args[1]);
		int height = Integer.parseInt(args[2]);
		int resamplingMethod = Integer.parseInt(args[3]);
		String outputFormat = args[4];

		int newWidth, newHeight;

		if (outputFormat.equals("O1"))
		{
			newWidth = 1920;
			newHeight = 1080;
		}
		else if (outputFormat.equals("O2"))
		{
			newWidth = 1280;
			newHeight = 720;
		}
		else if (outputFormat.equals("O3"))
		{
			newWidth = 640;
			newHeight = 480;
		}
		else
		{
			newWidth = height;
			newHeight = width;
		}

		byte[] bytes = null;
		try {
			File file = new File(args[0]);
			InputStream is = new FileInputStream(file);

			long len = file.length();
			bytes = new byte[(int)len];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (newWidth>width)//up sampling
		{
			if (resamplingMethod ==1)
			{
				imgReader.nearestNeighbor(bytes, width, height, newWidth, newHeight);
			}
			else if (resamplingMethod ==2)
			{
				imgReader.bilinearIntrapolation(bytes, width, height, newWidth, newHeight);
			}
		}
		else //down sampling
		{
			if (resamplingMethod ==1)
			{
				imgReader.specificSampling(bytes, width, height, newWidth, newHeight);
			}
			else if (resamplingMethod ==2)
			{
				imgReader.gaussianSmoothing(bytes, width, height, newWidth, newHeight);
			}
		}
	}
}
