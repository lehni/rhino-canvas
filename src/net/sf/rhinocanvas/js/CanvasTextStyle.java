/*
 * Created on 11/11/2006 by Stefan Haustein
 */
package net.sf.rhinocanvas.js;

import java.awt.Font;
import java.awt.FontMetrics;

import net.sf.css4j.Value;

public class CanvasTextStyle {
	
	private String fontFamily = "SansSerif";
	private String fontStyle = "plain";
	private String fontVariant = "none";
	private String fontWeight = "regular";
	private String fontSize = "12pt";

	private String textDecoration = "none";
	private String verticalAlign = "baseline";
	private String textTransform = "none";
	private String textAlign = "left"; // justify obviously not supported 

	CanvasRenderingContext2D context;

	Font font;
	FontMetrics metrics;
	
	CanvasTextStyle(CanvasRenderingContext2D context){
		this.context = context;
	}
	
	CanvasTextStyle(CanvasRenderingContext2D context, String attrs, String fontSize, String fontFamily){
		this(context);
		String[] attrArr = attrs.split(" ");
		
		for(int i = 0; i < attrArr.length; i++){
			if("bold".equals(attrArr[i])){
				fontWeight = "bold";
			}
			else if("italic".equals(attrArr[i])){
				fontVariant = "italic";
			}
		}
		this.fontSize = fontSize;
		this.fontFamily = fontFamily;
	}
	

	Font getFont(){
		if(font == null){
		
			Value size = new Value(fontSize);
			
			int style = 0;
			if("bold".equals(fontWeight)){
				style |= Font.BOLD;
			}
			if("italic".equals(fontVariant)){
				style |= Font.ITALIC;
			}

			double mm;
		    double nv = size.getNumValue();
		    String u = size.getUnit();
			if("px".equals(u)){
				mm = 0.26*nv;
			}
			else if("in".equals(u)) {
				mm = 25.4*nv;
			}
			else if("cm".equals(u)){
				mm = 10.0*nv;
			}
			else if("pt".equals(u)){
				mm = 25.4 * nv / 72.0;
			}
			else if("pc".equals(u)){
				mm = 25.4 * nv / 6.0;
			}
			else if("mm".equals(u)){
				mm = nv;
			}
			else {
				mm = 4 * 25.4 * nv / 72.0;
			}
			font = new Font(fontFamily, style, (int) (72.0 * mm / 25.4));
		}
		return font;
	}
	
	 FontMetrics getMetrics(){
		if(metrics == null){
			metrics = context.graphics.getFontMetrics(getFont());
		}
		return metrics;
	}
	
	public float stringWidth(String s){
		return getMetrics().stringWidth(s);
	}
	  
	public float getHeight(){
		return getMetrics().getHeight();
	}
	 
	public float getBaselinePosition(){
		  FontMetrics fm = getMetrics();
		  return  (fm.getLeading() + fm.getAscent());
	}
	
	public float getAscent(){
		return getMetrics().getAscent();
	}
	
	
	public float getDescent(){
		return getMetrics().getDescent();
	}
	
	public float getLeading(){
		return getMetrics().getLeading();
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
		this.font = null; metrics = null;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
		this.font = null; metrics = null;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
		this.font = null; metrics = null;
	}

	public String getFontVariant() {
		return fontVariant;
	}

	public void setFontVariant(String fontVariant) {
		this.fontVariant = fontVariant;
		this.font = null; metrics = null;
	}

	public String getFontWeight() {
		return fontWeight;
	}

	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
		this.font = null; metrics = null;
	}

	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
		this.font = null; metrics = null;
	}

	public String getTextDecoration() {
		return textDecoration;
	}

	public void setTextDecoration(String textDecoration) {
		this.textDecoration = textDecoration;
		this.font = null; metrics = null;
	}

	public String getTextTransform() {
		return textTransform;
	}

	public void setTextTransform(String textTransform) {
		this.textTransform = textTransform;
		this.font = null; metrics = null;
	}

	public String getVerticalAlign() {
		return verticalAlign;
	}

	public void setVerticalAlign(String verticalAlign) {
		this.verticalAlign = verticalAlign;
		this.font = null; metrics = null;
	}

}
