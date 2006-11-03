package net.sf.rhinocanvas.js;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;

/*
 * Created on 28/10/2006 by Stefan Haustein
 */

public class ContextState {

	AffineTransform transform;
	Paint fillPaint;
	Paint strokePaint;
	private Object fillStyle;
	private Object strokeStyle;
	private float globalAlpha;
	private float lineWidth;
	private String lineJoin;
	private String lineCap;
	private float miterLimit; // convert to rad?
	private String globalCompositeOperation;
	
	
	ContextState(CanvasRenderingContext2D ctx){
		transform = ctx.graphics.getTransform();
		fillPaint = ctx.fillPaint;
		fillStyle = ctx.fillStyle;
		strokeStyle = ctx.strokeStyle;
		strokePaint = ctx.strokePaint;
		globalAlpha = ctx.globalAlpha;
		globalCompositeOperation = ctx.globalCompositeOperation;
		lineWidth = ctx.lineWidth;
		lineJoin = ctx.lineJoin;
		lineCap = ctx.lineCap;
		miterLimit = ctx.miterLimit;		
	}
	
	
	public void apply(CanvasRenderingContext2D ctx){
		ctx.graphics.setTransform(transform);
		ctx.fillPaint = fillPaint;
		ctx.fillStyle = fillStyle;
		ctx.strokeStyle = strokeStyle;
		ctx.strokePaint = strokePaint;
		ctx.globalAlpha = globalAlpha;
		ctx.setGlobalCompositeOperation(globalCompositeOperation);
		ctx.lineWidth = lineWidth;
		ctx.lineJoin = lineJoin;
		ctx.lineCap = lineCap;
		ctx.miterLimit = miterLimit;
		ctx.updateStroke();
	}

}
