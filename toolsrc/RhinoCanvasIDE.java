import javax.swing.UIManager;

import net.sf.rhinocanvas.ide.IDE;


public class RhinoCanvasIDE {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		} 
		new IDE();
	}


}
