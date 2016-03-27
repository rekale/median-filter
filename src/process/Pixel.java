package process;

import java.awt.Color;

public class Pixel {
	

	
	public static int getRed(int red){
		return (red >> 16) & 0xFF;
	}
	
	public static int getGreen(int green){
		return (green >> 8) & 0xFF;
	}
	
	public static int getBlue(int blue){
		return blue & 0xFF;
	}
	
	public static int createRGB(int R, int G, int B){
		Color c = new Color(R, G, B);
		return c.getRGB();
	}
	
	
}
