package net.sf.rhinocanvas.js;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/*
 * Created on 28/10/2006 by Stefan Haustein
 */

public class ContextState {

	AffineTransform transform;
	
	ContextState(Graphics2D g){
		transform = g.getTransform();
	}
	
	
	public void apply(Graphics2D g){
		g.setTransform(transform);
	}

}
