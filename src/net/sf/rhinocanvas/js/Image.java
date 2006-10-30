package net.sf.rhinocanvas.js;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


public class Image  {

	BufferedImage image;

	public Image(int width, int height) {
    
    	image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    	Graphics g = image.getGraphics();
    	g.setColor(Color.WHITE);
    	g.fillRect(0, 0, width, height);
    
    }

    public int getWidth(){
    	return image.getWidth();
    }
	
    public int getHeight(){
    	return image.getHeight();
    }
	
	
    public CanvasRenderingContext2D getContext(String param){
    	return new CanvasRenderingContext2D(this);
 
    }

	 void dirty() {
		
	}


//
//	public String getClassName() {
//		
//		return "Image";
//	}
}
