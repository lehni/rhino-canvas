package net.sf.rhinocanvas.js;

import java.awt.Color;
import java.awt.Paint;
import java.util.TreeMap;
import java.util.Map;

import net.sf.css4j.Value;

import org.apache.batik.ext.awt.LinearGradientPaint;
import org.apache.batik.ext.awt.RadialGradientPaint;

class CanvasGradient {

	boolean radial;
	float x1;
	float y1;
	float x2;
	float y2;
	float r1;
	float r2;
	
	
	
	TreeMap<Float,String> stops = new TreeMap<Float,String>();
	Paint paint;
	
	CanvasGradient(float x1, float y1, float r1, float x2, float y2, float r2){
		this.x1 = x1;
		this.y1 = y1;
		this.r1 = r1;
		this.x2 = x2;
		this.y2 = y2;
		this.r2 = r2;
		radial = true;
	}
	
	

	CanvasGradient(float x1, float y1, float x2, float y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		radial = false;
	}
	
	
	
	public void addColorStop(float where, String color){
		
		if(radial && r1 > 0){
			if(stops.size() == 0 && where == 0){
				stops.put(where, color);
			}
			where = where * r1/r2 + (1 - r1/r2);
			
			System.out.println("where: "+where+ " r1: "+r1+ " r2:"+r2);
		}
		
		// todo: scale somehow with r1 and r2, so r1 can be omitted...
		if(stops.get(where) != null){
			where += where / 1000;
		}
		
		stops.put(where, color);
	}
	
	
	Paint getPaint(){
		if(paint == null){
			float[] where = new float[stops.size()];
			Color[] color = new Color[stops.size()];

			int i = 0;
			for(Map.Entry<Float,String> e: stops.entrySet()){
				where[i] = e.getKey();
				color[i] = new Color(new Value(e.getValue()).getColor(), true);
				i++;
			}
			
			paint = radial 
			? new RadialGradientPaint(x2, y2, r2, x1, y1, where, color) 
			: new LinearGradientPaint(x1, y1, x2, y2, where, color);
		}
		
		return paint;
	}
	
}
