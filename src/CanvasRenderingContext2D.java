import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Stack;

import org.mozilla.javascript.ScriptableObject;


public class CanvasRenderingContext2D extends ScriptableObject {

	public CanvasRenderingContext2D() {
	}
	
	Graphics2D graphics;
	GeneralPath path = new GeneralPath();
	Stack stack = new Stack();
	
	void init(Graphics2D graphics){
		this.graphics = graphics;
	}


	public String getClassName() {
		
		return "Context";
	}
	
	
	 // back-reference to the canvas
	//  readonly attribute HTMLCanvasElement canvas;

	  // state
//	 push state on state stack
	  void jsFunction_save(){
		  
		  
	  } 
	  void jsFunction_restore(){} // pop state stack and restore state

	  // transformations (default transform is the identity matrix)
	  void jsFunction_scale(float x, float y){}
	  void jsFunction_rotate(float angle){}
	  void jsFunction_translate(float x, float y){}
	  void jsFunction_transform(float m11, float m12, float m21, float m22, float dx, float dy){}
	  void jsFunction_setTransform(float m11, float m12, float m21, float m22, float dx, float dy){}

	  /*
	  // compositing
	           attribute float globalAlpha{} // (default 1.0)
	           attribute DOMString globalCompositeOperation; // (default over)

	  // colours and styles
	           attribute DOMObject strokeStyle; // (default black)
	           attribute DOMObject fillStyle; // (default black)
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
	  void jsFunction_clearRect(float x, float y, float w, float h){
		  
	  }
	  
	  void jsFunction_fillRect(float x, float y, float w, float h){
		  graphics.fill(new Rectangle2D.Float(x, y, w, h));
	  }
	  
	  void jsFunction_strokeRect(float x, float y, float w, float h){
		  graphics.draw(new Rectangle2D.Float(x, y, w, h));
	  }

	  // path API
	  void jsFunction_beginPath(){
		  path = new GeneralPath();
	  }
	  
	  void jsFunction_closePath(){
		  path.closePath();
	  }
	  
	  
	  void jsFunction_moveTo(float x, float  y){
		  path.moveTo(x, y);
	  }
	  
	  void jsFunction_lineTo(float x, float y){
		  path.lineTo(x, y);
	  }
	  
	  void jsFunction_quadraticCurveTo(float cpx, float cpy, float x, float y){}
	  void jsFunction_bezierCurveTo(float cp1x, float cp1y, float cp2x, float cp2y, float x, float y){}
	  void jsFunction_arcTo(float x1, float y1, float x2, float y2, float radius){}
	  void jsFunction_rect(float x, float y, float w, float h){}
	  void jsFunction_arc(float x, float y, float radius, float startAngle, float endAngle, boolean anticlockwise){}
	  void jsFunction_fill(){}
	  void jsFunction_stroke(){}
	  void jsFunction_clip(){}
	  boolean jsFunction_isPointInPath(float x, float y){
		return path.contains(x, y);
		}

	  // drawing images
//	  void jsFunction_drawImage(HTMLImageElement image, float dx, float dy){}
//	  void jsFunction_drawImage(HTMLImageElement image, float dx, float dy, float dw, float dh){}
//	  void jsFunction_drawImage(HTMLImageElement image, float sx, float sy, float sw, float sh, float dx, float dy, float dw, float dh){}
//	  void jsFunction_drawImage(HTMLCanvasElement image, float dx, float dy){}
//	  void jsFunction_drawImage(HTMLCanvasElement image, float dx, float dy, float dw, float dh){}
//	  void jsFunction_drawImage(HTMLCanvasElement image, float sx, float sy, float sw, float sh, float dx, float dy, float dw, float dh){}

	  // pixel manipulation
//	  ImageData jsFunction_getImageData(float sx, float sy, float sw, float sh){}
//	  void jsFunction_putImageData(ImageData image, float dx, float dy){}

	  // drawing text is not supported this version of the API
	  // (there is no way to predict what metrics the fonts will have,
	  // which makes fonts very hard to use for painting)

	
	
	
	
}
