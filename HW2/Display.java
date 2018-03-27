import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.lang.Math.*;

public class Display {
	
   public static void main(String[] args) 
   {
		JPanel  panel = new JPanel ();
		panel.add (new JLabel (new ImageIcon ("2.jpg")));
		//panel.add (new JLabel (new ImageIcon ("2.jpg")));
		
		JFrame frame = new JFrame("Display images");
		frame.setSize(400,400);
		frame.add (panel);
		frame.pack();
		frame.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
  
}