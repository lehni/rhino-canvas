package net.sf.rhinocanvas.js;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;


/*
 * Created on 28/10/2006 by Stefan Haustein
 */

public class Window  {
	

	Frame frame;
	Helper helper;
	Image content;
	Function onKeyDown;
	Function onKeyUp;
	Context context;
	
	
	class Helper extends java.awt.Canvas{
		public void paint(java.awt.Graphics g){
			g.drawImage(content.image, 0, 0, frame);
		}
		public void update(java.awt.Graphics g){
			g.drawImage(content.image, 0, 0, frame);
		}
		
		public Dimension getPreferredSize(){
			return new Dimension(content.getWidth(), content.getHeight());
			
		}
		public Dimension getMinimumSize(){
			return new Dimension(content.getWidth(), content.getHeight());
			
		}
	}

	
	  public Window(String title, Object content) {
		  
		  context = Context.getCurrentContext();

	    	frame = new Frame(title);
	    
	    	setContent(content);
	    	
	    	frame.addWindowListener(new WindowAdapter(){
	    		public void 	windowClosing(WindowEvent e) {
					frame.dispose();
				}
	    	});
	    	
	    	helper = new Helper();
    		/*
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

	    	});*/
	    	
	    	helper.addKeyListener(new KeyListener(){

				public void keyPressed(KeyEvent ke) {
					System.out.println("pressed Keycode: "+ke.getKeyCode()+" char: "+ke.getKeyChar());
					if(onKeyDown != null){
						NativeObject no = new NativeObject();
						ScriptableObject.putProperty(no, "which", new Double(ke.getKeyCode()));
						ScriptableObject.putProperty(no, "keyCode", new Double(ke.getKeyCode()));
						
						Context.enter(context);
						
						onKeyDown.call(context, onKeyDown, onKeyDown, new Object[]{no});
					}
				}

				public void keyReleased(KeyEvent ke) {
					System.out.println("rel. Keycode: "+ke.getKeyCode()+" char: "+ke.getKeyChar());
					
				}

				public void keyTyped(KeyEvent ke) {
					System.out.println("typed Keycode: "+ke.getKeyCode()+" char: "+ke.getKeyChar());
					
				}
	    		
	    	});
	    	
	    	
	    	frame.add(helper);
	    	frame.pack();
	    	frame.setVisible(true);
	    }
	  
	 void dirty(){
		 helper.repaint();
	 }

	 public Function getOnkeydown(){
		 return onKeyDown;
	 }
	 
	 public void setOnkeydown(Function okd){
		 onKeyDown = okd;
	 }
	 
	 public Function getOnkeyup(){
		 return onKeyUp;
	 }
	 
	 public void setOnkeyup(Function oku){
		 onKeyUp = oku;
	 }
	 
	 
	 public Object getContent(){
		 return content;
	 }
	 
	 public void setContent(Object content){
		 this.content = (Image) content;
		 this.content.owner = this;
	 }
}
