package process;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class PanelImage extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	private static int count = 0;
	
	public static PanelImage getInstance(BufferedImage img) {
		
		if(count <= 2)
			return new PanelImage(img);
		else
			return null;
	}
	
	private PanelImage(BufferedImage img) {
		
		this.img = img;
		count+=1;
	}
	
	
	public void paint(Graphics g){
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		g.dispose();
		repaint();
	}
	
	public static void reset(){
		count = 0;
	}
	
	public static boolean isFull(){
		if(count > 2)
			return true;
		else
			return false;
					
	}
	
	public static int getCount(){
		return count;
	}

}
