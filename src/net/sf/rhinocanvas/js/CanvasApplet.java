package net.sf.rhinocanvas.js;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JApplet;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import com.sun.jndi.toolkit.url.UrlUtil;

import net.sf.rhinocanvas.rt.RhinoRuntime;



public class CanvasApplet extends JApplet {

	Window window;
	RhinoRuntime runtime;
	String scriptUrl;
	
	public CanvasApplet(){
	}

	public void init(){
		runtime = new RhinoRuntime();
		window = new Window(this);
		runtime.defineProperty("canvas", window.content);

		URI base;
		try {
			base = new URI(getDocumentBase().toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		scriptUrl = base.resolve(getParameter("script")).toString();
	}
	
	
	public void start(){
		
		StringBuilder sb = new StringBuilder();
		try {
			runtime.setSource(scriptUrl);

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
	
}
