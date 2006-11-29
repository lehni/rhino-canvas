package net.sf.rhinocanvas.rt;

public class ConsoleTextArea extends org.mozilla.javascript.tools.shell.ConsoleTextArea{

	
	 public ConsoleTextArea(String[] argv) {
		 super(argv);
	 }
	
	 public void requestFocus(){
		 requestFocusInWindow();
	 }
	 
}
