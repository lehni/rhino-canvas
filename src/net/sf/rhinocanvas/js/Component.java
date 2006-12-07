package net.sf.rhinocanvas.js;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

public class Component {

	public Function onkeydown;
	public Function onkeyup;

	
	class EventHelper implements MouseListener, KeyListener{
		java.awt.Component awtComponent;
		
		EventHelper(java.awt.Component awtComponent){
			this.awtComponent = awtComponent;
		}
		

		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {
		//	System.out.println("Requesting the damn focus");
			awtComponent.requestFocus();
		}
		
		
		public void keyPressed(KeyEvent ke) {
			System.out.println("pressed Keycode: "+ke.getKeyCode()+" char: "+ke.getKeyChar());
			if(onkeydown != null){
				NativeObject no = new NativeObject();
				ScriptableObject.putProperty(no, "which", new Double(ke.getKeyCode()));
				ScriptableObject.putProperty(no, "keyCode", new Double(ke.getKeyCode()));
				
				onkeydown.call(Context.enter(), onkeydown, onkeydown, new Object[]{no});
				Context.exit();
			}
		}

		public void keyReleased(KeyEvent ke) {
//				System.out.println("rel. Keycode: "+ke.getKeyCode()+" char: "+ke.getKeyChar());
//				System.out.println("pressed Keycode: "+ke.getKeyCode()+" char: "+ke.getKeyChar());
			if(onkeyup != null){
				NativeObject no = new NativeObject();
				ScriptableObject.putProperty(no, "which", new Double(ke.getKeyCode()));
				ScriptableObject.putProperty(no, "keyCode", new Double(ke.getKeyCode()));
					
				onkeyup.call(Context.enter(), onkeyup, onkeyup, new Object[]{no});
			}
		}

		public void keyTyped(KeyEvent ke) {
	//		System.out.println("typed Keycode: "+ke.getKeyCode()+" char: "+ke.getKeyChar());
		}
		
	}
	
	

	
	
}

