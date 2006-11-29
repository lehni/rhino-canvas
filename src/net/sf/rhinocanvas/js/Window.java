package net.sf.rhinocanvas.js;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.JobAttributes;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

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
	
	boolean macOS = System.getProperty("mrj.version") != null;
	RootPaneContainer container;
	JFrame frame;
	JApplet applet;
	Helper helper;
	Image content;
	Function onKeyDown;
	Function onKeyUp;
	Context context;
	
	
	class Helper extends JPanel{
		
		public void paint(java.awt.Graphics g){
			// image may be transparent...
			g.setColor(getBackground());
			g.fillRect(0,0,getWidth(),getHeight());

			g.drawImage(content.image, 0, 0,(ImageObserver) container);
		}
//		public void update(java.awt.Graphics g){
//			g.drawImage(content.image, 0, 0, frame);
//		}
		
		public Dimension getPreferredSize(){
			return new Dimension(content.getWidth(), content.getHeight());
			
		}
		public Dimension getMinimumSize(){
			return new Dimension(content.getWidth(), content.getHeight());
		}
	}

	Window(RootPaneContainer container, Object content){
		this.container = container;
		if(content != null){
			setContent(content);
		}
		
		context = Context.getCurrentContext();
		helper = new Helper();
		helper.setDoubleBuffered(true);
	    	
		helper.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {
				System.out.println("Requesting the damn focus");
				helper.requestFocus();
			}
		});
	    	
		helper.setFocusable(true);
	    	
		helper.addComponentListener(new ComponentAdapter(){

			public void componentResized(ComponentEvent e) {
				if(helper.getWidth() != Window.this.content.getWidth()||helper.getHeight() != Window.this.content.getHeight() ){

					BufferedImage newImage = new BufferedImage(helper.getWidth(), helper.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
					
					Graphics g = newImage.getGraphics();
					g.drawImage(Window.this.content.image, 0, 0, null);
						//image.copyData(newImage.getRaster());
					Window.this.content.image = newImage;
				}
			}
		});
	    	
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
//					System.out.println("rel. Keycode: "+ke.getKeyCode()+" char: "+ke.getKeyChar());
//					System.out.println("pressed Keycode: "+ke.getKeyCode()+" char: "+ke.getKeyChar());
				if(onKeyUp != null){
					NativeObject no = new NativeObject();
					ScriptableObject.putProperty(no, "which", new Double(ke.getKeyCode()));
					ScriptableObject.putProperty(no, "keyCode", new Double(ke.getKeyCode()));
						
					Context.enter(context);
					
					onKeyUp.call(context, onKeyUp, onKeyUp, new Object[]{no});
				}
			}

			public void keyTyped(KeyEvent ke) {
				System.out.println("typed Keycode: "+ke.getKeyCode()+" char: "+ke.getKeyChar());
			}
	   	});
	    	
	    	
		container.getContentPane().add(BorderLayout.CENTER, helper);  	
		container.getContentPane().doLayout();
	}
	
	
	Window(CanvasApplet applet){
		this((RootPaneContainer) applet, new Image(applet.getWidth(), applet.getHeight()));
		this.applet = applet;
		
	}
		
	
	
	public Window(String title, Object content) {
		this(new JFrame(title), content);  
		  
		this.frame = (JFrame) container;
	    
		setContent(content);
	    	
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {	
				frame.dispose();
			}
		});
	    	
	    frame.pack();
	    frame.setVisible(true);
	    	
	    frame.requestFocus();
		frame.requestFocusInWindow();

	    frame.setResizable(false);
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
	
	 public void setResizable(boolean rs){
		 if(frame != null){
			 frame.setResizable(rs);
		 }
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
