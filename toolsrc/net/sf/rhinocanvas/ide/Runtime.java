package net.sf.rhinocanvas.ide;

import javax.swing.text.JTextComponent;

public interface Runtime {
	
	
	public Object exec(String expression);
	public void setSource(String url);
	public void stop();
	
	public int getLineNumber();
	public JTextComponent getConsole();
}
