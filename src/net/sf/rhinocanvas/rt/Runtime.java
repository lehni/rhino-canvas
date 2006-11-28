package net.sf.rhinocanvas.rt;

import javax.swing.text.JTextComponent;

public interface Runtime {
	
	
	public Object exec(String expression);
	public void setSource(String url);
	public void stop();
	
	public void defineProperty(String name, Object value);
	public int getLineNumber();
	public JTextComponent getConsole();
}
