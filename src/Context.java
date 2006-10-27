import java.awt.Graphics2D;

import org.mozilla.javascript.ScriptableObject;


public class Context extends ScriptableObject {

	public Context() {
	}
	
	Graphics2D graphics;
	
	void init(Graphics2D graphics){
		this.graphics = graphics;
	}

	@Override
	public String getClassName() {
		
		return "Context";
	}
	
	
	
	
}
