package net.sf.rhinocanvas.js;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


/*
 * Created on 28/10/2006 by Stefan Haustein
 */

public class CanvasFrame extends Image {

	Frame frame;
	Helper helper;
	
	class Helper extends java.awt.Canvas{
		public void paint(java.awt.Graphics g){
			g.drawImage(image, 0, 0, frame);
		}
		public void update(java.awt.Graphics g){
			g.drawImage(image, 0, 0, frame);
		}
		
		public Dimension getPreferredSize(){
			return new Dimension(image.getWidth(), image.getHeight());
			
		}
		public Dimension getMinimumSize(){
			return new Dimension(image.getWidth(), image.getHeight());
			
		}
	}

	
	  public CanvasFrame(String title, int width, int height) {
		  super (width, height);
	    	frame = new Frame(title);
	    
	    	frame.addWindowListener(new WindowAdapter(){
	    		public void 	windowClosing(WindowEvent e) {
					frame.dispose();
				}
	    	});
	    	
	    	helper = new Helper();
	    	helper.addComponentListener(new ComponentAdapter(){

				public void componentResized(ComponentEvent e) {
					if(helper.getWidth() > image.getWidth()||helper.getHeight() > image.getHeight() ){
						int w = Math.max(helper.getWidth(), image.getWidth());
						int h = Math.max(helper.getHeight(), image.getHeight());
						
						
						BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
					
						Graphics g = newImage.getGraphics();
						g.drawImage(image, 0, 0, null);
						//image.copyData(newImage.getRaster());
						image = newImage;
					}
				}

	    	});
	    	
	    	frame.add(helper);
	    	frame.pack();
	    	frame.setVisible(true);
	    }
	  
	 void dirty(){
		 helper.repaint();
	 }

}
