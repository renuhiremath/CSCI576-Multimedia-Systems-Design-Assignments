import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.lang.Math.*;

public class DCT {
	
	private int length = 512;
	private int width = 512;
	private float rImage[][]= new float[8][8];
	private byte gImage[][]= new byte[512][512];
	private byte bImage[][]= new byte[512][512];
	
	public void DCTEncoding()
	{		
		byte r,g,b;
		int ind;
		
		int Rblock[][]= {{52,55,61,66,70,61,64,73},{63,59,55,90,109,85,69,72},{62,59,68,113,144,104,66,73},{63,58,71,122,154,106,70,69},{67,61,68,104,126,88,68,70},{79,65,60,70,77,68,58,75},{85,71,64,59,55,61,65,83},{87,79,69,68,65,76,78,94}};
		int Q[][] = {{16,11,10,16,24,40,51,61}, {12,12,14,19,26,58,60,55},{14,13,16,24,40,57,69,56}, {14,17,22,29,51,87,80,62},{18,22,37,56,68,109,103,77},{24,35,55,64,81,104,113,92},{49,64,78,87,103,121,120,101},{72,92,95,98,112,100,103,99}};
		double F = 0;
		
		byte gFinalImage[][]= new byte[512][512];
				for(int i=0;i<8;i++)
		{
			for (int j=0; j<8; j++)
			{
				System.out.print(Rblock[i][j]+"\t");
			}
			System.out.println("");
		}
		for (int i = 0; i<8; i++)
		{
			for (int j = 0; j<8; j++)
			{
				Rblock[i][j] = Rblock[i][j] -128;
			}
		}				
		
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
				for (int x=0;x<8;x++)
				{
					for (int y=0;y<8;y++)
					{
						sumfxyR += (0.25 * alpha * Rblock[x][y] * Math.cos((2*x+1)*u*Math.PI/16.0)* Math.cos((2*y+1)*v*Math.PI/16.0));
					}
				}
					
				rImage[u][v] = (float)(sumfxyR);
				rImage[u][v] = Math.round((rImage[u][v]/(double)Q[u][v]));
			}
		}
		
		System.out.println("after quantization");
		for(int i=0;i<8;i++)
		{
			for (int j=0; j<8; j++)
			{
				System.out.print(rImage[i][j]+"\t");
			}
			System.out.println("");
		}
	}
	
	public  void DCTDecoding(int m)
	{		
		BufferedImage finalImg = new BufferedImage(width, length, BufferedImage.TYPE_INT_RGB);
		int Q1[][] = {{16,11,10,16,24,40,51,61}, {12,12,14,19,26,58,60,55},{14,13,16,24,40,57,69,56},{14,17,22,29,51,87,80,62},{18,22,37,56,68,109,103,77},{24,35,55,64,81,104,113,92},{49,64,78,87,103,121,120,101},{72,92,95,98,112,100,103,99}};
		int Q[][] = {{0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
		int coeff= 0,total=0;
		int Rblock[][] = new int [8][8];
		int u=0;
		int v=0;
		int i=0,j=0;
		int even=1;
		int sum = 0;
		int reverse=0;
		int max_elements = 8;
		int elem =1,e=0;
		int start_i=0;
		while (coeff<m)
		{
			for ( i=start_i,e=0;e<elem;i++,e++)
			{
			//System.out.println(i+" "+(sum-i));
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
		
		/*
		System.out.println("quantization");
		for( i=0;i<8;i++)
		{
			for ( j=0; j<8; j++)
			{
				System.out.print(Q[i][j]+"\t");
			}
			System.out.println("");
		}
		
		*/
		System.out.println("After * q");
		for( i=0;i<8;i++)
		{
			for ( j=0; j<8; j++)
			{
				rImage[i][j] = Math.round((rImage[i][j]*(double)Q[i][j]));
				System.out.print(rImage[i][j]+"\t");
			}
			System.out.println("");
		}
		
		for (int x = 0; x<8; x++)
		{
			for (int y = 0; y<8; y++)
			{
				double sumfxyR = 0.0;
				for (u=0;u<8;u++)
				{
					for (v=0;v<8;v++)
					{
						double alpha = 1;
						if (u==0)
							alpha /= Math.sqrt(2);
						if (v==0)
							alpha /= Math.sqrt(2);
						sumfxyR += (0.25 * alpha * rImage[u][v] * Math.cos((2*x+1)*u*Math.PI/16.0)* Math.cos((2*y+1)*v*Math.PI/16.0));
					}
				}
					
				Rblock[x][y] = (int)(sumfxyR)+128;
			}
		}
		System.out.println("After decoding");
		for( i=0;i<8;i++)
		{
			for ( j=0; j<8; j++)
			{
				System.out.print(Rblock[i][j]+"\t");
			}
			System.out.println("");
		}
		
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
		DCT imgReader = new DCT();
		
		//DCT imgReader = new DCT();
		imgReader.DCTEncoding();
		imgReader.DCTDecoding((262144/4096));
		
	}
  
}