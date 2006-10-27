import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.mozilla.javascript.ScriptableObject;

import com.sun.tools.javac.util.Diagnostic;


public class Image extends ScriptableObject {

	BufferedImage image;
	Frame frame;
	
	class Helper extends java.awt.Canvas{
		public void paint(java.awt.Graphics g){
			g.drawImage(image, 0, 0, frame);
		}
		
		public Dimension getPreferredSize(){
			return new Dimension(image.getWidth(), image.getHeight());
			
		}
		public Dimension getMinimumSize(){
			return new Dimension(image.getWidth(), image.getHeight());
			
		}
	}
	
	
	public Image(){
		
	}
	

    public void jsConstructor(int width, int height) {
    	frame = new Frame("Canvas");
    	image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    	frame.add(new Helper());
    	frame.pack();
    	frame.setVisible(true);
    }

    
    public Context jsFunction_getContext(String param){
    	Context ctx = new Context();
    	ctx.init((Graphics2D) image.getGraphics());
    	return ctx;
    }



	public String getClassName() {
		
		return "Image";
	}
}
