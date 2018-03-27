import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.lang.Math.*;

public class read {
	
	private int length = 512;
	private int width = 512;
	private float rImageDCT[][]= new float[512][512];
	private float gImageDCT[][]= new float[512][512];
	private float bImageDCT[][]= new float[512][512];
	private float rImage[][]= new float[512][512];
	private float gImage[][]= new float[512][512];
	private float bImage[][]= new float[512][512];
	
   public static void main(String[] args) 
   {
		String fileName = args[0];
		//int n = Integer.parseInt(args[1]);
		int[] bytes = new int[512*512*3];
		//String file = "/path/to/your/file.txt";

		InputStream is = null;
		int i;
		char c;
      
		try {
      
			// new input stream created
			is = new FileInputStream(fileName);
         
			System.out.println("Characters printed:");
         
			// reads till the end of the stream
			while((i = is.read())!=-1) {
         
            // prints character
				System.out.print(i);
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
		 try{
			if(is!=null)
		 is.close();}
	 catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
   }
}