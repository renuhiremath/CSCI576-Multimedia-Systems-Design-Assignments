import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.lang.Math.*;

public class imageReader {
	
	private int length = 512;
	private int width = 512;
	private float rImageDCT[][]= new float[512][512];
	private float gImageDCT[][]= new float[512][512];
	private float bImageDCT[][]= new float[512][512];
	private float rImage[][]= new float[512][512];
	private float gImage[][]= new float[512][512];
	private float bImage[][]= new float[512][512];
	
	public void DCTEncoding(int[] bytes)
	{
		int rChannel[][]= new int[512][512];
		int gChannel[][]= new int[512][512];
		int bChannel[][]= new int[512][512];
		
		byte r,g,b;
		int ind;
		for(int y = 0; y < length; y++)
		{
			for(int x = 0; x < width; x++)
			{
				ind = y*width + x;
				rChannel[x][y] = (int)bytes[ind];
				gChannel[x][y] = (int)bytes[ind+length*width];
				bChannel[x][y] = (int)bytes[ind+length*width*2];
				
				rChannel[x][y] = rChannel[x][y];
				gChannel[x][y] = gChannel[x][y];
				bChannel[x][y] = bChannel[x][y];
			}
		}
		int Q[][] = {{16,11,10,16,24,40,51,61}, {12,12,14,19,26,58,60,55},{14,13,16,24,40,57,69,56}, {14,17,22,29,51,87,80,62},{18,22,37,56,68,109,103,77},{24,35,55,64,81,104,113,92},{49,64,78,87,103,121,120,101},{72,92,95,98,112,100,103,99}};
	
		for(int i=0;i<width;i+=8)
		{
			for (int j=0; j<length; j+=8)
			{
				for (int u = 0; u<8; u++)
				{
					for (int v = 0; v<8; v++)
					{
						double alpha = 1;
						if (u==0)
							alpha /= Math.sqrt(2);
						if (v==0)
							alpha /= Math.sqrt(2);
						double sumfxyR = 0.0;
						double sumfxyG = 0.0;
						double sumfxyB = 0.0;
						for (int x=0;x<8;x++)
						{
							for (int y=0;y<8;y++)
							{
								sumfxyR += (0.25 * alpha * rChannel[i+x][j+y] * Math.cos((2*x+1)*u*Math.PI/16.0)* Math.cos((2*y+1)*v*Math.PI/16.0));
								sumfxyG += (0.25 * alpha * gChannel[i+x][j+y] * Math.cos((2*x+1)*u*Math.PI/16.0)* Math.cos((2*y+1)*v*Math.PI/16.0));
								sumfxyB += (0.25 * alpha * bChannel[i+x][j+y] * Math.cos((2*x+1)*u*Math.PI/16.0)* Math.cos((2*y+1)*v*Math.PI/16.0));
							}
						}
							
						rImageDCT[i+u][j+v] = (float)(sumfxyR/(double)Q[u][v]);
						gImageDCT[i+u][j+v] = (float)(sumfxyG/(double)Q[u][v]);
						bImageDCT[i+u][j+v] = (float)(sumfxyB/(double)Q[u][v]);
						
						//System.out.println(rImage[i+u][j+v]+"   "+gImage[i+u][j+v]+ "   " + bImage[i+u][j+v]);
					}
				}
			}
		}
	}
	
	public  BufferedImage DCTDecoding(int m)
	{		
		BufferedImage finalImg = new BufferedImage(width, length, BufferedImage.TYPE_INT_RGB);
		int Q1[][] = {{16,11,10,16,24,40,51,61}, {12,12,14,19,26,58,60,55},{14,13,16,24,40,57,69,56},{14,17,22,29,51,87,80,62},{18,22,37,56,68,109,103,77},{24,35,55,64,81,104,113,92},{49,64,78,87,103,121,120,101},{72,92,95,98,112,100,103,99}};
		int Q[][] = {{0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
		
		int Rblock[][] = new int [512][512];
		int Gblock[][] = new int [512][512];	
		int Bblock[][] = new int [512][512];
		
		float rImageDCT1[][]= new float[512][512];
		float gImageDCT1[][]= new float[512][512];
		float bImageDCT1[][]= new float[512][512];
		int i,j;
		for( i=0;i<width;i++)
		{
			for ( j=0; j<length; j++)
			{
				rImageDCT1[i][j] = rImageDCT[i][j];
				gImageDCT1[i][j] = gImageDCT[i][j];
				bImageDCT1[i][j] = bImageDCT[i][j];
			}
		}
		int coeff=0;
		int reverse=0,sum=0;
		int max_elements = 8;
		int elem =1,e;
		int start_i=0;
		while (coeff<m)
		{
			for (i=start_i,e=0;e<elem;i++,e++)
			{
				Q[i][sum-i] = Q1[i][sum-i];
				coeff++;
				if (coeff>=m)
					break;
			}
			if (elem==max_elements)
				reverse=1;
			if (reverse==0)
				elem++;
			else
			{
				elem--;
				start_i++;
			}
			sum++;
		}
		
		for( i=0;i<width;i+=8)
		{
			for ( j=0; j<length; j+=8)
			{
				for(int u=0;u<8;u++)
				{
					for (int v=0; v<8; v++)
					{
						rImageDCT1[i+u][j+v] *=Q[u][v];
						gImageDCT1[i+u][j+v] *=Q[u][v];
						bImageDCT1[i+u][j+v] *=Q[u][v];
					}
				}
			}
		}
		for( i=0;i<width;i+=8)
		{
			for ( j=0; j<length; j+=8)
			{
				for (int x = 0; x<8; x++)
				{
					for (int y = 0; y<8; y++)
					{
						double sumfxyR = 0.0;
						double sumfxyG = 0.0;
						double sumfxyB = 0.0;
						for (int u=0;u<8;u++)
						{
							for (int v=0;v<8;v++)
							{
								double alpha = 1;
								if (u==0)
									alpha /= Math.sqrt(2);
								if (v==0)
									alpha /= Math.sqrt(2);
								sumfxyR += (0.25 * alpha * rImageDCT1[i+u][j+v] * Math.cos((2*x+1)*u*Math.PI/16.0)* Math.cos((2*y+1)*v*Math.PI/16.0));
								sumfxyG += (0.25 * alpha * gImageDCT1[i+u][j+v] * Math.cos((2*x+1)*u*Math.PI/16.0)* Math.cos((2*y+1)*v*Math.PI/16.0));
								sumfxyB += (0.25 * alpha * bImageDCT1[i+u][j+v] * Math.cos((2*x+1)*u*Math.PI/16.0)* Math.cos((2*y+1)*v*Math.PI/16.0));
							}
						}
						
						Rblock[i+x][j+y] = (int)(sumfxyR);
						Gblock[i+x][j+y] = (int)(sumfxyG);
						Bblock[i+x][j+y] = (int)(sumfxyB);
					}
				}
			}
		}
		byte r,g,b;
		for(int y = 0; y < length; y++)
		{
			for(int x = 0; x < width; x++)
			{	
				if (Rblock[x][y]>255)
					Rblock[x][y]=255;
				if (Gblock[x][y]>255)
					Gblock[x][y]=255;
				if (Bblock[x][y]>255)
					Bblock[x][y]=255;
				if (Rblock[x][y]<0)
					Rblock[x][y]=0;
				if (Gblock[x][y]<0)
					Gblock[x][y]=0;
				if (Bblock[x][y]<0)
					Bblock[x][y]=0;
				r = (byte)Rblock[x][y];
				g = (byte)Gblock[x][y];
				b = (byte)Bblock[x][y];
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				
				finalImg.setRGB(x,y,pix);
			}
			
		}
		/*
		try
			{
				ImageIO.write(finalImg, "bmp",new File("DCToutput"+m+".bmp"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
		return finalImg;
	}
	
	public void DWTEncoding(int[] bytes)
	{
		BufferedImage finalImg = new BufferedImage(width, length, BufferedImage.TYPE_INT_RGB);
		
		float rChannel[][]= new float[512][512];
		float gChannel[][]= new float[512][512];
		float bChannel[][]= new float[512][512];
		
		int  ind=0,i;
		for(int y = 0; y < length; y++)
		{
			for(int x = 0; x < width; x++)
			{
				ind = y*width + x;
				rChannel[x][y] = (float)bytes[ind];
				gChannel[x][y] = (float)bytes[ind+length*width];
				bChannel[x][y] = (float)bytes[ind+length*width*2];
			}
		}
			
		int newWidth = width;
		while(newWidth>1)
		{
			for(int x=0; x<length;x++)
			{
				i=0;
				for (int y=0; y<newWidth; y+=2 )
				{
					rImage[x][i] = (rChannel[x][y]+rChannel[x][y+1])/2;
					gImage[x][i] = (gChannel[x][y]+gChannel[x][y+1])/2;
					bImage[x][i] = (bChannel[x][y]+bChannel[x][y+1])/2;
					i++;
				}
				for (int y=0; y<newWidth; y+=2 )
				{
					rImage[x][i] = (rChannel[x][y]-rChannel[x][y+1])/2;
					gImage[x][i] = (gChannel[x][y]-gChannel[x][y+1])/2;
					bImage[x][i] = (bChannel[x][y]-bChannel[x][y+1])/2;
					i++;
				}
			}
			for(int x=0; x<length;x++)
			{
				for (int y=0; y<width; y++ )
				{
					rChannel[x][y] = rImage[x][y];
					gChannel[x][y] = gImage[x][y];
					bChannel[x][y] = bImage[x][y];
				}
			}			
			newWidth = newWidth/2;
		}
		
		//columns
		int newHeight = length;
		while(newHeight>1)
		{
			for (int y=0; y<width; y++)
			{
				i=0;
				for(int x=0; x<newHeight;x+=2)
				{
					rImage[i][y] = (rChannel[x][y]+rChannel[x+1][y])/2;
					gImage[i][y] = (gChannel[x][y]+gChannel[x+1][y])/2;
					bImage[i][y] = (bChannel[x][y]+bChannel[x+1][y])/2;
					i++;
				}
				for(int x=0; x<newHeight;x+=2)
				{
					rImage[i][y] = (rChannel[x][y]-rChannel[x+1][y])/2;
					gImage[i][y] = (gChannel[x][y]-gChannel[x+1][y])/2;
					bImage[i][y] = (bChannel[x][y]-bChannel[x+1][y])/2;
					i++;
				}
			}
			for(int x=0; x<length;x++)
			{
				for (int y=0; y<width; y++ )
				{
					rChannel[x][y] = rImage[x][y];
					gChannel[x][y] = gImage[x][y];
					bChannel[x][y] = bImage[x][y];
				}
			}
			newHeight = newHeight/2;
		}
	}
	
	public BufferedImage DWTDecoding( int n)
	{
		BufferedImage finalImg = new BufferedImage(width, length, BufferedImage.TYPE_INT_RGB);
				
		int Q[][]=new int [512][512];
		int coeff= 0,total=0;
		int i=0,j=0;
		float rChannel[][]= new float[512][512];
		float gChannel[][]= new float[512][512];
		float bChannel[][]= new float[512][512];
		float rImage1[][]= new float[512][512];
		float gImage1[][]= new float[512][512];
		float bImage1[][]= new float[512][512];
		for( i=0;i<width;i++)
		{
			for ( j=0; j<length; j++)
			{
				rImage1[i][j] = rImage[i][j];
				gImage1[i][j] = gImage[i][j];
				bImage1[i][j] = bImage[i][j];
			}
		}
		int u=0,x,y;
		int v=0;
		int sum = 0;
		int reverse=0;
		int max_elements = width;
		int elem =1,e=0;
		int start_i=0;
		
		for( i=0;i<length;i++)
		{
			for ( j=0; j<width; j++)
			{
				Q[i][j] = 0;
			}
		}	
		
		while (coeff<n)
		{
			for ( i=start_i,e=0;e<elem;i++,e++)
			{
				Q[i][sum-i] = 1;
				coeff++;
				if (coeff>=n)
					break;
			}
			if (elem==max_elements)
				reverse=1;
			if (reverse==0)
				elem++;
			else
			{
				elem--;
				start_i++;
			}
			sum++;
		}
		
		for( i=0;i<length;i++)
		{
			for ( j=0; j<width; j++)
			{
				rImage1[i][j]= rImage1[i][j] * Q[i][j];
				rChannel[i][j] = rImage1[i][j];
				gImage1[i][j]= gImage1[i][j] * Q[i][j];
				gChannel[i][j] = gImage1[i][j];
				bImage1[i][j]= bImage1[i][j] * Q[i][j];
				bChannel[i][j] = bImage1[i][j];
			}
		}
		//columns
		int newHeight = 1;
		while(newHeight<length)
		{
			for ( y=0; y<width; y++)
			{
				i=0;
				for( x=0; x<newHeight;x++)
				{
					rChannel[i][y]  = rImage1[x][y] + rImage1[(newHeight)+x][y];
					rChannel[i+1][y] =rImage1[x][y] - rImage1[(newHeight)+x][y];	
					gChannel[i][y]  = gImage1[x][y] + gImage1[(newHeight)+x][y];
					gChannel[i+1][y] =gImage1[x][y] - gImage1[(newHeight)+x][y];	
					bChannel[i][y]  = bImage1[x][y] + bImage1[(newHeight)+x][y];
					bChannel[i+1][y] =bImage1[x][y] - bImage1[(newHeight)+x][y];		
					i+=2;					
				}
			}
			for( x=0; x<length;x++)
			{
				for ( y=0; y<width; y++ )
				{
					rImage1[x][y] = rChannel[x][y];
					gImage1[x][y] = gChannel[x][y];
					bImage1[x][y] = bChannel[x][y];
				}
			}
			newHeight = newHeight*2;
		}
		
		//rows
		int newWidth = 1;
		while(newWidth<width)
		{
			for( x=0; x<length;x++)
			{
				 i=0;
				for ( y=0; y<newWidth; y++ )
				{
					rChannel[x][i]  = rImage1[x][y] + rImage1[x][(newWidth)+y];
					rChannel[x][i+1] =rImage1[x][y] - rImage1[x][(newWidth)+y];
					gChannel[x][i]  = gImage1[x][y] + gImage1[x][(newWidth)+y];
					gChannel[x][i+1] =gImage1[x][y] - gImage1[x][(newWidth)+y];
					bChannel[x][i]  = bImage1[x][y] + bImage1[x][(newWidth)+y];
					bChannel[x][i+1] =bImage1[x][y] - bImage1[x][(newWidth)+y];					
					i+=2;					
				}
			}
			for( x=0; x<length;x++)
			{
				for ( y=0; y<width; y++ )
				{
					rImage1[x][y] = rChannel[x][y];
					gImage1[x][y] = gChannel[x][y];
					bImage1[x][y] = bChannel[x][y];
				}
			}			
			newWidth = newWidth*2;
		}
		
		//decoding
		for(y = 0; y < length; y++)
		{
			for(x = 0; x < width; x++)
			{
				if (rChannel[x][y]>255)
					rChannel[x][y]=255;
				if (gChannel[x][y]>255)
					gChannel[x][y]=255;
				if (bChannel[x][y]>255)
					bChannel[x][y]=255;
				if (rChannel[x][y]<0)
					rChannel[x][y]=0;
				if (gChannel[x][y]<0)
					gChannel[x][y]=0;
				if (bChannel[x][y]<0)
					bChannel[x][y]=0;
				byte r = (byte)rChannel[x][y];
				byte g = (byte)gChannel[x][y];
				byte b = (byte)bChannel[x][y]; 
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				
				finalImg.setRGB(x,y,pix);
			}
		}/*
		try
			{
				ImageIO.write(finalImg, "bmp",new File("DWToutput"+n+".bmp"));
			}catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
		return finalImg;
	}
	
	public void display(BufferedImage image, String name)
	{
		// Use a panel and label to display the image
		JPanel  panel = new JPanel ();
		panel.add (new JLabel (new ImageIcon (image)));
		
		JFrame frame = new JFrame(name);
		
		frame.getContentPane().add (panel);
		frame.pack();
		frame.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	
		
	}
	
	public BufferedImage[] displayAnimationDCT(int[] bytes,int m)
	{
		BufferedImage images[]= new BufferedImage[64];
		BufferedImage finalImg;
		imageReader imgReader = new imageReader();
		imgReader.DCTEncoding(bytes);
		for (int i=1;i<=64;i++)
		{
			finalImg = imgReader.DCTDecoding(i);
			images[i-1] = finalImg;
			
		}		
		return images;
	}
	
	public BufferedImage[] displayAnimationDWT(int[] bytes,int n)
	{
		BufferedImage images[]= new BufferedImage[64];
		imageReader imgReader = new imageReader();
		imgReader.DWTEncoding(bytes);
		int j=0;
		for (int i=4096;i<=262144;i+=4096)
		{	
			images[j]= imgReader.DWTDecoding(i);
			j++;
		}		
		return images;
	}
	public void displayAnimation(int[] bytes,int n)
	{
		BufferedImage dctImages[] = displayAnimationDCT(bytes,(n/4096));
		BufferedImage dwtImages[] = displayAnimationDWT(bytes,n);
		
		JPanel  panel = new JPanel ();
		ImageIcon dctImage = new ImageIcon ();
		ImageIcon dwtImage = new ImageIcon ();
		JLabel dctLabel = new JLabel(dctImage);
		JLabel dwtLabel = new JLabel(dwtImage);
		panel.add (dctLabel);
		panel.add (dwtLabel);
		
		JFrame frame = new JFrame("DCT vs DWT with the corresponding number of co-efficients");
		frame.setSize(2500,2000);
		frame.getContentPane().add (panel);
		//frame.pack();
		frame.setVisible(true);
		
		for (int i=0;i<64;i++)
		{
			dctLabel.setIcon(new ImageIcon(dctImages[i]));
			dwtLabel.setIcon(new ImageIcon(dwtImages[i]));
			try        
			{
				Thread.sleep(1000);
			} 
			catch(InterruptedException ex) 
			{
				Thread.currentThread().interrupt();System.exit(0);	
			}
		}
		frame.setVisible(false);
		frame.dispose();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	
			
		
	}
	
   public static void main(String[] args) 
   {
		imageReader imgReader = new imageReader();
		String fileName = args[0];
		int n = Integer.parseInt(args[1]);
		int[] bytes = new int[512*512*3];
		InputStream is = null;
		int i;
		int index= 0;
		char c;
      
		try {
  			is = new FileInputStream(fileName);
			while((i = is.read())!=-1) {
				bytes[index]=i;
				index++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
         
         // if any I/O error occurs
         e.printStackTrace();
		} finally {
         
         // releases system resources associated with this stream
			try
			{
				if(is!=null)
					is.close();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (n==-1)
		{
			imgReader.displayAnimation(bytes,n);
		}
		else
		{
			imgReader.DCTEncoding(bytes);
			BufferedImage finalImg = imgReader.DCTDecoding((n/4096));
			imgReader.display(finalImg,"DCT");
			imgReader.DWTEncoding(bytes);
			finalImg =imgReader.DWTDecoding(n);
			imgReader.display(finalImg,"DWT");
		}
	}
  
}