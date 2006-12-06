package net.sf.rhinocanvas.js;


import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JApplet;


import net.sf.rhinocanvas.rt.RhinoRuntime;



public class CanvasApplet extends JApplet {

	Frame window;
	RhinoRuntime runtime;
	String scriptUrl;
	
	public CanvasApplet(){
	}

	public void init(){
		runtime = new RhinoRuntime();
		window = new Frame(this);
		runtime.defineProperty("document", window);

		URI base;
		try {
			base = new URI(getDocumentBase().toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		scriptUrl = base.resolve(getParameter("script")).toString();
	}
	
	
	public void start(){
		
		StringBuffer sb = new StringBuffer();
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
