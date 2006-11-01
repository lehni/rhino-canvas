package net.sf.rhinocanvas.js;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Stack;

import org.mozilla.javascript.ScriptableObject;
import net.sf.css4j.Value;

public class CanvasRenderingContext2D {

	Graphics2D graphics;
	GeneralPath path = new GeneralPath();
	Stack stack = new Stack();
	Image image;
	Paint fillPaint = Color.BLACK;
	Paint strokePaint = Color.BLACK;
	private String fillStyle = "#000";
	private String strokeStyle = "#000";
	private float globalAlpha = 1.0f;
	
	
	CanvasRenderingContext2D(Image image) {
		this.image = image;
		this.graphics = (Graphics2D) image.image.getGraphics();
	//	graphics.setRenderingHint(RenderingHints.)
		this.graphics.setPaint(Color.BLACK);
	}
	
	
	
	 // back-reference to the canvas
	//  readonly attribute HTMLCanvasElement canvas;

	  // state
//	 push state on state stack
	  public void save(){
		  stack.push(new ContextState(graphics));  
	  } 
	  
	  
	  public void restore(){
		  ContextState st = (ContextState) stack.pop();
		  st.apply(graphics);
		  
	  } // pop state stack and restore state

	  // transformations (default transform is the identity matrix)
	  public void scale(double x, double y){
		  graphics.scale(x, y);
	  }
	  public void rotate(double angle){
		  graphics.rotate(angle);
		  
	  }
	  public void translate(double x, double y){
		  graphics.translate(x, y);
	  }
	  public void transform(float m11, float m12, float m21, float m22, float dx, float dy){
		  AffineTransform at = new AffineTransform(m11, m12, m21, m22, dx, dy);
		  graphics.transform(at);
	  }
	  public void setTransform(float m11, float m12, float m21, float m22, float dx, float dy){
		  AffineTransform at = new AffineTransform(m11, m12, m21, m22, dx, dy);
		  graphics.setTransform(at);
	  }

	  /*
	  // compositing
	   */
	  public float getGlobalAlpha(){
		  return globalAlpha;
		  
	  } // (default 1.0)
	  
	  public void setGlobalAlpha(float globalAlpha){
		  this.globalAlpha = globalAlpha;
		  graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, globalAlpha));
	  }
	  
	  //         attribute DOMString globalCompositeOperation; // (default over)
	
	  
	  
	  public String getFillStyle(){
		  return fillStyle;
	  }
	  
	  public void setFillStyle(String fillStyle){
		  this.fillStyle = fillStyle;
		  
		  Value fs = new Value(fillStyle);
		  
		  int color = fs.getColor();
		  
//		  System.out.println("color: "+Integer.toHexString(fs.getColor()));
		  
		  fillPaint = new Color(fs.getColor(), true);
		  
	  }

	  
	  public String getStrokeStyle(){
		  return strokeStyle;
	  }
	  
	  public void setStrokeStyle(String strokeStyle){
		  this.strokeStyle = strokeStyle;
		  
		  Value fs = new Value(strokeStyle);
		  
		  strokePaint = new Color(fs.getColor(), true);
	  }

	  
	  
	  /*
	   
	  CanvasGradient createLinearGradient(float x0, float y0, float x1, float y1);
	  CanvasGradient createRadialGradient(float x0, float y0, float r0, float x1, float y1, float r1);
	  CanvasPattern createPattern(HTMLImageElement image, DOMString repetition){}
	  CanvasPattern createPattern(HTMLCanvasElement image, DOMString repetition){}

	  // line caps/joins
	           attribute float lineWidth; // (default 1)
	           attribute DOMString lineCap; // "butt", "round", "square" (default "butt")
	           attribute DOMString lineJoin; // "round", "bevel", "miter" (default "miter")
	           attribute float miterLimit; // (default 10)

	  // shadows
	           attribute float shadowOffsetX; // (default 0)
	           attribute float shadowOffsetY; // (default 0)
	           attribute float shadowBlur; // (default 0)
	           attribute DOMString shadowColor; // (default black)

	  // rects
	   * 
	   * 
	   */
	  public void clearRect(float x, float y, float w, float h){
		  graphics.setPaint(Color.WHITE);
		  graphics.fill(new Rectangle2D.Double(x, y, w, h));
		  image.dirty();
	  }
	  
	  
	  
	  public void fillRect(double x, double y, double w, double h){
		  graphics.setPaint(fillPaint);
		  graphics.fill(new Rectangle2D.Double(x, y, w, h));
		  image.dirty();
	  }
	  
	  public void strokeRect(float x, float y, float w, float h){
		  graphics.setPaint(strokePaint);
		  graphics.draw(new Rectangle2D.Float(x, y, w, h));
		  image.dirty();
	  }

	  // path API
	  public void beginPath(){
		  path = new GeneralPath();
	  }
	  
	  public void closePath(){
		  path.closePath();
	  }
	  
	  
	  public void moveTo(float x, float  y){
		  path.moveTo(x, y);
	  }
	  
	  public void lineTo(float x, float y){
		  path.lineTo(x, y);
	  }
	  
	  public void quadraticCurveTo(float cpx, float cpy, float x, float y){
		  path.quadTo(cpx, cpy, x, y);
	  }
	  public void bezierCurveTo(float cp1x, float cp1y, float cp2x, float cp2y, float x, float y){
		  path.curveTo(cp1x, cp1y, cp2x, cp2y, x, y);
	  }
	  
	  public void arcTo(float x1, float y1, float x2, float y2, float radius){
		 System.out.println("arcTo Not Yet implemented...");
	  }

	  public void rect(float x, float y, float w, float h){
		  path.append(new Rectangle2D.Double(x, y, w, h), true);
	  }
	  
	  public void arc(double x, double y, double radius, double startAngle, double endAngle, boolean counterclockwise){
		 
		  boolean clockwise = !counterclockwise;
		  double twopi = 2*Math.PI;
		  
		  
		  while(startAngle < 0){
			  startAngle += twopi;
		  }
		  while(startAngle > twopi){
			  startAngle -= twopi;
		  }
		  
		  while(endAngle < 0){
			  endAngle += twopi;
		  }
		  while(endAngle > twopi){
			  endAngle -= twopi;
		  }
		  
//		  System.out.println("drawArc from "+ Math.toDegrees(startAngle)+" to "+Math.toDegrees(endAngle)+ 
//				  (counterclockwise ? " counterclockwise":" clockwise"));
		  
		  
		  if(clockwise){
			  if(startAngle> endAngle){
				  endAngle += twopi;
			  }
			  // ang must be negative!

		  }
		  else {
			  if(startAngle < endAngle){
				  endAngle -= twopi; 
			  }
		
			  // ang must be positve!
		  }
		  double ang = startAngle-endAngle;
		  
//		  System.out.println("Angle: "+Math.toDegrees(ang)+ " should be "+(counterclockwise ? " positive ": " negative "));
		  
		  // TODO: is this correct? cf: http://developer.mozilla.org/en/docs/Canvas_tutorial:Drawing_shapes
		  if(ang == 0.0){
			  ang = Math.PI * 2;
		  }
		  
		  

		  startAngle = -startAngle;
	

		  path.append(new Arc2D.Double(
				  x-radius, y-radius, 
				  2*radius, 2*radius, 
				  Math.toDegrees(startAngle), 
				  Math.toDegrees(ang), 
				  Arc2D.OPEN), true);
	  }
	  
	  public void fill(){
		  graphics.setPaint(fillPaint);
		  graphics.fill(path);
		  image.dirty();
	  }
	  public void stroke(){
		  graphics.setPaint(strokePaint);
		  graphics.draw(path);
		  image.dirty();
	  }
	  public void clip(){}
	  boolean jsFunction_isPointInPath(float x, float y){
		return path.contains(x, y);
		}

	  // drawing images
//	  public void drawImage(HTMLImageElement image, float dx, float dy){}
//	  public void drawImage(HTMLImageElement image, float dx, float dy, float dw, float dh){}
//	  public void drawImage(HTMLImageElement image, float sx, float sy, float sw, float sh, float dx, float dy, float dw, float dh){}
//	  public void drawImage(HTMLCanvasElement image, float dx, float dy){}
//	  public void drawImage(HTMLCanvasElement image, float dx, float dy, float dw, float dh){}
//	  public void drawImage(HTMLCanvasElement image, float sx, float sy, float sw, float sh, float dx, float dy, float dw, float dh){}

	  // pixel manipulation
//	  ImageData jsFunction_getImageData(float sx, float sy, float sw, float sh){}
//	  public void putImageData(ImageData image, float dx, float dy){}

	  // drawing text is not supported this version of the API
	  // (there is no way to predict what metrics the fonts will have,
	  // which makes fonts very hard to use for painting)

	
	
	
	
}
