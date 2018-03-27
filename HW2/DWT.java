import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.lang.Math.*;

public class DWT {
	
	private int length = 4;
	private int width = 4;
	private float rImage[][]= new float[4][4];
	
	public void DWTEncoding()
	{		
		byte r,g,b;
		int ind;
		
		float Rblock[][]= {{20,16,10,16},{18,22,2,18},{8,4,8,10},{10,12,18,28}};
		double F = 0;
		
		int i,j;
		System.out.println("start");
		for( i=0;i<4;i++)
		{
			for ( j=0; j<4; j++)
			{
				System.out.print(Rblock[i][j]+"\t");
			}
			System.out.println("");
		}
		//rows
		int newWidth = width;
		while(newWidth>1)
		{
			for(int x=0; x<length;x++)
			{
				i=0;
				for (int y=0; y<newWidth; y+=2 )
				{
					rImage[x][i] = (Rblock[x][y]+Rblock[x][y+1])/2;
					i++;
				}
				for (int y=0; y<newWidth; y+=2 )
				{
					rImage[x][i] = (Rblock[x][y]-Rblock[x][y+1])/2;
					i++;
				}
			}
			for(int x=0; x<length;x++)
			{
				for (int y=0; y<width; y++ )
				{
					Rblock[x][y] = rImage[x][y];
				}
			}
			
			System.out.println("row cycle");
			for( i=0;i<4;i++)
			{
				for ( j=0; j<4; j++)
				{
					System.out.print(Rblock[i][j]+"\t");
				}
				System.out.println("");
			}
			
			newWidth = newWidth/2;
		}
		/*System.out.println("row cycle");
			for(int i=0;i<4;i++)
			{
				for (int j=0; j<4; j++)
				{
					System.out.print(Rblock[i][j]+"\t");
				}
				System.out.println("");
			}*/
		//columns
		
		int newHeight = length;
		while(newHeight>1)
		{
			for (int y=0; y<width; y++)
			{
				i=0;
				for(int x=0; x<newHeight;x+=2)
				{
					rImage[i][y] = (Rblock[x][y]+Rblock[x+1][y])/2;
					i++;
				}
				for(int x=0; x<newHeight;x+=2)
				{
					rImage[i][y] = (Rblock[x][y]-Rblock[x+1][y])/2;
					i++;
				}
			}
			for(int x=0; x<length;x++)
			{
				for (int y=0; y<width; y++ )
				{
					Rblock[x][y] = rImage[x][y];
				}
			}
			System.out.println("col cycle");
			for( i=0;i<4;i++)
			{
				for ( j=0; j<4; j++)
				{
					System.out.print(Rblock[i][j]+"\t");
				}
				System.out.println("");
			}
			newHeight = newHeight/2;
		}
		
		System.out.println("after quantization");
		for( i=0;i<4;i++)
			{
				for ( j=0; j<4; j++)
				{
					System.out.print(Rblock[i][j]+"\t");
				}
				System.out.println("");
		}
	}
	
	public  void DWTDecoding(int n)
	{		
		BufferedImage finalImg = new BufferedImage(width, length, BufferedImage.TYPE_INT_RGB);
		int Q[][]=new int [4][4];
		int coeff= 0,total=0;
		float Rblock[][] = new float [4][4];
		int u=0,x,y;
		int v=0;
		int i=0,j=0;
		int even=1;
		int sum = 0;
		int reverse=0;
		int max_elements = 4;
		int elem =1,e=0;
		int start_i=0;
		n=16;
		while (coeff<n)
		{
			for ( i=start_i,e=0;e<elem;i++,e++)
			{
				//System.out.println(i+" "+(sum-i));
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
		
		//System.out.println("quantization");
		for( i=0;i<4;i++)
		{
			for ( j=0; j<4; j++)
			{
				//System.out.print(rImage[i][j]+"\t");
				rImage[i][j]= rImage[i][j] * Q[i][j];
				Rblock[i][j] = rImage[i][j];
			}
			//System.out.println("");
		}
		//columns
		
		int newHeight = 1;
		while(newHeight<length)
		{
			for ( y=0; y<width; y++)
			{
				i=0;
				System.out.println(newHeight);
				for( x=0; x<newHeight;x++)
				{
					System.out.println(x+"   "+ y+ "   " + i + "  " + ((newHeight*2)+x));
					Rblock[i][y]  = rImage[x][y] + rImage[(newHeight)+x][y];
					Rblock[i+1][y] =rImage[x][y] - rImage[(newHeight)+x][y];		
					i+=2;					
				}
			}
			for( x=0; x<length;x++)
			{
				for ( y=0; y<width; y++ )
				{
					rImage[x][y] = Rblock[x][y] ;
				}
			}
			System.out.println("col cycle decoded");
			for( i=0;i<4;i++)
			{
				for ( j=0; j<4; j++)
				{
					System.out.print(Rblock[i][j]+"\t");
				}
				System.out.println("");
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
					Rblock[x][i]  = rImage[x][y] + rImage[x][(newWidth)+y];
					Rblock[x][i+1] =rImage[x][y] - rImage[x][(newWidth)+y];		
					i+=2;					
				}
			}
			for( x=0; x<length;x++)
			{
				for ( y=0; y<width; y++ )
				{
					rImage[x][y] = Rblock[x][y] ;
				}
			}
			
			System.out.println("row cycle decoded");
			for( i=0;i<4;i++)
			{
				for ( j=0; j<4; j++)
				{
					System.out.print(Rblock[i][j]+"\t");
				}
				System.out.println("");
			}
			
			newWidth = newWidth*2;
		}
		/*System.out.println("row cycle");
			for( i=0;i<4;i++)
			{
				for ( j=0; j<4; j++)
				{
					System.out.print(Rblock[i][j]+"\t");
				}
				System.out.println("");
		}	
		*/
		/*
		System.out.println("After * q");
		for(int i=0;i<8;i++)
		{
			for (int j=0; j<8; j++)
			{
				rImage[i][j] = Math.round((rImage[i][j]*(double)Q[i][j]));
				System.out.print(rImage[i][j]+"\t");
			}
			System.out.println("");
		}
		*/
		
		System.out.println("After decoding");
		for(i=0;i<4;i++)
		{
			for (j=0; j<4; j++)
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
		DWT imgReader = new DWT();
		
		//DWT imgReader = new DWT();
		imgReader.DWTEncoding();
		imgReader.DWTDecoding((262144/4096));
		
	}
  
}