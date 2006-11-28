package net.sf.rhinocanvas.js;

import java.applet.Applet;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import com.sun.jndi.toolkit.url.UrlUtil;

import net.sf.rhinocanvas.rt.RhinoRuntime;



public class CanvasApplet extends Applet {

	Image image;
	RhinoRuntime runtime;
	String scriptUrl;
	
	public void init(){
		runtime = new RhinoRuntime();
		image = new Image(getWidth(), getHeight());
		scriptUrl = getParameter("script");
		
		runtime.defineProperty("canvas", image);
	}
	
	public void start(){
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(new URL(scriptUrl).openStream(), "UTF-8"));
			
			while(true){
				String line = r.readLine();
				if(line == null) break;
				sb.append(line);
				sb.append('\n');
			}
			
			runtime.exec(sb.toString());
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		

	}
	
	
	public void paint(Graphics g){
		g.drawImage(image.image, 0, 0, this);
	}
	
}
