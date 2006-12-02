package net.sf.rhinocanvas.js;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import net.sf.rhinocanvas.rt.RhinoRuntime;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;



public class Image  {

	Frame owner;
	BufferedImage image;
	String src;
	Function onload;
	boolean doNotify;
	
	public Image(){
		
	}
	
	public Image(int width, int height) {
		
    	image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    	Graphics g = image.getGraphics();
    	g.setColor(Color.WHITE);
    	g.fillRect(0, 0, width, height);
    
    }

	public void setSrc(String url){
		this.src = url;
		try {

			RhinoRuntime runtime = (RhinoRuntime) Context.getCurrentContext().getThreadLocal("runtime");

			Scriptable scope = runtime.getScope();
			
			String base = (String) scope.get("documentBase", scope);
			try {
				URI baseUri = new URI(base);
				image = ImageIO.read(baseUri.resolve(url).toURL());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(onload != null){
				onload.call(Context.getCurrentContext(), onload, onload, new Object[0]);
			}
			else{
				doNotify = true;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void setOnload(Function onload){
		this.onload = onload;
		if(doNotify){
			doNotify = false;
			onload.call(Context.getCurrentContext(), onload, onload, new Object[0]);
		}
	}
	
	public Function getOnload(){
		return onload;
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
		if(owner != null){
			owner.helper.repaint();
		}
	}


//
//	public String getClassName() {
//		
//		return "Image";
//	}
}
