package net.sf.rhinocanvas.js;

import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;

public class CanvasPattern {

	Paint paint;
	
	CanvasPattern(Image image, String repetition) {
		
		if(!"repeat".equals(repetition)){
			System.out.println("Currently unsupported CanvasPattern repetition: "+repetition);
		}
		
		Rectangle2D rect = new Rectangle2D.Float(0, 0, 
				image.getWidth(),
				image.getHeight());
		
		paint = new TexturePaint(image.image, rect);
	}

}
