package net.sf.rhinocanvas.js;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

import net.sf.rhinocanvas.rt.RhinoRuntime;

import org.mozilla.javascript.Context;


/*
 * Created on 28/10/2006 by Stefan Haustein
 */


/** Applet or Frame. Should behave like document to some extent. The variable document should initially
 * point to a Frame instance. */

public class Frame extends Component {
	
//	boolean macOS = System.getProperty("mrj.version") != null;
	RootPaneContainer container;
	JFrame frame;
	JApplet applet;
	Helper helper;
	Image content;
	Context context;
	int run = -1;
	
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

	Frame(RootPaneContainer container, Object content){
		this.container = container;
		if(content != null){
			setContent(content);
		}
		
		context = Context.getCurrentContext();
		helper = new Helper();
		helper.setDoubleBuffered(true);
	    	
		EventHelper eh = new EventHelper(helper);
		helper.addMouseListener(eh);
		helper.addKeyListener(eh);
		helper.setFocusable(true);
	    	
		helper.addComponentListener(new ComponentAdapter(){

			public void componentResized(ComponentEvent e) {
				if(helper.getWidth() != Frame.this.content.getWidth()||helper.getHeight() != Frame.this.content.getHeight() ){

					BufferedImage newImage = new BufferedImage(helper.getWidth(), helper.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
					
					Graphics g = newImage.getGraphics();
					g.drawImage(Frame.this.content.image, 0, 0, null);
						//image.copyData(newImage.getRaster());
					Frame.this.content.image = newImage;
				}
			}
		});
	    	
	    	
		container.getContentPane().add(BorderLayout.CENTER, helper);  	
		container.getContentPane().doLayout();
	}
	
	
	Frame(CanvasApplet applet){
		this((RootPaneContainer) applet, new Image(applet.getWidth(), applet.getHeight()));
		this.applet = applet;
	}
		
	
	
	public Frame(String title, Object content) {
		this(new JFrame(title), content);  
		  
		this.frame = (JFrame) container;
	    
		setContent(content);
	    	
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {	
				frame.dispose();
			}
		});
	    	
	    frame.pack();
	 //   frame.setVisible(true);
	    	
	 //   frame.requestFocus();
		frame.requestFocusInWindow();
	}
	  
	 void dirty(){
		 helper.repaint();
	 }

	
	 public void setResizable(boolean rs){
		 if(frame != null){
			 frame.setResizable(rs);
		 }
	 }
	 
	 public Object getElementById(String id){
		 RhinoRuntime runtime = (RhinoRuntime) Context.getCurrentContext().getThreadLocal("runtime");

		 if(frame != null && run != runtime.runNumber){
			 run = runtime.runNumber;
			 frame.setVisible(true);
			 frame.requestFocus();
		 }
		 return content;
	 }
	 
	 
	 public Object getContent(){
		 return content;
	 }
	 
	 public void setContent(Object content){
		 this.content = (Image) content;
		 this.content.owner = this;
	 }
}
